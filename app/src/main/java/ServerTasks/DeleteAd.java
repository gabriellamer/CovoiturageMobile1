package ServerTasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class DeleteAd extends AsyncTask<HashMap<String, String>, Void, String> {
	
	@Override
	protected String doInBackground(HashMap<String, String>... params) {
		try
		{
			return doDelete(params[0]);
		}
		catch(IOException e)
		{
			return "Unable to retrieve web page. URL may be invalid.";
		}
	}
	
	public String doDelete(HashMap<String, String> params) throws IOException
	{
		InputStream inputStream = null;
        String result = "";
        
		try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params.get("URL"));

            String json = "";
            JSONObject jsonObject = new JSONObject();
 
            for (HashMap.Entry<String, String> entry : params.entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();
			    
			    if(key != "URL")
			    {
			    	jsonObject.put(key, value);
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
