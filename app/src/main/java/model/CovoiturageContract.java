package model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class CovoiturageContract {
	public CovoiturageContract() { }
	
	public static abstract class  AdEntry implements BaseColumns {
		public static final String T_AD = "T_AD";
		public static final String F_ID_AD = "F_ID_AD";
		public static final String F_ID_USER = "F_ID_USER";
		public static final String F_DRIVER = "F_DRIVER";
		public static final String F_TITLE = "F_TITLE";
		public static final String F_DESCRIPTION = "F_DESCRIPTION";
		public static final String F_NB_PLACE = "F_NB_PLACE";
		public static final String F_AIR_CONDITIONNER = "F_AIR_CONDITIONNER";
		public static final String F_HEATER = "F_HEATER";
	}
	
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + AdEntry.T_AD + " (" +
													  AdEntry.F_ID_AD + " INTEGER PRIMARY KEY," +
													  AdEntry.F_ID_USER + " INTEGER, " +
													  AdEntry.F_DRIVER + " TINYINT, " +
													  AdEntry.F_TITLE + " VARCHAR(100), " +
													  AdEntry.F_DESCRIPTION + " VARCHAR(300), " +
													  AdEntry.F_NB_PLACE + " INTEGER, " +
													  AdEntry.F_AIR_CONDITIONNER + " TINYINT, " +
													  AdEntry.F_HEATER + " TINYINT)";
	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + AdEntry.T_AD;

	public static class CovoiturageDbHelper extends SQLiteOpenHelper {
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "covoiturage.db";
		
		public CovoiturageDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_ENTRIES);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DELETE_ENTRIES);
			onCreate(db);
		}
		
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onUpgrade(db, oldVersion, newVersion);
		}
	}
}