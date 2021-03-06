package com.alphasigma.kronos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.alphasigma.kronos.R;
import com.xmppapp.verification.Result;
import com.xmppapp.verification.VerificationHandler;
import com.xmppapp.verification.VerificationHandler.VerificationInterface;

public class Screen1 extends Fragment implements VerificationInterface {

	private static final String TAG = "SCREEN_1";
	private Button btnDone;
	private EditText edtUserMobileNumber;
	private EditText edtUserCountryCode;
	private VerificationHandler verificationHandler;
	ProgressDialog progress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.screen_1, container, false);

		btnDone = (Button) view.findViewById(R.id.btnDoneNumber);
		btnDone.setOnClickListener(mCommonClickListener);

		edtUserMobileNumber = (EditText) view.findViewById(R.id.edtUserMobileNumber);
		edtUserCountryCode = (EditText) view.findViewById(R.id.edtUserCountryCode);

		// Init verification class
		verificationHandler = getParentActivity().getVerificationHandler();
		verificationHandler.setListener(this);

		return view;
	}

	OnClickListener mCommonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// Get number details
			String mCountryCode = edtUserCountryCode.getText().toString();
			String mMobileNumber = edtUserMobileNumber.getText().toString();

			if (mMobileNumber.trim().length() < 2) {
				edtUserMobileNumber.setError("Enter correct mobile number");
			} else {
				verificationHandler.sendCode(mMobileNumber);
			}

		}
	};

	public void onStart() {
		verificationHandler.registerReciver(getActivity());
		super.onStart();
	};

	@Override
	public void onStop() {
		verificationHandler.unregisteredReciver(getActivity());
		super.onStop();
	}

	@Override
	public void onRandomCodeGenerate(String randomCode) {
		Log.d(TAG, "Random Code in fragment: " + randomCode);
	}

	@Override
	public void onPhoneNumberFound(String phoneNumber) {
		Log.d(TAG, "Phone found in fragment: " + phoneNumber);
		edtUserMobileNumber.setText(phoneNumber);
	}

	@Override
	public void onVerificationResult(Result mResult) {
	}

	@Override
	public void onVerificationProcessStart() {

	}

	@Override
	public void onVerificationProcessEnd() {

	}

	void showDialog(String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setTitle("Verification");
		alertDialog.setMessage(msg);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();
	}

	@Override
	public void onMsgSendingStart() {
		progress = ProgressDialog.show(getActivity(), "Sending", "Please wait while we send verification message", true);
	}

	@Override
	public void onMsgSendinEnd() {
		Screen2 screen2 = new Screen2();
		Bundle mBundle = new Bundle();
		mBundle.putString("msgnumber", "+" + edtUserCountryCode.getText().toString() + " " + edtUserMobileNumber.getText().toString());
		screen2.setArguments(mBundle);
		changeFragment(screen2);

		if (progress != null) {
			progress.dismiss();
		}

	}

	private void changeFragment(Fragment newFragment) {
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_home, newFragment);
		ft.commit();
	}

	@Override
	public void onMsgCodeReceived(String mCode) {

	}

	private MainActivity getParentActivity() {
		return ((MainActivity) getActivity());
	}
}
