package com.example.abc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TestReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent arg1) {
		Toast.makeText(context,"已经收到广播",Toast.LENGTH_SHORT).show();
	}

}
