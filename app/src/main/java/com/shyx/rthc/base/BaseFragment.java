package com.shyx.rthc.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.itheima.view.BridgeWebView;
import com.shyx.rthc.BaseApplication;
import com.shyx.rthc.Login.LoginActivity;
import com.shyx.rthc.R;
import com.shyx.rthc.common.ServiceConfig;
import com.shyx.rthc.manager.UiManager;
import com.shyx.rthc.ui.MainActivity;
import com.shyx.rthc.ui.fragment.Myself.DealInquireFragment;
import com.shyx.rthc.ui.fragment.QuoteFragment;
import com.shyx.rthc.ui.fragment.StockInfoFragment;
import com.shyx.rthc.utils.L;
import com.shyx.rthc.utils.StringUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 基本Fragment所有的activity父类，在这里实现一些公共的功能
 *1，实例化context,Resources,LayoutInflater,TitleManager供子类使用
 * 2，创建公共方法
 */
public class BaseFragment extends Fragment {
	protected BaseApplication app;
	protected Resources rs;
	protected BridgeWebView mBridWebView;
	protected LayoutInflater inflater;
	protected String TAG = getClass().getSimpleName();
	protected MainActivity ac;
	protected Boolean isUpdateData = false; //onResume 刷新数据

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		ac = (MainActivity) getActivity();
		L.i(TAG,"onAttach is run");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(enableEventBus()){
			EventBus.getDefault().register(this);
		}
		L.i(TAG,"onCreate is run");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(enableEventBus()){
			EventBus.getDefault().unregister(this);
		}
		L.i(TAG,"onDestroy is run");
	}

	protected boolean enableEventBus(){
		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		app = BaseApplication.getInstance();
		rs = this.getResources();
		this.inflater = inflater;
		L.i(TAG,"onCreateView is run");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mBridWebView !=null)
			mBridWebView.addBridgeInterface(new BaseJavaScriptInterface(ac));
	}

	protected <T extends View> T find(int resId, View view){
		return (T) view.findViewById(resId);
	}

	protected void setText(int id, String txt, View view){
		((TextView)find(id,view)).setText(txt);
	}

	public BridgeWebView getWebView(){
		 return mBridWebView;
	}

	public class BaseJavaScriptInterface{
		private Activity mActivity;

		public BaseJavaScriptInterface(Activity mActivity) {
			this.mActivity = mActivity;
		}

		/**
		 *退出登录 然后重新进入
		 */
		public void abortLogin(String[] params){
			L.i("abortLogin 被js调用" + params[0]);

			UiManager.switcher(ac, LoginActivity.class);
			ac.finish();
		}

		public void changeTabbar(String[] params){//js调用android方法
			L.i("changeTabbar 成功 " + params[0]);
			closeFragment();
			try {
				final JSONObject jsonObject = new JSONObject(params[0]);
				ac.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ac.startMainWithTabParams(MainActivity.TAB_TRANSACTION, jsonObject.optString("url"));
					}
				});

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public void pushDetailsViewWithTitle(String[] strs){//js调用android方法
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(strs[0]);
				String title = jsonObject.optString("title");
				final String url = jsonObject.optString("url");
				L.i("pushDetailsViewWithTitle 被js调用 " + strs[0] );
				Map<String, Object> maps= new HashMap<>();
				maps.put("title", title);
				maps.put("uri", url);
				maps.put("index", MainActivity.TAB_INDEX);
				maps.put("isVisitLeft", true);
				if (url.contains("stockDetail")){
					StockInfoFragment fragment = new StockInfoFragment();
					switchNewFragment(fragment.newInstance(maps, fragment));
				}else if (url.contains("consignSearchPre")){
					toModifyPassWd(strs);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public void pushTransationInquireView(String[] params){
			L.i("pushTransationInquireView 调用 " + params[0]);
			toModifyPassWd(params);
		}

		public void closeSearch(String[] strs) {//js调用android方法
			L.i("closeSearch 被js调用");
			getFragmentManager().popBackStack();
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

		public void loginSuccess(String[] strs) {//js调用android方法
			L.i("main activity重新登录成功 ");
			if (mActivity instanceof MainActivity) {
				closeFragment();
				if (ac.tabPostion() != 0) {
					ac.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ac.selectTabFragment(0);
						}
					});
				}
				QuoteFragment fragment = (QuoteFragment) ac.getSupportFragmentManager().findFragmentByTag(QuoteFragment.class.getSimpleName());
				fragment.getWebView().loadUrl(ServiceConfig.getRequestUrl(ServiceConfig.MAIN_PAGE));
			}
		}

		/**
		 * 网页刷新界面的 方法 url
		 * @param params
		 */
		public void update(String[] params) {
			L.i("刷新界面成功 " + params[0]);
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(params[0]);
				final String url = jsonObject.optString("url");
				if (!StringUtils.isEmpty(url) && mBridWebView != null) {
					ac.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mBridWebView.loadUrl(ServiceConfig.getRequestUrl(url));
						}
					});

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

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
	/**
	 * 关闭自己这个fragment 同时让页面切换到以前的页面中去
	 */
	protected void closeFragment() {
		getFragmentManager().popBackStack();
	}


}
