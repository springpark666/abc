package com.example.abc;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SelectPicPopActivity extends Activity implements OnClickListener{

	private Button bt_shipin,bt_paizhao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_pic_pop);
		
		bt_shipin=(Button)findViewById(R.id.bt_shipin);
		bt_paizhao=(Button)findViewById(R.id.bt_paizhao);
		
		bt_paizhao.setOnClickListener(this);
		bt_shipin.setOnClickListener(this);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_pic_pop, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_shipin:
			Toast.makeText(SelectPicPopActivity.this,"点击了小视频",Toast.LENGTH_SHORT).show();
			this.finish();
			break;
		case R.id.bt_paizhao:
			Toast.makeText(SelectPicPopActivity.this,"点击了拍照",Toast.LENGTH_SHORT).show();
			this.finish();
			break;

		default:
			break;
		}
	}

}
