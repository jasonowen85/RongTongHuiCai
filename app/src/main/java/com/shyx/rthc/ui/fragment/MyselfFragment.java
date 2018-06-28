package com.shyx.rthc.ui.fragment;

import com.shyx.rthc.R;
import com.shyx.rthc.common.ServiceConfig;
import com.shyx.rthc.manager.TitleManager;

/**
 * 作者：jason on 2017/6/14 0014 10:15
 * 邮箱：jianglu@eims.com.cn
 * 说明：我的  投资界面
 */
public class MyselfFragment extends QuoteFragment{



    @Override
    public int setTitleStyle(TitleManager titleManager) {
        mBridWebView.loadUrl(ServiceConfig.getRequestUrl(ServiceConfig.MAIN_MYSELE));

        titleManager.setTitleBgColor(R.color.myself_color);
        return R.string.main_tab_myself;
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//       if(hidden){
//          isFirst=false;
//       }else {
//           L.i("重新刷新界面");
//           if (!isFirst)
//                mBridWebView.reload();
//       }
//    }
}