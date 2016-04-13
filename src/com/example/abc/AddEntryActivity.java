package com.example.abc;

import java.util.UUID;

import com.example.abc.db.service.FeedService;
import com.example.abc.utils.StringUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEntryActivity extends Activity {

	private EditText et_nicheng,et_personsign;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_entry);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_entry, menu);
		return true;
	}
	
	public void back(View v){
		this.finish();
	}
	
	public void save(View v){
		et_nicheng=(EditText)findViewById(R.id.nicheng);
		et_personsign=(EditText)findViewById(R.id.personsign);
		
		String nicheng=null!=et_nicheng.getText()?et_nicheng.getText().toString():"";
		String personsign=null!=et_personsign.getText()?et_personsign.getText().toString():"";
		if(StringUtils.isEmpty(nicheng)){
			Toast.makeText(getApplicationContext(),"昵称不能为空",Toast.LENGTH_SHORT).show();
			return ;
		}
		if(StringUtils.isEmpty(personsign)){
			Toast.makeText(getApplicationContext(),"个性签名不能为空",Toast.LENGTH_SHORT).show();
			return ;
		}
		
		FeedService feedService=new FeedService(getApplicationContext());
		String id=UUID.randomUUID().toString();
		feedService.add(id,nicheng,personsign);
		
		Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
		
		Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
		intent.putExtra("addsuccess",true);
		intent.putExtra("id",id);
		intent.putExtra("title",nicheng);
		intent.putExtra("content",personsign);
		this.setResult(RESULT_OK,intent);
		this.finish();
		
	}

}
