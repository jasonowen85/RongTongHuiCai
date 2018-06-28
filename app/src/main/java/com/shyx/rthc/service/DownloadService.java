package com.shyx.rthc.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;


import com.shyx.rthc.common.ServiceConfig;

import java.io.File;

public class DownloadService extends Service {
    private DownloadManager dm;
    private long enqueue;
    private BroadcastReceiver receiver;
    private String apkName;
    //接口返回的
    private String apkPath;

    @Override
    public void onCreate() {
        super.onCreate();
        String[] list = ServiceConfig.getUpgradeUrl().split("/");
        apkName = list[list.length - 1];
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                installApk(intent);
                stopSelf();
            }
        };
        apkPath = intent.getStringExtra("apkPath");
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        startDownload();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void startDownload() {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        String url = ServiceConfig.getUpgradeUrl();
        if (!TextUtils.isEmpty(apkPath.trim())){
            url = apkPath;
            apkName = apkPath.split("/")[apkPath.split("/").length-1];
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setMimeType("application/vnd.android.package-archive");
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        //若该文件不存在，先手动生成
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        //先删除旧的包
        File apkFile = new File(Environment.getExternalStorageDirectory() + "/download/" + apkName);
        if (apkFile.exists()) {
            apkFile.delete();
        }

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName);
        enqueue = dm.enqueue(request);
    }

    private void installApk(Intent intent) {
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + apkName)), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}