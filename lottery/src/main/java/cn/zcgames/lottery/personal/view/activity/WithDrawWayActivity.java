package cn.zcgames.lottery.personal.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.presenter.UserInfoPresenter;
import cn.zcgames.lottery.utils.imagepick.ImagePicUtil;

/**
 * 选择提现方式界面
 *
 * @author NorthStar
 * @date 2018/8/31 10:47
 */
public class WithDrawWayActivity extends BaseActivity implements IBaseView {

    @BindView(R.id.tv_account_wx)
    TextView tvWxPay;
    @BindView(R.id.tv_account_ali)
    TextView tvAliPay;
    @BindView(R.id.line_account_wx)
    ImageView wxIndicator;
    @BindView(R.id.line_account_ali)
    ImageView aliIndicator;

    @BindView(R.id.iv_account_wx_qr)
    ImageView qrWxIv;
    @BindView(R.id.iv_account_ali_qr)
    ImageView qrAliIv;

    @BindView(R.id.btn_select_way)
    TextView qrSelectBtn;

    private String qrWx;
    private String qrAli;
    private ImagePicUtil imagePicUtil;
    private boolean hasQrWx = false;//有无微信提现码
    private boolean hasQrAli = false;//有无支付宝提现码

    private int qrType = 0;//0:是微信二维码 1:是支付宝二维码
    private static final String TAG = "WithDrawWayActivity";
    public static final String KEY_WITHDRAW_WAY = "withdraw_way";//0.微信 1.支付宝
    private UserBean user;


    //该界入口
    public static void launcher(Activity context, int way, int requestCode) {
        Intent intent = new Intent(context, WithDrawWayActivity.class);
        intent.putExtra(KEY_WITHDRAW_WAY, way);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_way);
        initView();
        uploadFile();
        initPresenter();
    }

    private void initPresenter() {
        UserInfoPresenter mPresenter = new UserInfoPresenter(this, this);
        mPresenter.requestUserInfo();
    }

    private void initView() {
        setButterKnife(this);
        user = MyApplication.getCurrLoginUser();
        setLoadQRCodeType(0);
        ((TextView) findViewById(R.id.title_tv)).setText("账户信息");
        UIHelper.showWidget(findViewById(R.id.title_back), true);
    }

    //上传文件
    private void uploadFile() {
        imagePicUtil = new ImagePicUtil(this, new ImagePicUtil.OnPhotoListener() {
            @Override
            public void closeDialog() {
                hideWaitingDialog();
            }

            @Override
            public void getPhotoPath(String url) {
                LogF.d(TAG, "url==>" + url);
                if (qrType == 0) {
                    user.setWx_qr(url);
                } else {
                    user.setAli_qr(url);
                }
                updateQRCode();
            }
        });
    }

    @OnClick({R.id.title_back, R.id.ll_account_wx, R.id.ll_account_ali, R.id.iv_account_wx_qr,
            R.id.iv_account_ali_qr, R.id.btn_select_way})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back://退出
                goBack(WithDrawWayActivity.this);
                break;
            case R.id.ll_account_wx:
                //微信收款码
                setLoadQRCodeType(0);
                break;
            case R.id.ll_account_ali:
                //支付宝收款码
                setLoadQRCodeType(1);
                break;

            case R.id.iv_account_wx_qr:
                //上传微信收款码
                imagePicUtil.writeExternalStorage(0);
                break;

            case R.id.iv_account_ali_qr:
                //上传支付宝收款码
                imagePicUtil.writeExternalStorage(1);
                break;

            case R.id.btn_select_way:
                //将选好的收款码回传到提现界面
                Intent intent = new Intent();
                intent.putExtra(KEY_WITHDRAW_WAY, qrType);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    //设置要上传的二维码类型(0:微信 1:支付宝)
    private void setLoadQRCodeType(int position) {
        qrType = position;
        qrWxIv.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        qrAliIv.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        wxIndicator.setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);
        aliIndicator.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        setEnable();
    }


    @Override
    public void requestResult(boolean isOk, Object object) {
        if (isOk) {
            user = (UserBean) object;
            if (user != null) updateQRCode();
        } else {
            LogF.d(TAG, "拉取失败");
        }
    }

    //更新二维码
    private void updateQRCode() {
        qrWx = user.getWx_qr();
        qrAli = user.getAli_qr();
        LogF.d(TAG, "qrWx==>" + qrWx + " ,qrAli==>" + qrAli);
        hasQrWx = !TextUtils.isEmpty(qrWx);
        hasQrAli = !TextUtils.isEmpty(qrAli);
        UserBean currLoginUser = MyApplication.getCurrLoginUser();
        if (hasQrWx) currLoginUser.setWx_qr(qrWx);
        if (hasQrAli) currLoginUser.setAli_qr(qrAli);
        MyApplication.updateCurrLoginUser(currLoginUser);
        showQRCode();
    }

    //展示提现二维码
    private void showQRCode() {
        setEnable();
        if (hasQrWx) loadQRCode(qrWx, qrWxIv);
        if (hasQrAli) loadQRCode(qrAli, qrAliIv);
    }

    private void setEnable() {
        if (qrType == 0) {
            qrSelectBtn.setEnabled(hasQrWx);
        } else if (qrType == 1) {
            qrSelectBtn.setEnabled(hasQrAli);
        } else {
            qrSelectBtn.setEnabled(false);
        }
    }

    private void loadQRCode(String url, ImageView iv) {
        Picasso.with(this).load(url)
                .placeholder(R.drawable.qr_holder)
                .error(R.drawable.qr_holder)
                .into(iv);
    }

    @Override
    public void showTipDialog(String msgStr) {
        showWaitingDialog(this, msgStr, false);
    }

    @Override
    public void hideTipDialog() {
        hideWaitingDialog();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imagePicUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            showWaitingDialog(this, CommonUtil.getString(this, R.string.tips_upload_doing), false);
        }
        imagePicUtil.onActivityResult(requestCode, resultCode, data);
    }
}
