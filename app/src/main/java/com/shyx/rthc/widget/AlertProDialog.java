package com.shyx.rthc.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.shyx.rthc.R;


/**
 * 进度条对话框，播放菊花gif动画显示等待状态
 * @author weiyunchao
 *
 */
public class AlertProDialog {
	
	private Context context;
	private Dialog dialog;
	private RelativeLayout rootLayout;//布局根目录
	private Display display;//屏幕分辨率
	private GifView gf1;//进度条gif动画


	public AlertProDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public AlertProDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_wait_progressbar, null);

		// 获取自定义Dialog布局中的控件
		rootLayout = (RelativeLayout) view.findViewById(R.id.root);
		  // 从xml中得到GifView的句柄  
	    gf1 = (GifView) view.findViewById(R.id.gif);
	    // 设置Gif图片源
	    gf1.setGifImage(R.drawable.progressbar);
//	    // 添加监听器
//	    gf1.setOnClickListener(this);
	    // 设置显示的大小，拉伸或者压缩
	    gf1.setShowDimension(100, 100);
	    // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
	    gf1.setGifImageType(GifImageType.COVER);
		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.TransparentStyle);
		dialog.setContentView(view);

		// 调整dialog背景大小
		rootLayout.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), 300));

		return this;
	}

	/**
	 * 显示进度条
	 */
	public void show() {

		if (context == null) return;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			if(((Activity)context).isDestroyed()){
				return;
			}
		}
		if (!((Activity)context).isFinishing()&&dialog!=null ){ 	//&&dialog.isShowing()
			dialog.show();
		}
	}
	/**
	 * 取消进度条
	 */
	public void dissmiss(){
		if (context == null) return;
//		L.d("AlertDialog", "isFinish= "+((Activity)context).isFinishing());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			if(((Activity)context).isDestroyed()){
               return;
            }
		}
		if (!((Activity)context).isFinishing()&&dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
		}
	}
}
