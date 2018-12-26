package cn.zcgames.lottery.home.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.base.CommonDialogFragment;
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter;
import cn.berfy.sdk.mvpbase.util.DisplayUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.home.bean.TrendSettingBean;
import cn.zcgames.lottery.home.bean.TrendSettingChangeEvent;
import cn.zcgames.lottery.model.local.LotteryUtils;

/**
 * author: Berfy
 * date: 2018/10/17
 * 走势图设置弹出框
 */
public class TrendSettingDialogFragment extends CommonDialogFragment implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.ll_period)
    LinearLayout mLlPeriod;
    @BindView(R.id.rb_period1)
    RadioGroup mRbPeriod1;
    @BindView(R.id.rb_period2)
    RadioGroup mRbPeriod2;
    @BindView(R.id.rb_period_1)
    RadioButton mRbPeriod_1;
    @BindView(R.id.rb_period_2)
    RadioButton mRbPeriod_2;
    @BindView(R.id.rb_period_3)
    RadioButton mRbPeriod_3;
    @BindView(R.id.rb_period_4)
    RadioButton mRbPeriod_4;
    @BindView(R.id.ll_list)
    LinearLayout mLlList;
    @BindView(R.id.rb_list)
    RadioGroup mRbList;
    @BindView(R.id.rb_list_asc)
    RadioButton mRbListAsc;
    @BindView(R.id.rb_list_desc)
    RadioButton mRbListDesc;
    @BindView(R.id.ll_line)
    LinearLayout mLlLine;
    @BindView(R.id.rb_line)
    RadioGroup mRbLine;
    @BindView(R.id.rb_line_show)
    RadioButton mRbLineShow;
    @BindView(R.id.rb_line_dismiss)
    RadioButton mRbLineDismiss;
    @BindView(R.id.ll_yilou)
    LinearLayout mLlYilou;
    @BindView(R.id.rb_yilou)
    RadioGroup mRbYilou;
    @BindView(R.id.rb_yilou_show)
    RadioButton mRbYilouShow;
    @BindView(R.id.rb_yilou_dismiss)
    RadioButton mRbYilouDismiss;
    @BindView(R.id.ll_statistic)
    LinearLayout mLlStatistic;
    @BindView(R.id.rb_statistic)
    RadioGroup mRbStatistic;
    @BindView(R.id.rb_statistic_show)
    RadioButton mRbStatisticShow;
    @BindView(R.id.rb_statistic_dismiss)
    RadioButton mRbStatisticDismiss;
    @BindView(R.id.btn_ok)
    Button mBtnOk;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;

    private String mLotteryType;//彩种
    private boolean mIsNeedListSort;//是否显示排列控制
    private boolean mIsNeedLine;//是否显示折现控制
    private boolean mIsNeedYilou;//是否显示遗漏控制
    private boolean mIsNeedStatistic;//是否需要显示统计控制
    private TrendSettingBean mCacheSettingBean;//设置缓存
    private TrendSettingBean mNewSet = new TrendSettingBean();//设置

    /**
     * @param lotteryType     彩种
     * @param isNeedListSort  是否显示排列控制
     * @param isNeedLine      是否显示折现控制
     * @param isNeedYilou     是否显示遗漏控制
     * @param isNeedStatistic 是否需要显示统计控制
     */
    public static TrendSettingDialogFragment newInstance(String lotteryType, boolean isNeedListSort,
                                                         boolean isNeedLine, boolean isNeedYilou, boolean isNeedStatistic) {
        TrendSettingDialogFragment trendSettingDialogFragment = new TrendSettingDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ActivityConstants.PARAM_LOTTERY_TYPE, lotteryType);
        bundle.putBoolean("isNeedListSort", isNeedListSort);
        bundle.putBoolean("isNeedLine", isNeedLine);
        bundle.putBoolean("isNeedYilou", isNeedYilou);
        bundle.putBoolean("isNeedStatistic", isNeedStatistic);
        trendSettingDialogFragment.setArguments(bundle);
        return trendSettingDialogFragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_dialog_trend_setting_layout;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (null != getArguments()) {
            mLotteryType = getArguments().getString(ActivityConstants.PARAM_LOTTERY_TYPE);
            mIsNeedListSort = getArguments().getBoolean("isNeedListSort");
            mIsNeedLine = getArguments().getBoolean("isNeedLine");
            mIsNeedYilou = getArguments().getBoolean("isNeedYilou");
            mIsNeedStatistic = getArguments().getBoolean("isNeedStatistic");
        }
        mCacheSettingBean = LotteryUtils.getTrendSettingCache(mLotteryType);
    }

    @Override
    public void initView() {
        mRbPeriod1.setOnCheckedChangeListener(this);
        mRbPeriod2.setOnCheckedChangeListener(this);
        mRbList.setOnCheckedChangeListener(this);
        mRbLine.setOnCheckedChangeListener(this);
        mRbYilou.setOnCheckedChangeListener(this);
        mRbStatistic.setOnCheckedChangeListener(this);
        //获取缓存设置选中状态
        if (null != mCacheSettingBean) {
            updateView(mCacheSettingBean);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == mRbPeriod1) {
            mRbPeriod2.setOnCheckedChangeListener(null);
            mRbPeriod2.clearCheck();
            mRbPeriod2.setOnCheckedChangeListener(this);
            if (mRbPeriod_1.isChecked()) {
                mNewSet.setPeriodPos(0);
            } else if (mRbPeriod_2.isChecked()) {
                mNewSet.setPeriodPos(1);
            }

        } else if (group == mRbPeriod2) {
            mRbPeriod1.setOnCheckedChangeListener(null);
            mRbPeriod1.clearCheck();
            mRbPeriod1.setOnCheckedChangeListener(this);
            if (mRbPeriod_3.isChecked()) {
                mNewSet.setPeriodPos(2);
            } else if (mRbPeriod_4.isChecked()) {
                mNewSet.setPeriodPos(3);
            }
        } else if (group == mRbList) {
            mNewSet.setListAsc(mRbListAsc.isChecked());
        } else if (group == mRbYilou) {
            mNewSet.setShowYilou(mRbYilouShow.isChecked());
        } else if (group == mRbLine) {
            mNewSet.setShowLine(mRbLineShow.isChecked());
        } else if (group == mRbStatistic) {
            mNewSet.setShowStatistic(mRbStatisticShow.isChecked());
        }
    }

    @OnClick({R.id.btn_ok, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                ok(true);
                break;
            case R.id.btn_cancel:
                ok(false);
                break;
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getWidth() {
        return DisplayUtil.getDisplayWidth(mContext) / 5 * 4;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    private void updateView(TrendSettingBean trendSettingBean) {
        mLlList.setVisibility(mIsNeedListSort ? View.VISIBLE : View.GONE);
        mLlLine.setVisibility(mIsNeedLine ? View.VISIBLE : View.GONE);
        mLlYilou.setVisibility(mIsNeedYilou ? View.VISIBLE : View.GONE);
        mLlStatistic.setVisibility(mIsNeedStatistic ? View.VISIBLE : View.GONE);
        switch (trendSettingBean.getPeriodPos()) {
            case 0:
                mRbPeriod_1.setChecked(true);
                break;
            case 1:
                mRbPeriod_2.setChecked(true);
                break;
            case 2:
                mRbPeriod_3.setChecked(true);
                break;
            case 3:
                mRbPeriod_4.setChecked(true);
                break;
        }
        mRbList.check(trendSettingBean.isListAsc() ? R.id.rb_list_asc : R.id.rb_list_desc);
        mRbYilou.check(trendSettingBean.isShowYilou() ? R.id.rb_yilou_show : R.id.rb_yilou_dismiss);
        mRbLine.check(trendSettingBean.isShowLine() ? R.id.rb_line_show : R.id.rb_line_dismiss);
        mRbStatistic.check(trendSettingBean.isShowStatistic() ? R.id.rb_statistic_show : R.id.rb_statistic_dismiss);
        mNewSet.setPeriodPos(trendSettingBean.getPeriodPos());
        mNewSet.setListAsc(trendSettingBean.isListAsc());
        mNewSet.setShowStatistic(trendSettingBean.isShowStatistic());
        mNewSet.setShowLine(trendSettingBean.isShowLine());
        mNewSet.setShowYilou(trendSettingBean.isShowYilou());
    }

    private void ok(boolean isSave) {
        if (isSave && !mNewSet.equals(mCacheSettingBean)) {
            LotteryUtils.saveTrendSettingCache(mLotteryType, mNewSet);
            TrendSettingChangeEvent trendSettingChangeEvent = new TrendSettingChangeEvent();
            if (mCacheSettingBean.getPeriodPos() != mNewSet.getPeriodPos()) {//修改了期次 需要刷新
                trendSettingChangeEvent.setNeedRefresh(true);
            }
            trendSettingChangeEvent.setTrendSettingBean(mNewSet);
            EventBus.getDefault().post(trendSettingChangeEvent);
        }
        dismiss();
    }
}
