package com.shyx.rthc.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class HomeWatchManager {
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static  final String TAG = "HomeListener";
    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener; 
    private InnerRecevier mRecevier; 
 
    // 回调接口  
    public interface OnHomePressedListener { 
        public void onHomePressed(); 
 
        public void onHomeLongPressed(); 
    } 
 
    public HomeWatchManager(Context context) {
        mContext = context; 
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    } 
 
    /**
     * 设置监听
     * 
     * @param listener
     */ 
    public void setOnHomePressedListener(OnHomePressedListener listener) { 
        mListener = listener; 
        mRecevier = new InnerRecevier(); 
    } 
 
    /**
     * 开始监听，注册广播
     */ 
    public void startWatch() { 
        if (mRecevier != null) { 
            mContext.registerReceiver(mRecevier, mFilter); 
        } 
    } 
 
    /**
     * 停止监听，注销广播
     */ 
    public void stopWatch() { 
        if (mRecevier != null) { 
            mContext.unregisterReceiver(mRecevier); 
        } 
    }
    
    class InnerRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) { 
                    if (mListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) { 
                            // 短按home键  
                            mListener.onHomePressed(); 
                        } else if (reason 
                                .equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) { 
                            // 长按home键  
                            mListener.onHomeLongPressed(); 
                        } 
                    } 
                } 
            } 
        } 
    } 
}