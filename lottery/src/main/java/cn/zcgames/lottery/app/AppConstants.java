package cn.zcgames.lottery.app;

import android.os.Environment;
import android.text.TextUtils;

/**
 * App中用到的常量在这里定义
 */
public class AppConstants {

    public static final int KEYBOARD_INPUT = 0;

    //闫洁 5bc1bb893b7750592f314aed (52)  5bc7f74fb3b0db517e5983a4(186)
    private static final String TEST_CHANNEL_ID1 = "5bac81ff3b7750724f13b69d";
    //测试渠道
    private static final String TEST_CHANNEL_ID2 = "5bc7f74fb3b0db517e5983a4";
    //
    private static final String TEST_CHANNEL_ID3 = "5c1b03319dc6d634424bf4a5";

    private static final String TEST_CHANNEL_ID4 = "5c11c6e89dc6d62d4461cf6f";

    private static final String TEST_CHANNEL_ID5 = "5c1ca1bf9dc6d62d455707a6";

    //开发  5baef9513b7750311a757b75 我自己5bc1ce063b7750113451c74a
    private static final String DEV_CHANNEL_ID = "5c1b04539dc6d62d4641b1c2";
    //正式服渠道
    private static final String PRO_CHANNEL_ID = "5c209123b3b0db69f44dba8b";

    public static String getChannelId() {
        //优先获取APK包是否包含渠道，没有则设置默认内置的渠道
        String apkChannelId = MyApplication.getInstance().getApkChannelInfo();
        if (!TextUtils.isEmpty(apkChannelId)) {
            return apkChannelId;
        }
//        return TEST_CHANNEL_ID1;
//        return TEST_CHANNEL_ID2;
//        return TEST_CHANNEL_ID3;
//        return TEST_CHANNEL_ID4;
        return PRO_CHANNEL_ID;
//        return DEV_CHANNEL_ID;//开发
    }

    public static final boolean SHOW_LOTTERY_RESULT_DEMO_DATA = false;//是否显示开奖假数据
    public static final boolean SHOW_LOTTERY_PAY_TEST = false;//是否测试支付
    public static final float LOTTERY_DEFAULT_PRICE = 2;//彩票每注金额 默认值
    public static final int REQUEST_COUNTRY_CODE = 1002;//国家或区号选择结果
    public static final int REQUEST_BIND_PHONE = 1000;//绑定手机号

    /**
     * 文件目录路径
     */
    public static final String DIR_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String LOCAL_PATH = DIR_ROOT + "/Lottery";
    public static final String LOCAL_FILE_TEMP_PATH = "/Lottery/temp/";
    public static final String DCIM_CAMERA_PATH = "/DCIM/Camera/";
    public static final String USER_HEADER_TEMP_FILE_SUFFIX = ".png";
    public static final String USER_PIC_TEMP_FILE_NAME = "We";
    //更换头像时打开的系统目录
    public static final String SYSTEM_PATH_CHANGE_HEADER = "image/*";

    //当收到中奖通知时
    public static final String BC_WIN_NOTIFY = "win_action";
    public static final String BC_NOTIFY_DATA = "win_notify_data";

    //关于通知配置
    public static String NOTIFY_OPTION = "notify_option";//通知开关设置
    public static String NOTIFY_SYSTEM_TYPE = "notify/public/";//订阅系统通知(后channelId)
    public static String NOTIFY_LOTTERY_TYPE = "notify/lottery/";//订阅开奖彩种通知(后lottery_tag)
    public static String NOTIFY_WIN_TYPE = "notify/personal/";//订阅中奖通知(后channelId/userId)

    //购彩首页，彩票类型tag
    public static final String LOTTERY_TYPE_2_COLOR = "double_color";//双色球
    public static final String LOTTERY_TYPE_3_D = "3D";//福彩3D
    public static final String LOTTERY_TYPE_FAST_3 = "fast_three";//快三
    public static final String LOTTERY_TYPE_FAST_3_JS = "jiangsu_fast_three";//江苏快三
    public static final String LOTTERY_TYPE_FAST_3_NEW = "new_fast_three";//新快三
    public static final String LOTTERY_TYPE_FAST_3_HB = "hubei_fast_three";//湖北快三
    public static final String LOTTERY_TYPE_FAST_3_EASY = "easy_fast_three";//易快三
    public static final String LOTTERY_TYPE_11_5 = "eleven_five";//11选5
    public static final String LOTTERY_TYPE_11_5_OLD = "old_eleven_five";//老11选5
    public static final String LOTTERY_TYPE_11_5_YUE = "yue_eleven_five";//粤11选5
    public static final String LOTTERY_TYPE_11_5_LUCKY = "luck_eleven_five";//好运11选5
    public static final String LOTTERY_TYPE_11_5_YILE = "yile_eleven_five";//易乐11选5

    public static final String LOTTERY_TYPE_ALWAYS_COLOR = "chongqing_tick_tick";//重庆时时彩
    public static final String LOTTERY_TYPE_ALWAYS_COLOR_NEW = "new_tick_tick";//新时时彩
    public static final String LOTTERY_TYPE_7_HAPPY = "seven_happy";//七乐彩
    public static final String LOTTERY_TYPE_ARRANGE_3 = "array_three";//排列3
    public static final String LOTTERY_TYPE_ARRANGE_5 = "array_five";//排列5
    public static final String LOTTERY_TYPE_7_STAR = "seven_star";//7星彩
    public static final String LOTTERY_TYPE_BIG_LOTTO = "big_lotto";//大乐透


    //以后优化需要
    //    public static final String LOTTERY_NAME_2_COLOR = "double_color";//双色球
    //    public static final String LOTTERY_NAME_3_D = "3D";//3DP
    //    public static final String LOTTERY_NAME_ALWAYS_COLOR = "chongqing_shishicai";//重庆时时彩
    //    public static final String LOTTERY_NAME_ALWAYS_COLOR_NEW = "new_shishicai";//新时时彩
    //    public static final String LOTTERY_NAME_7_HAPPY = "seven_happy";//七乐彩
    //    public static final String LOTTERY_NAME_ARRANGE_3 = "array_three";//排列3
    //    public static final String LOTTERY_NAME_ARRANGE_5 = "array_five";//排列5
    //    public static final String LOTTERY_NAME_WIN_LOSE = "win_lose";//胜负彩
    //    public static final String LOTTERY_NAME_BIG_LOTTO = "big_lotto";//大乐透
    //    public static final String LOTTERY_NAME_7_STAR = "seven_start";//7星彩
    //    public static final String LOTTERY_NAME_SELECT_NINE = "select_nine";//任选9
    //    public static final String LOTTERY_NAME_11_5_YILE = "yile_eleven_five";//易乐11选5
    //    public static final String LOTTERY_NAME_FAST_3_EASY = "easy_fast_three";//易快三
    //    public static final String LOTTERY_NAME_FAST_3_HAPPY_POKER = "happy_poker";//快乐扑克

    public static final String SPDATA_KEY_USER = "USER";
    public static final String SPDATA_KEY_PUBLIC = "PUBLIC";
    public static final String SPDATA_KEY_LOGIN_WAY = "loginWay";//登录方式 0:手机登录,1邮箱登录
    public static final String KEY_TREND_TYPE_DATA = "typeData";//各走势数据
    public static final String KEY_X_ITEM_WIDTH = "xItemWidth";//走势item的宽度
    public static final String KEY_LEFT_ITEM_WIDTH = "leftTitleItemWidths";//期号的宽度
    public static final String TREND_STYLE_BASIC = "01";//基本走势图
    public static final String TREND_STYLE_FORM = "50";//形态走势图


    //双色球类型
    public static final int BALL_TYPE_RED = 0;
    public static final int BALL_TYPE_BLUE = 1;
    public static final int BALL_TYPE_DAN = 0;
    public static final int BALL_TYPE_TUO = 1;
    public static final int BALL_TYPE_ONE = 0;
    public static final int BALL_TYPE_TOW = 1;
    public static final int BALL_TYPE_THREE = 2;

    //双色球玩法：普通和胆拖
    public static final int DOUBLE_COLOR_PLAY_STYLE_NORMAL = 0;
    public static final String DC_PLAY_STYLE_NORMAL = "plain";
    public static final int DOUBLE_COLOR_PLAY_STYLE_DANTUO = 1;
    public static final String DC_PLAY_STYLE_DANTUO = "dantuo";

    //福彩3d玩法：直选、组选3和组选6
    public static final int THREE_D_PLAY_DIRECT = 0;
    public static final int THREE_D_PLAY_GROUP_THREE = 1;
    public static final int THREE_D_PLAY_GROUP_SIX = 2;
    public static final String TD_PLAY_DIRECT = "direct";
    public static final String TD_PLAY_GROUP_THREE = "group3";
    public static final String TD_PLAY_GROUP_SIX = "group6";

    //快三玩法
    public static final int FAST_DEFAULT = -1;//和值 默认-1
    public static final int FAST_THREE_SUM = 3;//和值
    public static final int FAST_THREE_3_SAME_ONE = 4;//三同号单选
    public static final int FAST_THREE_3_SAME_ALL = 5;//三同号通选
    public static final int FAST_THREE_3_TO_ALL = 6;//三连号通选
    public static final int FAST_THREE_3_DIFFERENT = 7;//三不同号
    public static final int FAST_THREE_2_SAME_ONE = 8;//二同号单选
    public static final int FAST_THREE_2_SAME_MORE = 9;//二同号复选
    public static final int FAST_THREE_2_DIFFERENT = 10;//二不同号
    public static final String FAST_THREE_ORDER_TYPE_SUM = "sum";//和值
    public static final String FAST_THREE_ORDER_TYPE_3_SAME_ONE = "triple_same_each";//三同号单选
    public static final String FAST_THREE_ORDER_TYPE_3_SAME_ALL = "triple_same_all";//三同号通选
    public static final String FAST_THREE_ORDER_TYPE_3_TO_ALL = "triple_consecutive_all";//三连号通选
    public static final String FAST_THREE_ORDER_TYPE_3_DIFFERENT = "triple_different";//三不同号
    public static final String FAST_THREE_ORDER_TYPE_2_SAME_ONE = "double_same_each";//二同号单选
    public static final String FAST_THREE_ORDER_TYPE_2_SAME_MORE = "double_same_plural";//二同号复选
    public static final String FAST_THREE_ORDER_TYPE_2_DIFFERENT = "double_different_single";//二不同号
    public static final String TREND_VIEW = "trendView";
    public static final String LATEST_OPEN_AWARD = "openAward";

    //时时彩玩法
    public static final int ALWAYS_COLOR_5_ALL = 11;//五星通选
    public static final int ALWAYS_COLOR_5_DIRECT = 12;//五星直选
    public static final int ALWAYS_COLOR_3_DIRECT = 13;//三星直选
    public static final int ALWAYS_COLOR_3_GROUP_3 = 131;//三星组三
    public static final int ALWAYS_COLOR_3_GROUP_6 = 132;//三星组六
    public static final int ALWAYS_COLOR_2_DIRECT = 14;//二星直选
    public static final int ALWAYS_COLOR_2_GROUP = 15;//二星组选
    public static final int ALWAYS_COLOR_1_DIRECT = 16;//一星直选
    public static final int ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE = 17;//大小单双
    public static final String ALWAYS_COLOR_ORDER_TYPE_1_DIRECT = "one_star";//一星直选
    public static final String ALWAYS_COLOR_ORDER_TYPE_2_DIRECT = "two_star";//二星直选
    public static final String ALWAYS_COLOR_ORDER_TYPE_2_GROUP = "two_star_group";//二星组选
    public static final String ALWAYS_COLOR_ORDER_TYPE_3_DIRECT = "three_star";//三星直选
    public static final String ALWAYS_COLOR_ORDER_TYPE_3_GROUP_3 = "three_star_group_multiple";//三星组三
    public static final String ALWAYS_COLOR_ORDER_TYPE_3_GROUP_6 = "three_star_group_six";//三星组六
    public static final String ALWAYS_COLOR_ORDER_TYPE_5_DIRECT = "five_star";//五星直选
    public static final String ALWAYS_COLOR_ORDER_TYPE_5_ALL = "five_star_all";//五星通选
    public static final String ALWAYS_COLOR_ORDER_TYPE_BIG_SMALL_SINGLE_DOUBLE = "big_small_odd_even";//大小单双

    //七乐彩玩法：普通和胆拖
    public static final int SEVEN_HAPPY_PLAY_STYLE_NORMAL = 18;
    public static final String SEVEN_HAPPY_ORDER_NORMAL = "plain";
    public static final int SEVEN_HAPPY_PLAY_STYLE_DANTUO = 19;
    public static final String SEVEN_HAPPY_ORDER_DANTUO = "dantuo";

    //排列三玩法：直选、组选3和组选6
    public static final int ARRANGE_3_PLAY_DIRECT = 20;
    public static final int ARRANGE_3_PLAY_GROUP_THREE = 21;
    public static final int ARRANGE_3_PLAY_GROUP_SIX = 22;
    public static final String A3_PLAY_DIRECT = "direct";
    public static final String A3_PLAY_GROUP_THREE = "group3";
    public static final String A3_PLAY_GROUP_SIX = "group6";

    //排列五玩法：直选、组选3和组选6
    public static final int ARRANGE_5_PLAY_DIRECT = 23;
    public static final String A5_PLAY_DIRECT = "direct";

    //七星彩玩法：直选
    public static final int SEVEN_STAR_PLAY_DIRECT = 24;
    public static final String SEVEN_STAR_ORDER_DIRECT = "direct";

    //11选5
    public static final int PLAY_11_5_ANY_2 = 25;//任选二
    public static final int PLAY_11_5_ANY_3 = 26;//任选三
    public static final int PLAY_11_5_ANY_4 = 27;//任选四
    public static final int PLAY_11_5_ANY_5 = 28;//任选五
    public static final int PLAY_11_5_ANY_6 = 29;//任选六
    public static final int PLAY_11_5_ANY_7 = 30;//任选七
    public static final int PLAY_11_5_ANY_8 = 31;//任选八
    public static final int PLAY_11_5_FRONT_1_DIRECT = 38;//前一直选
    public static final int PLAY_11_5_FRONT_2_DIRECT = 39;//前二直选
    public static final int PLAY_11_5_FRONT_2_GROUP = 40;//前二组选
    public static final int PLAY_11_5_FRONT_3_DIRECT = 42;//前三直选
    public static final int PLAY_11_5_FRONT_3_GROUP = 43;//前三组选
    public static final int PLAY_11_5_ANY_2_DAN = 32;//任选二 胆拖
    public static final int PLAY_11_5_ANY_3_DAN = 33;//任选三 胆拖
    public static final int PLAY_11_5_ANY_4_DAN = 34;//任选四 胆拖
    public static final int PLAY_11_5_ANY_5_DAN = 35;//任选五 胆拖
    public static final int PLAY_11_5_ANY_6_DAN = 36;//任选六 胆拖
    public static final int PLAY_11_5_ANY_7_DAN = 37;//任选七 胆拖
    public static final int PLAY_11_5_ANY_8_DAN = 45;//任选八 胆拖
    public static final int PLAY_11_5_FRONT_2_GROUP_DAN = 41;//前二组选 胆拖
    public static final int PLAY_11_5_FRONT_3_GROUP_DAN = 44;//前三组选 胆拖
    public static final String ORDER_11_5_ANY_2 = "any_two";
    public static final String ORDER_11_5_ANY_3 = "any_three";
    public static final String ORDER_11_5_ANY_4 = "any_four";
    public static final String ORDER_11_5_ANY_5 = "any_five";
    public static final String ORDER_11_5_ANY_6 = "any_six";
    public static final String ORDER_11_5_ANY_7 = "any_seven";
    public static final String ORDER_11_5_ANY_8 = "any_eight";
    public static final String ORDER_11_5_FRONT_1_DIRECT = "head_one";
    public static final String ORDER_11_5_FRONT_2_DIRECT = "head_two";
    public static final String ORDER_11_5_FRONT_2_GROUP = "head_group_two";
    public static final String ORDER_11_5_FRONT_3_DIRECT = "head_three";
    public static final String ORDER_11_5_FRONT_3_GROUP = "head_group_three";

    //双色球投注方式：单式和复式
    public static final int DOUBLE_COLOR_ORDER_TYPE_DANSHI = 0;
    public static final int DOUBLE_COLOR_ORDER_TYPE_FUSHI = 1;

    //用于区分doubleColorDantuoAdapter
    public static final int DOUBLE_COLOR_DANTUO_RED_DAN = 2;
    public static final int DOUBLE_COLOR_DANTUO_RED_TUO = 3;
    public static final int DOUBLE_COLOR_DANTUO_BLUE = 4;

    //双色球总数
    public static final int BALL_NUMBER_RED = 33;
    public static final int BALL_NUMBER_BLUE = 16;
    public static final int BALL_MIN_SELECTED_NUMBER_RED = 6;
    public static final int BALL_MAX_SELECTED_NUMBER_RED = 16;
    public static final int BALL_MAX_SELECTED_NUMBER_RED_DAN = 5;
    public static final int BALL_MIN_SELECTED_NUMBER_BLUE = 1;

    //QQ平台登录所需的appId和appKey
    public static final String THIRD_PLATFORM_QQ_ID = "1106167784";//
    public static final String THIRD_PLATFORM_QQ_KEY = "yEQEWPlCQu4SOv8g";

    //微信平台登录所需的appId和appKey
    public static final String THIRD_PLATFORM_WECHAT_ID = "wxdc1e388c3822c80b";
    public static final String THIRD_PLATFORM_WECHAT_KEY = "3baf1193c85774b3fd9d18447d76cab0";

    //图片剪裁完的结果标识
    public static final int CROP_REQUEST = 666;

    //用户登录方式
    public static final int USER_LOGIN_TYPE_PHONE = 0;//手机号登录
    public static final int USER_LOGIN_TYPE_EMAIL = 1;//邮箱登录
    public static final int USER_LOGIN_TYPE_THIRD_PLATFORM = 2;//第三方登录

    //密码的最少长度和最长长度
    public static final int USER_PASSWORD_MIN_LENGTH = 6;
    public static final int USER_PASSWORD_MAX_LENGTH = 22;

    //图片上传的标识分类
    public static final String UPLOAD_AVATAR = "avatar";
    public static final String RECHARGE_TYPE_ZHIFUBAO = "alipay";
    public static final String RECHARGE_TYPE_HUIWANGPAY = "huiwangpay";

    //网络连接状态
    public static final int NETWORK_TYPE_NONE = 0;
    public static final int NETWORK_TYPE_MOBILE = 1;
    public static final int NETWORK_TYPE_WIFI = 2;

    //QQ平台登录回调参数
    public static final String QQ_NAME = "name";
    public static final String QQ_TOKEN_ID = "accessToken";
    public static final String QQ_ICON_URL = "iconurl";
    public static final String QQ_UUID = "uid";

    //微信平台登录回调参数
    public static final String WEIXIN_NAME = "name";
    public static final String WEIXIN_TOKEN_ID = "accessToken";
    public static final String WEIXIN_ICON_URL = "iconurl";
    public static final String WEIXIN_UUID = "uid";

    public static final int COUNT_DOWN_MAX_TIME = 60;//倒计时60秒

    //账户详细类型
    public static final String ACCOUNT_DETAIL_TYPE_ALL = "all";//全部
    public static final String ACCOUNT_DETAIL_TYPE_BUY = "buy";//购买
    public static final String ACCOUNT_DETAIL_TYPE_RECHARGE = "recharge";//充值
    public static final String ACCOUNT_DETAIL_TYPE_AWARD = "award";//奖品
    public static final String ACCOUNT_DETAIL_TYPE_WITHDRAW = "withdraw";//提现

    public static final String SYSTEM_ACTION_TYPE_BROWSER = "android.intent.action.VIEW";

    public static final float DIGITS = 100.0f;

}
