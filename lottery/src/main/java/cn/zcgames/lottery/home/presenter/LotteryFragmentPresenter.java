package cn.zcgames.lottery.home.presenter;

import android.content.Context;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.home.bean.ADInfo;
import cn.zcgames.lottery.home.bean.LotteryType;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.RequestLotteryPageDataCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.home.view.iview.ILotteryFragment;

/**
 * Created by admin on 2017/4/12.
 */
public class LotteryFragmentPresenter {

    private ILotteryFragment iLotteryFragment;
    private ILotteryModel iLotteryFragmentModel;

    private Context mContext;

    public LotteryFragmentPresenter(Context context, ILotteryFragment iLotteryFragment) {
        this.mContext = context;
        this.iLotteryFragment = iLotteryFragment;
        iLotteryFragmentModel = new LotteryModel();
    }

    private void showTips(List<String> tips) {
        List<TextView> noticeTvs = new ArrayList<>();
        if (null != tips)
            for (String str : tips) {
                TextView tv = new TextView(mContext);
                tv.setText(str);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                noticeTvs.add(tv);
            }
        iLotteryFragment.requestNoticeTips(noticeTvs);
    }

    /**
     * 获取购彩首页的全部数据
     */
    public void requestLotteryPageData() {
        LogF.d("LotteryFragmentPresenter", "刷新首页接口requestLotteryPageData");
        iLotteryFragmentModel.requestLotteryData(new RequestLotteryPageDataCallback() {

            @Override
            public void requestAdInfo(List<ADInfo> ads) {
                iLotteryFragment.requestAdList(ads);
            }

            @Override
            public void requestLotteryType(List<LotteryType> types) {
                if (null == types) {
                    iLotteryFragment.requestLotteryType(new ArrayList<>());
                    return;
                }
                for (int i = 0; i < types.size(); i++) {
                    if (AppConstants.LOTTERY_TYPE_2_COLOR.equals(types.get(i).getName())
                            || AppConstants.LOTTERY_TYPE_3_D.equals(types.get(i).getName())
                            || AppConstants.LOTTERY_TYPE_2_COLOR.equals(types.get(i).getName())
                            || AppConstants.LOTTERY_TYPE_7_HAPPY.equals(types.get(i).getName())
                            || AppConstants.LOTTERY_TYPE_ARRANGE_3.equals(types.get(i).getName())
                            || AppConstants.LOTTERY_TYPE_ARRANGE_5.equals(types.get(i).getName())
                            || AppConstants.LOTTERY_TYPE_7_STAR.equals(types.get(i).getName())
                            || AppConstants.LOTTERY_TYPE_BIG_LOTTO.equals(types.get(i).getName())
                            || AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW.equals(types.get(i).getName())) {
                        types.remove(i);
                        i--;
                    }
//                    if (i % 2 == 0) {
//                        types.get(i).setStaking_countdown(10000 + "");
//                    } else {
//                        types.get(i).setStaking_countdown(20000 + "");
//                    }
                }
//                LotteryType lotteryType115 = new LotteryType();
//                lotteryType115.setName("11选5");
//                lotteryType115.setTag(AppConstants.LOTTERY_TYPE_11_5);
//                lotteryType115.setUrl("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1956898433,2015067345&fm=27&gp=0.jpg");
//                types.add(lotteryType115);
//                LotteryType lotteryTypeold = new LotteryType();
//                lotteryTypeold.setName("老11选5");
//                lotteryTypeold.setTag(AppConstants.LOTTERY_TYPE_11_5_OLD);
//                lotteryTypeold.setUrl("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1956898433,2015067345&fm=27&gp=0.jpg");
//                types.add(lotteryTypeold);
//                LotteryType lotteryTypeYue = new LotteryType();
//                lotteryTypeYue.setName("粤11选5");
//                lotteryTypeYue.setTag(AppConstants.LOTTERY_TYPE_11_5_YUE);
//                lotteryTypeYue.setUrl("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1956898433,2015067345&fm=27&gp=0.jpg");
//                types.add(lotteryTypeYue);
//                LotteryType lotteryTypeLucky = new LotteryType();
//                lotteryTypeLucky.setName("好运11选5");
//                lotteryTypeLucky.setTag(AppConstants.LOTTERY_TYPE_11_5_LUCKY);
//                lotteryTypeLucky.setUrl("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1956898433,2015067345&fm=27&gp=0.jpg");
//                types.add(lotteryTypeLucky);
//                LotteryType lotteryTypeAC = new LotteryType();
//                lotteryTypeAC.setName("重庆时时彩");
//                lotteryTypeAC.setTag(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR);
//                lotteryTypeAC.setUrl("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1956898433,2015067345&fm=27&gp=0.jpg");
//                types.add(lotteryTypeAC);
//                LotteryType caise = new LotteryType();
//                caise.setShow("新时时彩");
//                caise.setName(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW);
//                caise.setIcon_url("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541498585632&di=b7baebf1848268e0ca315369dfaa4f90&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0e2442a7d933c89525bd465dda1373f0830200d1.jpg");
//                caise.setLottery_state("2");
//                types.add(caise);
//                LotteryType heibai = new LotteryType();
//                heibai.setShow("新时时彩");
//                heibai.setName(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW);
//                heibai.setIcon_url("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541498586190&di=955d5ffc45a7b404bb8a8ec1481a9ca0&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fb21c8701a18b87d61a7753390c0828381f30fddb.jpg");
//                heibai.setLottery_state("1");
//                types.add(heibai);
                iLotteryFragment.requestLotteryType(types);
            }

            @Override
            public void requestTips(List<String> tips) {
            }

            @Override
            public void requestFailed(String msgStr) {
                iLotteryFragment.requestFailed(msgStr);
            }
        });

        //中奖信息
        requestWinningNotice();
    }

    //获取中奖循环滚动的信息
    public void requestWinningNotice() {
        iLotteryFragmentModel.requestLotteryWinningNotice(new RequestLotteryPageDataCallback() {
            @Override
            public void requestAdInfo(List<ADInfo> ads) {

            }

            @Override
            public void requestLotteryType(List<LotteryType> types) {

            }

            @Override
            public void requestTips(List<String> tips) {
                showTips(tips);
            }

            @Override
            public void requestFailed(String msgStr) {

            }
        });
    }

}
