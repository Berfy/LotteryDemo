package cn.zcgames.lottery.app;

import android.text.TextUtils;

import cn.berfy.sdk.mvpbase.util.HostSettings;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * HOST管理
 *
 * @author NorthStar
 * @date 2018/8/22 10:44
 */
public class HttpHelper {

    //服务器域名
    public static String HTTP = HostSettings.getHost();
    public static final String TAG = "HttpHelper";

    //网络请求结果字段key
    //StatInvalidToken    = &StdStatus{201, "token失效"}
    //	StatNoExistsUser    = &StdStatus{202, "用户不存在"}
    //	StatIdentityError   = &StdStatus{203, "用户身份错误"}
    //	StatNamePswdError   = &StdStatus{204, "用户不存在或密码错误"}
    //	StatInvalidParam    = &StdStatus{205, "参数不合法"}
    //	StatWrongCaptcha    = &StdStatus{206, "验证码错误"}
    //	StatPlayerFreezed   = &StdStatus{207, "您的账户被冻结,请联系运营商"}
    //	StatMerchantFreezed = &StdStatus{208, "渠道商被冻结"}
    public static final int RESPONSE_CODE_OK = 200;//业务成功
    public static final int RESPONSE_CODE_TOKEN_INVALID = 201;//token失效
    public static final int RESPONSE_CODE_STOLEN = 202;//被抢登
    public static final int RESPONSE_CODE_FORBIDEN_USER = 207;//用户被冻结
    public static final int RESPONSE_CODE_FORBIDEN_CHANNEL = 208;//渠道被冻结
    public static final String RESPONSE_STATUS_UN_LOGIN = "unlogin";
    public static final String RESPONSE_STATUS_FAIL = "false";

    //网络请求返回码
    public static final String RESPONSE_CODE_502 = "502";
    public static final String RESPONSE_CODE_404 = "404";
    public static final String RESPONSE_CODE_400 = "400";
    public static final String RESPONSE_CODE_307 = "307";
    public static final String RESPONSE_CONNECT_FAIL = "failed to connect to";

    //网络请求header的参数key
    public static final String HEADER_PARAMS_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_PARAMS_AUTHORIZATION = "Authorization";
    public static final String HEADER_PARAMS_CHANNEL_ID = "MerchantId";//渠道id

    //post请求参数的key
    public static final String PARAMS_EMAIL = "email";//邮箱
    public static final String PARAMS_MOBILE = "mobile";//手机号
    public static final String PARAMS_PASSWORD = "password";
    public static final String PARAMS_SERVANT_ID = "servant_id";//客服id
    public static final String PARAMS_PAY_PSWD = "pay_pswd";
    public static final String PARAMS_NEW_PASSWORD = "new_password";
    public static final String PARAMS_OLD_PASSWORD = "old_password";
    public static final String PARAMS_CAPTCHA = "captcha";//验证码
    public static final String PARAMS_REAL_NAME = "real_name";//真实姓名
    public static final String PARAMS_ID = "id";
    public static final String PARAMS_ORDER_ID = "order_id";
    public static final String PARAMS_NICK_NAME = "nickname";//昵称
    public static final String PARAMS_UPLOAD_HEADER_FILE = "avatar";//头像
    public static final String PARAMS_UPLOAD_ALI_QR_FILE = "ali_qr";//支付宝收款二维码
    public static final String PARAMS_UPLOAD_WX_QR_FILE = "wx_qr";//微信收款二维码
    public static final String PARAMS_UPLOAD_NAME = "file";//上传图片的name
    public static final String PARAMS_OEDER_ID = "order_id";//充值订单
    public static final String PARAMS_SEQUENCE = "sequence";//期号
    public static final String PARAMS_AMOUNT = "amount";//充值金额
    public static final String PARAMS_GOODS_NAME = "goods_name";//充值钱数选项
    public static final String PARAMS_CHANNEL = "paytype";//充值方式
    public static final String PARAMS_WITHDRAW_CHANNEL = "pay_type";//提现方式
    public static final String PARAMS_FILE_TAG = "type";//文件标识
    public static final String PARAMS_URLS = "urls";//文件url
    public static final String PARAMS_START_PAGE = "start_page";//page_size
    public static final String PARAMS_PAGE_SIZE = "page_size";
    public static final String PARAMS_TS = "ts";//时间戳
    public static final String PARAMS_STATUS = "status";//开奖状态：全部、已开奖、未开奖
    //1: 注册验证码  2: 重置登录密码验证码  3: 重置支付密码验证码 4: 邮箱设置验证吗 5: 手机号设置验证码
    public static final String PARAMS_AUTH_TAG = "type";//获取验证码标识


    //各个模块url
    //    public static String LOTTERY_FRAGMENT_URL = HTTP + "/platform/home/page/";
    public static String LOTTERY_FRAGMENT_URL = HTTP + "/platform/home_page/"; //首页
    public static String LOTTERY_WINNING_NOTICE = HTTP + "/platform/winner_message/";
    public static String RESULT_FRAGMENT_URL = HTTP + "/platform/draw_info/";//开奖信息
    public static String RESULT_SYSTEM_MSG_URL = HTTP + "/platform/system_message/";//站内信息

    public static String LOTTERY_WINNING_HISTORY = HTTP + "/platform/history/";//各彩种历史开奖
    public static String LOTTERY_TREND = HTTP + "/crawler/trend/";//走势图
    public static String LOTTERY_TREND_OLD = "http://api.caipiao.163.com/missNumber_trend.html";//各彩种走势图
    public static String LOTTERY_WINNING_HISTORY_DETAIL = HTTP + "/platform/detail/";//各彩种历史开奖详情v
    public static String PAY = HTTP + "/wallet/pay_stake_order/";//新付款
    private static String MINE_MODULAR = HTTP + "/player";
    private static String DOUBLE_COLOR = HTTP + "/double_color";//双色球
    private static String WALLET_MODULAR = HTTP + "/wallet";
    private static String THREE_D = HTTP + "/fucai3d";//福彩3D
    private static String FAST_THREE = HTTP + "/kuaisan";
    private static String FAST_THREE_DETAIL = HTTP + "/lottery";
    private static String SHI_SHI_CAI = HTTP + "/shishicai";
    private static String SHI_SHI_CAI_NEW = HTTP + "/newshishicai";
    private static String SEVEN_HAPPY = HTTP + "/seven_happy";
    private static String ARRANGE_THREE = HTTP + "/arraythree";//排列3
    private static String ARRANGE_FIVE = HTTP + "/arrayfive";
    private static String SEVEN_STAR = HTTP + "/sevenstar";
    private static String ELEVEN_FIVE = HTTP + "/elevenfive";

    //用户信息相关的url
    public static String ACCOUNT_REQUEST_CODE = MINE_MODULAR + "/send_auth_code/";//获取验证码
    public static String ACCOUNT_REGISTER = MINE_MODULAR + "/register/";//校验用于注册的验证码
    public static String ACCOUNT_FORGIT_PWD = MINE_MODULAR + "/verify_auth_code/";//校验修改密码的验证码
    public static String ACCOUNT_LOGIN = MINE_MODULAR + "/login/";
    public static String ACCOUNT_LOGOUT = MINE_MODULAR + "/logout/";
    public static String ACCOUNT_MINE_FRAGMENT = MINE_MODULAR + "/get_mine_info/";
    public static String ACCOUNT_SET_IDENTIFY_INFO = MINE_MODULAR + "/set_identity/";
    public static String ACCOUNT_UPDATE_NICK_NAME = MINE_MODULAR + "/set_nickname/";
    public static String ACCOUNT_UPLOAD_FILE = MINE_MODULAR + "/upload/";
    public static String ACCOUNT_SEND_FILE = MINE_MODULAR + "/send_file/";
    public static String ACCOUNT_SET_PASSWORD = MINE_MODULAR + "/set_password/";
    public static String ACCOUNT_RESET_PASSWORD = MINE_MODULAR + "/reset_password/";
    public static String ACCOUNT_SET_PAY_PASSWORD = MINE_MODULAR + "/set_pay_password/";
    public static String ACCOUNT_RESET_PAY_PASSWORD = MINE_MODULAR + "/reset_pay_password/";
    public static String ACCOUNT_CHANGE_PASSWORD = MINE_MODULAR + "/change_password/";
    public static String ACCOUNT_UPDATE_PHONE = MINE_MODULAR + "/update_mobile/";
    public static String ACCOUNT_SET_MOBILE = MINE_MODULAR + "/set_mobile/";
    public static String ACCOUNT_SET_EMAIL = MINE_MODULAR + "/set_email/";
    public static String ACCOUNT_GET_USER_INFO = MINE_MODULAR + "/get_user_info/";
    public static String ACCOUNT_PERSONAL_BILLS = MINE_MODULAR + "/stakes_list/";
    public static String ACCOUNT_DEFAULT_CODE = MINE_MODULAR + "/default_captcha/";
    public static String ACCOUNT_VERIFY_PHONE = MINE_MODULAR + "/verify_mobile/";
    public static String ACCOUNT_CHASE_BILL = MINE_MODULAR + "/chase_bills/";
    public static String ACCOUNT_CHASE_BILL_NEW = MINE_MODULAR + "/chases_list/";
    public static String ACCOUNT_KEFU_INFO = MINE_MODULAR + "/get_servant/";//客服信息

    //双色球
    public static String DOUBLE_COLOR_LATEST_WINNING = DOUBLE_COLOR + "/latest_winning/";
    public static String DOUBLE_COLOR_DETAIL = DOUBLE_COLOR + "/detail/";
    public static String DOUBLE_COLOR_BUY_LOTTERY = DOUBLE_COLOR + "/buy_lottery/";
    public static String DOUBLE_COLOR_SEQUENCE = DOUBLE_COLOR + "/sequence/";
    public static String DOUBLE_COLOR_PERSONAL_BILL = DOUBLE_COLOR + "/personal_bill/";
    public static String DOUBLE_COLOR_BILL_DETAIL = DOUBLE_COLOR + "/bill_detail/";
    public static String DOUBLE_COLOR_CHASE_DETAIL = DOUBLE_COLOR + "/chase_detail/";

    //钱包
    public static String WALLET_RECHARGE = WALLET_MODULAR + "/ccpay/charge/";
    public static String WALLET_PAY_RESULT = WALLET_MODULAR + "/ccpay/query/";
    public static String WALLET_REMAIN = WALLET_MODULAR + "/remain";
    public static String WALLET_WITHDRAW = WALLET_MODULAR + "/withdraw/";
    public static String WALLET_WITHDRAW_RECORD = WALLET_MODULAR + "/withdraw_list/";
    public static String WALLET_ACCOUNT_DETAIL = WALLET_MODULAR + "/detail/";
    public static String WALLET_CARDS = WALLET_MODULAR + "/cards/";
    public static String WALLET_UPDATE_CARD = WALLET_MODULAR + "/update_card/";

    //福彩3d

    public static String THREE_D_LATEST_WINNING = THREE_D + "/latest_winning/";
    public static String THREE_D_WINNING_DETAIL = THREE_D + "/winning_detail/";
    public static String THREE_D_BUY_LOTTERY = THREE_D + "/buy_lottery/";
    public static String THREE_D_SEQUENCE = THREE_D + "/sequence/";
    public static String THREE_D_BILL_DETAIL = THREE_D + "/bill_detail/";

    //快三
    public static String FAST_THREE_LATEST_WINNING = HTTP + "/lottery/draw_list/";//快三最近开奖
    public static String FAST_THREE_WINNING_DETAIL = FAST_THREE + "/winning_detail/";
    public static String FAST_SEQUENCE = HTTP + "/lottery/staking_period/";//快三期数
    public static String FAST_THREE_BUY_ORDER = HTTP + "/stake/fast_three/";//快三下单
    public static String FAST_THREE_BILL_DETAIL = FAST_THREE + "/bill_detail/";

    //订单详情接口url前置
    public static String PRE_ORDER_DETAIL = HTTP + "/lottery/stake_order/";

    //时时彩
    public static String SHI_SHI_CAI_LATEST_WINNING = HTTP + "/lottery/draw_list/";
    public static String SHI_SHI_CAI_WINNING_DETAIL = SHI_SHI_CAI + "/winning_detail/";
    public static String SHI_SHI_CAI_SEQUENCE = HTTP + "/lottery/staking_period/";//时时彩期数
    public static String SHI_SHI_CAI_BUY_ORDER = HTTP + "/stake/tick_tick/";//时时彩下单
    public static String SHI_SHI_CAI_BILL_DETAIL = SHI_SHI_CAI + "/bill_detail/";

    //七乐彩
    public static String SEVEN_HAPPY_LATEST_WINNING = SEVEN_HAPPY + "/latest_winning/";
    public static String SEVEN_HAPPY_WINNING_DETAIL = SEVEN_HAPPY + "/winning_detail/";
    public static String SEVEN_HAPPY_SEQUENCE = SEVEN_HAPPY + "/sequence/";
    public static String SEVEN_HAPPY_BUY = SEVEN_HAPPY + "/buy/";
    public static String SEVEN_HAPPY_BILL_DETAIL = SEVEN_HAPPY + "/bill_detail/";

    //排列三
    public static String ARRANGE3_LATEST_WINNING = ARRANGE_THREE + "/latest_winning/";
    public static String ARRANGE3_WINNING_DETAIL = ARRANGE_THREE + "/winning_detail/";
    public static String ARRANGE3_SEQUENCE = ARRANGE_THREE + "/sequence/";
    public static String ARRANGE3_BUY = ARRANGE_THREE + "/buy/";
    public static String ARRANGE3_BILL_DETAIL = ARRANGE_THREE + "/bill_detail/";

    //排列五
    public static String ARRANGE5_LATEST_WINNING = ARRANGE_FIVE + "/latest_winning/";
    public static String ARRANGE5_WINNING_DETAIL = ARRANGE_FIVE + "/winning_detail/";
    public static String ARRANGE5_SEQUENCE = ARRANGE_FIVE + "/sequence/";
    public static String ARRANGE5_BUY = ARRANGE_FIVE + "/buy/";
    public static String ARRANGE5_BILL_DETAIL = ARRANGE_FIVE + "/bill_detail/";

    //七星彩
    public static String STAR7_LATEST_WINNING = SEVEN_STAR + "/latest_winning/";
    public static String STAR7_WINNING_DETAIL = SEVEN_STAR + "/winning_detail/";
    public static String STAR7_SEQUENCE = SEVEN_STAR + "/sequence/";
    public static String STAR7_BUY = SEVEN_STAR + "/buy/";
    public static String STAR7_BILL_DETAIL = SEVEN_STAR + "/bill_detail/";

    //11选5
    public static String ELEVEN5_LATEST_WINNING = HTTP + "/lottery/draw_list/";
    public static String ELEVEN5_WINNING_DETAIL = ELEVEN_FIVE + "/winning_detail/";
    public static String ELEVEN5_SEQUENCE = HTTP + "/lottery/staking_period/";//11选5期数
    public static String ELEVEN5_BUY_ORDER = HTTP + "/stake/eleven_five/";//11选5下单
    public static String ELEVEN5_BILL_DETAIL = ELEVEN_FIVE + "/bill_detail/";

    //更改主机地址后刷新
    public static void refreshHost(String http) {
        //各个模块url
        HTTP = http;
        LogF.d(TAG, "HttpHelper.HTTP==>" + HttpHelper.HTTP);
        LOTTERY_FRAGMENT_URL = HTTP + "/platform/home/page/";
        LOTTERY_WINNING_NOTICE = HTTP + "/platform/winner_message/";
        RESULT_FRAGMENT_URL = HTTP + "/platform/winning_info/";//开奖信息
        RESULT_SYSTEM_MSG_URL = HTTP + "/platform/system_message/";//站内信息
        LOTTERY_WINNING_HISTORY = HTTP + "/platform/history/";//各彩种历史开奖
        LOTTERY_TREND = HTTP + "/crawler/trend/";//走势图
        MINE_MODULAR = HTTP + "/player";
        DOUBLE_COLOR = HTTP + "/double_color";//双色球
        WALLET_MODULAR = HTTP + "/wallet";
        THREE_D = HTTP + "/fucai3d";//福彩3D
        FAST_THREE = HTTP + "/kuaisan";
        SHI_SHI_CAI = HTTP + "/shishicai";
        SEVEN_HAPPY = HTTP + "/seven_happy";
        ARRANGE_THREE = HTTP + "/arraythree";//排列3
        ARRANGE_FIVE = HTTP + "/arrayfive";
        SEVEN_STAR = HTTP + "/sevenstar";
        ELEVEN_FIVE = HTTP + "/elevenfive";

        //用户信息相关的url
        ACCOUNT_REQUEST_CODE = MINE_MODULAR + "/send_auth_code/";//获取验证码
        ACCOUNT_REGISTER = MINE_MODULAR + "/register/";//注册
        ACCOUNT_SET_PASSWORD = MINE_MODULAR + "/set_password/";
        ACCOUNT_LOGIN = MINE_MODULAR + "/login/";
        ACCOUNT_LOGOUT = MINE_MODULAR + "/logout/";
        ACCOUNT_MINE_FRAGMENT = MINE_MODULAR + "/get_mine_info/";
        ACCOUNT_SET_IDENTIFY_INFO = MINE_MODULAR + "/set_identity/";
        ACCOUNT_UPDATE_NICK_NAME = MINE_MODULAR + "/set_nickname/";
        ACCOUNT_UPLOAD_FILE = MINE_MODULAR + "/upload_avatar/";
        ACCOUNT_SEND_FILE = MINE_MODULAR + "/send_file/";
        ACCOUNT_RESET_PASSWORD = MINE_MODULAR + "/reset_password/";
        ACCOUNT_CHANGE_PASSWORD = MINE_MODULAR + "/change_password/";
        ACCOUNT_SET_PAY_PASSWORD = MINE_MODULAR + "/set_pay_password/";
        ACCOUNT_RESET_PAY_PASSWORD = MINE_MODULAR + "/reset_pay_password/";
        ACCOUNT_UPDATE_PHONE = MINE_MODULAR + "/update_mobile/";
        ACCOUNT_SET_MOBILE = MINE_MODULAR + "/set_mobile/";
        ACCOUNT_SET_EMAIL = MINE_MODULAR + "/set_email/";
        ACCOUNT_GET_USER_INFO = MINE_MODULAR + "/get_user_info/";
        ACCOUNT_PERSONAL_BILLS = MINE_MODULAR + "/stakes_list/";
        ACCOUNT_DEFAULT_CODE = MINE_MODULAR + "/default_captcha/";
        ACCOUNT_VERIFY_PHONE = MINE_MODULAR + "/verify_mobile/";
        ACCOUNT_CHASE_BILL = MINE_MODULAR + "/chase_bills/";

        //双色球
        DOUBLE_COLOR_LATEST_WINNING = DOUBLE_COLOR + "/latest_winning/";
        DOUBLE_COLOR_DETAIL = DOUBLE_COLOR + "/detail/";
        DOUBLE_COLOR_BUY_LOTTERY = DOUBLE_COLOR + "/buy_lottery/";
        DOUBLE_COLOR_SEQUENCE = DOUBLE_COLOR + "/sequence/";
        DOUBLE_COLOR_PERSONAL_BILL = DOUBLE_COLOR + "/personal_bill/";
        DOUBLE_COLOR_BILL_DETAIL = DOUBLE_COLOR + "/bill_detail/";
        DOUBLE_COLOR_CHASE_DETAIL = DOUBLE_COLOR + "/chase_detail/";

        //钱包
        WALLET_RECHARGE = WALLET_MODULAR + "/recharge/";
        WALLET_REMAIN = WALLET_MODULAR + "/remain";
        WALLET_ACCOUNT_DETAIL = WALLET_MODULAR + "/detail/";
        WALLET_WITHDRAW = WALLET_MODULAR + "/withdraw/";
        WALLET_CARDS = WALLET_MODULAR + "/cards/";
        WALLET_UPDATE_CARD = WALLET_MODULAR + "/update_card/";

        //福彩3d
        THREE_D_LATEST_WINNING = THREE_D + "/latest_winning/";
        THREE_D_WINNING_DETAIL = THREE_D + "/winning_detail/";
        THREE_D_BUY_LOTTERY = THREE_D + "/buy_lottery/";
        THREE_D_SEQUENCE = THREE_D + "/sequence/";
        THREE_D_BILL_DETAIL = THREE_D + "/bill_detail/";

        //快三
        FAST_THREE_LATEST_WINNING = HTTP + "/lottery/draw_list/fast_three/";//快三最近开奖
        FAST_THREE_WINNING_DETAIL = FAST_THREE + "/winning_detail/";
        FAST_SEQUENCE = HTTP + "/lottery/period/fast_three/";//快三期数
        FAST_THREE_BUY_ORDER = HTTP + "/lottery/stake/fast_three/";//快三下单
        PAY = HTTP + "/wallet/pay_stake_order/";//新付款
        FAST_THREE_BILL_DETAIL = FAST_THREE + "/bill_detail/";

        //订单与追期详情接口
        PRE_ORDER_DETAIL = HTTP + "/lottery/stake_order";  //订单详情接口url前置

        //时时彩
        SHI_SHI_CAI_LATEST_WINNING = SHI_SHI_CAI + "/latest_winning/";
        SHI_SHI_CAI_WINNING_DETAIL = SHI_SHI_CAI + "/winning_detail/";
        SHI_SHI_CAI_SEQUENCE = HTTP + "/lottery/period/tick_tick/";
        SHI_SHI_CAI_BUY_ORDER = HTTP + "/lottery/stake/tick_tick/";
        SHI_SHI_CAI_BILL_DETAIL = SHI_SHI_CAI + "/bill_detail/";

        //七乐彩
        SEVEN_HAPPY_LATEST_WINNING = SEVEN_HAPPY + "/latest_winning/";
        SEVEN_HAPPY_WINNING_DETAIL = SEVEN_HAPPY + "/winning_detail/";
        SEVEN_HAPPY_SEQUENCE = SEVEN_HAPPY + "/sequence/";
        SEVEN_HAPPY_BUY = SEVEN_HAPPY + "/buy/";
        SEVEN_HAPPY_BILL_DETAIL = SEVEN_HAPPY + "/bill_detail/";

        //排列三
        ARRANGE3_LATEST_WINNING = ARRANGE_THREE + "/latest_winning/";
        ARRANGE3_WINNING_DETAIL = ARRANGE_THREE + "/winning_detail/";
        ARRANGE3_SEQUENCE = ARRANGE_THREE + "/sequence/";
        ARRANGE3_BUY = ARRANGE_THREE + "/buy/";
        ARRANGE3_BILL_DETAIL = ARRANGE_THREE + "/bill_detail/";

        //排列五
        ARRANGE5_LATEST_WINNING = ARRANGE_FIVE + "/latest_winning/";
        ARRANGE5_WINNING_DETAIL = ARRANGE_FIVE + "/winning_detail/";
        ARRANGE5_SEQUENCE = ARRANGE_FIVE + "/sequence/";
        ARRANGE5_BUY = ARRANGE_FIVE + "/buy/";
        ARRANGE5_BILL_DETAIL = ARRANGE_FIVE + "/bill_detail/";

        //七星彩
        STAR7_LATEST_WINNING = SEVEN_STAR + "/latest_winning/";
        STAR7_WINNING_DETAIL = SEVEN_STAR + "/winning_detail/";
        STAR7_SEQUENCE = SEVEN_STAR + "/sequence/";
        STAR7_BUY = SEVEN_STAR + "/buy/";
        STAR7_BILL_DETAIL = SEVEN_STAR + "/bill_detail/";

        //11选5
        ELEVEN5_LATEST_WINNING = ELEVEN_FIVE + "/latest_winning/";
        ELEVEN5_WINNING_DETAIL = ELEVEN_FIVE + "/winning_detail/";
        ELEVEN5_SEQUENCE = HTTP + "/lottery/period/eleven_five/";
        ELEVEN5_BUY_ORDER = HTTP + "/lottery/stake/eleven_five/";
        ELEVEN5_BILL_DETAIL = ELEVEN_FIVE + "/bill_detail/";
    }

    /**
     * 根据返回的错误码获取提示信息
     *
     * @param responseCode
     * @return
     */
    public static String getErrorTipsByResponseCode(String responseCode) {

        String errorMsg = StaticResourceUtils
                .getStringResourceById(R.string.tips_response_code_none);
        if (TextUtils.isEmpty(responseCode)) {
            return errorMsg;
        }
        if (responseCode.contains(RESPONSE_CODE_502)) {
            errorMsg = StaticResourceUtils
                    .getStringResourceById(R.string.tips_response_code_502);
        } else if (responseCode.contains(RESPONSE_CODE_404)) {
            errorMsg = StaticResourceUtils
                    .getStringResourceById(R.string.tips_response_code_404);
        } else if (responseCode.contains(RESPONSE_CODE_400)) {
            errorMsg = StaticResourceUtils
                    .getStringResourceById(R.string.tips_response_code_400);
        } else if (responseCode.contains(RESPONSE_CODE_307)) {
            errorMsg = StaticResourceUtils
                    .getStringResourceById(R.string.tips_response_code_400);
        } else if (responseCode.contains(RESPONSE_CONNECT_FAIL) || responseCode.contains("Failed to connect to")) {
            errorMsg = "服务器连接失败";
        }
        return errorMsg;
    }
}
