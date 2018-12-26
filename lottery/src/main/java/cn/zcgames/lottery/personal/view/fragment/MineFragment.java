package cn.zcgames.lottery.personal.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.message.view.activity.MessageCenterActivity;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.event.LogoutEvent;
import cn.zcgames.lottery.event.UserInfoUpdateEvent;
import cn.zcgames.lottery.personal.presenter.MinePresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.personal.view.activity.BindingPhoneActivity;
import cn.zcgames.lottery.personal.view.activity.LotteryOrderRecordActivity;
import cn.zcgames.lottery.personal.view.activity.AccountDetailActivity;
import cn.zcgames.lottery.personal.view.activity.AccountInfoActivity;
import cn.zcgames.lottery.personal.view.activity.AfterPhaseActivity;
import cn.zcgames.lottery.personal.view.activity.IdentityInfoActivity;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.activity.PayPasswordActivity;
import cn.zcgames.lottery.personal.view.activity.RechargeActivity;
import cn.zcgames.lottery.personal.view.activity.ServiceLineActivity;
import cn.zcgames.lottery.personal.view.activity.SettingsActivity;
import cn.zcgames.lottery.personal.view.activity.UploadInfoActivity;
import cn.zcgames.lottery.personal.view.activity.VerifyCodeActivity;
import cn.zcgames.lottery.personal.view.activity.WithDrawActivity;
import cn.zcgames.lottery.view.common.picasso.CircleTransformation;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.base.IBaseView;

import static cn.zcgames.lottery.personal.view.activity.IdentityInfoActivity.TYPE_ACTIVITY_WITHDRAW;

/**
 * 首页-我的
 *
 * @author NorthStar
 * @date 2018/8/24 14:22
 */
public class MineFragment extends BaseFragment implements IBaseView {

    @BindView(R.id.mine_tv_nickName)
    TextView mNickNameTv;

    @BindView(R.id.mine_tv_money)
    TextView mMoneyTv;

    @BindView(R.id.mine_tv_income)
    TextView mIncomeTv;

    @BindView(R.id.mine_iv_header)
    ImageView mHeaderIv;

    @BindView(R.id.mine_iv_header_bg)
    ImageView mHeaderIvBg;

    @BindView(R.id.title_right_button)
    ImageButton rightButton;

    @BindView(R.id.tv_line)
    View lineView;

    @BindView(R.id.ll_logout)
    LinearLayout logoutLL;

    private Unbinder unbinder;
    private BaseActivity mContext;
    private MinePresenter mPresenter;
    private static final String TAG = "MineFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContext = (BaseActivity) getActivity();
        View mFragmentView = inflater.inflate(R.layout.fragment_mine_new, container, false);
        unbinder = ButterKnife.bind(this, mFragmentView);
        initData();
        initView();
        return mFragmentView;
    }

    private void initData() {
        EventBus.getDefault().register(this);
        mPresenter = new MinePresenter(getActivity(), this);
    }

    private void initView() {
        rightButton.setImageResource(R.drawable.label_setting);
        UIHelper.showWidget(rightButton, true);
    }

    public void requestMineData() {
        if (MyApplication.isLogin()) {
            if (mPresenter != null) mPresenter.requestMineData();//获取用户数据
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//登出后显示
    public void onEventUserLogout(LogoutEvent event) {
        MyApplication.logout();
        updateMineInfo(null, 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//提现或充值后显示
    public void onEventUserUpdate(UserInfoUpdateEvent event) {
        updateMineInfo(event.getUserBean(), 0);
    }

    //设置我的模块数据UI
    private void updateMineInfo(UserBean user, int type) {
        UserBean mCurrentUser = MyApplication.getCurrLoginUser();
        if (mCurrentUser == null) mCurrentUser = new UserBean();
        if (user == null) {
            mNickNameTv.setText(R.string.mine_no_login);
            mIncomeTv.setText("0.0元");
            mMoneyTv.setText("0.0元");
            mHeaderIv.setImageResource(R.drawable.pic_default_avatar);
            mHeaderIvBg.setVisibility(View.GONE);
        } else {
            String nickname = user.getNickname();
            boolean hasName = !TextUtils.isEmpty(nickname);//昵称
            if (hasName) mCurrentUser.setNickname(nickname);
            mNickNameTv.setText(hasName ? nickname : "未设置昵称");

            //余额
            if (type == 0) mCurrentUser.setMoney(user.getMoney() + "");
            String money = user.getMoney();
            boolean hasMoney = !TextUtils.isEmpty(money);
            if (hasMoney) mCurrentUser.setMoney(money);
            mMoneyTv.setText(hasMoney ? StringUtils.getCash(money, AppConstants.DIGITS) + "元" : "0.0元");

            //可提现余额
            String income = user.getIncome();
            boolean hasIncome = !TextUtils.isEmpty(income);
            if (hasIncome) mCurrentUser.setIncome(income);
            mIncomeTv.setText(hasIncome ? StringUtils.getCash(income, AppConstants.DIGITS) + "元" : "0.0元");

            //头像
            String avatar = user.getAvatar();
            boolean hasAvatar = !TextUtils.isEmpty(avatar);
            if (hasAvatar) mCurrentUser.setAvatar(avatar);
            if (hasAvatar) {
                //清除头像缓存在本地
                //Picasso.with(getActivity()).invalidate(HttpHelper.getPicUrl(bean.getAvatar()));
                LogF.d(TAG, "avatar==>" + mCurrentUser.getAvatar());
                Picasso.with(getActivity())
                        .load(mCurrentUser.getAvatar())
                        .placeholder(R.drawable.pic_default_avatar)
                        .transform(new CircleTransformation())
                        .into(mHeaderIv, new Callback() {
                            @Override
                            public void onSuccess() {
                                mHeaderIvBg.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {
                                mHeaderIvBg.setVisibility(View.GONE);
                            }
                        });
            }

            String isPayPwd = user.getIsPayPwd();//支付密码
            mCurrentUser.setIsPayPwd(isPayPwd);

            String isIdentSet = user.getIsIdentSet();//身份认证
            mCurrentUser.setIsIdentSet(isIdentSet);

            MyApplication.updateCurrLoginUser(mCurrentUser);
        }
    }

    //设置退出
    private void setShowLogoutLL(boolean isShowLogout) {
        logoutLL.setVisibility(isShowLogout ? View.VISIBLE : View.INVISIBLE);
        lineView.setVisibility(isShowLogout ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()) {
            requestMineData();
        }
    }

    @OnClick({R.id.mine_rl_info, R.id.mine_tv_recharge, R.id.mine_tv_cash, R.id.mine_tv_history,
            R.id.mine_tv_accountDetail, R.id.mine_tv_afterPhase, R.id.mine_tv_userInfo,
            R.id.title_right_button, R.id.mine_tv_messages, R.id.mine_tv_service_line, R.id.ll_logout})
    public void onClick(View view) {
        if (checkoutLogin(view)) return;
        if (!AppUtils.checkJump()) return;
        switch (view.getId()) {
            case R.id.mine_rl_info://用户资料编辑页
                UIHelper.gotoActivity(getActivity(), AccountInfoActivity.class, false);
                break;

            case R.id.title_right_button://设置
                UIHelper.gotoActivity(getActivity(), SettingsActivity.class, false);
                break;

            case R.id.mine_tv_recharge://充值
                UIHelper.gotoActivity(getActivity(), RechargeActivity.class, false);
                break;

            case R.id.mine_tv_cash://提现
                setWithDraw();
                break;

            case R.id.mine_tv_history://投注记录
                UIHelper.gotoActivity(getActivity(), LotteryOrderRecordActivity.class, false);
                break;

            case R.id.mine_tv_accountDetail://账户明细
                UIHelper.gotoActivity(getActivity(), AccountDetailActivity.class, false);
                break;

            case R.id.mine_tv_afterPhase://追期管理
                UIHelper.gotoActivity(getActivity(), AfterPhaseActivity.class, false);
                break;

            case R.id.mine_tv_userInfo://账户信息
                UIHelper.gotoActivity(getActivity(), UploadInfoActivity.class, false);
                break;

            case R.id.mine_tv_messages://站内信息页
                UIHelper.gotoActivity(getActivity(), MessageCenterActivity.class, false);
                break;

            case R.id.mine_tv_service_line://我的客服
                UIHelper.gotoActivity(getActivity(), ServiceLineActivity.class, false);
                break;

            case R.id.ll_logout://退出登录
                mPresenter.setLogout();
                break;
        }
    }

    //设置提现
    private void setWithDraw() {
        if (!MyApplication.getCurrLoginUser().isIdentSet()) {
            IdentityInfoActivity.intoThisActivity(getActivity(), TYPE_ACTIVITY_WITHDRAW);
            return;
        }
        if (!MyApplication.getCurrLoginUser().isPhoneOk()) {
            BindingPhoneActivity.bindingPhoneLauncher(getActivity(), AppConstants.REQUEST_BIND_PHONE);
            return;
        }
        if (!MyApplication.getCurrLoginUser().isPayPasswordOk()) {
            PayPasswordActivity.intoThisActivity(this, ActivityConstants.REQUEST_CODE_SET_PAYPASSWORD);
            return;
        }
        UIHelper.gotoActivity(getActivity(), WithDrawActivity.class, false);
    }

    //检查是否登录
    private boolean checkoutLogin(View view) {
        if (!MyApplication.isLogin()) {
            if (view.getId() == R.id.mine_rl_info) {
                UIHelper.gotoActivity(getActivity(), LoginActivity.class, false);
            } else {
                showGotoLogin();
            }
            return true;
        }
        return false;
    }

    /**
     * 提示登录对话框
     */
    private void showGotoLogin() {
        UIHelper.showConfirmDialog(getActivity(),
                R.string.tips_no_login,
                isOk -> {
                    if (isOk) UIHelper.gotoActivity(getActivity(), LoginActivity.class, false);
                });
    }

    @Override
    public void requestResult(final boolean isOk, final Object object) {
        if (isOk) {
            updateMineInfo((UserBean) object, 1);
        } else {
            LogF.d(TAG, "errorMsg" + object);
            String tokenId = MyApplication.getTokenId();
            UserBean user = new UserBean();
            boolean hasTokenId = !TextUtils.isEmpty(tokenId);
            if (hasTokenId) user.setTokenId(tokenId);
            MineFragment.this.updateMineInfo(hasTokenId ? user : null, 1);
        }
    }

    @Override
    public void showTipDialog(String msg) {
        UIHelper.showWaitingDialog(getActivity(), msg, false);
    }

    @Override
    public void hideTipDialog() {
        UIHelper.hideWaitingDialog();
    }

    @Override
    public void onDestroyView() {
        mContext.mIsDestroy = true;
        if (unbinder != null) unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ActivityConstants.REQUEST_CODE_SET_PAYPASSWORD://设置支付密码成功
                    LogF.d(TAG, "设置支付密码成功");
                    UIHelper.gotoActivity(getActivity(), WithDrawActivity.class, false);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
