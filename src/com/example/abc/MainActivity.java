package com.example.abc;

import com.example.abc.db.service.FeedService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
	}
	
	public void sendMsg(View v){
		Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
		startActivity(intent);
	}
}
