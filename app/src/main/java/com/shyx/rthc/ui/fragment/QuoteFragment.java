package com.shyx.rthc.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shyx.rthc.Login.LoginActivity;
import com.shyx.rthc.R;
import com.shyx.rthc.base.BaseFragment;
import com.shyx.rthc.common.ServiceConfig;
import com.shyx.rthc.manager.TitleManager;
import com.shyx.rthc.manager.UiManager;
import com.shyx.rthc.ui.MainActivity;
import com.shyx.rthc.ui.fragment.Myself.DealInquireFragment;
import com.shyx.rthc.utils.L;
import com.shyx.rthc.utils.WebViewUtil;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：jason on 2017/6/14 0014 10:15
 * 邮箱：jianglu@eims.com.cn
 * 说明：行情显示界面  implements SelectDataFragment
 */
public class QuoteFragment extends BaseFragment {

    protected View rootView; //父布局
    protected TitleManager titleManager;
    protected boolean isFirst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
            L.i(TAG, "onCreateView is run 第二次调用 复用以前的view");
        } else {
            rootView = inflater.inflate(R.layout.fragment_quote, null);
            initView();
        }
        return rootView;
    }

    protected void initView() {
        titleManager = new TitleManager(rootView);
        mBridWebView = find(R.id.web_view_quote, rootView);
//        mBridWebView.addBridgeInterface(new QuoteSctiptInterface(ac));

        WebViewUtil.setWebView(ac,mBridWebView);
        titleManager.setTitleTxt(setTitleStyle(titleManager));
    }

    protected void goStockDetails(String title, String url, int index) {
        Map<String, Object> maps= new HashMap<>();
        maps.put("title", title);
        maps.put("uri", url);
        maps.put("index", index);
        maps.put("isVisitLeft", true);
        StockInfoFragment fragment = new StockInfoFragment();
        switchNewFragment(fragment.newInstance(maps, fragment));
    }

    public int setTitleStyle(TitleManager titleManager) {
        mBridWebView.loadUrl(ServiceConfig.getRequestUrl(ServiceConfig.MAIN_PAGE));
        titleManager.setRightImg(R.drawable.search_white);
        titleManager.setRightLayoutListener(new TitleManager.RightLayoutListener() {
            @Override
            public void rightOnListener() {
                //跳入搜索页面;
                switchNewFragment(new SearchFragment());
            }
        });
        isFirst = true;
        return R.string.main_quote;
    }

    /**
     * @param fragment 要跳转的fragment 可能会有参数传递
     */
    protected void switchNewFragment(Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        getFragmentManager().beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.fragment1, fragment, tag)
                .commit();
    }

    public void receiveDataCallBack(String callBackData) {
        L.i("股票详情页回调结果==" + callBackData);
        if (getArguments() != null) getArguments().clear();
        if (null !=mBridWebView && !StringUtils.isEmpty(callBackData)){
            mBridWebView.loadUrl(ServiceConfig.getRequestUrl(callBackData));
//            mBridWebView. loadDataWithBaseURL(ServiceConfig.getRequestUrl(callBackData),null, null, "utf-8", null);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden){
            isFirst=false;
        }else {
            L.i("重新刷新界面");
            if (!isFirst)
                mBridWebView.reload();
        }
    }

    public class QuoteSctiptInterface{
        private Activity mActivity;

        public QuoteSctiptInterface(Activity mActivity) {
            this.mActivity = mActivity;
        }

        public void pushDetailsViewWithTitle(String[] strs){//js调用android方法
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(strs[0]);
                String title = jsonObject.optString("title");
                String url = jsonObject.optString("url");
                L.i("pushDetailsViewWithTitle 被js调用title " + title +"  url="+ url );
                goStockDetails(title,url,MainActivity.TAB_INDEX);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void pushTransationInquireView(String[] params){
            L.i("pushTransationInquireView 调用 " + params[0]);
            toModifyPassWd(params);
        }


        public void toModifyPassWd(String[] params) {
            L.i("toModifyPassWd  " + params[0]);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(params[0]);
                String title = jsonObject.optString("title");
                Map<String, Object> maps = new HashMap<>();
                if (title.equals("修改密码")) {
                    title= "修改登录密码";
                    maps.put("isVisitLeft", true);
                }
                maps.put("title", title);
                maps.put("uri", jsonObject.optString("url"));
                DealInquireFragment fragment = new DealInquireFragment();
                switchNewFragment(fragment.newInstance(maps, fragment));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         *退出登录 然后重新进入
         */
        public void abortLogin(String[] params){
            L.i("abortLogin 被js调用" + params[0]);
            UiManager.switcher(ac, LoginActivity.class);
            ac.finish();
        }
    }


//
//    @Override
//    public void transferDatas(int tabIndex, Object obj) {
//
//    }

}
