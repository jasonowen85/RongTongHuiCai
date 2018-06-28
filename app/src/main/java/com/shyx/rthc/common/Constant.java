package com.shyx.rthc.common;

import android.os.Environment;

import java.io.File;

public class Constant {

    /**
     * Android 6.0 权限申请
     */
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 201;//写SD卡的权限

    /*****************
     * 保存用户的账号和密码
     ***************/
    public static final String ACCOUNT = "account";// 登录账号
    public static final String PASSWORD = "password";// 登录密码
    public static final String USERID = "userid";// 用户id

    /**
     * 标识是否为第一次进入程序
     */
    public static final String FLAG_FIRST = "first_enter";


    /************
     * 设备类型为android
     ******************/
    public static final String DEVICE_TYPE = "2";

    /******************
     * 两种调用获取验证码的场景
     ******************/
    public static final String FORGET_PWD_SENCE = "forgetPwd";//忘记密码场景
    public static final String REGISTER_PWD_SENCE = "register";//注册场景
    public static final String APP_UPDATE_MOBILE = "updateMobile";//完善手机号码 场景

    /******************
     * 公共请求返回码
     *******************/
    public static final int RESULT_OK = 1;//网络请求成功
    public static final int NOT_OPEN_PAYMENTACCOUNT = -102;  //没有资金托管开户
    public static final int NOT_REAL_NAME = -103;  //没有实名认证
    public static final int NOT_BIND_BANKCARD = -104;  //没有绑定银行卡
    public static final int TIME_OUT = -105;  //登录超时
    public static final int NOT_MAIL = -107;  //未绑定邮箱

    public static final int REGISTER_CODE = 6;  //注册
    /**
     * startActivityForResult请求码
     */
    public static final int OPEN_ACCOUNT_CODE = 102;
    public static final int REAL_NAME_CODE = 103;
    public static final int BIND_CARD_CODE = 104;
    /**********************
     * 接口OPT start
     ****************************************/
    public static final String REGISTER = "111";  //注册发送短信验证码
    public static final String REGISTER_PROTOCOL = "112";  //注册会员协议
    public static final String REGISTERING = "113";  //会员注册
    public static final String OPEN_ACCOUNT = "114";  //开户
    public static final String REGISTER_READ = "115";  //注册准备
    public static final String VERIFICATION_CODE = "121";  //验证验证码
    public static final String UPDATE_PWD = "122";  //修改登录密码
    public static final String LOGIN = "123";  //登录

    public static final String AUTH_NAME = "127";  //实名认证
    public static final String OPEN_INIT_OLD = "128";  //开户初始化
    public static final String GET_CITY_INFO = "129";  //获取市

    public static final String RECHARGE_READ = "216";  //充值初始化
    public static final String RECHARGE = "211";   //充值

    public static final String WITHDRAWAL_PRE = "213";  //提现初始化
    public static final String WITHDRAWAL = "214";  //提现

    public static final String RECHARGE_RECORDS = "212";  //充值记录
    public static final String WITHDRAWAL_RECORD = "215";  //提现记录

    public static final String USER_BANK_LIST = "221";  //银行卡列表
    public static final String BIND_CARD = "222";  //绑卡请求
    public static final String SETTING_DEFALUT_CARD = "223";  //设置默认银行卡

    public static final String MYINVEST = "231";  //我的投资列表
    public static final String INVEST_BILL = "232";  //投资账单
    public static final String INVEST_BILL_DETAILS = "237";  //投资账单详情
    public static final String LOAN_BILL_DETAILS = "238";  //借款账单详情
    public static final String LOAN = "233";  //我的借款列表
    public static final String LOAN_BILL = "234";  //借款账单
    public static final String REPAYMENT = "235";  //还款
    public static final String CONTRACT = "236";  //合同
    public static final String ACT_ASSIGN_TRANSFER = "239";  //我的转让/受让

    public static final String USER_DEAL_RECORD = "241";  //交易记录
    public static final String RETUEN_MONEY_PLAN = "242";  //回款计划

    public static final String USER_INFO = "251";  //个人基本信息
    public static final String MESSAGE = "252";  //消息列表
    public static final String USER_INFO_DETAIL = "253";  //获取会员信息详情
    public static final String USER_INFO_UPDATE = "254";  //保存会员信息详情
    public static final String HEAD_IMG_UPDATE = "255";  //保存上传头像
    public static final String SECURITY = "261";  //安全中心
    public static final String UPDATE_PWDBYOLD = "262";  //根据原密码修改密码
    public static final String UPDATE_EMAIL = "263";  //绑定邮箱
    public static final String EXPERIENCE_ACCOUNT = "271"; // 体验标账户信息
    public static final String EXPERIENCE_RECORDS = "272"; // 体验标投标记录
    public static final String EXPERIENCE_GET = "273"; // 体验标领取
    public static final String EXPERIENCE_EXCHANGE = "274"; // 体验标收益兑换

    public static final String INVEST_BIDS = "311"; // 理财产品列表接口
    public static final String INVEST_DETAILS = "312"; // 理财产品详情

    public static final String BID = "321";  //投标
    public static final String INVEST_BIDS_DETAILS = "322"; // 借款标详情
    public static final String INVEST_BIDS_REPAYMENT_PLAN = "323";//回款计划
    public static final String INVEST_BIDS_RECORDS = "324"; //投标记录
    public static final String INVEST_TRANSFERS = "331"; // 债权转让列表接口
    public static final String TRANSFERS_DETAILS = "332"; // 债权转让详情
    public static final String TRANSFERS_REBACK = "333"; // 债权转让回款计划
    public static final String TRANSFERS_BUY = "334"; // 购买债权

    public static final String EXP_DETAILS = "341"; // 体验金详情
    public static final String EXP_BORROW_DES = "342"; // 体验金借款详情
    public static final String EXP_RECORDS = "343"; // 体验金投标记录
    public static final String EXP_INVEST = "344"; // 体验金投标

    public static final String COMPANY_INFO = "411";  //公司介绍
    public static final String CONTACT_US = "421";  // 联系我们
    public static final String APP_LOGO = "422";  // logo
    public static final String UPDATE_VERSION = "423";  // logo
    public static final String INDEX = "511";

    public static final String INDEX_DIRECT = "611";  //定向标列表
    public static final String DIRECT_DETAILS = "612"; //定向标详情
    public static final String DIRECT_BID = "621"; //定向标投标
    public static final String DIRECT_BID_DETAILS = "622";  //定向标标借款详情   ---- 用户
    public static final String PERFECT_USE_PHONE = "711";  //定向标标借款详情   ---- 用户


    public static final String TRANSFER_PROTOCOL = "2312";//转让协议
    public static final String TRANSFER_INIT = "2313";//转让申请初始化
    public static final String TRANSFER_APPLY = "2314";//转让申请

    /*********************** 接口OPT end ****************************************/
    /**********************
     * //     * 个人设置信息常量
     * //
     ****************************************/
    public static final String HEAD_URL = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + "head.jpg";// 相机照相保存路径
    //
//    public static final String LOGO_URL = Environment
//            .getExternalStorageDirectory().getAbsolutePath()
//            + File.separator
//            + "logo.jpg";
    public static final String SD_STORAGE_DIR_NAME = "p2p9.0";
}
