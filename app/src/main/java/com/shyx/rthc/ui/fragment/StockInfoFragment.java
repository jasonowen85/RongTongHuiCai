package com.shyx.rthc.ui.fragment;

import android.widget.ProgressBar;

import com.shyx.rthc.R;
import com.shyx.rthc.manager.TitleManager;
import com.shyx.rthc.ui.fragment.Myself.DealInquireFragment;

/**
 * 作者：jason on 2017/6/15 0014 10:15
 * 邮箱：jianglu@eims.com.cn
 * 说明：股票详情页面
 */
public class StockInfoFragment extends DealInquireFragment {


    private ProgressBar pg1;

    @Override
    protected void initView() {
        super.initView();
        titleManager.setRightLayout(0, R.drawable.refresh_icon, new TitleManager.RightLayoutListener() {
            @Override
            public void rightOnListener() {
//                mBridWebView.loadUrl("{'111' : '110'}");
                //loadUrl("javascript:js的方法名(根据需要传参数,一般可以传json)");
//                mBridWebView.loadUrl("javascript:reloadPage()");
                mBridWebView.reload();
//                mBridWebView.loadUrl(ServiceConfig.getRequestUrl(getArguments().getString("uri")));
//                mBridWebView.callHandler("callNative", "hello H5, 我是java", new JavaCallHandler() {
//                });
//                mBridWebView.callbackJavaScript();
            }
        });

//        pg1 = (ProgressBar) rootView.findViewById(R.id.web_view_progress_bar1);
//        mBridWebView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if(newProgress==100){
//                    NetWorkUtil.dialogDissmiss();
//                    pg1.setVisibility(View.GONE);//加载完网页进度条消失
//                }
//                else{
//                    NetWorkUtil.dialogShow(ac);
//                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
//                    pg1.setProgress(newProgress);//设置进度值
//                }
//
//            }
//        });
    }

//    @Override
//    protected boolean enableEventBus() {
//        return true;
//    }
//
//    public void onEvent(String event) {
//        T.showShort(getActivity(), event);
//    }

}