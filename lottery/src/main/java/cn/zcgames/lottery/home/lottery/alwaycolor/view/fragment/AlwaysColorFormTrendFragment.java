package cn.zcgames.lottery.home.lottery.alwaycolor.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.berfy.sdk.mvpbase.util.DisplayUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.bean.TrendTypeData;
import cn.zcgames.lottery.home.view.fragment.fastthree.LotteryTrendBaseFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.TrendFragment;
import cn.zcgames.lottery.model.local.LotteryTrendData;

import static cn.zcgames.lottery.app.AppConstants.KEY_TREND_TYPE_DATA;

/**
 * author: Berfy
 * date: 2018/10/26
 * 时时彩-大小单双-形态走势 作为标题显示
 */
public class AlwaysColorFormTrendFragment extends LotteryTrendBaseFragment {

    public static final String TAG = "时时彩走势图-形态走势";
    private TrendFragment mFormTrendFragment;//形态 -3
    private int mPlayType;
    private String mLotteryType;
    private TrendTypeData mTypeData;
    private String mFromType;

    public static AlwaysColorFormTrendFragment newInstance(LotteryTrendData lotteryTrendData,
                                                           int xItemWidth, int leftTitleItemWidth, int leftSecondRow) {
        AlwaysColorFormTrendFragment basicFragment = new AlwaysColorFormTrendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TREND_TYPE_DATA, lotteryTrendData);
        bundle.putSerializable("xItemWidth", xItemWidth);
        bundle.putSerializable("leftTitleItemWidth", leftTitleItemWidth);
        bundle.putSerializable("leftSecondRow", leftSecondRow);
        basicFragment.setArguments(bundle);
        return basicFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View totalView = inflater.inflate(R.layout.fragment_trend_ac_xingtai_layout, container, false);
        initBundle();
        initView();
        return totalView;
    }

    //获取彩种及玩法
    public void initBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            LotteryTrendData data = (LotteryTrendData) bundle.getSerializable(KEY_TREND_TYPE_DATA);
            if (data != null) {
                mLotteryType = data.getLotteryType();
                mPlayType = data.getPlayType();
                mTypeData = data.getTypeData();
                mFromType = data.getSourceType();
                LogF.d(TAG, "初始化参数" + mLotteryType + " ,玩法类型==>" + mPlayType
                        + "来源类型==>" + mFromType);
            }
        } else {
            LogF.d(TAG, "参数为空");
        }
    }

    public void initView() {
        FragmentTransaction transition = getChildFragmentManager().beginTransaction();
        showFormTrendFrag(transition);
    }

    //形态
    private void showFormTrendFrag(FragmentTransaction transaction) {
        if (mFormTrendFragment == null && mTypeData != null) {
            LogF.d(TAG, "形态走势 init");
            int width = DisplayUtil.getDisplayWidth(getActivity());
            int xItemWidth = (width - DisplayUtil.dip2px(mContext, 60) * 2) / 8;
            //重新new
            LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, -3);
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(R.color.color_trend_ssc_form1);
            colors.add(R.color.color_trend_ssc_form2);
            colors.add(R.color.color_trend_ssc_form3);
            colors.add(R.color.color_trend_ssc_form4);
            mFormTrendFragment = TrendFragment.newInstance(lotteryTrendData, xItemWidth,
                    DisplayUtil.dip2px(mContext, 60), DisplayUtil.dip2px(mContext, 60),
                    AppConstants.TREND_STYLE_FORM, colors, false);
            transaction.add(R.id.fragment_layout, mFormTrendFragment, "title_form");
        }
        transaction.show(mFormTrendFragment).commit();
        LogF.d(TAG, "形态走势");
    }
}
