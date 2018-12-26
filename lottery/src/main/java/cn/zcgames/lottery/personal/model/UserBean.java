package cn.zcgames.lottery.personal.model;

import android.text.TextUtils;

/**
 * 用户数据
 *
 * @author NorthStar
 * @date 2018/8/28 18:30
 */
public class UserBean {

    private String player_id;//用户id
    private String email;//用户的邮箱号
    private String tokenId;//token
    private String nickname;//昵称
    private String pswd;//登录密码
    private String avatar;//头像
    private Mobile mobile;//手机号
    private String consumable;//可用余额
    private String withdrawable;//提现余额
    private String wx_qr; //微信二维码
    private String ali_qr;//支付宝二维码
    private String tip;//手续费的百分比

    private String identity; //身份证号
    private String realname;//用户真实姓名
    private String is_mobile_set;//是否绑定手机号
    private String is_pay_pswd_set;//是否设置支付密码 1:未设置  2:设置
    private String is_ident_set;//是否已身份认证 1:未设置  2:设置
    private int loginType = -1;//0:账号密码登录；1:第三方登录
    private int loginWay = 0;//登录方式   0手机号登录  1.邮箱登录
    private boolean isPasswordOk;//用于三方登录,暂时不用

    public String getPlayerId() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return pswd;
    }

    public void setPassword(String password) {
        this.pswd = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String iconUrl) {
        this.avatar = iconUrl;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIdentity() {
        return identity == null ? "" : identity;
    }

    public String getRealName() {
        return realname == null ? "" : realname;
    }

    public String getIncome() {
        return withdrawable;
    }

    public void setIncome(String withdrawable) {
        this.withdrawable = withdrawable;
    }

    public String getWx_qr() {
        return wx_qr == null ? "" : wx_qr;
    }

    public String getAli_qr() {
        return ali_qr == null ? "" : ali_qr;
    }

    public void setWx_qr(String wx_qr) {
        this.wx_qr = wx_qr;
    }

    public void setAli_qr(String ali_qr) {
        this.ali_qr = ali_qr;
    }

    public String getMoney() {
        return consumable;
    }

    public void setMoney(String consumable) {
        this.consumable = consumable;
    }

    public boolean isIdentSet() {
        return "2".equals(getIsIdentSet());
    }

    public boolean isPhoneOk() {
        return "2".equals(getIs_mobile_set());
    }

    public boolean isPayPasswordOk() {
        return "2".equals(getIsPayPwd());
    }

    public String getIsPayPwd() {
        return is_pay_pswd_set == null ? "" : is_pay_pswd_set;
    }

    public void setIsPayPwd(String is_pay_pswd_set) {
        this.is_pay_pswd_set = is_pay_pswd_set;
    }

    public String getIsIdentSet() {
        return is_ident_set == null ? "" : is_ident_set;
    }

    public void setIsIdentSet(String is_ident_set) {
        this.is_ident_set = is_ident_set;
    }

    public String getIs_mobile_set() {
        return is_mobile_set == null ? "" : is_mobile_set;
    }

    public void setIs_mobile_set(String is_mobile_set) {
        this.is_mobile_set = is_mobile_set;
    }

    public boolean isPasswordOk() {
        return isPasswordOk;
    }

    public void setPasswordOk(boolean passwordOk) {
        isPasswordOk = passwordOk;
    }

    public Mobile getMobile() {
        return mobile;
    }

    public void setMobile(Mobile mobile) {
        this.mobile = mobile;
    }

    public String getTip() {
        return tip == null ? "" : tip;
    }

    public int getLoginWay() {
        return loginWay;
    }

    public void setLoginWay(int loginWay) {
        this.loginWay = loginWay;
    }

    @Override
    public String toString() {
        String phone = mobile == null ? "" : mobile.getNumbers();
        return "UserBean{" +
                "player_id='" + player_id + '\'' +
                ", email='" + email + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", pswd='" + pswd + '\'' +
                ", avatar='" + avatar + '\'' +
                ", mobile=" + phone +
                ", consumable='" + consumable + '\'' +
                ", withdrawable='" + withdrawable + '\'' +
                ", wx_qr='" + wx_qr + '\'' +
                ", ali_qr='" + ali_qr + '\'' +
                ", tip='" + tip + '\'' +
                ", identity='" + identity + '\'' +
                ", realname='" + realname + '\'' +
                ", is_mobile_set='" + is_mobile_set + '\'' +
                ", is_pay_pswd_set='" + is_pay_pswd_set + '\'' +
                ", is_ident_set='" + is_ident_set + '\'' +
                ", loginType=" + loginType +
                ", loginWay=" + loginWay +
                ", isPasswordOk=" + isPasswordOk +
                '}';
    }
}
