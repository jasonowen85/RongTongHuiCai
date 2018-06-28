package com.shyx.rthc.manager;

import android.app.Activity;
import android.content.Context;

import com.shyx.rthc.BaseApplication;
import com.shyx.rthc.bus.BusUser;
import com.shyx.rthc.common.Constant;
import com.shyx.rthc.ui.MainActivity;
//import com.shyx.yixun.ui.gesture.CreateGesturePasswordActivity;
import com.shyx.rthc.Login.LoginActivity;
import com.shyx.rthc.utils.EncryptUtil;
import com.shyx.rthc.utils.NetWorkUtil;
import com.shyx.rthc.utils.SpUtils;
import com.shyx.rthc.utils.StringUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * 注册，登陆，获取验证码，异步实现类
 *
 * @author weiyunchao
 */
public abstract class LoginManager {
    public static final String TAG = "LoginManager";

    /**
     * 登陆结果回调函数code为 1登陆成功，其他登陆失败
     *
     * @author weiyunchao
     */
    public interface LoginListener {
        void callBack();
    }

    public interface ErrorListener {
        void onErrorResponse();
    }

    /**
     * 获取验证码回调函数code为 -1获取成功，其他登陆失败
     *
     * @author weiyunchao
     */
    public interface GetVerficationCodeListener {
        void callBack();
    }

    /**
     * 手机号码是否合法
     *
     * @param telNum
     * @return
     */
    public static boolean isMobiPhoneNum(String telNum) {
        String regex = "^(1[2-9][0-9])\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(telNum);
        return m.matches();
    }

    /**
     * 是否已经登陆
     *
     * @param ct
     */
    public static boolean isLogin(Context ct) {
        if (StringUtils.isEmpty(BaseApplication.getInstance().getUserId())) {
            UiManager.switcher(ct, LoginActivity.class);
            return false;
        }
        return true;
    }

    /***
     * 获取验证码网络请求
     */
    public static void getVerficationCode(Context mContext, String account, String scene,
                                          final GetVerficationCodeListener getVerficationCodeListener) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("OPT", Constant.REGISTER);
        map.put("mobile", account);
        map.put("scene", scene);
        NetWorkUtil.volleyHttpGet(mContext, map, new NetWorkUtil.ResponseCallBack() {
            @Override
            public void response(JSONObject obj) {
                if (getVerficationCodeListener != null) {
                    getVerficationCodeListener.callBack();
                }
            }
        }, null);
    }

    /**
     * 用户登录网络请求
     */
    public static void login(final Context mContext, final String account, final String pwd,
                             final LoginListener loginResponse, final ErrorListener errorListener) {
        final BaseApplication app = BaseApplication.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", account);
        map.put("password", pwd);
        map.put("OPT", Constant.LOGIN);
        map.put("deviceType", Constant.DEVICE_TYPE);
        NetWorkUtil.volleyHttpGet(mContext, map, new NetWorkUtil.ResponseCallBack() {
            @Override
            public void response(JSONObject obj) {
                if (loginResponse != null) {
                    loginResponse.callBack();
                }
                app.setUserId(EncryptUtil.decodeSign(obj.optString("userId"),EncryptUtil.USER_ID_SIGN) + "");
                SpUtils.put(mContext, Constant.ACCOUNT, account);
                SpUtils.put(mContext, Constant.PASSWORD, pwd);

//                if (!app.getLockPatternUtils().savedPatternExists()) {
//                    UiManager.switcher(mContext, CreateGesturePasswordActivity.class);
//                }else {
//                       UiManager.switcher(mContext, MainActivity.class);
                    MainActivity.startMainWithTab(mContext,MainActivity.TAB_INDEX);
//                }
                EventBus.getDefault().post(new BusUser());
                ((Activity)mContext).finish();
            }
        }, new NetWorkUtil.ErrorListener() {

            @Override
            public void errorCallBack(String msg) {
                if (errorListener != null) {
                    errorListener.onErrorResponse();
                }

            }
        });
    }
}
