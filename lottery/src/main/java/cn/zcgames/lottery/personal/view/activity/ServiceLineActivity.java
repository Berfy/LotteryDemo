package cn.zcgames.lottery.personal.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.model.KefuInfo;
import cn.zcgames.lottery.personal.model.Mobile;
import cn.zcgames.lottery.personal.presenter.KefuPresenter;
import cn.zcgames.lottery.personal.view.iview.IKefuView;

/**
 * author: Berfy
 * date: 2018/11/8
 * 我的客服
 */
public class ServiceLineActivity extends BaseActivity implements IKefuView {

    @BindView(R.id.title_back)
    ImageButton mIBtnBack;
    @BindView(R.id.title_tv)
    TextView mTvTitle;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_wx)
    TextView mTvWx;
    @BindView(R.id.tv_qq)
    TextView mTvQq;
    @BindView(R.id.btn_copy_wx)
    Button mBtnCopyWX;
    @BindView(R.id.btn_copy_qq)
    Button mBtnCopyQQ;
    @BindView(R.id.tv_channel)
    TextView mTvChannel;
    @BindView(R.id.tv_kefu)
    TextView mTvKefu;
    @BindView(R.id.iv_bg)
    ImageView mIvAvatar;

    private KefuPresenter mKefuPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIHelper.hideSystemTitleBar(this);
        setContentView(R.layout.activity_service_line_layout);
        setButterKnife(this);
        initView();
        getInfo();
    }

    private void initView() {
        mKefuPresenter = new KefuPresenter(this, this);
        mIBtnBack.setVisibility(View.VISIBLE);
        mTvTitle.setText("我的客服");
//        if (Constant.DEBUG) {
//            mTvChannel.setText("渠道ID:" + AppConstants.getChannelId());
//            mTvKefu.setText("客服ID:" + MyApplication.getInstance().getApkKeFuInfo());
//        } else {
//            mTvChannel.setVisibility(View.GONE);
//            mTvKefu.setVisibility(View.GONE);
//        }
    }

    private void getInfo() {
        //请求接口展示信息
        mKefuPresenter.getKefuInfo();
    }

    @OnClick({R.id.title_back, R.id.btn_copy_wx, R.id.btn_copy_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(this);
                break;
            case R.id.btn_copy_wx:
                String wx = mTvWx.getText().toString();
                StringUtils.copy(mContext, wx);
                ToastUtil.getInstances().showShort("已复制微信到剪贴板");
                break;
            case R.id.btn_copy_qq:
                String qq = mTvQq.getText().toString();
                StringUtils.copy(mContext, qq);
                ToastUtil.getInstances().showShort("已复制QQ到剪贴板");
                break;
        }
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        if (isFinishing()) {
            return;
        }
        if (isOk) {
            if (object instanceof KefuInfo.Kefu) {
                KefuInfo.Kefu kefu = (KefuInfo.Kefu) object;
                mBtnCopyQQ.setVisibility(View.GONE);
                mBtnCopyWX.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(kefu.getQq())) {
                    mTvQq.setText(kefu.getQq());
                    mBtnCopyQQ.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(kefu.getWechat())) {
                    mTvWx.setText(kefu.getWechat());
                    mBtnCopyWX.setVisibility(View.VISIBLE);
                }
                Mobile mobile = kefu.getMobile();
                if (!TextUtils.isEmpty(mobile.getNumbers())) {
                    mTvPhone.setText(String.format("+%s %s", mobile.getCode(), mobile.getNumbers()));
                }
                Glide.with(mContext)
                        .load(kefu.getAvatar())
                        .error(R.drawable.ic_service_line_bg)
                        .placeholder(R.drawable.ic_service_line_bg)
                        .into(mIvAvatar);
            } else {
                close();
            }
        } else {
            if (object instanceof String) {
                ToastUtil.getInstances().showShort(object.toString());
            } else {
                close();
            }
        }
    }

    private void close() {
        ToastUtil.getInstances().showShort("客服信息获取失败");
        finish();
    }

    @Override
    public void showTipDialog(String msgStr) {

    }

    @Override
    public void hideTipDialog() {

    }
}
