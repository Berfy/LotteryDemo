package cn.zcgames.lottery.personal.view.iview;


/**
 * 上传图片的iView
 * @author NorthStar
 * @date  2018/8/31 18:44
 */
public interface IUploadView {
    //上传图片
    void uploadPic(boolean isOk, Object msg);

    //提交图片
    void commitPic(boolean isOk, Object msg);
}
