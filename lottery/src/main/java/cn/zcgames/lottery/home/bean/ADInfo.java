package cn.zcgames.lottery.home.bean;

/**
 * 描述：广告信息</br>
 */
public class ADInfo {

    String image_url = "";
    String href = "";
    String tag = "";

    public String getUrl() {
        return image_url == null ? "" : image_url;
    }

    public void setUrl(String url) {
        this.image_url = image_url;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
