package com.scconsulting.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_NAME_1 = "employee";
	private static final String DATABASE_NAME = "classdb.db";
	private static final int DATABASE_VERSION = 1;

	/*
	 *  employee table creation sql statement
	 *  _id is set automatically
	 */
	private static final String CREATE_EMPL = "create table " + TABLE_NAME_1 +
			"(_id integer primary key autoincrement, " +
			"firstname text not null, " +
			"lastname text not null, " +
			"title text not null, " +
			"salary integer not null);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {

		database.execSQL(CREATE_EMPL);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " +
						oldVersion +
						" to " +
						newVersion +
						", which will destroy all old data");
		
		// Delete the tables, then create them again.
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
	    onCreate(db);
	  }

}
