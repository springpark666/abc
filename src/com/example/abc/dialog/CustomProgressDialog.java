package com.example.abc.dialog;

import com.example.abc.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomProgressDialog extends Dialog {

	private Context context=null;
	private static CustomProgressDialog customProgressDialog=null;
	
	public CustomProgressDialog(Context context) {
		super(context);
		this.context=context;
	}

	public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context=context;
    }
	
	public static CustomProgressDialog createDialog(Context context){
		customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.progressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setCancelable(false);
		
		return customProgressDialog;
	}
 
    public void onWindowFocusChanged(boolean hasFocus){
    	
    	if (customProgressDialog == null){
    		return;
    	}
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.iv_image);
        //AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        //animationDrawable.start();
        
      
           // 加载动画  
           Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
                   context, R.anim.loading_animation);  
           // 使用ImageView显示动画  
           imageView.startAnimation(hyperspaceJumpAnimation); 

    }
 
    /**
     * 
     * [Summary]
     *       setTitile 标题
     * @param strTitle
     * @return
     *
     */
    public CustomProgressDialog setTitile(String strTitle){
    	return customProgressDialog;
    }
    
    /**
     * 
     * [Summary]
     *       setMessage 提示内容
     * @param strMessage
     * @return
     *
     */
    public CustomProgressDialog setMessage(String strMessage){
    	TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.tv_text);
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    	return customProgressDialog;
    }

}
