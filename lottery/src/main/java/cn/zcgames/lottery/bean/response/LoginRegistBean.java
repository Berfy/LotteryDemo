package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.personal.model.Mobile;

/**
 * author: Berfy
 * date: 2018/9/28
 * 登录注册返回值
 */
public class LoginRegistBean extends ResponseBaseBean {

    private UserBean payload;

    public UserBean getPayload() {
        return payload;
    }

    public static class UserBean {

        private String email;//邮箱
        private Mobile mobile;//手机号
        private String countryCode;//国家码
        private String merchant_id;//渠道id
        private String password;//密码
        private String player_id;//用户id
        private String token;//用户标识
        private String is_pay_pswd_set;//是否设置支付密码 1:没设置 2:设置

        public String getEmail() {
            return email == null ? "" : email;
        }

        public Mobile getMobile() {
            return mobile;
        }

        public String getCountryCode() {
            return countryCode == null ? "" : countryCode;
        }

        public String getMerchant_id() {
            return merchant_id == null ? "" : merchant_id;
        }

        public String getPassword() {
            return password == null ? "" : password;
        }

        public String getPlayer_id() {
            return player_id == null ? "" : player_id;
        }

        public String getToken() {
            return token == null ? "" : token;
        }

        public String getIs_pay_pswd_set() {
            return is_pay_pswd_set == null ? "" : is_pay_pswd_set;
        }
    }
}
