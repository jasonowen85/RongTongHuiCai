package com.shyx.rthc.ui.fragment;

import com.shyx.rthc.R;
import com.shyx.rthc.common.ServiceConfig;
import com.shyx.rthc.manager.TitleManager;
import com.shyx.rthc.utils.L;

import org.apache.commons.lang3.StringUtils;

/**
 * 作者：jason on 2017/6/14 0014 10:15
 * 邮箱：jianglu@eims.com.cn
 * 说明：交易首页
 */
public class TransactionFragment extends QuoteFragment {
    private Object objData;

    @Override
    public int setTitleStyle(TitleManager titleManager) {
        mBridWebView.loadUrl(ServiceConfig.getRequestUrl(ServiceConfig.MAIN_TRANSATION));
        return R.string.main_tab_transaction;
    }

    @Override
    protected void initView() {
        super.initView();
        if (getArguments() != null) {
            String params = getArguments().getString("params");
            L.i("getArguments  id=" + params);
            if (!StringUtils.isEmpty(params))
                mBridWebView.loadUrl(ServiceConfig.getRequestUrl(params));
        }
    }

}
