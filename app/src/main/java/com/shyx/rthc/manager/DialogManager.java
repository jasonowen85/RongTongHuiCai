package com.shyx.rthc.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.shyx.rthc.BaseApplication;
import com.shyx.rthc.Login.LoginActivity;
import com.shyx.rthc.service.DownloadService;
import com.shyx.rthc.widget.P2pAlertDialog;


/**
 * Created by lian on 2016/4/23.
 */
public class DialogManager {

    public static final String alert = "提示";
    public static String cancel = "取消";
    public static final String confirm = "确定";

    private static final String UP_SHARE_TIME = "UPTime";
    private static final String UP_LAST_TIME = "LastTime";
    /*
     *  取消次数(非强制更新,并且距离上次进入时间在30分钟内计一次),
     *  超过3次后,8小时后再提示更新;
     */
    private static final String UP_CONCAL_TIMES = "cancelTimes";
    //30分钟
    private static final long ADD_TIMES = 30 * 60 * 1000;
    //8小时
    private static final long Hour8 = 8 * 60 * 60 * 1000;

    /**
     * 是否退出登录
     */
    public static void logoutDialog(final Context context) {

        final P2pAlertDialog dialog = new P2pAlertDialog(context);
        dialog.builder().setTitle(alert);
        dialog.setMsg("退出后下次将需要重新登录，确定退出？");
        dialog.setPositiveButton(confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApplication.getInstance().Logout(context);
                dialog.dismiss();
            }
        }).setNegativeButton(cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 请求超时，重新登录
     */
    public static void timeOutDialog(final Context context, String msg) {

        final P2pAlertDialog dialog = new P2pAlertDialog(context);
        dialog.builder().setTitle(alert);
        dialog.setMsg(msg);
        dialog.setPositiveButton(confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApplication.getInstance().exit();
                dialog.dismiss();
                UiManager.switcher(context, LoginActivity.class);
            }
        }).setNegativeButton(cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 实名制
     */
    public static void openRealNameDialog(final Activity context, String msg) {

        final P2pAlertDialog dialog = new P2pAlertDialog(context);
        dialog.builder().setTitle(alert);
        dialog.setMsg(msg);
        dialog.setPositiveButton(confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // WebDataManager.openPaymentAccount(context);
//                UiManager.switcher(context, RealNameActivity.class);
                dialog.dismiss();

            }
        }).setNegativeButton(cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 开户
     */
    public static void openAccountDialog(final Activity context, String msg) {

        final P2pAlertDialog dialog = new P2pAlertDialog(context);
        dialog.builder().setTitle(alert);
        dialog.setMsg(msg);
        dialog.setPositiveButton(confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WebDataManager.openPaymentAccount(context);
                dialog.dismiss();

            }
        }).setNegativeButton(cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }).show();
    }


    /**
     * 绑定银行卡
     */
    public static void bindCardDialog(final Activity context, String msg) {

        final P2pAlertDialog dialog = new P2pAlertDialog(context);
        dialog.builder().setTitle(alert);
        dialog.setMsg(msg);
        dialog.setPositiveButton(confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WebDataManager.bidBankCard(context);
                dialog.dismiss();

            }
        }).setNegativeButton(cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }).show();
    }


    public static void updateDialog(final Activity context, final boolean promotionType, final String apkURL){
        final SharedPreferences shared = context.getSharedPreferences(UP_SHARE_TIME, Context.MODE_PRIVATE);
        final P2pAlertDialog dialog = new P2pAlertDialog(context);
        dialog.builder().setTitle(alert);
        dialog.setMsg("检测到新版本，是否升级？");
        if (promotionType){
//            if (NetWorkUtil.isWifi(context)){  //WiFi状态直接启动下载
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                intent.putExtras(bundle);
//                intent.setClass(context,DownloadService.class);
//                context.startService(intent);
//                return;
//            }
            cancel = "退出";
            dialog.mDialog.setCanceledOnTouchOutside(false);
        }else{
            //非强制更新时
            int times = shared.getInt(UP_CONCAL_TIMES, 0);
            long lastTime = shared.getLong(UP_LAST_TIME, 0 );
            if (lastTime != 0 && System.currentTimeMillis() - lastTime > Hour8){
                //距离上次进入系统大于8小时清空记录
                shared.edit().putLong(UP_LAST_TIME, 0).commit();
                shared.edit().putInt(UP_CONCAL_TIMES, 0).commit();
            }else if (times >= 2){
                return;
            }
        }

        dialog.setPositiveButton(confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (apkURL != null) bundle.putString("apkPath", apkURL);
                intent.putExtras(bundle);
                intent.setClass(context,DownloadService.class);
                context.startService(intent);
                dialog.dismiss();
            }
        }).setNegativeButton(cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是强制升级
                if(promotionType){
//                    BaseApplication.getInstance().exit();
                    context.finish();
                }else {
                    long lastTime = shared.getLong(UP_LAST_TIME, 0);
                    if (System.currentTimeMillis() - lastTime < Hour8|| lastTime==0){
                        int addTimes = shared.getInt(UP_CONCAL_TIMES, 0) + 1;
                        shared.edit().putInt(UP_CONCAL_TIMES, addTimes).commit();
                        Log.i("jiang", "取消的次数= "+addTimes+ "时间差= "+(System.currentTimeMillis() - lastTime)/60000);
                    }
                    shared.edit().putLong(UP_LAST_TIME, System.currentTimeMillis()).commit();
                }
                dialog.dismiss();

           }
        }).show();

    }

    public static void webInfoErrorDialog(final Activity context, String msg){
        final P2pAlertDialog dialog = new P2pAlertDialog(context);
        dialog.builder().setTitle(alert);
        dialog.setMsg(msg);
        dialog.setPositiveButton(confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();

            }
        }).setNegativeButton(cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        }).show();
    }

}
