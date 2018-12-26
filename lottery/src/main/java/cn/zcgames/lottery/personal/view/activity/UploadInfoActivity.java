package cn.zcgames.lottery.personal.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.event.UserInfoUpdateEvent;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.presenter.UserInfoPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.utils.imagepick.ImagePicUtil;

/**
 * 用户编辑中心
 *
 * @author NorthStar
 * @date 2018/8/20 17:09
 */
public class UploadInfoActivity extends BaseActivity implements IBaseView {

    @BindView(R.id.userInfo_tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.userInfo_tv_email)
    TextView emailTV;
    @BindView(R.id.userInfo_tv_realName)
    TextView mTvRealName;
    @BindView(R.id.userInfo_tv_idCard)
    TextView mTvIdCard;
    @BindView(R.id.userInfo_tv_phone)
    TextView phoneTV;
    @BindView(R.id.userInfo_tv_phone_code)
    TextView codeTV;
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

    @BindView(R.id.userInfo_rl_phone)
    RelativeLayout phoneRL;

    private String qrWx;
    private String qrAli;
    private ImagePicUtil imagePicUtil;
    private UserInfoPresenter mPresenter;
    private boolean hasQrWx = false;//有无微信提现码
    private boolean hasQrAli = false;//有无支付宝提现码
    private UserBean user = MyApplication.getCurrLoginUser();

    private static final String TAG = "UserInfoActivity";

    public static final int RESPONSE_COED_UPDATE_PHONE = 3;

    private int qrType = 0;//0:是微信二维码 1:是支付宝二维码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setButterKnife(this);
        EventBus.getDefault().register(this);
        initView();
        initPresenter();
        uploadFile();
    }

    private void initPresenter() {
        mPresenter = new UserInfoPresenter(this, this);
        mPresenter.requestUserInfo();
    }

    private void initView() {
        setLoadQRCodeType(0);
        ((TextView) findViewById(R.id.title_tv)).setText("账户信息");
        UIHelper.showWidget(findViewById(R.id.title_back), true);
    }

    //上传文件
    private void uploadFile() {
        //上传文件
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

    @Subscribe(threadMode = ThreadMode.MAIN)//登出后显示
    public void updateIdentity(UserInfoUpdateEvent event) {
        if (mPresenter != null && event != null) mPresenter.requestUserInfo();
    }

    @OnClick({R.id.title_back, R.id.userInfo_rl_phone, R.id.userInfo_rl_loginPassword, R.id.ll_account_wx,
            R.id.ll_account_ali, R.id.iv_account_wx_qr, R.id.iv_account_ali_qr, R.id.userInfo_rl_realName,
            R.id.userInfo_rl_idCard, R.id.userInfo_rl_email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back://退出
                goBack(UploadInfoActivity.this);
                break;
            case R.id.userInfo_rl_email://绑定邮箱 绑定后不可更改
                if (isNotSetting(emailTV)) {
                    BindingEmailActivity.bindingEmailLauncher(UploadInfoActivity.this);
                }
                break;

            case R.id.userInfo_rl_phone://绑定手机号 绑定后不可更改
                if (isNotSetting(phoneTV)) {
                    BindingPhoneActivity.bindingPhoneLauncher(UploadInfoActivity.this, RESPONSE_COED_UPDATE_PHONE);
                }
                break;

            case R.id.userInfo_rl_loginPassword://修改密码
                UIHelper.gotoActivity(UploadInfoActivity.this, SetLoginPasswordActivity.class, false);
                break;
            case R.id.ll_account_wx:
                //微信收款码
                setLoadQRCodeType(0);
                break;

            case R.id.userInfo_rl_realName:
            case R.id.userInfo_rl_idCard:
                //跳转到身份认证页
                if (isNotSetting(mTvRealName)) {
                    IdentityInfoActivity.intoThisActivity(this, IdentityInfoActivity.TYPE_ACTIVITY_USER_INFO);
                }
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
        }
    }

    private boolean isNotSetting(TextView tv) {
        String content = tv.getText().toString();
        String isNotSet = CommonUtil.getString(this, R.string.mine_unbind);
        return content.equals(isNotSet);
    }

    //设置要上传的二维码类型(0:微信 1:支付宝)
    private void setLoadQRCodeType(int position) {
        qrType = position;
        tvWxPay.setTextColor(CommonUtil.getColor(this, position == 0 ? R.color.color_333333 : R.color.C01));
        tvAliPay.setTextColor(CommonUtil.getColor(this, position == 0 ? R.color.C01 : R.color.color_333333));
        qrWxIv.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        qrAliIv.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        wxIndicator.setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);
        aliIndicator.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    //更新二维码
    private void updateQRCode() {
        qrWx = user.getWx_qr();
        qrAli = user.getAli_qr();
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
        if (hasQrWx) loadQRCode(qrWx, qrWxIv);
        if (hasQrAli) loadQRCode(qrAli, qrAliIv);
    }

    private void loadQRCode(String url, ImageView iv) {
        Picasso.with(this).load(url)
                .placeholder(R.drawable.qr_holder)
                .error(R.drawable.qr_holder)
                .into(iv);
    }

    //展示条目内容
    private void showContentText() {
        if (user != null) {
            setContent(false, user.getNickname(), mTvNickname);
            setContent(false, user.getEmail(), emailTV);
            setContent(false, user.getIdentity(), mTvIdCard);
            setContent(false, user.getRealName(), mTvRealName);
            setContent(true, user.getMobile().getNumbers(), phoneTV);
            updateQRCode();
        } else {
            showDialog();
        }
    }

    //设置内容
    private void setContent(boolean isPhone, String contentStr, TextView tv) {
        boolean empty = TextUtils.isEmpty(contentStr);
        String content = CommonUtil.getString(this, R.string.mine_unbind);
        if (!empty) {
            if (isPhone) {
                codeTV.setVisibility(View.VISIBLE);
                codeTV.setText(String.format("+%s", user.getMobile().getCode()));
            } else {
                codeTV.setVisibility(View.GONE);
            }
            content = contentStr;
            tv.setCompoundDrawables(null, null, null, null);
            phoneRL.setClickable(false);
        } else {
            Drawable rightDrawable = getResources().getDrawable(R.drawable.btn_right);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
            tv.setCompoundDrawables(null, null, rightDrawable, null);
            phoneRL.setClickable(true);
        }

        if (!TextUtils.isEmpty(content)) {
            tv.setText(content);
            tv.setTextColor(CommonUtil.getColor(this, empty ? R.color.color_red_ball : R.color.color_333333));
        }
    }


    private void showDialog() {
        AlertDialog confirmDialog = new AlertDialog(UploadInfoActivity.this)
                .builder()
                .setCancelable(false)
                .setMsg("获取用户信息失败，重新获取?")
                .setNegativeButton("不了", v ->
                        goBack(UploadInfoActivity.this)).setPositiveButton
                        ("好的", v -> mPresenter.requestUserInfo());
        confirmDialog.show();
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        if (isOk) {
            user = (UserBean) object;
            showContentText();
        } else {
            showDialog();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            showWaitingDialog(this,  CommonUtil.getString(this, R.string.tips_upload_doing), false);
        }
        imagePicUtil.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imagePicUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
