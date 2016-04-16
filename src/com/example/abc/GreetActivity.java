package com.example.abc;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class GreetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greet);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.greet, menu);
		return true;
	}
	
	public void cancle(View v){
		this.finish();
	}
	
	public void send(View v){
		Toast.makeText(getApplicationContext(),"发送成功",Toast.LENGTH_SHORT).show();
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
				
			}
		}, 3000);
	}

}
