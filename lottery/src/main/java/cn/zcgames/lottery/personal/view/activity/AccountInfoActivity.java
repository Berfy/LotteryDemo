package cn.zcgames.lottery.personal.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.event.UserInfoUpdateEvent;
import cn.zcgames.lottery.utils.imagepick.ImagePicUtil;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.view.common.XCRoundImageView;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 用户信息页
 *
 * @author NorthStar
 * @date 2018/8/20 15:50
 */
public class AccountInfoActivity extends BaseActivity {

    @BindView(R.id.account_iv_header)
    XCRoundImageView mHeaderIv;

    @BindView(R.id.account_tv_nickName)
    TextView mNicknameTv;

    @BindView(R.id.account_tv_identityInfo)
    TextView mIdentifyInfoTv;

    @BindView(R.id.account_iv_nickName)
    View mNicknameView;

    @BindView(R.id.account_iv_identityInfo)
    View mIdentifyView;


    private UserBean user;
    private ImagePicUtil imagePicUtil;

    private static final String TAG = "AccountInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        initData();
        initView();
        uploadFile();
        showData();
    }

    private void initData() {
        setContentView(R.layout.activity_acount_info);
        EventBus.getDefault().register(this);
        user = MyApplication.getCurrLoginUser();
        setButterKnife(this);
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.mine_account_info);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
    }

    //获取头像的url
    private void uploadFile() {
        imagePicUtil = new ImagePicUtil(this, new ImagePicUtil.OnPhotoListener() {
            @Override
            public void closeDialog() {
                hideWaitingDialog();
            }

            @Override
            public void getPhotoPath(String url) {
                LogF.d(TAG, "avatar==>" + url);
                user.setAvatar(url);
                MyApplication.updateCurrLoginUser(user);
                setAvatar();
            }
        });
    }

    private void showData() {
        if (user == null) return;
        setAvatar();
        setNick(user);
        UIHelper.showWidget(mIdentifyInfoTv, !user.isIdentSet());
        UIHelper.showWidget(mIdentifyView, !user.isIdentSet());
    }

    //设置昵称
    private void setNick(UserBean user) {
        boolean hasNickname = !TextUtils.isEmpty(user.getNickname());
        mNicknameTv.setText(hasNickname ? user.getNickname() : "待补全");
        mNicknameTv.setTextColor(StaticResourceUtils.getColorResourceById(hasNickname ? R.color.color_666666 : R.color.color_app_main));
        UIHelper.showWidget(mNicknameView, !hasNickname);
    }

    //设置头像
    private void setAvatar() {
        if (user == null) return;
        boolean hasHeader = !TextUtils.isEmpty(user.getAvatar());
        if (hasHeader) {
            Picasso.with(this).load(user.getAvatar())
                    .placeholder(R.drawable.label_default_header_red)
                    .error(R.drawable.label_default_header_red)
                    .into(mHeaderIv);
        } else {
            mHeaderIv.setImageResource(R.drawable.label_default_header_red);
        }
    }

    @OnClick({R.id.account_rl_nickName, R.id.account_rl_identityInfo, R.id.title_back, R.id.account_rl_header})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_rl_nickName:
                UIHelper.gotoActivity(AccountInfoActivity.this, SetNicknameActivity.class, false);
                break;
            case R.id.account_rl_identityInfo:
                UIHelper.gotoActivity(AccountInfoActivity.this, IdentityInfoActivity.class, false);
                break;
            case R.id.title_back:
                goBack(AccountInfoActivity.this);
                break;
            case R.id.account_rl_header:
                imagePicUtil.writeExternalStorage(2);
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUserInfoUpdate(UserInfoUpdateEvent event) {
        setNick(event.getUserBean());
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
