package com.example.abc.db;
import android.provider.BaseColumns;

public class FeedReaderContract {

	public FeedReaderContract() {
	}

	public static abstract class FeedEntry implements BaseColumns {
		public static final String TABLE_NAME = "entry";
		public static final String COLUMN_NAME_ENTRY_ID = "eid";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_SUBTITLE = "subtitle";
	}

	public static final String TEXT_TYPE = " TEXT";
	public static final String COMMA_SEP = ",";
	public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ FeedReaderContract.FeedEntry.TABLE_NAME + " ("
			+ FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE
			+ COMMA_SEP + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
			+ TEXT_TYPE + COMMA_SEP + FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
			+ TEXT_TYPE+ " )";
	public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ FeedReaderContract.FeedEntry.TABLE_NAME;

}
