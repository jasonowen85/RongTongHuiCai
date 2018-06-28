package com.shyx.rthc.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.shyx.rthc.R;

/**
 * 输入密码对话框，在需要重置手势密码时弹出
 * @author weiyunchao
 *
 */
public class P2pAlertDialog {
	private Context context;
	public Dialog mDialog;
	private LinearLayout mLinearLayout;
	private TextView mTxtTitle;
	private TextView mTxtMsg;
	private Button mBtnNeg;
	private Button mBtnPos;
	private Display display;
	private View view;
	private Animation mShakeAnim;

	public P2pAlertDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public P2pAlertDialog builder() {
		// 获取Dialog布局
		view = LayoutInflater.from(context)
				.inflate(R.layout.dialog_alert, null);

		// 获取自定义Dialog布局中的控件
		mLinearLayout = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		mTxtTitle = (TextView) view.findViewById(R.id.txt_title);
		mTxtMsg = (TextView) view.findViewById(R.id.txt_msg);
		mBtnNeg = (Button) view.findViewById(R.id.btn_neg);
		mBtnPos = (Button) view.findViewById(R.id.btn_pos);
		mBtnPos.setBackgroundResource(R.drawable.alertdialog_right_selector);
		mBtnNeg.setBackgroundResource(R.drawable.alertdialog_left_selector);
		// 定义Dialog布局和参数
		mDialog = new Dialog(context, R.style.AlertDialogStyle);
		mDialog.setContentView(view);

		// 调整dialog背景大小
		mLinearLayout.setLayoutParams(new FrameLayout.LayoutParams(
				(int) (display.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

		return this;
	}

	/**
	 * 输入密码错误，晃动对话框提示错误
	 */
	public void dialogAnimation() {
		mShakeAnim = AnimationUtils.loadAnimation(context, R.anim.shake_x);
		view.startAnimation(mShakeAnim);
	}

	/**
	 * 对标题赋值，如果没有赋值，默认显示为“标题”
	 * 
	 * @param title
	 * @return
	 */
	public P2pAlertDialog setTitle(String title) {

		if ("".equals(title)) {
			mTxtTitle.setText("标题");

		} else {
			mTxtTitle.setText(title);
		}
		mTxtMsg.setTextColor(Color.BLACK);
		return this;
	}



	/**
	 * 如果密码输入错误，给予提示密码错误
	 * 
	 * @param title
	 * @return
	 */
	public P2pAlertDialog setErrorTitle(String title) {

		mTxtTitle.setText(title);
		mTxtTitle.setTextColor(Color.RED);
		return this;
	}


	/**
	 * 设置msg内容
	 * 
	 * @param msg
	 * @return
	 */
	public P2pAlertDialog setMsg(String msg) {
		if ("".equals(msg)) {
			mTxtMsg.setText("内容");
		} else {
			mTxtMsg.setText(msg);
		}
		return this;
	}

	/**
	 * 获取msg内容
	 * 
	 * @return
	 */
	public String getMsg() {
		if (mTxtMsg != null) {
			return mTxtMsg.getText().toString();
		}
		return "";

	}

	/**
	 * 右边确定按钮监听
	 * 
	 * @param text
	 * @param listener
	 * @return
	 */
	public P2pAlertDialog setPositiveButton(String text,
			final OnClickListener listener) {
		if ("".equals(text)) {
			mBtnPos.setText("确定");
		} else {
			mBtnPos.setText(text);
		}
		mBtnPos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
			}
		});
		return this;
	}

	/**
	 * 左边取消按钮监听,默认显示文字为取消，用户可以根据自定义显示文字
	 * 
	 * @param text
	 * @param listener
	 * @return
	 */
	public P2pAlertDialog setNegativeButton(String text,
			final OnClickListener listener) {
		if ("".equals(text)) {
			mBtnNeg.setText("取消");
		} else {
			mBtnNeg.setText(text);
		}
		mBtnNeg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				mDialog.dismiss();
			}
		});
		return this;
	}
	/**
	 * 监听Msg EditTxt输入的密码是否正确，不正确提示密码错误，当重新输入密码时，错误提示消失
	 * @return
	 */
	public P2pAlertDialog setEditListener(final String rightTitleTxt, final String errorMsgTxt){
		mTxtMsg.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
										  int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
				String content = editable.toString().replaceAll(" ", "");
				if(!content.equals(errorMsgTxt)){
					mTxtTitle.setText(rightTitleTxt);
					mTxtTitle.setTextColor(Color.BLACK);
				}
				
			}
		});
		return this;
	}

	/**
	 * 显示对话框
	 */
	public void show() {
		mDialog.show();
	}

	/**
	 * 取消显示对话框
	 */
	public void dismiss() {
		mDialog.dismiss();
	}
}
