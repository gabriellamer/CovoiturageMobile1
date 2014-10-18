package uqtr.covoituragemobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import model.Session;
import model.User;
import model.Address;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class Login extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mUsername;
	private String mPassword;

	// UI references.
	private EditText mUsernameView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private Context loginContext = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mUsernameView = (EditText) findViewById(R.id.user_username);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUsernameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUsernameView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 1) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		private static final String URL_LOGIN = "http://mil08.uqtr.ca/~milnx613/selectUser.php";

		@Override
		protected Boolean doInBackground(Void... params) {

			try 
			{
				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		        
		        if(networkInfo != null && networkInfo.isConnected())
		        {
		        	InputStream inputStream = null;
		            String result = "";

		    		try 
		    		{
		                HttpClient httpclient = new DefaultHttpClient();
		                HttpPost httpPost = new HttpPost(URL_LOGIN);

		                String json = "";
		                JSONObject jsonObject = new JSONObject();
		                
		    			jsonObject.put("username", mUsername);
		    			jsonObject.put("password", hashPassword(mPassword));
		    			
		                
		                json = jsonObject.toString();
		                StringEntity se = new StringEntity(json);
		                httpPost.setEntity(se);
		                httpPost.setHeader("Accept", "application/json");
		                httpPost.setHeader("Content-type", "application/json");
		               
		                HttpResponse httpResponse = httpclient.execute(httpPost);
		     
		                inputStream = httpResponse.getEntity().getContent();
		                if(inputStream != null)
		                {
		                    result = convertInputStreamToString(inputStream);

		                    try
		                    {
		                    	JSONObject o = new JSONObject(result);
		                    	
		                    	if(o.getInt("success") == 0) {
			                    	return false;
			                    }
		                    	
			                    JSONArray jsonUser = o.getJSONArray("message");
			                    
			                    int arrSize = jsonUser.length();
			                    for (int i = 0; i < arrSize; ++i) {
			                        o = jsonUser.getJSONObject(i);
			                        
			                        Address address = new Address(o.getInt("F_STREET_NB"),
				                    		o.getString("F_STREET_NAME"), 
				                    		o.getString("F_APP_NB"), 
				                    		o.getString("F_CITY"), 
				                    		o.getString("F_PROVINCE"), 
				                    		o.getString("F_POST_CODE"),
				                    		o.getDouble("F_LATITUDE"),
				                    		o.getDouble("F_LONGITUDE"));
				                    
			                        Session.setCurrentUser(new User(o.getInt("F_ID_USER"), 
				                    				o.getString("F_LASTNAME"), 
				                    				o.getString("F_NAME"), 
				                    				o.getString("F_USERNAME"), 
				                    				o.getString("F_PASSWORD"), 
				                    				address, 
				                    				o.getString("F_PHONE"), 
				                    				o.getString("F_EMAIL"), 
				                    				o.getString("F_SEX").charAt(0), 
				                    				o.getInt("F_AGE")));
			                    }
		                    }
		                    catch(JSONException e) {
		                    e.printStackTrace();
		                    }
		                }
		                else
		                    return false;
		     
		            } catch (Exception e) {
		                Log.d("InputStream", e.getLocalizedMessage());
		            }
		        }
		        
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			return true;
		}
		
		private String convertInputStreamToString(InputStream inputStream) throws IOException{
	        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
	        String line = "";
	        String result = "";
	        while((line = bufferedReader.readLine()) != null)
	            result += line;
	 
	        inputStream.close();
	        return result;
	    }
		
		private String hashPassword(String password) {
	        StringBuffer sb = new StringBuffer();
			
			try
			{
				MessageDigest md = MessageDigest.getInstance("SHA-256");
		        md.update(password.getBytes());
		 
		        byte byteData[] = md.digest();
		 
		        //convert the byte to hex format method 1
		        for (int i = 0; i < byteData.length; i++) {
		         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		        }
			}
			catch(NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}

	        return sb.toString();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) 
			{
				
				Intent searchIntent = new Intent(loginContext, Search.class);
				startActivity(searchIntent);
				
				finish();
			} 
			else 
			{
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
