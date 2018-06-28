package com.shyx.rthc;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.shyx.rthc.Login.LoginActivity;
import com.shyx.rthc.bus.BusUser;
import com.shyx.rthc.common.Constant;
import com.shyx.rthc.common.ServiceConfig;
import com.shyx.rthc.entites.MemberInfo;
import com.shyx.rthc.utils.EncryptUtil;
import com.shyx.rthc.utils.SpUtils;
import com.shyx.rthc.utils.StringUtils;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者：jason on 2017/6/13 0013 15:02
 * 邮箱：jianglu@eims.com.cn
 * 说明：
 */
public class BaseApplication extends Application {

    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";
    //是否必须登录才可访问
    public static final boolean NECESSARY_LOGINED = false;

    // 创建application对象
    private static BaseApplication mInstance;
    //Activity栈保存打开的activity，退出时关闭所有的activity
    private List<Activity> mList = new LinkedList<Activity>();
//    // 手势解锁工具类
//    private LockPatternUtils mLockPatternUtils;
    // volley队列
    private RequestQueue requestQueue;
    //UserId根据userId获取用户信息
    private String userId;
    //账号信息
    private MemberInfo accountInfo;
    //保存activity
    private Activity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //release 模式才启用Bugly
        if (ServiceConfig.SERVICE_MODE != ServiceConfig.Mode.debug){
            Log.d("Application", "enable Bugly");
            CrashReport.initCrashReport(getApplicationContext(), "6165cac06b", true);
        }

    }

    /**
     * 创建application便于没有application的类中调用
     *
     * @return
     */
    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    /**
     * add Activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (mList != null) {
            mList.add(activity);
        }

    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    /**
     * 退出登陆
     */
    public void exit() {
        BaseApplication.getInstance().setUserId("");
        EventBus.getDefault().post(new BusUser());
//        clearLockPatern();
    }

    //退出app 清除账号信息
    public void exitApp() {
        BaseApplication.getInstance().setUserId("");
        EventBus.getDefault().post(new BusUser());
//        clearLockPatern();
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }


    /**
     * 注销账户
     */
    public void Logout(Context context) {
        exit();
        if (NECESSARY_LOGINED) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            ((Activity) context).finish();
        } else {
//            MainActivity.startMainWithTab(context, MainActivity.TAB_INDEX);
        }

    }

    /**
     * 获取volley请求队列
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.start();
        }
        return requestQueue;
    }

    /**
     * 添加网络请求到请求队列中， 请求队列可以并发的向服务器发送请求， 对网络请求设置tag可以根据tag取消网络请求
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * 添加网络请求到请求队列中， 请求队列可以并发的向服务器发送请求，不设置tag
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {

        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * 根据tag取消相应的网络请求
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    public String getUserId() {

        if(!TextUtils.isEmpty(userId)){
            return EncryptUtil.addSign(Long.valueOf(userId), EncryptUtil.USER_ID_SIGN);
        }else if(!TextUtils.isEmpty((CharSequence) SpUtils.get(getApplicationContext(), Constant.USERID, ""))){
            return EncryptUtil.addSign(Long.valueOf((String) SpUtils.get(getApplicationContext(),Constant.USERID,"")), EncryptUtil.USER_ID_SIGN);
        }else{
            return "";
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
        SpUtils.put(getApplicationContext(), Constant.USERID, userId);
    }

    /**
     * 标识当前用户是否登录
     *
     * @return
     */
    public boolean isUserLogin() {
        return !StringUtils.isEmpty(getUserId());
    }

}
