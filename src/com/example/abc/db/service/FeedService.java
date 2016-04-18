package com.example.abc.db.service;


import java.util.ArrayList;
import java.util.List;
import org.apache.http.entity.StringEntity;

import com.example.abc.bean.Feed;
import com.example.abc.db.FeedReaderContract;
import com.example.abc.db.helper.FeedReaderDbHelper;
import com.example.abc.utils.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class FeedService {
	FeedReaderDbHelper mDbHelper=null;
	public FeedService(Context context){
		  mDbHelper=new FeedReaderDbHelper(context);
	}
	
	public  long add(String id,String title,String content){
		
		Log.e("mmm","添加数据开始");
		// Gets the data repository in write mode
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID,id);
		values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
		values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, content);

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(
		         FeedReaderContract.FeedEntry.TABLE_NAME,
		         "",
		         values);
		Log.e("mmm","添加数据end"+newRowId);

		return newRowId;
	}
	
	public  Feed getById(String id){
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    FeedReaderContract.FeedEntry._ID,
		    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
		    FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE,
		    FeedReaderContract.FeedEntry.COLUMN_NAME_HEADIMAGE
		    };
		// How you want the results sorted in the resulting Cursor
		String sortOrder =
		    FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " DESC";
		
		String selection=FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID+" = ?";
		String []selectionArgs={id};
		Cursor c = db.query(
		    FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
		    projection,                               // The columns to return
		    selection,                                // The columns for the WHERE clause
		    selectionArgs,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );
		
		c.moveToFirst();
		
		Feed feed=new Feed();
		feed.setId(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)));
		feed.setTitle(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
		feed.setContent(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE)));
		feed.setHeadimage(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_HEADIMAGE)));
		return feed;

	}
	
	public int deleteById(String id){
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		// Define 'where' part of query.
		String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " = ?";
		// Specify arguments in placeholder order.
		String[] selelectionArgs = { id};
		// Issue SQL statement.
		return db.delete(FeedReaderContract.FeedEntry.TABLE_NAME,selection,selelectionArgs);
	}
	
	public int updateById(String id,String title,String content,String headimage){
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// New value for one column
		ContentValues values = new ContentValues();
		if(!StringUtils.isEmpty(title)){
			values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,title);
		}
		

		if(!StringUtils.isEmpty(content)){
			values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE,content);
		}
		
		if(null!=headimage){
			values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_HEADIMAGE,headimage);
		}
		
		// Which row to update, based on the ID
		String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " = ?";
		String[] selectionArgs = { id };

		int count = db.update(
		    FeedReaderContract.FeedEntry.TABLE_NAME,
		    values,
		    selection,
		    selectionArgs);

		return count;
	}
	public List<Feed> getList(){
		Log.e("mmm","数据查询");
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    FeedReaderContract.FeedEntry._ID,
		    FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID,
		    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
		    FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE,
		    FeedReaderContract.FeedEntry.COLUMN_NAME_HEADIMAGE
		    };
		// How you want the results sorted in the resulting Cursor
		String sortOrder =
		    FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " DESC";

		Cursor c = db.query(
		    FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
		    projection,                               // The columns to return
		    null,                                // The columns for the WHERE clause
		    null,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
		
		c.moveToFirst();
		List<Feed> list=new ArrayList<Feed>();
		Log.e("mmm","***********************_ID"+c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
		Log.e("mmm","***********************ENTRY_ID"+c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID));
		Log.e("mmm","***********************TITLE)"+c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
		Log.e("mmm","***********************SUBTITLE"+c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE));
		while(!c.isAfterLast()){
			Feed feed=new Feed();
			feed.setId(c.getString(1));
			feed.setTitle(c.getString(2));
			feed.setContent(c.getString(3));
			
			list.add(feed);
			
			c.moveToNext();
		}
		Log.e("mmm","数据查询end,size:"+list.size());
		c.close();
		return list;
	}
	
	
	
	public List<Feed> getListWithPage(int page,int count){
		int start=(page-1)*count;
		Log.e("mmm","分页数据查询");
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		String sql = "select * from entry limit ?,?";
		String selectionArgs[]= new String[]{String.valueOf(start), String.valueOf(count)};
		Cursor c = db.rawQuery(sql,selectionArgs);
		List<Feed> list=new ArrayList<Feed>();
		while(c.moveToNext()){
			Feed feed=new Feed();
			feed.setId(c.getString(1));
			feed.setTitle(c.getString(2));
			feed.setContent(c.getString(3));
			feed.setHeadimage(c.getString(4));
			list.add(feed);
		}
		c.close();
		return list;
	}
	
}
