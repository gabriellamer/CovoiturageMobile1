package uqtr.covoituragemobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.*;
import model.CovoiturageContract.AdEntry;
import model.CovoiturageContract.CovoiturageDbHelper;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UserAds extends Activity {

	private ListView listView;
	private ActionMode actionMode;
	private User userTest;
	private ArrayList<Ad> listAds;
	private ArrayAdapter<Ad> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_ads);
		
		listAds = new ArrayList<Ad>();
		arrayAdapter = new AdsListAdapter(this, R.layout.list_ad, listAds);
		
		String locationName = "282 rue Boulard Trois-Rivières Québec G8T9G9"; 
		
		Geocoder gc = new Geocoder(this);
		List<android.location.Address> list = null;
		try {
			list = gc.getFromLocationName(locationName, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		android.location.Address add = list.get(0);
		
		Address addressTest = new Address(282, "rue Boulard", null, "Trois-Rivières", "Québec", "G8T9G9", add.getLatitude(), add.getLongitude());
		
		userTest = new User(1, "lamer", "gabriel", "glamer", "glamer", addressTest, "450-808-8877", "gabriel.lamer@hotmail.ca", 'M', 21);
		
		listView = (ListView) findViewById(R.id.lvAd);
		listView.setAdapter(arrayAdapter);
		
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				editAd(arrayAdapter.getItem((int)arg3));
			}
		});
		
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(actionMode != null) {
					return false;
				}
				
				actionMode = startActionMode(actionModeCallback);
				arg1.setSelected(true);
				actionMode.setTag(arrayAdapter.getItem((int)arg3));
				return true;
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		CovoiturageDbHelper mDbHelper = new CovoiturageDbHelper(this);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		String[] projection = {
				AdEntry.F_ID_AD,
				AdEntry.F_ID_USER,
				AdEntry.F_DRIVER,
				AdEntry.F_TITLE,
				AdEntry.F_DESCRIPTION,
				AdEntry.F_NB_PLACE,
				AdEntry.F_AIR_CONDITIONNER,
				AdEntry.F_HEATER
		};
		
		String selection = AdEntry.F_ID_USER + " = ?";
		
		String[] selectionArgs = {
				"1"
		};
		
		Cursor cursor = db.query(
				AdEntry.T_AD,  // The table to query
				projection,    // The columns to return
				selection,     // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null,          // don't group the rows
				null,          // don't filter by row groups
				null           // The sort order
		);
		
		arrayAdapter.clear();
		
		while(cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex(AdEntry.F_ID_AD));
			boolean driver = cursor.getInt(cursor.getColumnIndex(AdEntry.F_DRIVER)) == 1;
			String title = cursor.getString(cursor.getColumnIndex(AdEntry.F_TITLE));
			String description = cursor.getString(cursor.getColumnIndex(AdEntry.F_DESCRIPTION));
			int nbPlace = cursor.getInt(cursor.getColumnIndex(AdEntry.F_NB_PLACE));
			boolean airConditionner = cursor.getInt(cursor.getColumnIndex(AdEntry.F_AIR_CONDITIONNER)) == 1;
			boolean heater = cursor.getInt(cursor.getColumnIndex(AdEntry.F_HEATER)) == 1;
			
			Ad ad = new Ad(id, userTest, driver, title, description, nbPlace, airConditionner, heater);
			arrayAdapter.add(ad);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_add:
	        	addAd();
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
		getMenuInflater().inflate(R.menu.user_ads, menu);
		return true;
	}
	
	private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch(item.getItemId()) {
				case R.id.action_edit :
					editAd((Ad)mode.getTag());
					mode.finish();
					return true;
				case R.id.action_delete :
					deleteAd((Ad)mode.getTag());
					mode.finish();
					return true;
				default :
					return false;
			}
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.action_menu, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			actionMode = null;
			
		}
	};
	
	private void addAd() {
		Intent addIntent = new Intent(this, ManageAds.class);
		addIntent.putExtra("userId", ""+ userTest.getId());
		startActivity(addIntent);
	}
	
	private void deleteAd(Ad ad) {
		CovoiturageDbHelper mDbHelper = new CovoiturageDbHelper(this);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		String selection = AdEntry.F_ID_AD + " = ?";
		
		String[] selectionArgs = {
				""+ad.getId()
		};
		
		db.delete(AdEntry.T_AD, selection, selectionArgs);
		arrayAdapter.remove(ad);
	}
	
	private void editAd(Ad ad) {
		Intent sendIntent = new Intent(this, ManageAds.class);
		sendIntent.putExtra("adId", ""+ ad.getId());
		sendIntent.putExtra("adDriver", ""+ ad.isDriver());
		sendIntent.putExtra("adTitle", ""+ ad.getTitle());
		sendIntent.putExtra("adDescription", ""+ ad.getDescription());
		sendIntent.putExtra("adNbPlace", ""+ ad.getNbPlace());
		sendIntent.putExtra("adAirCond", ""+ ad.isAirCond());
		sendIntent.putExtra("adHeater", ""+ ad.isHeater());
		startActivity(sendIntent);
	}
	
	

}
