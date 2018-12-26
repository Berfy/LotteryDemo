package cn.zcgames.lottery.personal.view.iview;

import android.app.Activity;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import cn.zcgames.lottery.personal.model.Mobile;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;

/**
 * 用户相关的 iViewModel
 *
 * @author NorthStar
 * @date 2018/9/13 10:38
 */
public interface IAccountModel {

    /**
     * 请求mine fragment data
     *
     * @param callback
     */
    void requestMinePageData(NormalCallback callback);

    /**
     * 校验注册验证码
     *
     * @param captcha
     * @param callback
     */
    void verifyVerificationCode(String captcha, UserBean user, NormalCallback callback);

    /**
     * 校验修改密码验证码
     *
     * @param captcha
     * @param callback
     */
    void verifyVerificationCaptcha(String captcha, UserBean user, NormalCallback callback);

    /**
     * 绑定邮箱
     *
     * @param captcha
     * @param email
     * @param callback
     */
    void bindEmail(String captcha, String email, NormalCallback callback);


    /**
     * 绑定手机号
     *
     * @param captcha
     * @param mobile
     */
    void bindPhone(String captcha, Mobile mobile, NormalCallback callback);

    /**
     * 请求验证码
     *
     * @param type 用做区分注册或者找回密码    1.注册  2.找回登录密码  3.找回支付密码
     */
    void requestVerifyCode(int type, UserBean user, NormalCallback callback);

    /**
     * 请求身份信息
     *
     * @param callback
     */
    void requestIdentityInfo(NormalCallback callback);

    /**
     * 设置身份信息
     *
     * @param name
     * @param id
     * @param callback
     */
    void setIdentityInfo(String name, String id, NormalCallback callback);

    /**
     * 用户登录
     *
     * @param user
     * @param callback
     */
    void login(UserBean user, NormalCallback callback);

    /**
     * 登录第三方平台
     *
     * @param platform
     */
    void loginThirdPlatform(final SHARE_MEDIA platform, Activity activity, NormalCallback callback);

    /**
     * 更新登录密码
     *
     * @param oldPsw     原密码
     * @param newPsw     新密码
     * @param confirmPsw 确认新密码
     * @param callback
     */
    void updateLoginPassword(String oldPsw, String newPsw, String confirmPsw, NormalCallback callback);

    /**
     * 设置登录密码
     *
     * @param password   密码
     * @param confirmPsw 确认新密码
     * @param callback
     */
    void setLoginPassword(String password, String confirmPsw, NormalCallback callback);

    /**
     * 注册设置密码
     *
     * @param password 密码
     * @param callback
     */
    void setPassword(UserBean user, String password, NormalCallback callback);


    /**
     * 修改支付密码
     *
     * @param mobile      手机号
     * @param captcha     验证码
     * @param newPassword 新密码
     */
    void updatePayPassword(Mobile mobile, String captcha, String newPassword, NormalCallback callback);

    /**
     * 设置支付密码
     * @param captcha 手机号验证码
     * @param password 密码
     */
    void setPayPassword(String captcha, String password, NormalCallback callback);


    /**
     * 找回密码
     *
     * @param captcha
     * @param psw
     * @param callback
     */
    void resetPassword(UserBean user, String captcha, String psw, NormalCallback callback);

    /**
     * 获取购彩记录
     *
     * @param startPageIdx
     * @param ts
     * @param lotteryStatus 全部、未开奖、已开奖
     * @param callback
     */
    void requestOrderRecord(int startPageIdx, long ts, int lotteryStatus, NormalCallback callback);


    /**
     * 获取追期列表
     *
     * @param status
     * @param page
     * @param pageSize
     * @param ts       时间戳 page=1不传 page>1传
     * @param callback
     */
    void requestChaseBillNew(int page, int pageSize, int status, long ts, NormalCallback callback);

    /**
     * 获取追期详情
     *
     * @param lotteryName 彩票类型
     * @param orderId     订单id
     * @param callback
     */
    void requestChaseDetail(String lotteryName, String orderId, NormalCallback callback);

    /**
     * 上传图片
     *
     * @param name
     * @param filePath
     */
    void uploadFile(String name, String filePath, NormalCallback callback);


    /**
     * 发送图片
     *
     * @param type avatar ali_qr, wx_qr
     * @param urls ["http://172.1.2.12:8090/account/upload/9c53b56a-ad99-11e8-ba4d-9a00b89a3501.png"]
     */
    void sendFile(String type, List<String> urls, NormalCallback callback);

    /**
     * 获取站内信息
     *
     * @param startPageIdx 从1开始
     * @param ts           第一次传0,之后不传
     * @param callback
     */
    void requestMessage(int startPageIdx, long ts, NormalCallback callback);

    /**
     * 验证原手机号
     *
     * @param mOldMobile
     * @param s
     * @param callback
     */
    void verifyPhone(String mOldMobile, String s, NormalCallback callback);

    /**
     * 获取追期列表
     *
     * @param status
     * @param startPageIndex
     * @param pageMax
     * @param callback
     */
    void requestChaseBill(String status, int startPageIndex, int pageMax, NormalCallback callback);

    /**
     * 往旧手机号上发送验证码
     *
     * @param callback
     */
    void requestDefaultMobileCode(NormalCallback callback);

    /**
     * 获取客服信息
     *
     * @param callback
     */
    void requestKefuInfo(NormalCallback callback);

}
