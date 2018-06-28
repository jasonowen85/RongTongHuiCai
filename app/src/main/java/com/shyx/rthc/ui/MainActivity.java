package com.shyx.rthc.ui;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.itheima.view.BridgeWebView;
import com.shyx.rthc.R;
import com.shyx.rthc.base.BaseActivity;
import com.shyx.rthc.base.BaseFragment;
import com.shyx.rthc.common.Constant;
import com.shyx.rthc.manager.DialogManager;
import com.shyx.rthc.ui.fragment.MoreFragment;
import com.shyx.rthc.ui.fragment.QuoteFragment;
import com.shyx.rthc.ui.fragment.MyselfFragment;
import com.shyx.rthc.ui.fragment.TransactionFragment;
import com.shyx.rthc.utils.AppUtils;
import com.shyx.rthc.utils.L;
import com.shyx.rthc.utils.NetWorkUtil;
import com.shyx.rthc.utils.T;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import qiu.niorgai.StatusBarCompat;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    @Bind(R.id.main_tab_group)
    RadioGroup mTabGroup;
//    private int backNum;

    public static final int TAB_INDEX = 0;
    public static final int TAB_TRANSACTION = 1;
    public static final int TAB_MYSELF = 2;
    public static final int TAB_MORE = 3;

    private int mCurrentTab = TAB_INDEX; //默认选项
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment[] mFragments;
    private int last = -1;

    private boolean promotionType = false;//是否强制更新
    private String apkPath;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }


    public void startMainWithTabParams(int targetTab, Object params) {
        if (targetTab >= 0) {
            mTabGroup.check(getCheckId(targetTab));
            //数据怎么办给fragment
            if (params!=null) {
                //activity 是否add  fragment
                if (getSupportFragmentManager().findFragmentByTag(Integer.toString(targetTab)) == null) {
                    Bundle args = new Bundle();
                    L.i("这里是通过tab index 传递数据" + params);
                    args.putString("params", (String)params);
                    mFragments[targetTab].setArguments(args);
                } else { //1 通过eventbus   2 接口回调  3 activity  4 arguments
                    ((QuoteFragment) mFragments[targetTab]).
                            receiveDataCallBack((String)params);
//                    EventBus.getDefault().post(params+"eventBus");
                }
            }
        }
    }

    /**
     * 打开MainActivity指定的页面 不带参数
     *
     * @param context
     * @param targetTab 需要显示的页面
     */
    public static void startMainWithTab(Context context, int targetTab) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("targetTab", targetTab);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setupFragment();
//        if (!ServiceConfig.isDebugMode()) {
//            upgrade();
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            mTabGroup.check(getCheckId(intent.getIntExtra("targetTab", TAB_INDEX)));
        }
    }

    /**
     * 初始化各个模块的fragment添加到viewPager中
     */
    private void setupFragment() {
        fragmentManager = getSupportFragmentManager();
        mFragments = new Fragment[4];
        mFragments[0] = new QuoteFragment();
        mFragments[1] = new TransactionFragment();
        mFragments[2] = new MyselfFragment();
        mFragments[3] = new MoreFragment();
        for (int i = 0; i < 4; i++)
            mFragments[i].setRetainInstance(true);
        mTabGroup = (RadioGroup) findViewById(R.id.main_tab_group);
        mTabGroup.setOnCheckedChangeListener(this);
        RadioButton radio = (RadioButton) findViewById(R.id.main_tab_quote);
        radio.setOnClickListener(radioClick);
        findViewById(R.id.main_tab_transaction).setOnClickListener(radioClick);
        findViewById(R.id.main_tab_myself).setOnClickListener(radioClick);
        findViewById(R.id.main_tab_more).setOnClickListener(radioClick);
        radio.performClick();
    }

    private View.OnClickListener radioClick = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            getSupportFragmentManager().popBackStack();
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_tab_quote:
                mCurrentTab = TAB_INDEX;
                break;
            case R.id.main_tab_transaction:
                mCurrentTab = TAB_TRANSACTION;
                break;
            case R.id.main_tab_myself:
//                if (!LoginManager.isLogin(context)) {
//                    group.check(getCheckId(mCurrentTab));
//                    return;
//                }
                mCurrentTab = TAB_MYSELF;
                break;
            case R.id.main_tab_more:
                mCurrentTab = TAB_MORE;
                break;
            default:
                break;
        }
        if(last == mCurrentTab) return;

        fragmentTransaction = fragmentManager.beginTransaction();
        if (fragmentManager.findFragmentByTag(Integer.toString(mCurrentTab)) == null) {
            if (last >= 0) fragmentTransaction.hide(mFragments[last]);
            fragmentTransaction.add(R.id.fragment1, mFragments[mCurrentTab], Integer.toString(mCurrentTab));
        } else {
            fragmentTransaction.hide(mFragments[last]);
            fragmentTransaction.show(mFragments[mCurrentTab]);
        }
        fragmentManager.popBackStack();
        fragmentTransaction.commit();
        StatusBarCompat.setStatusBarColor(this, mCurrentTab==2 ? rs.getColor(R.color.myself_color) : rs.getColor(R.color.my_theme));
        last = mCurrentTab;
    }


    private int getCheckId(int index) {
        int checkId;
        switch (index) {
            case TAB_INDEX:
            default:
                checkId = R.id.main_tab_quote;
                break;
            case TAB_TRANSACTION:
                checkId = R.id.main_tab_transaction;
                break;
            case TAB_MYSELF:
                checkId = R.id.main_tab_myself;
                break;
            case TAB_MORE:
                checkId = R.id.main_tab_more;
                break;
        }
        return checkId;
    }



    /**
     * 自动检查升级
     */
    private void upgrade() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", Constant.UPDATE_VERSION);
        map.put("deviceType", Constant.DEVICE_TYPE);
        NetWorkUtil.volleyHttpGet(this, map, new NetWorkUtil.ResponseCallBack() {
            @Override
            public void response(JSONObject obj) {
                String version = obj.optString("version");
                promotionType = obj.optBoolean("promotionType");
                apkPath = obj.optString("apk_path");
                String localVersion = AppUtils.getVersionName(context);
                if (Integer.parseInt(version.replace(".", "")) > Integer.parseInt(localVersion.replace(".", ""))) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    }else{
                        DialogManager.updateDialog(context, promotionType, apkPath);
                    }
                }
            }
        }, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限通过
                DialogManager.updateDialog(context, promotionType, apkPath);
            } else {
                // 权限禁止
//                T.showShort(context,"该操作需要读写SD卡权限！");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            BridgeWebView mWebView = null;
            BaseFragment fragment = (BaseFragment) getVisibleFragment();
            if (fragment == null) {
                mWebView = ((BaseFragment) mFragments[mCurrentTab]).getWebView();
            } else {
                mWebView = fragment.getWebView();
            }
            if (mWebView.canGoBack()) {
                //goBack()表示返回WebView的上一页面
                mWebView.goBack();
                return true;
            }
//            setBackNumEvent(mWebView,keyCode, event);
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean setBackNumEvent(BridgeWebView mWebView,int keyCode, KeyEvent event ) {
        if (null !=mWebView.getTag()&& (int)mWebView.getTag()==2){
            MainActivity.this.finish();
            return super.onKeyDown(keyCode, event);
        }
        T.showShort(this, "在点击一次直接退出");
        mWebView.setTag(2);
        return true;
    }

    /**
     * @return 获取BackStackEntry 的栈顶显示的页面
     * 除了首页以外 其他页面直接正常返回
     */
    public Fragment getVisibleFragment(){
        FragmentManager fmManager = getSupportFragmentManager();
        if(fmManager.getBackStackEntryCount() == 0){
            L.i("BackStack entry count is 0: " + this);
            return null;
        }
        String fragmentNameOnStackTop = "";
        final FragmentManager.BackStackEntry topEntry = fmManager.getBackStackEntryAt(fmManager.getBackStackEntryCount() -1);
        if(topEntry != null) {
            fragmentNameOnStackTop = topEntry.getName();
        }
        L.i("Got fragments name: " + fragmentNameOnStackTop);
        return fmManager.findFragmentByTag(fragmentNameOnStackTop);
    }


    public QuoteFragment getTransferFragment(int tabIndex) {
        //获取fragment接口对象
        return (QuoteFragment)mFragments[tabIndex];
    }

    public void selectTabFragment(int tabPosition){
        if (tabPosition>=0) mTabGroup.check(getCheckId(tabPosition));
    }

    public int tabPostion(){
        return mCurrentTab;
    }
}
