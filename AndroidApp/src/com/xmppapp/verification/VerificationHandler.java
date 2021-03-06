package com.xmppapp.verification;

import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.xmppapp.verification.SecurePreferences.SecurePreferencesException;

public class VerificationHandler {

	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	private static final String ACTION_DELIVERED_MSG = "delivered_msg";
	private static final String ACTION_SENT_MSG = "sent_msg";
	private static final String TAG = "VERIFICATION_HANDLER";
	private static final String EXTRA_NUMBER = "extra_number";
	private static final String EXTRA_MSG_BODY = "extra_body";
	private static final String EXTRA_CODE = "extra_code";
	private static final String EXTRA_MSG_DATETIME = "extra_msg_datetime";
	private static final String EXTRA_VERIFIED = "extra_verified";

	private static final int MIN_NUMBER_RANGE = 100000;
	private static final int MAX_NUMBER_RANGE = 999999;

	private static final String VERIFICATION_PREF = "verification_pref";
	private static final String SECURE_KEY = "testKey";

	private static final long MSG_VALIDATION_TIME = 1000 * 60 * 2;

	private SecurePreferences _Preferences;
	private Context mContext;
	private VerificationInterface verificationInterface;

	public interface VerificationInterface {

		void onVerificationProcessStart();

		void onVerificationProcessEnd();

		void onMsgCodeReceived(String mCode);

		void onMsgSendingStart();

		void onMsgSendinEnd();

		void onRandomCodeGenerate(String randomCode);

		void onPhoneNumberFound(String phoneNumber);

		void onVerificationResult(Result mResult);

	}

	public void setListener(VerificationInterface verificationInterface) {
		this.verificationInterface = verificationInterface;
		fetchPhoneNumber();
	}
	public String getNumber() {
		return _Preferences.getString(EXTRA_NUMBER);
	}
	// Incoming message broadcast receiver
	private BroadcastReceiver reciverMessage = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String mAction = intent.getAction();
			if (mAction.matches(ACTION_SMS_RECEIVED)) {
				handleIncomingMsg(intent);
			}

			if (mAction.equals(ACTION_SENT_MSG)) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					// Toast.makeText(context, "Message is sent successfully",
					// Toast.LENGTH_SHORT).show();
					verificationInterface.onMsgSendinEnd();
					break;
				default:
					Toast.makeText(context, "Error in sending Message", Toast.LENGTH_SHORT).show();
					verificationInterface.onMsgSendinEnd();
					break;
				}
			}

			if (mAction.equals(ACTION_DELIVERED_MSG)) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					// Toast.makeText(context, "Message is delivered",
					// Toast.LENGTH_SHORT).show();
					break;
				default:
					// Toast.makeText(context,
					// "Error in the delivery of message",
					// Toast.LENGTH_SHORT).show();
					break;

				}
			}
		}
	};

	public VerificationHandler(Context mContext) {
		this.mContext = mContext;
		fetchPhoneNumber();
		_Preferences = new SecurePreferences(mContext, VERIFICATION_PREF, SECURE_KEY, true);
	}

	public VerificationHandler(Context mContext, VerificationInterface verificationInterface) {
		this.mContext = mContext;
		this.verificationInterface = verificationInterface;

		fetchPhoneNumber();
		_Preferences = new SecurePreferences(mContext, VERIFICATION_PREF, SECURE_KEY, true);
	}

	/** Register incoming msg receiver */
	public void registerReciver(Activity mActivity) {
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ACTION_SMS_RECEIVED);
		mFilter.addAction(ACTION_SENT_MSG);
		mFilter.addAction(ACTION_DELIVERED_MSG);
		mActivity.registerReceiver(reciverMessage, mFilter);
	}

	public void unregisteredReciver(Activity mActivity) {
		mActivity.unregisterReceiver(reciverMessage);
	}

	/** Handle verification code sending */
	public void sendCode(String mMobileNumber) {
		verificationInterface.onMsgSendingStart();

		// Start sending msg
		verificationInterface.onVerificationProcessStart();

		String mGenerateCode = generateCode();

		String message = "This is sample code: " + mGenerateCode;

		// Save code in preference
		_Preferences.put(EXTRA_CODE, mGenerateCode);
		_Preferences.put(EXTRA_NUMBER, mMobileNumber);
		_Preferences.put(EXTRA_MSG_BODY, message);
		_Preferences.put(EXTRA_MSG_DATETIME, getCurrentTime());

		/**
		 * Creating a pending intent which will be broadcasted when an sms
		 * message is successfully sent
		 */
		PendingIntent piSent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_SENT_MSG), 0);

		/**
		 * Creating a pending intent which will be broadcasted when an sms
		 * message is successfully delivered
		 */
		PendingIntent piDelivered = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_DELIVERED_MSG), 0);

		/**
		 * Getting an instance of SmsManager to sent sms message from the
		 * application
		 */
		SmsManager smsManager = SmsManager.getDefault();

		/** Sending the Sms message to the intended party */
		//smsManager.sendTextMessage(mMobileNumber, null, message, piSent, piDelivered);
		//DEBUG . remove this code and uncomment the above one
		verificationInterface.onMsgSendinEnd();
	}

	/** Generate verification code and handle lifetime and temporary storage */
	private String generateCode() {
		String randomCode = Integer.toString(randInt(MIN_NUMBER_RANGE, MAX_NUMBER_RANGE));

		Log.d(TAG, "Random Code : " + randomCode);

		// Transmit random code
		verificationInterface.onRandomCodeGenerate(randomCode);
		return randomCode;
	}

	/** Handle the incoming msg and verify code and its lifetime */
	private void handleIncomingMsg(final Intent mIntent) {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Bundle pudsBundle = mIntent.getExtras();
				Object[] pdus = (Object[]) pudsBundle.get("pdus");

				// Get SMS
				SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
				Log.i(TAG, "Incoming Msg: " + messages.getMessageBody());

				String msgBody = messages.getMessageBody();

				if (msgBody.trim().length() == 27) {
					final String incomingCode = msgBody.substring(msgBody.length() - 6);
					Log.i(TAG, "Code in incoming msg: " + incomingCode);
					verificationInterface.onMsgCodeReceived(incomingCode);

					checkForVerification(incomingCode);

					return;
				} else {
					verificationInterface.onVerificationResult(Result.FAILED);
					verificationInterface.onVerificationProcessEnd();
					return;
				}
			}
		}, 2000);
	}

	/** Check the incoming code verification and send appropriate result */
	public boolean checkForVerification(String mCodeReceived) {

		long mMsgDateTime = Long.parseLong(_Preferences.getString(EXTRA_MSG_DATETIME));
		long mCurrentDateTime = new Date().getTime();
		String mCodeSent = _Preferences.getString(EXTRA_CODE);
		//DEBUG
		mCodeReceived=mCodeSent;
		if (mCodeSent.matches(mCodeReceived)) {

			if (getDifferenceBetweenDates(mCurrentDateTime, mMsgDateTime) <= MSG_VALIDATION_TIME) {
				saveVerificationStatus(Result.PASSED);
				verificationInterface.onVerificationResult(Result.PASSED);
				verificationInterface.onVerificationProcessEnd();
				return true;
			} else {
				saveVerificationStatus(Result.EXPIRED);
				verificationInterface.onVerificationResult(Result.EXPIRED);
				verificationInterface.onVerificationProcessEnd();
				return false;
			}
		}
		saveVerificationStatus(Result.FAILED);
		verificationInterface.onVerificationResult(Result.FAILED);
		verificationInterface.onVerificationProcessEnd();

		return false;

	}

	protected String fetchPhoneNumber() {
		TelephonyManager tMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String number = tMgr.getLine1Number();

		// If found real number
		if (number != null) {
			if (number.trim().length() >= 10) {
				Log.d(TAG, "Number Found : " + number);

				if (verificationInterface != null) {
					// Transmit phone number
					verificationInterface.onPhoneNumberFound(number);
				}
				return number;
			}
		}
		Log.d(TAG, "No Number Fund");
		return "NA";
	}

	/**
	 * Returns a pseudo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 * 
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	private static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	/** Provide current time in milliseconds */
	private String getCurrentTime() {
		return Long.toString(new Date().getTime());
	}

	private static long getDifferenceBetweenDates(Long dateSent, Long dateReceived) {
		return Math.abs(dateReceived - dateSent);
	}

	public boolean gotoCodeWindow() {
		try {
			float getLastMsgDate = Float.parseFloat(_Preferences.getString(EXTRA_MSG_DATETIME));
			float getToadyDay = Float.parseFloat(getCurrentTime());

			float diff = getToadyDay - getLastMsgDate;
			if (diff < MSG_VALIDATION_TIME) {
				return true;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SecurePreferencesException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return false;
	}

	private void saveVerificationStatus(Result result) {
		switch (result) {
		case PASSED:
			_Preferences.put(EXTRA_VERIFIED, "YES");
			break;
		case FAILED:
			_Preferences.put(EXTRA_VERIFIED, "NO");
			break;
		case EXPIRED:
			_Preferences.put(EXTRA_VERIFIED, "EXPIRED");
			break;

		default:
			break;
		}
	}

	public Result getVerificationStatus() {
		try {
			String mVerificationStatus = _Preferences.getString(EXTRA_VERIFIED);
			if (mVerificationStatus.matches("YES")) {
				//return Result.PASSED;
			} else if (mVerificationStatus.matches("NO")) {
				return Result.FAILED;
			} else {
				return Result.EXPIRED;
			}
		} catch (Exception e) {
		}

		return Result.FAILED;
	}
}
