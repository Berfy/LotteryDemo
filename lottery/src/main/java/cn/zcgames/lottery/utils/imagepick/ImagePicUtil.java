package cn.zcgames.lottery.utils.imagepick;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.qiaoxg.dialoglibrary.ActionSheetDialog;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.ImageUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.personal.presenter.UploadFilePresenter;
import cn.zcgames.lottery.personal.view.iview.IUploadView;
import cn.zcgames.lottery.utils.FileUtils;
import cn.zcgames.lottery.utils.ImageFileUtils;
import cn.zcgames.lottery.utils.PermissionUtils;

/**
 * 图片选取工具类
 *
 * @author NorthStar
 * @date 2018/8/31 14:56
 */
public class ImagePicUtil implements IUploadView {
    private Activity mContext;
    private OnPhotoListener mOnPhotoListener;
    private String fileTag;//文件标识 avatar ali_qr wx_qr
    private static final int CHANGE_HEADER_SELECT = 1;//从相册中选
    private static final int CHANGE_HEADER_TAKE_PHOTO = 2;//拍照
    public static final String TAG = "ImagePicUtil";
    private final UploadFilePresenter mPresenter;
    private List<String> urls;
    private String picUrl;


    public interface OnPhotoListener {

        //回传图片链接
        void getPhotoPath(String url);

        //关闭加载框
        void closeDialog();
    }

    public ImagePicUtil(Activity context, OnPhotoListener onPhotoListener) {
        mContext = context;
        urls = new ArrayList<>();
        mOnPhotoListener = onPhotoListener;
        mPresenter = new UploadFilePresenter(context, this);
    }

    public void writeExternalStorage(int tag) {
        switch (tag) {
            case 0://微信提现码
                fileTag = HttpHelper.PARAMS_UPLOAD_WX_QR_FILE; //微信收款二维码
                break;

            case 1://支付宝提现码
                fileTag = HttpHelper.PARAMS_UPLOAD_ALI_QR_FILE; //支付宝收款二维码
                break;

            case 2://头像
                fileTag = HttpHelper.PARAMS_UPLOAD_HEADER_FILE;//头像
                break;
        }

        PermissionUtils.requestPermission(mContext, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = requestCode -> {
        switch (requestCode) {
            case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                showBottomDialog();
                break;
        }
    };

    //Callback received when a permissions request has been completed.
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(mContext, requestCode, permissions, grantResults, mPermissionGrant);
    }

    private void showBottomDialog() {
        ActionSheetDialog actionSheetDialog = new ActionSheetDialog(mContext)
                .builder()
                .addSheetItem("从相册中选", ActionSheetDialog.SheetItemColor.Blue, which -> {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, AppConstants.SYSTEM_PATH_CHANGE_HEADER);
                    mContext.startActivityForResult(intent, CHANGE_HEADER_SELECT);
                })
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, which -> {
                    LogF.d(TAG, "射像机权限开启成功");
                    File mFile = new File(FileUtils.getLocalUserHeaderTempPath());
                    Intent iconCameraIntent = new Intent();
                    iconCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    //7.0权限适配
                    boolean compareVersion = Build.VERSION.SDK_INT > Build.VERSION_CODES.M;
                    String authority = mContext.getPackageName() + ".fileprovider";
                    Uri mUri = compareVersion ? FileProvider.getUriForFile(mContext, authority, mFile) : Uri.fromFile(mFile);
                    iconCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                    mContext.startActivityForResult(iconCameraIntent, CHANGE_HEADER_TAKE_PHOTO);
                })
                .setCancelable(true)
                .setCanceledOnTouchOutside(true);
        actionSheetDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        String filePath = FileUtils.getLocalUserHeaderTempPath();
        switch (requestCode) {
            case CHANGE_HEADER_TAKE_PHOTO:
                File temp = new File(filePath);
                ImageFileUtils.startCropActivity(mContext, Uri.fromFile(temp));
                break;
            case CHANGE_HEADER_SELECT:
                if (data == null) return;
                ImageFileUtils.startCropActivity(mContext, data.getData());
                break;
            case UCrop.REQUEST_CROP://剪裁后的图片
                try {
                    Uri cropUri = ImageFileUtils.handleCropResult(data);
                    if (cropUri != null) {
                        LogF.d(TAG, "裁剪返回 uri=" + cropUri.getPath());
                        Bitmap bitmap = decodeUriAsBitmap(cropUri);
                        if (bitmap != null) {
                            temp = new File(filePath);
                            ImageUtil.saveFile(bitmap, temp);
                            LogF.d(TAG, "裁剪返回 uri=" + temp.getAbsolutePath());
                            mPresenter.uploadFile(HttpHelper.PARAMS_UPLOAD_NAME, filePath);//上传文件
                        } else {
                            mOnPhotoListener.closeDialog();
                        }
                    } else {
                        LogF.d(TAG, "裁剪返回 null");
                        mOnPhotoListener.closeDialog();
                    }
                } catch (Exception e) {
                    mOnPhotoListener.closeDialog();
                    LogF.d(TAG, "裁剪错误");
                    e.printStackTrace();
                }
                break;
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    @Override
    public void uploadPic(boolean isOk, Object msg) {
        if (isOk) {
            urls.clear();
            picUrl = (String) msg;
            urls.add(picUrl);
            mPresenter.sendFile(fileTag, urls);
        } else {
            mOnPhotoListener.closeDialog();
            if (msg instanceof String) {
                ToastUtil.getInstances().showShort((String) msg);
            }
        }
    }

    @Override
    public void commitPic(boolean isOk, Object msg) {
        if (isOk) {
            mOnPhotoListener.getPhotoPath(picUrl);
            mOnPhotoListener.closeDialog();
            ToastUtil.getInstances().showShort("上传成功");
        } else {
            mOnPhotoListener.closeDialog();
            ToastUtil.getInstances().showShort((String) msg);
        }
    }
}
