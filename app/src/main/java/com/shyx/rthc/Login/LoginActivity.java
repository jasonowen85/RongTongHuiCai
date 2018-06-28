package com.shyx.rthc.Login;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.itheima.view.BridgeWebView;
import com.shyx.rthc.R;
import com.shyx.rthc.base.BaseActivity;
import com.shyx.rthc.bus.BusUser;
import com.shyx.rthc.common.Constant;
import com.shyx.rthc.common.ServiceConfig;
import com.shyx.rthc.manager.DialogManager;
import com.shyx.rthc.ui.MainActivity;
import com.shyx.rthc.utils.AppUtils;
import com.shyx.rthc.utils.L;
import com.shyx.rthc.utils.NetWorkUtil;
import com.shyx.rthc.utils.SystemStatusUtil;
import com.shyx.rthc.utils.T;
import com.shyx.rthc.utils.WebViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import qiu.niorgai.StatusBarCompat;

//implements View.OnLayoutChangeListener
public class LoginActivity extends BaseActivity {
    @Bind(R.id.web_view_login)
    BridgeWebView mWebView;
    private boolean promotionType;
    private String apkPath;
    private String version;
    //   @Bind(R.id.account_layout)
//    AccountEditText mAccountEdt;
//    @Bind(R.id.pwd_layout)
//    AccountEditText mPwdEdt;
//    @Bind(R.id.login_btn)
//    Button mLoginBtn;
//    @Bind(R.id.include_layout)
//    ViewGroup mLogoViewGroup;
//    private int keyHeight;

//    private final int PASSWOED_MAX_LENG=15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isSetStatusBar = false;
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            StatusBarCompat.translucentStatusBar(this, true);
        else
            SystemStatusUtil.setTranslucentStatus(this, R.color.my_theme);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        WebViewUtil.setWebView(this,mWebView);
//        mWebView.loadUrl("http://html5.mail.10086.cn");
        mWebView.addBridgeInterface(new MyJavaSctiptInterface(this));//注册桥梁类，该类负责H5和android通信

//        keyHeight = ScreenUtil.getScreenHeight(this)/3;
//        mAccountEdt.addEdtTextChangeListener(new AccountEditText.EdtListener() {
//            @Override
//            public void onTxtChangeListener(String s) {
//                loginRead();
//            }
//        });
//        mPwdEdt.setInputType(getString(R.string.password),getString(R.string.register_password_hint));
//        mPwdEdt.addEdtTextChangeListener(new AccountEditText.EdtListener() {
//            @Override
//            public void onTxtChangeListener(String s) {
//                loginRead();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        find(R.id.root_layout).addOnLayoutChangeListener(this);
        if (!NetWorkUtil.isNetworkAvailable(this)){
            T.showShort(this,"请检查网络后重新登录");
            findViewById(R.id.tv_no_data_view).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.tv_no_data_view).setVisibility(View.GONE);
            mWebView.loadUrl(ServiceConfig.getRequestUrl(ServiceConfig.LOGIN_PAGE));
        }
    }

    public class MyJavaSctiptInterface {
        private Activity mActivity;

        public MyJavaSctiptInterface(Activity mActivity) {
            this.mActivity = mActivity;
        }

        public void loginSuccess(String[] strs) {//js调用android方法
            L.i("js 登录成功了 ");
            MainActivity.startMainWithTab(mActivity, MainActivity.TAB_INDEX);
            finish();
            EventBus.getDefault().post(new BusUser());
        }

        public void updateVersion(String[] params) {
            L.i("getVersion " + params[0]);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(params[0]);
                version =  jsonObject.optString("version").substring(1);
                promotionType = "no".equals(jsonObject.optString("forceUpdate"))? false : true;
                apkPath = jsonObject.optString("url");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Integer.parseInt(version.replace(".", "")) > Integer.parseInt(AppUtils.getVersionName(context).replace(".", ""))) {
                            //申请WRITE_EXTERNAL_STORAGE权限
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogManager.updateDialog(context, promotionType, apkPath);
                                    }
                                });

                            }
                        }
                    }
                });



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

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
//
//    @OnClick(R.id.login_btn)
//    void onclick(View v) {
//        switch (v.getId()) {
//            case R.id.login_btn://登录
//                login();
//                break;
//        }
//    }

    /**
     * 验证用户名，密码是否都符合登录条件 ，
     * 都符合登录条件才可以登录，
     * 不符合登录条件给与提示，
     * 不能登录
     *
     * @return
     */
//    private void loginRead() {
//        boolean isPhone = LoginManager.isMobiPhoneNum(mAccountEdt.getText());
//        boolean isPwd = mPwdEdt.getText().length() >= 6 && mPwdEdt.getText().length() <= PASSWOED_MAX_LENG;
//        mLoginBtn.setEnabled(isPhone && isPwd);
//    }

    /**
     * 登陆，登陆成功跳入MainActivity
     */
//    private void login() {
//        String phone = mAccountEdt.getText();
//        String pwd = Encrypt.encrypt3DES(mPwdEdt.getText(), ServiceConfig.DES_KEY);
//        LoginManager.login(this, phone, pwd, null,null);
//    }




//    @Override
//    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//        if (oldBottom != 0 && bottom != 0) {
//            int changeHeight = bottom - oldBottom;
//            if (changeHeight > keyHeight) {
//                mLogoViewGroup.setVisibility(View.VISIBLE);
//            } else if (changeHeight < 0 && (oldBottom - bottom) > keyHeight) { //键盘弹出的时候
//                mLogoViewGroup.setVisibility(View.GONE);
//            }
//        }
//    }
}
