package com.example.androidapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xmppapp.verification.Result;
import com.xmppapp.verification.VerificationHandler;
import com.xmppapp.verification.VerificationHandler.VerificationInterface;

public class Screen2 extends Fragment implements VerificationInterface {

	private Button btnDone;
	private EditText edtUserVerificationCode;
	private TextView txtMsgNotifiation;
	private Button btnResend;

	private VerificationHandler verificationHandler;

	public void onCreate(Bundle savedInstanceState) {
		verificationHandler = getParentActivity().getVerificationHandler();
		verificationHandler.setListener(this);
		super.onCreate(savedInstanceState);
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.screen_2, container, false);
		edtUserVerificationCode = (EditText) view.findViewById(R.id.edtUserVerificationCode);
		btnDone = (Button) view.findViewById(R.id.btnDoneUserVerification);
		btnDone.setOnClickListener(mCommonClickListener);
		txtMsgNotifiation = (TextView) view.findViewById(R.id.txtMsgNotification);
		btnResend = (Button) view.findViewById(R.id.btnResend);
		btnResend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View V) {
				changeFragment(new Screen1());
			}
		});

		return view;
	}

	OnClickListener mCommonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String mUserCode = edtUserVerificationCode.getText().toString();
			if (mUserCode.trim().length() < 6) {
				edtUserVerificationCode.setError("Please enter 6 digit code");
			} else {
				verificationHandler.checkForVerification(mUserCode);
			}
		}
	};
	private ProgressDialog progress;

	@Override
	public void onVerificationProcessStart() {
		progress = ProgressDialog.show(getActivity(), "Verification", "Please wait while we verify the code received", true);
	}

	@Override
	public void onVerificationProcessEnd() {
		if (progress != null) {
			progress.dismiss();
		}
	}

	@Override
	public void onRandomCodeGenerate(String randomCode) {

	}

	@Override
	public void onPhoneNumberFound(String phoneNumber) {

	}

	@Override
	public void onVerificationResult(Result mResult) {

		switch (mResult) {
		case PASSED:
			showDialog("Congratulation! You have verified your number successfully.");
			break;
		case FAILED:
			showDialog("Sorry! Your number verification has been failed.");
			break;
		case EXPIRED:
			showDialog("Sorry!! This code has been expired.");
			break;

		default:
			break;
		}
	}

	public void onStart() {
		Bundle mBundle = getArguments();
		if (mBundle != null) {
			txtMsgNotifiation.setText(txtMsgNotifiation.getText() + " " + mBundle.getString("msgnumber"));
		}

		verificationHandler.registerReciver(getActivity());
		super.onStart();
	};

	@Override
	public void onStop() {
		verificationHandler.unregisteredReciver(getActivity());
		super.onStop();
	}

	void showDialog(String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setTitle("Verification");
		alertDialog.setMessage(msg);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				getFragmentManager().beginTransaction().replace(R.id.fragment_home, new FragmentHome()).commitAllowingStateLoss();
			}
		});
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();
	}

	@Override
	public void onMsgSendingStart() {

	}

	@Override
	public void onMsgSendinEnd() {

	}

	@Override
	public void onMsgCodeReceived(String mCode) {
		edtUserVerificationCode.setText(mCode);
	}

	private void changeFragment(Fragment newFragment) {
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_home, newFragment);
		ft.commit();
	}

	private MainActivity getParentActivity() {
		return ((MainActivity) getActivity());
	}
}
