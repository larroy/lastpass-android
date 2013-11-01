package com.nhinds.lastpass.android;

import org.apache.commons.lang.Validate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.nhinds.lastpass.GoogleAuthenticatorRequired;
import com.nhinds.lastpass.LastPass.PasswordStoreBuilder;
import com.nhinds.lastpass.LastPassException;
import com.nhinds.lastpass.PasswordStore;
import com.nhinds.lastpass.impl.LastPassImpl;

/**
 * Activity which displays a login screen to the user.
 */
public class LoginActivity extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mOtpView;
	private TextView mLoginStatusMessageView;

	private PasswordStoreBuilder passwordStoreBuilder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		this.mEmailView = (EditText) findViewById(R.id.email);
		this.mPasswordView = (EditText) findViewById(R.id.password);
		this.mOtpView = (EditText) findViewById(R.id.otp);

		this.mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		addListener(R.id.password, R.id.sign_in_button, new Runnable() {
			@Override
			public void run() {
				attemptLogin();
			}
		});
		addListener(R.id.otp, R.id.sign_in_button_otp, new Runnable() {
			@Override
			public void run() {
				attemptOtpLogin();
			}
		});
	}

	private void addListener(final int textId, final int buttonId, final Runnable action) {
		((EditText) findViewById(textId)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					action.run();
					return true;
				}
				return false;
			}
		});
		findViewById(buttonId).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				action.run();
			}
		});
	}

	/**
	 * Attempts to sign in specified by the login form. If there are form errors
	 * (invalid email, missing fields, etc.), the errors are presented and no
	 * actual login attempt is made.
	 */
	private void attemptLogin() {
		if (this.mAuthTask != null) {
			return;
		}

		if (validateNotEmpty(this.mEmailView, this.mPasswordView)) {
			String email = this.mEmailView.getText().toString();
			String password = this.mPasswordView.getText().toString();
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			this.mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			setState(FormState.PROGRESS);
			this.passwordStoreBuilder = new LastPassImpl().getPasswordStoreBuilder(email, password, null);
			this.mAuthTask = new UserLoginTask(this.passwordStoreBuilder);
			this.mAuthTask.execute();
		}
	}

	private void attemptOtpLogin() {
		Log.d(getPackageName(), "Attempting OTP login");
		if (this.mAuthTask != null) {
			return;
		}
		if (this.passwordStoreBuilder == null) {
			throw new IllegalStateException("No password store builder found");
		}

		if (validateNotEmpty(this.mOtpView)) {
			String otp = this.mOtpView.getText().toString();
			setState(FormState.PROGRESS);
			this.mAuthTask = new UserLoginTask(this.passwordStoreBuilder);
			this.mAuthTask.execute(otp);
		}
	}

	private boolean validateNotEmpty(final EditText... views) {
		boolean valid = true;
		for (final EditText view : views) {
			if (TextUtils.isEmpty(view.getText())) {
				view.setError(getString(R.string.error_field_required));
				if (valid) {
					valid = false;
					// focus the first field with an error
					view.requestFocus();
				}
			} else {
				// reset errors for valid fields
				view.setError(null);
			}
		}
		return valid;
	}

	private enum FormState {
		LOGIN(R.id.login_form), OTP(R.id.login_otp_form), PROGRESS(R.id.login_status);

		public final int viewId;

		private FormState(final int viewId) {
			this.viewId = viewId;
		}
	}

	/**
	 * Shows the progress UI and hides the login form. TODO push some of this
	 * into the enum
	 */
	private void setState(final FormState desiredState) {
		for (FormState formState : FormState.values()) {
			setVisible(formState.viewId, formState == desiredState);
		}
	}

	private void setVisible(final int viewId, final boolean show) {
		final View view = findViewById(viewId);
		if (show || view.getVisibility() == View.VISIBLE) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			view.setVisibility(View.VISIBLE);
			view.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					view.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});
		}
	}

	private static class LoginResult {
		public final LoginFailureReason failureReason;
		public final PasswordStore passwordStore;
		public final String reasonString;

		public LoginResult(PasswordStore passwordStore) {
			Validate.notNull(passwordStore);
			this.passwordStore = passwordStore;
			this.failureReason = null;
			this.reasonString = null;
		}

		public LoginResult(LoginFailureReason failureReason, String reasonString) {
			Validate.notNull(failureReason);
			this.failureReason = failureReason;
			this.reasonString = reasonString;
			this.passwordStore = null;
		}

		public LoginResult(LoginFailureReason failureReason) {
			this(failureReason, null);
		}
	}

	private enum LoginFailureReason {
		FAIL, OTP
	}

	/**
	 * Represents an asynchronous login task used to authenticate the user.
	 */
	public class UserLoginTask extends AsyncTask<String, Void, LoginResult> {
		private final PasswordStoreBuilder passwordStoreBuilder;

		public UserLoginTask(PasswordStoreBuilder passwordStoreBuilder) {
			this.passwordStoreBuilder = passwordStoreBuilder;
		}

		@Override
		protected LoginResult doInBackground(String... params) {
			try {
				PasswordStore passwordStore;
				if (params.length == 0)
					passwordStore = this.passwordStoreBuilder.getPasswordStore();
				else
					passwordStore = this.passwordStoreBuilder.getPasswordStore(params[0]);
				return new LoginResult(passwordStore);
			} catch (GoogleAuthenticatorRequired authenticatorRequired) {
				return new LoginResult(LoginFailureReason.OTP);
			} catch (LastPassException failure) {
				return new LoginResult(LoginFailureReason.FAIL, failure.getMessage());
			}
		}

		@Override
		protected void onPostExecute(final LoginResult loginResult) {
			LoginActivity.this.mAuthTask = null;
			if (loginResult.passwordStore != null) {
				SoftKeyboard.setPasswordStore(loginResult.passwordStore);
				finish();
			} else if (loginResult.failureReason == LoginFailureReason.OTP) {
				android.util.Log.i(getPackageName(), "OTP Required");
				setState(FormState.OTP);
				LoginActivity.this.mOtpView.requestFocus();
			} else {
				setState(FormState.LOGIN);
				LoginActivity.this.mPasswordView.setError(loginResult.reasonString);
				LoginActivity.this.mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			// TODO who calls me?
			LoginActivity.this.mAuthTask = null;
			setState(FormState.LOGIN);
		}
	}
}
