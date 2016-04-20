package com.example.abc.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TestService extends Service{
	private boolean runflag=true;
	@Override
	public IBinder onBind(Intent arg0) {
		Log.e("mmm","service bind");
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.e("mmm","service create");
		final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		new Thread(){
			@Override
			public void run() {
				while(runflag){
					Log.e("mmm",sdf.format(new Date()));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("mmm","service destroy");
		runflag=false;
		super.onDestroy();
	}
}
