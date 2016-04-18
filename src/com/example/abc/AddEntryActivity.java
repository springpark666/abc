package com.example.abc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.example.abc.bean.Feed;
import com.example.abc.db.service.FeedService;
import com.example.abc.utils.StringUtils;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddEntryActivity extends Activity {

	private TextView tv_eid,tv_title;
	private EditText et_nicheng,et_personsign;
	private FeedService feedService=null;
	private ImageView headimage;
	
	private String imageName;
	
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_entry);
		
		tv_eid=(TextView)findViewById(R.id.eid);
		tv_title=(TextView)findViewById(R.id.tv_title);
		
		et_nicheng=(EditText)findViewById(R.id.nicheng);
		et_personsign=(EditText)findViewById(R.id.personsign);
		
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		
		headimage=(ImageView)findViewById(R.id.headimage);
		
		if(null!=bundle){
			String id=bundle.getString("id");
			if(!StringUtils.isEmpty(id)){
				feedService=new FeedService(getApplicationContext());
				
				Feed feed=feedService.getById(id);
				
				tv_eid.setText(id);
				
				et_nicheng.setText(feed.getTitle());
				et_personsign.setText(feed.getContent());
				
				tv_title.setText("修改");
				
				if(!StringUtils.isEmpty(feed.getHeadimage())){
					if(new File("/sdcard/abc/"+ feed.getHeadimage()).exists()){
						Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/abc/"+ feed.getHeadimage());
						headimage.setImageBitmap(bitmap);
						Log.e("mmm","图片存在");
					}else{
						Log.e("mmm","图片不存在");
					}
				}else{
					Log.e("mmm","headimage is null");
				}
			}
			
		}
		
		headimage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showPhotoDialog();
			}
		});
		
		
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
		String eid=null!=tv_eid.getText()?tv_eid.getText().toString():"";
		String nicheng=null!=et_nicheng.getText()?et_nicheng.getText().toString():"";
		String personsign=null!=et_personsign.getText()?et_personsign.getText().toString():"";
		
		if(StringUtils.isEmpty(imageName)){
			Toast.makeText(getApplicationContext(),"请上传头像",Toast.LENGTH_SHORT).show();
			return ;
		}
		
		if(StringUtils.isEmpty(nicheng)){
			Toast.makeText(getApplicationContext(),"昵称不能为空",Toast.LENGTH_SHORT).show();
			return ;
		}
		if(StringUtils.isEmpty(personsign)){
			Toast.makeText(getApplicationContext(),"个性签名不能为空",Toast.LENGTH_SHORT).show();
			return ;
		}
		
		if(StringUtils.isEmpty(eid)){
			FeedService feedService=new FeedService(getApplicationContext());
			String id=UUID.randomUUID().toString();
			feedService.add(id,nicheng,personsign);
			
			Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
			
			Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
			intent.putExtra("addsuccess",true);
			intent.putExtra("id",id);
			intent.putExtra("title",nicheng);
			intent.putExtra("content",personsign);
			intent.putExtra("headimage","/sdcard/abc/"+imageName);
			this.setResult(RESULT_OK,intent);
			this.finish();
		}else{
			FeedService feedService=new FeedService(getApplicationContext());
			feedService.updateById(eid, nicheng, personsign,imageName);
			
			Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
			
			Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
			intent.putExtra("updatesuccess",true);
			intent.putExtra("id",eid);
			intent.putExtra("title",nicheng);
			intent.putExtra("content",personsign);
			intent.putExtra("headimage","/sdcard/abc/"+imageName);
			this.setResult(RESULT_OK,intent);
			this.finish();
		}
		
		
		
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
                break;

            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

}
