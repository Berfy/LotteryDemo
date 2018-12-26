package cn.zcgames.lottery.home.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.berfy.sdk.mvpbase.util.DisplayUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.bean.ADInfo;
import cn.zcgames.lottery.home.bean.LotteryType;
import cn.zcgames.lottery.home.presenter.LotteryFragmentPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.view.activity.WapBannerActivity;
import cn.zcgames.lottery.home.view.adapter.LotteryTypeAdapter;
import cn.zcgames.lottery.view.common.adcycleviewpager.BannerView;
import cn.zcgames.lottery.home.view.iview.ILotteryFragment;
import cn.zcgames.lottery.home.listener.AdCycleViewListener;
import okhttp3.OkHttpClient;

/**
 * 首页-购彩
 * Created by admin on 2017/3/31.
 */
public class LotteryFragment extends BaseFragment
        implements ILotteryFragment, AdCycleViewListener {

    private static final String TAG = "LotteryFragment";

    private Unbinder mUnbinder;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    private View mFragmentView;
    @BindView(R.id.bannelView)
    BannerView mBannerView;
    @BindView(R.id.layout_notice)
    LinearLayout mLlNotice;
    @BindView(R.id.lottery_vf)
    ViewFlipper mNoticeVf;
    @BindView(R.id.lottery_rv_type)
    RecyclerView mTypeRv;
    private LotteryTypeAdapter mTypeAdapter;
    private LotteryFragmentPresenter mPresenter;
    //    private XRefreshView mXScrollView;

    private BaseActivity mContext;
    private List<TextView> tempTvs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.mContext = (BaseActivity) getActivity();
        mFragmentView = inflater.inflate(R.layout.fragment_lottery, container, false);
        mUnbinder = ButterKnife.bind(this, mFragmentView);
        initPresenter();
        initView();
        initListener();
        initTypeAAdapter();
        return mFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    public void requestData() {
        LogF.d(TAG, "刷新首页接口requestData");
        if (null != mPresenter)
            mPresenter.requestLotteryPageData();
    }

    private void initPresenter() {
        mPresenter = new LotteryFragmentPresenter(getActivity(), this);
    }

    private void initTypeAAdapter() {
        mTypeAdapter = new LotteryTypeAdapter(getActivity(), new LotteryTypeAdapter.OnLotteryDataRefreshListener() {
            @Override
            public void onRefresh() {
                LogF.d(TAG, "倒计时结束 刷新首页接口");
                requestData();
            }
        });
        mTypeRv.setAdapter(mTypeAdapter);
    }

    private void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mTypeRv.setLayoutManager(gridLayoutManager);
        mTitleTv.setText(R.string.lottery);

        mNoticeVf.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.my_text_up_in));
        mNoticeVf.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.my_text_up_out));

        //防止scrollView自动滑动到底部
        View parentView = mFragmentView.findViewById(R.id.parent_ll_layout);
        parentView.setFocusable(true);
        parentView.setFocusableInTouchMode(true);
        parentView.requestFocus();

        //下拉刷新
        //        mXScrollView = (XRefreshView) mFragmentView.findViewById(R.id.refreshview_xscrooll);
        //        mXScrollView.setPullRefreshEnable(true);
        //        mXScrollView.setAutoRefresh(false);
        //        mXScrollView.setPullLoadEnable(false);
        //        mXScrollView.setPinnedTime(500);
        //        mXScrollView.setAutoLoadMore(false);
        //        mXScrollView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
        //            @Override
        //            public void onLoadMore(boolean isSilence) {
        //            }
        //
        //            @Override
        //            public void onRefresh() {
        //                requestData();
        //            }
        //        });
    }

    private void setAdData(List<ADInfo> infos) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mBannerView.getLayoutParams();
        layoutParams.width = DisplayUtil.getDisplayWidth(mContext);
        layoutParams.height = (int) (layoutParams.width / 2.4f);
        mBannerView.setLayoutParams(layoutParams);
        mBannerView.setData(infos, new BannerView.ImageCycleViewListener() {
            @Override
            public void onImageClick(ADInfo info) {
                if (null != info && !TextUtils.isEmpty(info.getHref())) {
                    WapBannerActivity.intoThisActivity(mContext, "", info.getHref());
                }
            }
        });
    }

    public void initListener() {
        //viewFliper动画监听
        mNoticeVf.getOutAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //最后一个的时候请求数据
                if (mNoticeVf.getDisplayedChild() == tempTvs.size() - 1) {
                    LogF.d("111111", "再次请求公告数据");
                    mPresenter.requestWinningNotice();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void requestAdList(final List<ADInfo> adInfoList) {
        if (isFragmentDestroy) {
            return;
        }
        if (adInfoList == null || adInfoList.size() <= 0) {
            mBannerView.setVisibility(View.GONE);
        } else {
            mBannerView.setVisibility(View.VISIBLE);
            setAdData(adInfoList);
            mContext.hideWaitingDialog();
        }
    }

    @Override
    public void requestNoticeTips(final List<TextView> tipTvs) {
        if (isFragmentDestroy) {
            return;
        }
        if (null == tipTvs || tipTvs.size() <= 0) {
            mLlNotice.setVisibility(View.GONE);
        } else {
            mLlNotice.setVisibility(View.VISIBLE);
            mNoticeVf.removeAllViews();
            for (TextView tv : tipTvs) {
                mNoticeVf.addView(tv);
                mNoticeVf.getDisplayedChild();
            }
            mNoticeVf.startFlipping();
            tempTvs = tipTvs;
        }
    }

    @Override
    public void requestLotteryType(final List<LotteryType> typeList) {
        if (isFragmentDestroy) {
            return;
        }
//        mXScrollView.stopRefresh();
        //缓存type和name字典  正反各存一对
        for (LotteryType lotteryType : typeList) {
            SharedPreferenceUtil.put(mContext, lotteryType.getName(), lotteryType.getShow());
            SharedPreferenceUtil.put(mContext, lotteryType.getShow(), lotteryType.getName());
            //存储各彩种单注金额
            SharedPreferenceUtil.put(mContext, lotteryType.getName() + "_price", 2f);
        }
        mTypeAdapter.setLotteryTypeList(typeList);
        mContext.hideWaitingDialog();
    }

    @Override
    public void requestFailed(final String errorStr) {
        //        mXScrollView.stopRefresh();
        mContext.hideWaitingDialog();
        UIHelper.showToast(errorStr);
    }

    @Override
    public void requestingData(String msgStr) {
        mContext.showWaitingDialog(getActivity(), msgStr, false);
    }

    @Override
    public void onAdImageClick(ADInfo info, int position, View imageView) {

    }

    @Override
    public void showTipDialog(String msgStr) {
        UIHelper.showWaitingDialog(getActivity(), msgStr, false);
    }

    @Override
    public void hideTipDialog() {
        UIHelper.hideWaitingDialog();
    }

    @Override
    public void requestResult(boolean isOk, Object object) {

    }

    private boolean isFragmentDestroy = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFragmentDestroy = true;
        if (null != mUnbinder)
            mUnbinder.unbind();
        if (null != mTypeAdapter) {
            mTypeAdapter.stopTimer();
        }
    }

    @Override
    public void onDestroyView() {
        mContext.mIsDestroy = true;
        super.onDestroyView();
        mTypeAdapter.cancelAllCountTimers();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mNoticeVf.setVisibility(View.GONE);
            mNoticeVf.stopFlipping();
        } else {
            mNoticeVf.setVisibility(View.VISIBLE);
        }
    }
}
