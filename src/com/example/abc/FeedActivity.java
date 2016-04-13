package com.example.abc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.abc.bean.Feed;
import com.example.abc.db.service.FeedService;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FeedActivity extends Activity{
	private ListView lv;
	private FeedService feedService=null;
	private List<Map<String,Object>> data=null;
	private SimpleAdapter simpleAdapter=null;
	private static int REQUEST_ADD=0;
	private static int REQUEST_DELETE=1;
	private static int REQUEST_UPDATE=2;
	private int index;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		feedService=new FeedService(getApplicationContext());
		lv=(ListView)findViewById(R.id.lv);
		//所有数据
		List<Feed> list=feedService.getList();
		data=new ArrayList<Map<String,Object>>();
		for(Feed feed:list){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("id",feed.getId());
			map.put("title",feed.getTitle());
			map.put("content",feed.getContent());
			Log.e("mmm","============="+feed.getId());
			data.add(map);
		}
		String []from={"id","title","content"};
		int []to={R.id.feedid,R.id.msg_name,R.id.content};
		simpleAdapter=new SimpleAdapter(this,data,R.layout.activity_entry,from, to);
		lv.setAdapter(simpleAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				String id=((TextView)v.findViewById(R.id.feedid)).getText().toString();
				Toast toast=Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT);
			    toast.show();
			    
			    index=arg2;
			    
//			   Intent intent=new Intent(getApplicationContext(),DetailEntryActivity.class);
//			    
//			   Bundle bundle=new Bundle();
//			   bundle.putString("id",id);
//			    
//			   intent.putExtras(bundle);
//			   startActivityForResult(intent,REQUEST_DELETE);//删除返回
			    
			    Intent intent=new Intent(getApplicationContext(),AddEntryActivity.class);
			    Bundle bundle=new Bundle();
				   bundle.putString("id",id);
				    
				   intent.putExtras(bundle);
				   startActivityForResult(intent,REQUEST_ADD);//删除返回
			   
			}
		});
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_ADD) {
	        if (resultCode == RESULT_OK) {
	        	Bundle bundle=intent.getExtras();
	        	if(bundle.getBoolean("addsuccess")){
	        		String id=bundle.getString("id");
	        		String title=bundle.getString("title");
	        		String content=bundle.getString("content");
	        		Map<String,Object> map=new HashMap<String, Object>();
	        		map.put("title",title);
	        		map.put("content",content);
	        		data.add(map);
	        		simpleAdapter.notifyDataSetChanged();
	        	}else if(bundle.getBoolean("updatesuccess")){
	        		String title=bundle.getString("title");
	        		String content=bundle.getString("content");
	        		Map<String,Object> map=data.get(index);
	        		map.put("title",title);
	        		map.put("content",content);
	        		
	        		simpleAdapter.notifyDataSetChanged();
	        	}
	        }
	    }else if(requestCode==REQUEST_DELETE){
	    	 if (resultCode == RESULT_OK) {
		        	Bundle bundle=intent.getExtras();
		        	if(bundle.getBoolean("deletesuccess")){
		        		data.remove(index);
		        		simpleAdapter.notifyDataSetChanged();
		        	}
	    	 }
	    }
	}

	public void back(View v){
		this.finish();
	}
	
	public void add(View v){
//		String id=UUID.randomUUID().toString();
//		feedService.add(id,"张三","你好吗");
//		Map<String,Object> map=new HashMap<String, Object>();
//		map.put("title","张三");
//		map.put("content","你好吗");
//		
//		data.add(map);
//		
//		simpleAdapter.notifyDataSetChanged();
		
		Intent intent=new Intent(getApplicationContext(),AddEntryActivity.class);
		startActivityForResult(intent, REQUEST_ADD);
		
	}
}
