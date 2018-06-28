package com.shyx.rthc.ui.fragment;

import com.shyx.rthc.R;
import com.shyx.rthc.common.ServiceConfig;
import com.shyx.rthc.manager.TitleManager;

/**
 * 作者：jason on 2017/6/14 0014 10:15
 * 邮箱：jianglu@eims.com.cn
 * 说明： 更多
 */
public class MoreFragment extends QuoteFragment{
    @Override
    public int setTitleStyle(TitleManager titleManager) {
        mBridWebView.loadUrl(ServiceConfig.getRequestUrl(ServiceConfig.MAIN_MORE));
        return R.string.main_tab_my_more;
    }
}