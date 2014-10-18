package uqtr.covoituragemobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Ad;
import model.Address;
import model.Session;
import model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends Activity {
	private Context searchContext = this;
	static ArrayList<Ad> listAds = new ArrayList<Ad>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		final Button btnDelete = (Button) findViewById(R.id.search_btn_search);
		btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAds.clear();
                searchAdd();
            }
        });
		
		final SeekBar maxDistance = (SeekBar) findViewById(R.id.search_range);
		final TextView distanceKm = (TextView) findViewById(R.id.search_distance_km);

		maxDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

			@Override 
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { 
				distanceKm.setText(String.valueOf(progress) + " km"); 
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void searchAdd() {
		EditText description = (EditText) findViewById(R.id.search_description);
		CheckBox man = (CheckBox) findViewById(R.id.search_man);
		CheckBox woman = (CheckBox) findViewById(R.id.search_woman);
		CheckBox driver = (CheckBox) findViewById(R.id.search_driver);
		CheckBox passenger = (CheckBox) findViewById(R.id.search_passenger);
		CheckBox heater = (CheckBox) findViewById(R.id.search_heater);
		CheckBox airConditionner = (CheckBox) findViewById(R.id.search_air_conditionner);
		SeekBar maxDistance = (SeekBar) findViewById(R.id.search_range);

        Address addressTest = new Address(282, "rue Boulard", null, "Trois-Rivières", "Québec", "G8T9G9", 46.368892, -72.523911);

        User userTest = new User(1, "lamer", "gabriel", "glamer", "glamer", addressTest, "450-808-8877", "gabriel.lamer@hotmail.ca", 'M', 22);

        Ad ad = new Ad(1, userTest, true, "annonce 1", "description 1", 2, true, true);

        listAds.add(ad);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            SearchAdsFragment fragment = (SearchAdsFragment) getFragmentManager().findFragmentById(R.id.idSearchAdsFragment);
            fragment.refreshList();
        }
        else
        {
            Intent userIntent = new Intent(searchContext, SearchAds.class);
            startActivity(userIntent);
        }

		/* Hardcoder les annonces dans une liste
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("idUser", "" + Session.getCurrentUser().getId());
		params.put("description", description.getText().toString());
		params.put("man", man.isChecked()? "1": "0");
		params.put("woman", woman.isChecked()? "1": "0");
		params.put("driver", driver.isChecked()? "1": "0");
		params.put("passenger", passenger.isChecked()? "1": "0");
		params.put("heater", heater.isChecked()? "1": "0");
		params.put("airConditionner", airConditionner.isChecked()? "1": "0");
		params.put("maxDistance", "" + maxDistance.getProgress());

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        
        if(networkInfo != null && networkInfo.isConnected())
        {
        	new SelectAllAdsTask().execute(params);
        }
        else
        {
        	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

            dlgAlert.setMessage("No network connection available.");
            dlgAlert.setTitle("Error Message...");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }*/
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_user_ads:
	        	Intent adsIntent = new Intent(this, UserAds.class);
	    		startActivity(adsIntent);
	            return true;
	        case R.id.action_user_profile:
	        	Intent userIntent = new Intent(this, ManageUser.class);
	    		startActivity(userIntent);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class SelectAllAdsTask extends AsyncTask<HashMap<String, String>, Void, ArrayList<Ad>> {
		final String URL_SELECT_ALL_ADS = "http://mil08.uqtr.ca/~milnx613/selectAllAds.php";

		@Override
		protected ArrayList<Ad> doInBackground(HashMap<String, String>... params) {
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
		                HttpPost httpPost = new HttpPost(URL_SELECT_ALL_ADS);

		                String json = "";
		                JSONObject jsonObject = new JSONObject();
		                
		                jsonObject.put("idUser", Session.getCurrentUser().getId());
		                
		                for (HashMap.Entry<String, String> entry : params[0].entrySet()) {
		    			    String key = entry.getKey();
		    			    String value = entry.getValue();
		    			    
		    			    jsonObject.put(key,(String) value);
		    			}
		                
		    			jsonObject.put("latitude", Session.getCurrentUser().getAddress().getLatitude());
		    			jsonObject.put("longitude", Session.getCurrentUser().getAddress().getLongitude());
		    			
		                
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
			                    	return null;
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
				                    
			                        User user = new User(o.getInt("F_ID_USER"), 
					                    				 o.getString("F_LASTNAME"), 
					                    				 o.getString("F_NAME"), 
					                    				 o.getString("F_USERNAME"), 
					                    				 o.getString("F_PASSWORD"), 
					                    				 address, 
					                    				 o.getString("F_PHONE"), 
					                    				 o.getString("F_EMAIL"), 
					                    				 o.getString("F_SEX").charAt(0), 
					                    				 o.getInt("F_AGE"));
			                        
			                        
			                        Ad ad = new Ad(o.getInt("F_ID_AD"),
			                        			   user,
			                        			   o.getInt("F_DRIVER") == 1 ? true : false,
			                        			   o.getString("F_TITLE"),
			                        			   o.getString("F_DESCRIPTION"),
			                        			   o.getInt("F_NB_PLACE"),
			                        			   o.getInt("F_AIR_CONDITIONNER") == 1 ? true : false,
			                        			   o.getInt("F_HEATER") == 1 ? true : false);
			                        
			                        listAds.add(ad);
			                    }
		                    }
		                    catch(JSONException e) {
		                    e.printStackTrace();
		                    }
		                }
		                else
		                    return null;
		    		} catch (Exception e) {
		                Log.d("InputStream", e.getLocalizedMessage());
		            }
		        }
		        
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return null;
			}
			
			return listAds;
		}

		@Override
		protected void onPostExecute(ArrayList<Ad> result) {
			Intent userIntent = new Intent(searchContext, SearchAds.class);
    		startActivity(userIntent);
            
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
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
}
