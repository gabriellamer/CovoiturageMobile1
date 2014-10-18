package uqtr.covoituragemobile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import model.Session;
import model.User;
import ServerTasks.DeleteUser;
import ServerTasks.ModifyUser;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class ManageUser extends Activity {

	private static final String URL_DELETE_USER = "http://mil08.uqtr.ca/~milnx613/deleteUser.php";
	private static final String URL_MODIFY_USER = "http://mil08.uqtr.ca/~milnx613/manageUser.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_user);
		
		if(Session.getCurrentUser() != null) {
			
			EditText username   = (EditText) findViewById(R.id.user_username);
			EditText name       = (EditText) findViewById(R.id.user_name);
			EditText lastname   = (EditText) findViewById(R.id.user_lastname);
			EditText phone      = (EditText) findViewById(R.id.user_phone);
			EditText email      = (EditText) findViewById(R.id.user_email);
			RadioButton man     = (RadioButton) findViewById(R.id.user_man);
			RadioButton woman   = (RadioButton) findViewById(R.id.user_woman);
			EditText age        = (EditText) findViewById(R.id.user_age);
			EditText streetnbr  = (EditText) findViewById(R.id.user_street_nbr);
			EditText streetname = (EditText) findViewById(R.id.user_streetname);
			EditText city       = (EditText) findViewById(R.id.user_city);
			EditText province   = (EditText) findViewById(R.id.user_province);
			EditText postalCode = (EditText) findViewById(R.id.user_postalcode);
			EditText appNb      = (EditText) findViewById(R.id.user_appNb);
			
			User currentUser = Session.getCurrentUser();
			
			username.setText(currentUser.getUsername());
			name.setText(currentUser.getName());
			lastname.setText(currentUser.getLastName());
			phone.setText(currentUser.getPhone());
			email.setText(currentUser.getEmail());
			
			if(currentUser.getSex() == 'm')
				man.setChecked(true);
			else
				woman.setChecked(true);
			
			age.setText("" + currentUser.getAge());
			streetnbr.setText("" + currentUser.getAddress().getStreetNb());
			streetname.setText(currentUser.getAddress().getStreetName());
			postalCode.setText(currentUser.getAddress().getPostalCode());
			city.setText(currentUser.getAddress().getCity());
			province.setText(currentUser.getAddress().getProvince());
			appNb.setText(currentUser.getAddress().getAppNb());

			final Button btnDelete = (Button) findViewById(R.id.btnDelUser);
			btnDelete.setVisibility(View.VISIBLE);
			btnDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					deleteUser();
				}
			});
		}
		
		final Button btnSave = (Button) findViewById(R.id.btnSaveUser);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Session.getCurrentUser() != null) {
					modifyUser();
				} else {
					createUser();
				}
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_user_ads:
	        	Intent adsIntent = new Intent(this, UserAds.class);
	    		startActivity(adsIntent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}
	
	private void deleteUser() {
			
		// Building Parameters
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("URL", URL_DELETE_USER);
		params.put("idUser", "" + Session.getCurrentUser().getId());
		
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        
        if(networkInfo != null && networkInfo.isConnected())
        {
        	new DeleteUser().execute(params);
        }
        else
        {
        	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

            dlgAlert.setMessage("No network connection available.");
            dlgAlert.setTitle("Error Message...");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
		
		Session.setCurrentUser(null);
		
		Intent logoutIntent = new Intent(this, Login.class);
		startActivity(logoutIntent);
	}
	
	private void createUser() {
//		EditText username   = (EditText) findViewById(R.id.user_username);
//		EditText name       = (EditText) findViewById(R.id.user_name);
//		EditText lastname   = (EditText) findViewById(R.id.user_lastname);
//		EditText phone      = (EditText) findViewById(R.id.user_phone);
//		EditText email      = (EditText) findViewById(R.id.user_email);
//		RadioButton sex     = (RadioButton) findViewById(R.id.user_man);
//		EditText age        = (EditText) findViewById(R.id.user_age);
//		EditText streetnbr  = (EditText) findViewById(R.id.user_street_nbr);
//		EditText streetname = (EditText) findViewById(R.id.user_streetname);
//		EditText city       = (EditText) findViewById(R.id.user_city);
//		EditText province   = (EditText) findViewById(R.id.user_province);
		
		
		
		
		//TODO query the server for add
		//TODO new user from db
		
		
		//Session.setCurrentUser(newUser);
		//Intent searchIntent = new Intent(this, /*TODO search activity*/);
		//startActivity(searchIntent);

	}

	private void modifyUser() {
		
		User user = Session.getCurrentUser();
		
		EditText userName   = (EditText) findViewById(R.id.user_username);
		EditText name       = (EditText) findViewById(R.id.user_name);
		EditText lastName   = (EditText) findViewById(R.id.user_lastname);
		EditText phone      = (EditText) findViewById(R.id.user_phone);
		EditText email      = (EditText) findViewById(R.id.user_email);
		RadioButton sex     = (RadioButton) findViewById(R.id.user_man);
		EditText age        = (EditText) findViewById(R.id.user_age);
		EditText streetNbr  = (EditText) findViewById(R.id.user_street_nbr);
		EditText streetName = (EditText) findViewById(R.id.user_streetname);
		EditText city       = (EditText) findViewById(R.id.user_city);
		EditText province   = (EditText) findViewById(R.id.user_province);
		EditText postalCode = (EditText) findViewById(R.id.user_postalcode);
		EditText appNb      = (EditText) findViewById(R.id.user_appNb);
		
		user.setUsername(userName.getText().toString());
		user.setName(name.getText().toString());
		user.setLastName(lastName.getText().toString());
		user.setPhone(phone.getText().toString());
		user.setEmail(email.getText().toString());
		user.setSex(sex.isChecked()? 'm' : 'f');
		user.setAge(Integer.parseInt(age.getText().toString()));
		user.getAddress().setStreetNb(Integer.parseInt(streetNbr.getText().toString()));
		user.getAddress().setStreetName(streetName.getText().toString());
		user.getAddress().setCity(city.getText().toString());
		user.getAddress().setProvince(province.getText().toString());
		user.getAddress().setPostalCode(postalCode.getText().toString());
		user.getAddress().setAppNb(appNb.getText().toString());
		
		String locationName = user.getAddress().getStreetNb() + " " + user.getAddress().getStreetName() + " " + user.getAddress().getAppNb() + " " + user.getAddress().getCity() + " " + user.getAddress().getProvince() + " " + user.getAddress().getPostalCode(); 
		
		Geocoder gc = new Geocoder(this);
		List<android.location.Address> list = null;
		try {
			list = gc.getFromLocationName(locationName, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		android.location.Address add = list.get(0);
		
		user.getAddress().setLatitude(add.getLatitude());
		user.getAddress().setLongitude(add.getLongitude());
		
		// Building Parameters
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("URL", URL_MODIFY_USER);
		params.put("idUser", "" + user.getId());
		params.put("username", user.getUsername());
		params.put("lastName", user.getLastName());
		params.put("name", user.getName());
		params.put("phone", user.getPhone());
		params.put("email", user.getEmail());
		params.put("sex", "" + user.getSex());
		params.put("age", "" + user.getAge());
		
		params.put("streetNb", "" + user.getAddress().getStreetNb());
		params.put("streetName", user.getAddress().getStreetName());
		params.put("appNb", user.getAddress().getAppNb());
		params.put("city", user.getAddress().getCity());
		params.put("province", user.getAddress().getProvince());
		params.put("postalCode", user.getAddress().getPostalCode());
		params.put("appNb", user.getAddress().getAppNb());
		params.put("latitude", "" + user.getAddress().getLatitude());
		params.put("longitude", "" + user.getAddress().getLongitude());
		
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        
        if(networkInfo != null && networkInfo.isConnected())
        {
        	new ModifyUser().execute(params);
        }
        else
        {
        	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

            dlgAlert.setMessage("No network connection available.");
            dlgAlert.setTitle("Error Message...");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
        
        Toast toast = Toast.makeText(this, "Modification effectuee.", Toast.LENGTH_SHORT);
        toast.show();
	}
}
