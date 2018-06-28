package com.shyx.rthc.ui;

import android.app.Activity;
import android.os.Bundle;

import android.text.TextUtils;

import com.shyx.rthc.Login.LoginActivity;
import com.shyx.rthc.R;
import com.shyx.rthc.common.Constant;
import com.shyx.rthc.manager.LoginManager;
import com.shyx.rthc.manager.UiManager;
import com.shyx.rthc.utils.SpUtils;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        boolean firstFlag = (boolean) SpUtils.get(this, Constant.FLAG_FIRST, true);
//        if(firstFlag){
//            UiManager.switcher(WelcomeActivity.this, GuideActivity.class);
//            finish();
//        }else {
            startLogin();
//        }
    }

    private void startLogin() {
        // 1秒后进入主界面
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                //不需要登录也可看首页内容
//                if (app.getLockPatternUtils().savedPatternExists()) {
//                    //已经设置过手势密码时
//                    UiManager.switcher(WelcomeActivity.this, UnlockGesturePasswordActivity.class);
//                }
//                login();
                UiManager.switcher(WelcomeActivity.this, LoginActivity.class);
                finish();
            }
        }, 1000);


    }

    private void login() {
        String account = (String) SpUtils.get(this, Constant.ACCOUNT, "");
        String pwd = (String) SpUtils.get(this, Constant.PASSWORD, "");
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
            LoginManager.login(this, account, pwd, new LoginManager.LoginListener() {
                @Override
                public void callBack() {
//                        headImgAnimation();
                }
            }, new LoginManager.ErrorListener() {
                @Override
                public void onErrorResponse() {

                }
            });
        }else {
            //进入登录界面的 输入密码 用户名
            UiManager.switcher(this, LoginActivity.class);
        }
    }
}
