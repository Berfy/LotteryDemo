package cn.zcgames.lottery.personal.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.response.ResultRecharge;
import cn.zcgames.lottery.personal.model.CashEntity;
import cn.zcgames.lottery.personal.presenter.RechargePresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.personal.view.adapter.RechargeAdapter;

/**
 * 我的-充值
 *
 * @author NorthStar
 * @date 2018/8/20 16:42
 */
public class RechargeActivity extends BaseActivity implements IBaseView {

    @BindView(R.id.recharge_rv)
    RecyclerView rv;

    @BindView(R.id.tv_wx)
    TextView wxBtn;

    @BindView(R.id.tv_aliPay)
    TextView aliPayBtn;

    private long mRechargeMoney = 1000;//10元
    private RechargePresenter mPresenter;
    private int payType; //100 支付宝,200 微信
    private static final int MSG_RECHARGE_OK = 0;
    private static final int MSG_RECHARGE_FAIL = 1;
    private static final String TAG = "RechargeActivity";
    private Integer[] rechargeCash = {10, 30, 50, 100, 300, 500};
    private List<CashEntity> cashList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RECHARGE_OK:
                    if (msg.obj != null && msg.obj instanceof ResultRecharge.RechargeBean) {
                        ResultRecharge.RechargeBean bean = (ResultRecharge.RechargeBean) msg.obj;
                        String url = bean.getQr_url();
                        // String tempUrl = Constant.DEBUG ? url.replace("http://test.522zf.com", "http://103.47.242.219:12010") : url;
                        payType = bean.getPaytype();
                        String orderId = bean.getOrder_id();
                        RechargeOrderActivity.launcher(RechargeActivity.this, url, "订单页", orderId, payType);
                    }
                    break;
                case MSG_RECHARGE_FAIL:
                    if (msg.obj != null && msg.obj instanceof String) {
                        ToastUtil.getInstances().showShort(msg.obj.toString());
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initData();
        initView();

    }

    private void initData() {
        setButterKnife(this);
        mPresenter = new RechargePresenter(this, this);
        for (int cash : rechargeCash) {
            cashList.add(new CashEntity(cash, cash == 10));
        }
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.mine_money_recharge);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        RechargeAdapter mAdapter = new RechargeAdapter(this, cashList);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        rv.setItemAnimator(null);
        rv.setAdapter(mAdapter);
        setPayType(0);//默认支付宝充值
        mAdapter.setOnItemListener((view, position) -> {
            int prePosition = 0;
            for (int i = 0; i < cashList.size(); i++) {
                if (cashList.get(i).isSelectedFlag()) {
                    prePosition = i;
                }
                if (i == position) {
                    cashList.get(position).setSelectedFlag(true);
                    mRechargeMoney = cashList.get(i).getCash() * 100;
                    LogF.d(TAG, "mRechargeMoney==>" + mRechargeMoney / 100 + "元" + ", payType" + payType);
                } else {
                    cashList.get(i).setSelectedFlag(false);
                }
            }
            mAdapter.notifyItemChanged(prePosition);
            mAdapter.notifyItemChanged(position);
        });
    }

    @OnClick({R.id.title_back, R.id.tv_go_recharge, R.id.ll_aliPay, R.id.ll_wxPay, R.id.tv_recharge_instructions})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back://返回
                goBack(RechargeActivity.this);
                break;
            case R.id.ll_aliPay:
                setPayType(0);
                break;
            case R.id.ll_wxPay:
                setPayType(1);
                break;
            case R.id.tv_go_recharge://去充值
                if (!AppUtils.checkJump()) return;
                if (mRechargeMoney == 0) {
                    ToastUtil.getInstances().showShort("请选择充值金额");
                } else {
                    LogF.d(TAG, "充值金额==>" + mRechargeMoney);
                    mPresenter.recharge(mRechargeMoney / 100 + "元", mRechargeMoney, payType);
                    //                    long amount = mRechargeMoney / 100;
                    //                    mPresenter.recharge(amount + "分", amount, payType);
                    LogF.d(TAG, "goodName==>" + mRechargeMoney / 100 + "元" + ", payType" + payType);
                    //                    mPresenter.recharge(1 + "角", 10, payType);
                }
                break;

            case R.id.tv_recharge_instructions://跳转充值说明界面
                AppUtils.startActivity(mContext, new Intent(mContext, RechargeDirectionActivity.class));
                break;

        }
    }

    private void setPayType(int type) {
        payType = type == 0 ? 100 : 200;
        aliPayBtn.setSelected(type == 0);
        wxBtn.setSelected(type == 1);
        LogF.d(TAG, "payType==>" + payType);
    }


    @Override
    public void requestResult(boolean isOk, Object msgStr) {
        Message msg = new Message();
        msg.obj = msgStr;
        if (isOk) {
            msg.what = MSG_RECHARGE_OK;
        } else {
            msg.what = MSG_RECHARGE_FAIL;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void showTipDialog(String msg) {
        showWaitingDialog(RechargeActivity.this, msg, false);
    }

    @Override
    public void hideTipDialog() {
        hideWaitingDialog();
    }
}
