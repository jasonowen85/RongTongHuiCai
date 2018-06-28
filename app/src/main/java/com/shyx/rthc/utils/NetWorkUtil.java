package com.shyx.rthc.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shyx.rthc.BaseApplication;
import com.shyx.rthc.common.Constant;
//import com.shyx.yixun.manager.DialogManager;
import com.shyx.rthc.manager.LoginManager;
import com.shyx.rthc.widget.AlertProDialog;
//import com.shyx.yixun.ui.gesture.UnlockGesturePasswordActivity;
//import com.shyx.yixun.widget.pulltorefresh.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

public class NetWorkUtil {


    private static final String TAG = "NetWorkUtil";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    private static AlertProDialog dialog;// 等待进度条

    /**
     * 判断网络是否可用 true 有网络 false 网络异常
     *
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Context context) {

        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {

                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断WIFI是否打开
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断是wifi还是3g网络
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 网络请求返回值回调函数
     *
     * @author weiyunchao
     */
    public interface ResponseCallBack {
        public void response(JSONObject obj);
    }

    /**
     * 网络请求返回值回调函数
     *
     * @author weiyunchao
     */
    public interface ErrorListener {
        public void errorCallBack(String msg);
    }

    /**
     * 显示加载进度条
     *
     * @param context
     */
    public static void dialogShow(Context context) {
        if (dialog == null) {
            dialog = new AlertProDialog(context);
            dialog.builder().show();
        }
    }

    /**
     * 隐藏加载进度条
     */
    public static void dialogDissmiss() {
        if (dialog != null) {
            dialog.dissmiss();
            dialog = null;
        }
    }

    /**
     * 使用volley开源框架实现httpGet请求+异常完成刷新view
     */
//    public static void volleyHttpGet(final Context context, Map<String, String> map,
//                                     final ResponseCallBack callback, final ErrorListener errorListener, final PullToRefreshBase rfs) {
//        volleyHttpGet(context, map, callback, new ErrorListener() {
//                    @Override
//                    public void errorCallBack(String msg) {
//                        if (errorListener != null) errorListener.errorCallBack(msg);
//                        if (rfs != null ) rfs.onRefreshComplete();
//                    }
//                }
//        );
//    }

    /**
     * 使用volley开源框架实现httpGet请求获取Json对象
     */
    public static void volleyHttpGet(final Context context, Map<String, String> map,
                                     final ResponseCallBack callback, final ErrorListener errorListener) {

        dialogShow(context);
        map.put("body", "");
        String encryptUrl = EncryptUtil.encryptUrl(map);
        L.d(TAG, encryptUrl);
        RequestQueue queue = BaseApplication.getInstance().getRequestQueue();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Method.GET, encryptUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        if (null == context || ((Activity) context).isFinishing()) {
                            return;
                        }
                        dialogDissmiss();
                        if (obj != null) {
                            Result(context, obj, callback);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogDissmiss();
                if (errorListener != null) {
                    errorListener.errorCallBack(getErrorMsg(error));
                }
                errorMsg(context, error);
            }

        });
        queue.add(jsonRequest);
    }

    /**
     * 请求结果
     *
     * @param context
     * @param obj
     * @param callback
     */
    private static void Result(Context context, JSONObject obj, ResponseCallBack callback) {
        int code = obj.optInt("code");
        String msg = obj.optString("msg");
        L.i(TAG, obj.toString());
        PrintJson.print(obj, TAG);
        switch (code) {
            case Constant.RESULT_OK:
                callback.response(obj);
                break;
            case Constant.NOT_REAL_NAME:
//                if(ServiceConfig.platform.equals(ServiceConfig.PayPlatform.FUYOU)){
//                    DialogManager.openRealNameDialog(((Activity)context),msg);
//                }else{
//                    DialogManager.openAccountDialog(((Activity) context), msg);
//                }
                break;
            case Constant.NOT_BIND_BANKCARD:
//                DialogManager.bindCardDialog(((Activity) context), msg);
                break;
            case Constant.NOT_OPEN_PAYMENTACCOUNT:
//                DialogManager.openAccountDialog(((Activity) context), msg);
                break;
            case Constant.TIME_OUT:
                if (!LoginManager.isLogin(context)) {
                    return;
                } else {
//                    UiManager.switcher(context, UnlockGesturePasswordActivity.class);
//                DialogManager.timeOutDialog(context,msg);
                }
                break;
            default:
                T.showToast(context, msg);
                break;
        }
    }


    /**
     * 提示错误信息
     *
     * @param context
     * @param error
     */
    public static void errorMsg(final Context context, VolleyError error) {
        Throwable th = error.getCause();
        String msg = getErrorMsg(error);
        if (th != null) {
            if (th instanceof ConnectException) {
                T.showShort(context, msg);
            }
            if (th instanceof UnknownHostException) {
                T.showShort(context, msg);
            }
            if (th instanceof JSONException) {
                T.showShort(context, msg);
            }
            if (th instanceof SocketException)
                T.showShort(context, msg);
        } else {
            T.showShort(context, msg);
        }
        L.e("DataHander request Error! msg:" + error.getMessage() + "\n cause:"
                + error.getCause() + "\n" + " local msg:"
                + error.getLocalizedMessage());
    }

    /**
     * 获取错误信息
     *
     * @param error
     * @return
     */
    public static String getErrorMsg(VolleyError error) {

        Throwable th = error.getCause();
        if (th != null) {
            if (th instanceof ConnectException) {
                return "无网络连接";
            }
            if (th instanceof UnknownHostException) {
                return "网络异常";
            }
            if (th instanceof JSONException) {
                return "服务器错误";
            }
            if (th instanceof SocketException) {
                return "链接超时";
            }
        }
        return "服务器维护中o(︶︿︶)o";
    }


}
