package com.shyx.rthc.ui.fragment.Myself;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.view.BridgeWebView;
import com.shyx.rthc.R;
import com.shyx.rthc.base.BaseFragment;
import com.shyx.rthc.common.ServiceConfig;
import com.shyx.rthc.manager.SelectDataFragment;
import com.shyx.rthc.manager.TitleManager;
import com.shyx.rthc.ui.MainActivity;
import com.shyx.rthc.utils.WebViewUtil;

import java.util.Iterator;
import java.util.Map;

/**
 * 作者：jason on 2017/6/16 0016 18:12
 * 邮箱：jianglu@eims.com.cn
 * 说明：
 */
public class DealInquireFragment extends BaseFragment {
    protected View rootView; //父布局
    protected SelectDataFragment listener;
    protected TitleManager titleManager;


    public static Fragment newInstance(Map<String, Object> maps, BaseFragment fragment) {
        if (maps != null) {
            Bundle args = new Bundle();
            Iterator<String> keys = maps.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Object value = maps.get(key);
                if (value instanceof String) {
                    args.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    args.putInt(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    args.putBoolean(key, (Boolean) value);
                }
                fragment.setArguments(args);
            }
        }
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quote, null);
        initView();// 控件初始化
        return rootView;
    }


    protected void initView() {
        boolean isVisitLeft = false ;
        int index = MainActivity.TAB_INDEX;

        titleManager = new TitleManager(rootView);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            isVisitLeft = bundle.getBoolean("isVisitLeft", false);
            index = bundle.getInt("index");
            String titleId = getArguments().getString("title");
            titleManager.setTitleTxt(titleId);
        }
        ac.getTransferFragment(index);
        mBridWebView = (BridgeWebView)rootView.findViewById(R.id.web_view_quote);
//        mBridWebView.addBridgeInterface(new InquireJavaScriptInterface(ac));
        WebViewUtil.setWebView(ac, mBridWebView);

        mBridWebView.loadUrl(ServiceConfig.getRootUrl() + getArguments().getString("uri"));

        if (isVisitLeft) {
            titleManager.setLeftImageClickListener(R.drawable.go_back, new TitleManager.LeftLayoutListener() {
                @Override
                public void leftOnListener() {
                    closeFragment();
                }
            });
        }
    }
}
