package com.example.abc;
import java.io.File;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
	}
	
	public void sendMsg(View v){
		Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
		startActivity(intent);
	}
	
	private void initData(){
		File file=new File("sdcard/abc/");
		if(!file.exists()){
			file.mkdirs();
		}
	}
}
