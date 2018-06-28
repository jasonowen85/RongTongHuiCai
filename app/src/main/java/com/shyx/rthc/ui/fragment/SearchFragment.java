package com.shyx.rthc.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.view.BridgeWebView;
import com.shyx.rthc.R;
import com.shyx.rthc.base.BaseFragment;
import com.shyx.rthc.common.ServiceConfig;
import com.shyx.rthc.utils.WebViewUtil;

/**
 * 作者：jason on 2017/6/14 0014 10:15
 * 邮箱：jianglu@eims.com.cn
 * 说明：搜索界面支持下拉的选择
 */
public class SearchFragment extends BaseFragment {
    private View layout; //父布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        layout = inflater.inflate(R.layout.fragment_search, container, false);
        initView();
        return layout;
    }

    private void initView() {
        mBridWebView = (BridgeWebView) layout.findViewById(R.id.web_view_search);
//        mBridWebView.addBridgeInterface(new SearchJavaSctiptInterface(ac));
        WebViewUtil.setWebView(ac, mBridWebView);
        mBridWebView.loadUrl(ServiceConfig.getRequestUrl(ServiceConfig.MAIN_SEARCH));

//        EditText inputEdit = (EditText) layout.findViewById(R.id.input_search);
//        layout.findViewById(R.id.right_txt).setOnClickListener(this);
//        inputEdit.addTextChangedListener(mTextWatcher);
    }




}