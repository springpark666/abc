package com.example.abc;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.abc.bean.Feed;
import com.example.abc.db.service.FeedService;
import com.example.abc.utils.StringUtils;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class DetailEntryActivity extends Activity implements android.view.View.OnClickListener{

	private TextView tv_name,tv_gxqm;
	private String eid;
	private FeedService feedService=null;
	private ImageView iv_more,headimage;
	private String imageName;
	
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_entry);
		
		iv_more=(ImageView)findViewById(R.id.icon_more);
		
		
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		String id=bundle.getString("id");
		
		eid=id;
		
		feedService=new FeedService(getApplicationContext());
		
		Feed feed=feedService.getById(id);
		
		tv_name=(TextView)findViewById(R.id.name);
		tv_gxqm=(TextView)findViewById(R.id.gxqmvalue);
		
		tv_name.setText(feed.getTitle());
		tv_gxqm.setText(feed.getContent());
		
		headimage=(ImageView)findViewById(R.id.headimage);
		headimage.setOnClickListener(this);
		if(!StringUtils.isEmpty(feed.getHeadimage())){
			Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/abc/"+ feed.getHeadimage());
			headimage.setImageBitmap(bitmap);
		}
		
	}

	public void showmore(View v){
		Log.e("mmm","=======================展现菜单");
		DetailPopWindow detailPopWindow=new DetailPopWindow(DetailEntryActivity.this,eid);
		detailPopWindow.showPopupWindow(iv_more);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_entry, menu);
		return true;
	}
	
	public void back(View v){
		this.finish();
	}
	
	public void delete(View v){
		
		int a=feedService.deleteById(eid);
		if(a>0){
			Intent intent=new Intent(getApplicationContext(), FeedActivity.class);
			intent.putExtra("deletesuccess",true);
			this.setResult(RESULT_OK,intent);
			this.finish();
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.headimage://头像
			//showPhotoDialog();
			
			showLargeImage();
			
			break;

		default:
			break;
		}
	}
	
	private void showLargeImage(){
		LayoutInflater inflater = LayoutInflater.from(this);
		View imageView=inflater.inflate(R.layout.largeimage,null);
		final AlertDialog dialog=new AlertDialog.Builder(this).create();
		ImageView img=(ImageView)imageView.findViewById(R.id.large_image);

		headimage.setDrawingCacheEnabled(true);
		img.setImageBitmap(headimage.getDrawingCache());
		
		
		dialog.setView(imageView);
		dialog.show();
		
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.cancel();
			}
		});
		
	}
	
	@SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }
	
	@SuppressLint("SdCardPath")
    private void startPhotoZoom(Uri uri1, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File("/sdcard/abc/", imageName)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

	
	private void showPhotoDialog(){
		final AlertDialog dialog=	new AlertDialog.Builder(this).create();
		dialog.show();
		Window win=dialog.getWindow();
		win.setContentView(R.layout.alertdialog);
		
		TextView tv_menu1=(TextView)win.findViewById(R.id.tv_menu1);
		tv_menu1.setText("拍照");
		
		tv_menu1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				imageName = getNowTime() + ".png";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定调用相机拍照后照片的储存路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File("/sdcard/abc/", imageName)));
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                dialog.cancel();
			}
		});
		
		TextView tv_menu2=(TextView)win.findViewById(R.id.tv_menu2);
		tv_menu2.setText("相册");
		tv_menu2.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	                getNowTime();
	                imageName = getNowTime() + ".png";
	                Intent intent = new Intent(Intent.ACTION_PICK, null);
	                intent.setDataAndType(
	                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
	                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

	                dialog.cancel();
	            }
	        });
	
	}
	
	
	@SuppressLint({ "SdCardPath", "NewApi" })
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:

                startPhotoZoom(
                        Uri.fromFile(new File("/sdcard/abc/", imageName)),
                        480);
                break;

            case PHOTO_REQUEST_GALLERY:
                if (data != null)
                    startPhotoZoom(data.getData(), 480);
                break;

            case PHOTO_REQUEST_CUT:
                 BitmapFactory.Options options = new BitmapFactory.Options();
                
                 /**
                 * 最关键在此，把options.inJustDecodeBounds = true;
                 * 这里再decodeFile()，返回的bitmap为空
                 * ，但此时调用options.outHeight时，已经包含了图片的高了
                 */
                 options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/abc/"+ imageName);
                headimage.setImageBitmap(bitmap);
                
//                try ( InputStream is = new URL( "/sdcard/abc/"+ imageName ).openStream() ) {
//                	  Bitmap bitmap = BitmapFactory.decodeStream( is );
//                	  headimage.setImageBitmap(bitmap);
//                	}catch (Exception e) {
//                		Log.e("mmm","裁剪头像失败");
//				}
                updateAvatarInServer(imageName);
                break;

            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }
	
	private void updateAvatarInServer(String image){
		if(new File("/sdcard/abc/"+ imageName).exists()){
			feedService.updateById(eid,"","",image);
			Toast.makeText(getApplicationContext(),"更新本地数据库成功",Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(),"头像文件不存在",Toast.LENGTH_SHORT).show();
		}
	}
	
}
