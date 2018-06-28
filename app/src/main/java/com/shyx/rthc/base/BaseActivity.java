package com.shyx.rthc.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.shyx.rthc.BaseApplication;
import com.shyx.rthc.R;
import com.shyx.rthc.manager.HomeWatchManager;
import com.shyx.rthc.manager.TitleManager;
import com.shyx.rthc.utils.L;
import com.shyx.rthc.utils.SystemStatusUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import qiu.niorgai.StatusBarCompat;

/**
 * 基本acitivity所有的activity父类，在这里实现一些公共的功能
 *1，实例化context,Resources,LayoutInflater,TitleManager供子类使用
 * 2，监听灭屏时间，如果超过5分钟退出登陆，再次打开跳转到手势密码页面进行登陆
 * @author weiyunchao
 */
public class BaseActivity extends FragmentActivity {

    protected int time;// 灭屏时间
    protected Timer timer;// 倒计时定时器，超过5分钟计算重新登录，不超过，输完手势密码直接进入退出时的页面
    protected Activity context;
    private HomeWatchManager mHomeWatcher;//广播监听器
    private boolean DownHomeKey;//是否按home键灭屏
    protected Resources rs;
    protected BaseApplication app;
    protected TitleManager titleManager;
    protected LayoutInflater inflater;
    protected boolean isSetStatusBar = true;
    protected String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        time = 60;
        if(isSetStatusBar){
//            SystemStatusUtil.setTranslucentStatus(this, R.color.orange);//设置android状态栏沉浸模式颜色为橙色
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        app = BaseApplication.getInstance();
        app.addActivity(this);
        context = this;
        rs = this.getResources();
        inflater = LayoutInflater.from(this);
        titleManager = new TitleManager(this);

        if(enableEventBus()){
            EventBus.getDefault().register(this);
        }

        if(getLayoutRes() != 0){
            setContentView(getLayoutRes());
            ButterKnife.bind(this);
        }

        if(isSetStatusBar){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                StatusBarCompat.setStatusBarColor(this, rs.getColor(R.color.my_theme));
            }else {
                SystemStatusUtil.setTranslucentStatus(this, R.color.my_theme);
            }
        }

        L.i(TAG, "onCreate() is run");
        initView();
    }

    protected String getRunningActivityName() {
        String contextString = context.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }


    /**
     * 返回布局文件
     * @return
     */
    protected int getLayoutRes(){
        return 0;
    }

    protected void initView(){

    }

    protected boolean enableEventBus(){
        return false;
    }


    protected <T extends View> T find(int resId){
        return (T) findViewById(resId);
    }

    protected void setText(int id,String txt){
        ((TextView) find(id)).setText(txt);
    }


    /**
     * 注册Home键的监听
     */
    protected void registerHomeListener() {
        mHomeWatcher = new HomeWatchManager(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatchManager.OnHomePressedListener() {

            @Override
            public void onHomePressed() {
                //TODO 进行点击Home键的处理
                BaseApplication.getInstance().setActivity(context);
                DownHomeKey = true;
                timerManager(new TimerListener() {
                    @Override
                    public void listener() {
                        --time;
                        if (time == 0) {
                            timer.cancel();
                        }
                    }
                });
//                timer();
            }

            @Override
            public void onHomeLongPressed() {
                //TODO 进行长按Home键的处理
            }
        });
        mHomeWatcher.startWatch();//注册广播
    }

    public interface TimerListener{
        public void listener();

    }

    /**
     * 倒计时定时器，灭屏超过5分钟重新登录
     */
//    protected void timer() {
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//
//                --time;
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        Log.e("time", time + "");
//                    }
//                });
//
//                if (time == 0) {
//                    timer.cancel();
//                }
//            }
//        }, 1000, 1000);
//    }

    /**
     * 倒计时定时器，灭屏超过5分钟重新登录
     */
    protected void timerManager(final TimerListener timerListener){
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                timerListener.listener();
            }
        }, 1000, 1000);
    }


    @Override
    protected void onResume() {

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
        registerHomeListener();
        L.i(TAG, "onResume() is run");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L.i(TAG, "onRestart() is run");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHomeWatcher != null) {
            mHomeWatcher.stopWatch();
        }

        Activity activity = app.getActivity();
        if (activity == null) {
            DownHomeKey = false;

        }
        L.i(TAG, "onRestart() is run");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }

        if(enableEventBus()){
            EventBus.getDefault().unregister(this);
        }

        ButterKnife.unbind(this);
        L.i(TAG, "onDestroy() is run");
    }

    //判断是否全部输入
    protected final List<EditText> mInputViews = new ArrayList<>();
    protected boolean mAllNotEmpty;
    protected InputWatcher mInputWatcher;

    protected void watchEditText(EditText editText) {
        if (mInputWatcher == null) {
            mInputWatcher = new InputWatcher();
        }
        mInputViews.add(editText);
        editText.addTextChangedListener(mInputWatcher);
    }
    /**
     * 输入框空白状态发生变化时回调
     * @param allNotEmpty
     */
    protected void onInputEmptyChanged(boolean allNotEmpty) {
    }

    public class InputWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {
            boolean allNotEmpty = true;
            for (int i = 0; i < mInputViews.size(); i++) {
                if(TextUtils.isEmpty(mInputViews.get(i).getText())){
                    allNotEmpty = false;
                    break;
                }
            }
            if (mAllNotEmpty != allNotEmpty) {
                onInputEmptyChanged(allNotEmpty);
                mAllNotEmpty = allNotEmpty;
            }

        }
    }
}
