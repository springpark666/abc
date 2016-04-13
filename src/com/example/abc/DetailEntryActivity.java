package com.example.abc;

import com.example.abc.bean.Feed;
import com.example.abc.db.service.FeedService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class DetailEntryActivity extends Activity {

	private TextView tv_name,tv_gxqm;
	private String eid;
	private FeedService feedService=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_entry);
		
		
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		String id=bundle.getString("id");
		
		eid=id;
		
		feedService=new FeedService(getApplicationContext());
		
		Feed feed=feedService.getById(id);
		
		tv_name=(TextView)findViewById(R.id.name);
		tv_gxqm=(TextView)findViewById(R.id.gxqmvalue);
		
		tv_name.setText(feed.getTitle());
		tv_gxqm.setText(feed.getContent());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_entry, menu);
		return true;
	}
	
	public void back(View v){
		this.finish();
	}
	
	public void delete(View v){
		
		int a=feedService.deleteById(eid);
		if(a>0){
			Intent intent=new Intent(getApplicationContext(), FeedActivity.class);
			intent.putExtra("deletesuccess",true);
			this.setResult(RESULT_OK,intent);
			this.finish();
		}
		
	}

}
