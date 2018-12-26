package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;

import java.util.List;

import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.personal.view.iview.IUploadView;
import cn.zcgames.lottery.utils.FileUtils;

/**
 * 上传文件的presenter
 *
 * @author NorthStar
 * @date 2018/8/20 17:47
 */
public class UploadFilePresenter {

    private static final String TAG = "UploadFilePresenter";

    private Activity mContext;
    private IAccountModel mModel;
    private IUploadView iUploadView;

    public UploadFilePresenter(Activity activity, IUploadView iActivity) {
        mContext = activity;
        iUploadView = iActivity;
        mModel = new AccountModel();
    }

    /**
     * 上传图片
     */
    public void uploadFile(String name, String filePath) {
        if (!FileUtils.isFileExistByPath(filePath)) {
            ToastUtil.getInstances().showShort("本地文件不存在");
            return;
        }

        mModel.uploadFile(name, filePath, new NormalCallback() {
            @Override
            public void responseOk(Object msg) {
                if (iUploadView == null) return;
                iUploadView.uploadPic(true, msg);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                if (iUploadView == null) return;
                iUploadView.uploadPic(false, errorMsg);
            }
        });
    }

    /**
     * 将上传的图片url发送给后台
     */
    public void sendFile(String type, List<String> urls) {
        mModel.sendFile(type, urls, new NormalCallback() {
            @Override
            public void responseOk(Object msg) {
                if (iUploadView == null) return;
                iUploadView.commitPic(true, msg);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                if (iUploadView == null) return;
                iUploadView.commitPic(false, errorMsg);
            }
        });
    }
}
