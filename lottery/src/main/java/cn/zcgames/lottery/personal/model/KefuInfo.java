package cn.zcgames.lottery.personal.model;

import cn.zcgames.lottery.bean.response.ResponseBaseBean;

/**
 * author: Berfy
 * date: 2018/12/19
 * 客服信息返回
 */
public class KefuInfo extends ResponseBaseBean {

    private Kefu payload;

    public Kefu getPayload() {
        return payload;
    }

    public static class Kefu {
        private String avatar;//头像
        private Mobile mobile;//手机
        private String wechat;//微信
        private String qq;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Mobile getMobile() {
            return mobile;
        }

        public void setMobile(Mobile mobile) {
            this.mobile = mobile;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }
    }
}
