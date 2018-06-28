package com.shyx.rthc.common;

import com.shyx.rthc.BuildConfig;
import com.shyx.rthc.utils.L;

/**
 * 环境路径配置
 */
public class ServiceConfig {

    /**
     * release模式是否输出日记
     */
    public static boolean RELEASE_DEBUG = false;

    public static final String RELEASE_ROOT_URL = "http://www.zxrt808.com";//演示环境根路径
    public static final String TEST_ROOT_URL = "http://192.168.8.15:9000";//测试环境根路径
    public static final String DEBUG_ROOT_URL = "http://p2p-4.test4.wangdai.me";//本地环境根路径

    public static final String APP_SERVICES = "/app/services";
    public static final String MAIN_PAGE= "/app/hangqing/index";
    public static final String LOGIN_PAGE= "/app/login/init";
    public static final String MAIN_SEARCH= "/app/hangqing/stockSearch";
    public static final String MAIN_TRANSATION = "/app/deal/dealPre";
    public static final String MAIN_MYSELE = "/app/user/userIndex";
    public static final String MAIN_MORE = "/app/more/moreInfo";

    public enum Mode {
        debug, _test, release
    }

    public static Mode SERVICE_MODE = Mode.debug;

    /**
     * 3DES MD5
     * 本地  md5  3Dnyy1xs3KFu5rbN     des  08k1yn229r18t456
     *http://p2p.guangruncf.com
     * JM1y2hjVAOX1rIv8    gxyuMlNElsHqkvV9
     **/
    public static final String DES_KEY = "08k1yn229r18t456";
    public static final String MD5_KEY = "3Dnyy1xs3KFu5rbN";

    /**
     * 上传头像路径
     */
    public static final String UPDATE_HEAD_URL = "/common/appImagesUpload";
    /**
     * 版本更新路径
     **/
    public static final String UPGRADE_ROOT_URL = "http://pre-d.eims.com.cn/download/sp2p";
    public static final String UPGRADE_TEST = "/sp2p9-test.apk";
    public static final String UPGRADE_RELEASE = "/sp2p9.apk";

    static {
//        SERVICE_MODE = Enum.valueOf(Mode.class,
//                AppUtils.getMetaValue(BaseApplication.getInstance(), "server_mode"));
        SERVICE_MODE = Enum.valueOf(Mode.class, BuildConfig.BUILD_TYPE);
        if (RELEASE_DEBUG) L.IS_DEBUG = true;
        else L.IS_DEBUG = SERVICE_MODE != Mode.release; //release模式默认不输出日记
    }

    public static String getServicesRootUrl() {
        return getRootUrl() + APP_SERVICES;
    }

    public static String getRequestUrl(String pagePoct) {
        String url =getRootUrl() + pagePoct;
        L.i("加载的网页=" + url);
        return url;
    }

    public static String getRootUrl() {
        switch (SERVICE_MODE) {
            case debug:
                return DEBUG_ROOT_URL;
            case _test:
                return TEST_ROOT_URL;
            case release:
                return RELEASE_ROOT_URL;
            default:
                break;
        }
        return null;
    }

    /**
     * 获取下载路径
     *
     * @return
     */
    public static String getUpgradeUrl() {
        switch (SERVICE_MODE) {
            case debug:
                return UPGRADE_ROOT_URL + UPGRADE_TEST;
            case _test:
                return UPGRADE_ROOT_URL + UPGRADE_TEST;
            case release:
                return UPGRADE_ROOT_URL + UPGRADE_RELEASE;
            default:
                break;
        }
        return null;
    }


    //是否为debug模式
    public static boolean isDebugMode() {
        switch (SERVICE_MODE) {
            case debug:
                return true;
            case _test:
                return false;
            case release:
                return false;
        }
        return false;
    }
}
