package com.example.abc.db.helper;

import com.example.abc.db.FeedReaderContract;
import com.example.abc.def.TagValue;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
	public FeedReaderDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	// If you change the database schema, you must increment the database
	// version.
	public static final int DATABASE_VERSION = 5;
	public static final String DATABASE_NAME = "FeedReader.db";


	public void onCreate(SQLiteDatabase db) {
		db.execSQL(FeedReaderContract.SQL_CREATE_ENTRIES);
		Log.e(TagValue.TAG_INFO,"========数据库创建==========");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade
		// policy is
		// to simply to discard the data and start over
		db.execSQL(FeedReaderContract.SQL_DELETE_ENTRIES);
		onCreate(db);
		Log.e(TagValue.TAG_INFO,"========数据库删除重新创建==========");
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}