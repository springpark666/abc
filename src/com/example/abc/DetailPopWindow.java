package com.example.abc;
import com.example.abc.db.service.FeedService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class DetailPopWindow extends PopupWindow {

	private View conentView=null;
	private String eid;
	
	public DetailPopWindow(final Activity context,String id){
		eid=id;
		 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 conentView = inflater.inflate(R.layout.activity_menu, null);
		 
		// 设置SelectPicPopupWindow的View
	        this.setContentView(conentView);
	        // 设置SelectPicPopupWindow弹出窗体的宽
	        this.setWidth(LayoutParams.WRAP_CONTENT);
	        // 设置SelectPicPopupWindow弹出窗体的高
	        this.setHeight(LayoutParams.WRAP_CONTENT);
	        // 设置SelectPicPopupWindow弹出窗体可点击
	        this.setFocusable(true);
	        this.setOutsideTouchable(true);
	        // 刷新状态
	        this.update();
	        // 实例化一个ColorDrawable颜色为半透明
	        ColorDrawable dw = new ColorDrawable(0000000000);
	        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
	        this.setBackgroundDrawable(dw);
	        // 设置SelectPicPopupWindow弹出窗体动画效果
	        this.setAnimationStyle(R.style.AnimationPreview);
	        
	        
	        TextView tv_delete=(TextView)context.findViewById(R.id.bt_delete);
	        TextView tv_friend=(TextView)context.findViewById(R.id.bt_friend);
	        
	        tv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					DetailEntryActivity activity=new DetailEntryActivity();
					activity.deleteWarn();
				}
			});
	        
	        tv_friend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
//					Intent intent=new Intent(context,DetailEntryActivity.class);
//					Bundle bundle=new Bundle();
//					bundle.putString("id",eid);
//					context.startActivity(intent);
//					DetailPopWindow.this.dismiss();
				}
			});
	        
	        
	}
	
	public void delete(View v){
		Log.e("mmm","==================delete");
	}
	
	public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
