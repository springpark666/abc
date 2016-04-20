package com.example.abc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.abc.bean.Feed;
import com.example.abc.db.service.FeedService;
import com.example.abc.dialog.CustomProgressDialog;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FeedActivity extends Activity implements OnClickListener{
	private PullToRefreshListView lv;
	private LinkedList<String> mListItems;
	private ArrayAdapter<String> mAdapter;
	
	
	private FeedService feedService=null;
	private List<Map<String,Object>> data=null;
	private SimpleAdapter simpleAdapter=null;
	private static int REQUEST_ADD=0;
	private static int REQUEST_DELETE=1;
	private static int REQUEST_UPDATE=2;
	private int index;
	private boolean isRefreshing=false;
	private int pullflag=0;
	private int curpage=1;
	private int pagesize=5;
	private String selectId;
	private PopupWindow popupWindow=null;
	private TextView hideView=null;
	
	private Button bt_shipin,bt_paizhao;
	private View view;
	private Intent serviceIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		feedService=new FeedService(getApplicationContext());
		lv=(PullToRefreshListView)findViewById(R.id.pull_to_refresh_listview);
		//setPullToRefreshLable();
		//所有数据
		data=new ArrayList<Map<String,Object>>();
		
		loadData();//加载1页数据
		
		String []from={"id","title","content","headimage"};
		int []to={R.id.feedid,R.id.msg_name,R.id.content,R.id.headimage};
		
		hideView=(TextView)findViewById(R.id.hideView);
		
		
		
		ListView actualListView = lv.getRefreshableView();

		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);

		simpleAdapter=new SimpleAdapter(this,data,R.layout.activity_entry,from, to);
		//lv.setAdapter(simpleAdapter);

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		actualListView.setAdapter(simpleAdapter);
		
		
		lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View arg1,
					ContextMenuInfo arg2) {
				menu.add(0, 0, 0,"修改");
				menu.add(0,1,0,"删除");
				menu.add(0,2,0,"查看资料");
			}
		});
		
		
		
		
		
		
		
		
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				String id=((TextView)v.findViewById(R.id.feedid)).getText().toString();
			    
				
				index=arg2-1;
				
			    detail(id);
			    
//			    Intent intent=new Intent(getApplicationContext(),AddEntryActivity.class);
//			    Bundle bundle=new Bundle();
//				   bundle.putString("id",id);
//				    
//				   intent.putExtras(bundle);
//				   startActivityForResult(intent,REQUEST_ADD);//删除返回
			   
			}
		});
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int arg2, long arg3) {
				String id=((TextView)v.findViewById(R.id.feedid)).getText().toString();
				index=arg2-1;
				selectId=id;
				Log.e("mmm","long click event"+arg2);
				
				return false;
			}
		});
		
		
		lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2(){

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				pullflag=1;//下拉
				Toast.makeText(getApplicationContext(), "onPullDownToRefresh", Toast.LENGTH_SHORT).show();
				new GetDataTask().execute();
				
				//设置下拉时显示的日期和时间
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // 更新显示的label
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pullflag=2;//上拉
				Toast.makeText(getApplicationContext(), "onPullUpToRefresh", Toast.LENGTH_SHORT).show();
				new GetDataTask().execute();
			}
			
		});
		
		
		
		
		LayoutInflater inflater = LayoutInflater.from(this);
		view=inflater.inflate(R.layout.activity_select_pic_pop, null);
		view.setFocusableInTouchMode(true);
		
		bt_shipin=(Button)view.findViewById(R.id.bt_shipin);
		bt_paizhao=(Button)view.findViewById(R.id.bt_paizhao);
		bt_shipin.setOnClickListener(this);
		bt_paizhao.setOnClickListener(this);
		
		
		popupWindow=new PopupWindow(view, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, true);
		popupWindow.setAnimationStyle(R.style.MyDialogStyleBottom);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		
		

	}
	
	private void detail(String id){
		   Log.e("mmm","===================index====="+index);
		   Intent intent=new Intent(getApplicationContext(),DetailEntryActivity.class);
		   Bundle bundle=new Bundle();
		   bundle.putString("id",id);
		   Log.e("mmm","===================id====="+id);
		   intent.putExtras(bundle);
		   startActivityForResult(intent,REQUEST_DELETE);//删除返回
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item){
		AdapterView.AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		switch (item.getItemId()) {
		case 0://添加
			Toast.makeText(FeedActivity.this,"点击了修改",Toast.LENGTH_SHORT).show();
			
		    Intent intent=new Intent(getApplicationContext(),AddEntryActivity.class);
		    Bundle bundle=new Bundle();
			bundle.putString("id",selectId);
			intent.putExtras(bundle);
			startActivityForResult(intent,REQUEST_ADD);//
			
			
			//
			//unbindService(conn);
			
			break;
		case 1://删除
			Toast.makeText(FeedActivity.this,"点击了删除",Toast.LENGTH_SHORT).show();
			break;
		case 2://查看资料
			Toast.makeText(FeedActivity.this,"点击了查看资料",Toast.LENGTH_SHORT).show();
			detail(selectId);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	private void setPullToRefreshLable() {
		// 1:第一种设置 (个人推荐第一种)
		ILoadingLayout startLoading = lv.getLoadingLayoutProxy(
				true, false);
		startLoading.setPullLabel("下拉刷新");// 刚下拉时显示的提示
		startLoading.setRefreshingLabel("正在刷新中...");// 刷新时显示的提示
		startLoading.setReleaseLabel("释放即可刷新");// 下拉达到一定距离时显示的提示

		ILoadingLayout endLoading = lv.getLoadingLayoutProxy(false,
				true);
		endLoading.setPullLabel("上拉加载更多");// 刚上拉时显示的提示
		endLoading.setRefreshingLabel("拼命加载中...");// 加载时的提示
		endLoading.setReleaseLabel("释放即可加载更多");// 上拉达到一定距离时显示的提示
	}

	
	private void loadData(){
		List<Feed> list=feedService.getListWithPage(curpage,pagesize);
		for(Feed feed:list){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("id",feed.getId());
			map.put("title",feed.getTitle());
			map.put("content",feed.getContent());
			map.put("headimage","/sdcard/abc/"+ feed.getHeadimage());
			Log.e("mmm","============="+feed.getId());
			data.add(map);
		}
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
	    @Override
	    protected void onPostExecute(String[] result) {
	        // Call onRefreshComplete when the list has been refreshed.
	    	if(1==pullflag){//下拉
	    		curpage=1;
	    		data=new ArrayList<Map<String,Object>>();
	    		loadData();
	    		
		    	simpleAdapter.notifyDataSetChanged();
	    	}else if(2==pullflag){
	    		curpage+=1;
	    		loadData();
	    		Log.e("MMM","===============curpage"+curpage);
	    		//Log.e("MMM","===============size"+data.size());
		    	simpleAdapter.notifyDataSetChanged();
	    	}
	    	

	        lv.onRefreshComplete();
	        super.onPostExecute(result);
	    }

		@Override
		protected String[] doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	private void updateList(){
		simpleAdapter.notifyDataSetChanged();
		lv.onRefreshComplete();  
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
	        		String headimage=bundle.getString("headimage");
	        		Map<String,Object> map=new HashMap<String, Object>();
	        		map.put("id",id);
	        		map.put("title",title);
	        		map.put("content",content);
	        		map.put("headimage",headimage);
	        		data.add(map);
	        		simpleAdapter.notifyDataSetChanged();
	        	}else if(bundle.getBoolean("updatesuccess")){
	        		String id=bundle.getString("id");
	        		String title=bundle.getString("title");
	        		String content=bundle.getString("content");
	        		String headimage=bundle.getString("headimage");
	        		Map<String,Object> map=data.get(index);
	        		map.put("id",id);
	        		map.put("title",title);
	        		map.put("content",content);
	        		map.put("headimage",headimage);
	        		
	        		simpleAdapter.notifyDataSetChanged();
	        	}
	        }
	    }else if(requestCode==REQUEST_DELETE){
	    	 if (resultCode == RESULT_OK) {
		        	Bundle bundle=intent.getExtras();
		        	if(bundle.getBoolean("deletesuccess")){
		        		Log.e("mmm","===================data size"+data.size());
		        		data.remove(index);
		        		Log.e("mmm","===================data size"+data.size());
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
		
		//Intent intent=new Intent(getApplicationContext(),AddEntryActivity.class);
		//startActivityForResult(intent, REQUEST_ADD);
		
		
		//加载提示
		
//		final ProgressDialog progressDialog=new ProgressDialog(FeedActivity.this);
//		
//		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		progressDialog.setIcon(R.drawable.process);
//		progressDialog.setMessage("加载中...");
//		progressDialog.setCancelable(false);
//		progressDialog.show();
		
		final CustomProgressDialog progressDialog=CustomProgressDialog.createDialog(FeedActivity.this);
		progressDialog.show();
		
		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				progressDialog.dismiss();
			}
		}.start();
		
		
		//Intent intent2=new Intent("abcd");
		//sendBroadcast(intent2);
		
		//serviceIntent=new Intent("testservice");
		//bindService(serviceIntent, conn,BIND_AUTO_CREATE);
		
		//Intent intent=new Intent(FeedActivity.this,SelectPicPopActivity.class);
		//startActivity(intent);
		
		//changePopupWindowState();
	}
	
	ServiceConnection conn = new ServiceConnection(){
		      public void onServiceConnected(ComponentName name, IBinder service) {  
		          Log.e("mmm", "onServiceConnected");  
		      }  
		      public void onServiceDisconnected(ComponentName name) {  
		          Log.e("mmm", "onServiceDisconnected");  
		      }  
	};  

	
	private void changePopupWindowState() {
        if (popupWindow.isShowing()) {
            // 隐藏窗口，如果设置了点击窗口外消失，则不需要此方式隐藏
        	popupWindow.dismiss();
        } else {
            // 弹出窗口显示内容视图,默认以锚定视图的左下角为起点，这里为点击按钮
        	popupWindow.showAtLocation(hideView, Gravity.BOTTOM, 0, 0);
        }
    }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_shipin:
			Toast.makeText(getApplicationContext(),"点击了小视频",Toast.LENGTH_SHORT).show();
			changePopupWindowState();
			break;
		case R.id.bt_paizhao:
			Toast.makeText(getApplicationContext(),"点击了拍照",Toast.LENGTH_SHORT).show();
			changePopupWindowState();
			break;

		default:
			break;
		}
	}

}
