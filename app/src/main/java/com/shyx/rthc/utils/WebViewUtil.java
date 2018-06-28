package com.shyx.rthc.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shyx.rthc.R;
import com.shyx.rthc.common.Constant;
import com.shyx.rthc.manager.DialogManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by pc on 2016/11/1.
 */
public class WebViewUtil {

    /**
     * 设置webview属性
     * @param webView
     */


    public static void setWebView(final Activity context, WebView webView){
        if(null==webView){return;}

        //允许弹出网页对话框
        webView.setWebChromeClient(new WebChromeClient());
        //设置初始缩放比例
       // webView.setInitialScale(100);

        // 设置WebView的一些缩放功能点:
        WebSettings settings = webView.getSettings();

        //扩大比例的缩放
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setDisplayZoomControls(false);//隐藏webview缩放按钮
//        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
//        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //设置 缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // 在同种分辨率的情况下,屏幕密度不一样的情况下,自动适配页面:
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int scale = dm.densityDpi;
        if (scale == 240) { //
            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (scale == 160) {
            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }  else if(scale == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(scale == DisplayMetrics.DENSITY_XHIGH){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (scale == DisplayMetrics.DENSITY_TV){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

        settings.setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
       // settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.setWebChromeClient(new WebChromeClient()); // new MyWebChromeClient(context)
        // 只在本应用中跳转不打开浏览器
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受证书
            }

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return loadingWeb(context,view, url);
            }

            //开始加载html页面，
            //获取加载的html路径，判断是否开户成功，如果开户成功跳转到手势密码界面
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                L.e("onPageFinished", url);
                NetWorkUtil.dialogDissmiss();
                if (!StringUtils.isEmpty(url)&& url.contains("stockSearch")){
                    view.requestFocus();
                    view.loadUrl("javascript:getFocus()");
                }
            }

            //html页面加载开始
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                if(url.contains("bankcard/addCard") || url.contains("result.code=1")){
                if (url.contains("result.code=1")) {
                    context.setResult(Constant.RESULT_OK, new Intent());
                    context.overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                    context.finish();
                }
                L.e("onPageStarted", url);
                NetWorkUtil.dialogShow(context);
            }
        });

    }
    /**
     * 加载网页
     *
     * @param view
     * @param url
     * @return
     */
    private static boolean loadingWeb(Activity context,WebView view, String url) {
        //如果请求失败弹出对话框给以提示
        if (url.contains("payment/redirectapp?") && !url.contains("result.code=1")) {
            int start = url.indexOf("%");
            int end = url.lastIndexOf("&");
            String msg = "";
            try {
                msg = URLDecoder.decode(url.substring(start, end), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            DialogManager.webInfoErrorDialog(context, msg);
            return true;
            //请求成功关闭页面返回上个页面
        }
        view.loadUrl(url);
        return true;
    }

    static class MyWebChromeClient extends WebChromeClient {

        private Activity mActivity;
        private MyWebChromeClient(Activity mActivity){
            this.mActivity = mActivity;
        }

        @Override
        public boolean onJsAlert(WebView view, String url, final String message, JsResult result) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(mActivity, R.style.AlertDialogCustom)
                            .setTitle("提示")
                            .setMessage(message)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
            result.confirm();//这里必须调用，否则页面会阻塞造成假死
            return true;
        }
    }


}
