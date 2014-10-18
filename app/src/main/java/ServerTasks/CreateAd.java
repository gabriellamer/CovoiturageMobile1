package ServerTasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import model.CovoiturageContract.AdEntry;
import model.CovoiturageContract.CovoiturageDbHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class CreateAd extends AsyncTask<HashMap<String, Object>, Void, String> {

	@Override
	protected String doInBackground(HashMap<String, Object>... params) {
		try
		{
			return doCreate(params[0]);
		}
		catch(IOException e)
		{
			return "Unable to retrieve web page. URL may be invalid.";
		}
	}
	
	private String doCreate(HashMap<String, Object> params) throws IOException
	{
		InputStream inputStream = null;
        String result = "";

		try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost((String) params.get("URL"));

            String json = "";
            JSONObject jsonObject = new JSONObject();
 
            for (HashMap.Entry<String, Object> entry : params.entrySet()) {
			    String key = entry.getKey();
			    Object value = entry.getValue();
			    
			    if(key != "URL" && key != "context")
			    {
			    	jsonObject.put(key,(String) value);
			    }
			}
            
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
           
            HttpResponse httpResponse = httpclient.execute(httpPost);
 
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
		
		String adId = "";
		
		try 
		{
			JSONObject response = new JSONObject(result);
			adId = response.getString("message");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		CovoiturageDbHelper mDbHelper = new CovoiturageDbHelper((Context)params.get("context"));
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(AdEntry.F_ID_AD, Integer.parseInt(adId));
		values.put(AdEntry.F_ID_USER, Integer.parseInt((String)params.get("idUser")));
		values.put(AdEntry.F_DRIVER, (String)params.get("driver"));
		values.put(AdEntry.F_TITLE, (String)params.get("title"));
		values.put(AdEntry.F_DESCRIPTION, (String)params.get("description"));
		values.put(AdEntry.F_NB_PLACE, Integer.parseInt((String)params.get("nbPlace")));
		values.put(AdEntry.F_AIR_CONDITIONNER, (String)params.get("airConditionner"));
		values.put(AdEntry.F_HEATER, (String)params.get("heater"));
		
		db.insert(AdEntry.T_AD, null, values);
		
		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
    }
}
