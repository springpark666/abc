package com.example.abc;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Call2Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.call2, menu);
		return true;
	}

}
