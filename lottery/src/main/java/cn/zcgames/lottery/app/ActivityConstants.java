package cn.zcgames.lottery.app;

/**
 * Activity之间跳转需要的传递的参数
 */
public class ActivityConstants {

    //activity的标题
    public static final String PARAM_ACTIVITY_TITLE = "PARAM_ACTIVITY_TITLE";
    //彩票类型
    public static final String PARAM_LOTTERY_TYPE = "PARAM_LOTTERY_TYPE";
    //彩票玩法
    public static final String PARAM_LOTTERY_PLAY_TYPE = "PARAM_LOTTERY_PLAY_TYPE";
    //彩票玩法
    public static final String PARAM_LOTTERY_PLAY_NUM_POS = "PARAM_LOTTERY_PLAY_NUM_POS";//个十百千万
    //彩票期号
    public static final String PARAM_SEQUENCE = "PARAM_SEQUENCE";
    //总钱数
    public static final String PARAM_TOTAL_MONEY = "PARAM_TOTAL_MONEY";
    //创建好的订单string
    public static final String PARAM_ORDER_STR = "PARAM_ORDER_STR";
    //订单ID
    public static final String PARAM_ID = "PARAM_ID";
    //web链接Url
    public static final String PARAM_URL = "PARAM_URL";
    //user realName
    public static final String PARAM_REAL_NAME = "PARAM_REAL_NAME";

    public static final String PARAM_DOUBLE_COLOR_HISTORY_BEAN = "PARAM_DOUBLE_COLOR_HISTORY_BEAN";


    public static final String PARAM_SOURCE_TYPE = "PARAM__SOURCE_TYP";

    //去设置还是修改密码(登录/支付)
    public static final String PARAM_KEY_VERIFIY_CAPTCHA_TYPE = "PARAM_KEY_VERIFIY_CAPTCHA_TYPE";
    //1: 注册验证码  2: 重置登录密码验证码  3: 重置支付密码验证码 4: 邮箱设置验证吗 5: 手机号设置验证码 6.设置支付密码
    public static final int PARAM_VALUE_REGISTER = 1;//设置登录密码--注册
    public static final int PARAM_VALUE_FIND_PASSWORD = 2;//重置登录密码
    public static final int PARAM_VALUE_UPDATE_PAY_PASSWORD = 3;//重置支付密码
    public static final int PARAM_VALUE_BIND_EMAIL = 4;//绑定邮箱
    public static final int PARAM_VALUE_BIND_MOBILE = 5;//绑定手机
    public static final int PARAM_VALUE_SET_PAY_PASSWORD = 6;//设置支付密码
    public static final int PARAM_VALUE_VERIFIY_PAY_PASSWORD = 7;//验证支付密码

    //设置支付密码type
    public static final String PARAM_KEY_PAY_PASSWORD_REQUESTCODE = "pay_password_requestCod";
    public static final int REQUEST_CODE_SET_PAYPASSWORD = 0;//设置
    public static final int REQUEST_CODE_RESET_PAYPASSWORD = 1;//重置
    public static final int REQUEST_CODE_VERIFIY_PAYPASSWORD = 2;//验证


    public static final int PARAM_VALUE_SAVE_CENTER_LOGIN_PASSWORD = 2;
//    public static final int PARAM_VALUE_SAVE_CENTER_PHONE = 3;

    public static final String PARAM_KEY_LOGIN_TOKEN = "PARAM_KEY_LOGIN_TOKEN";

    //Activity之间传值key：验证码
    public static final String PARAM_ACTION_TYPE = "actionType";
    public static final String PARAM_KEY_MOBILE = "MOBILE";
    public static final String PARAM_KEY_COUNTRYCODE = "COUNTRY_CODE";
    public static final String PARAM_KEY_CAPTCHA = "PARAM_KEY_CAPTCHA";
    public static final String PARAM_KEY_EMAIL = "PARAM_KEY_EMAIL";
    public static final String PARAM_KEY_WIDTH = "PARAM_KEY_WIDTH";
    public static final String PARAM_KEY_HEIGHT = "PARAM_KEY_HEIGHT";
    public static final String KEY_PAYPASSWORD = "KEY_PAY_PASSWOED";//支付密码
}
