package cn.zcgames.lottery.home.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.bean.BasicTrendBean;
import cn.zcgames.lottery.home.bean.OpenAwardBean;
import cn.zcgames.lottery.home.bean.TrendData;
import cn.zcgames.lottery.home.bean.TrendResponseData;
import cn.zcgames.lottery.home.bean.TrendTypeData;
import cn.zcgames.lottery.home.view.iview.ITrendActivity;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;

/**
 * @author Berfy
 * 走势图presenter
 */
public class TrendPresenter {

    private Activity mContext;
    private ILotteryModel iLotteryModel;
    private ITrendActivity iTrendView;
    public static final String TAG = "走势图频率";
    private List<Integer> primeNums = new ArrayList<>();//质数

    public TrendPresenter(Activity activity, ITrendActivity iTrendView) {
        mContext = activity;
        this.iTrendView = iTrendView;
        this.iLotteryModel = new LotteryModel();
        primeNums.add(1);
        primeNums.add(2);
        primeNums.add(3);
        primeNums.add(5);
        primeNums.add(7);
        primeNums.add(11);
    }

    /**
     * 查询当前期号
     */
    public void getTrendData(String lotteryType) {
        iLotteryModel.requestTrend(lotteryType, new NormalCallback() {
            @Override
            public void responseOk(Object msg) {
                iTrendView.requestResult(true, msg);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {
                iTrendView.requestResult(false, errorMsg);
            }
        });
    }

    public void getFastThreeData(String lotteryType, List<TrendResponseData.TrendData> missDatas) {
        TrendTypeData trendTypeData = new TrendTypeData();
        List<TrendData> trendDatasSum = new ArrayList<>();
        List<TrendData> trendDatas2Diff = new ArrayList<>();
        for (int i = 0; i < missDatas.size(); i++) {
            TrendResponseData.TrendData missBean = missDatas.get(i);
            String winNumber = "", period = "", times = "", times2Diff = "";
            if (missBean != null) {
                period = missBean.getPeriod().substring(missBean.getPeriod().length() - 2, missBean.getPeriod().length());
                for (int j = 0; j < missBean.getWinnerNumber().size(); j++) {
                    if (j == missBean.getWinnerNumber().size() - 1) {
                        winNumber = winNumber + missBean.getWinnerNumber().get(j);
                    } else {
                        winNumber = winNumber + missBean.getWinnerNumber().get(j) + " ";
                    }
                }
                if (missBean.getMissNumber().getSum().size() > 0) {
                    for (int j = 0; j < missBean.getMissNumber().getSum().size(); j++) {
                        if (j == missBean.getMissNumber().getSum().size() - 1) {
                            times = times + missBean.getMissNumber().getSum().get(j);
                        } else {
                            times = times + missBean.getMissNumber().getSum().get(j) + ",";
                        }
                    }
                } else {
                    times = "等待开奖";
                }

                if (missBean.getMissNumber().getErbutong().size() > 0) {
                    for (int j = 0; j < missBean.getMissNumber().getErbutong().size(); j++) {
                        if (j == missBean.getMissNumber().getErbutong().size() - 1) {
                            times2Diff = times2Diff + missBean.getMissNumber().getErbutong().get(j);
                        } else {
                            times2Diff = times2Diff + missBean.getMissNumber().getErbutong().get(j) + ",";
                        }
                    }
                } else {
                    times2Diff = "等待开奖";
                }

                TrendData trendData = new TrendData();
                trendData.setPid(period + "期");
                trendData.setType("row");
                trendData.setBlue(times);
                trendDatasSum.add(trendData);
                TrendData trendData2Diff = new TrendData();
                trendData2Diff.setPid(period + "期");
                trendData2Diff.setType("row");
                trendData2Diff.setWinNumbers(winNumber);
                trendData2Diff.setBlue(times2Diff);
                trendDatas2Diff.add(trendData2Diff);
            }
        }
        //和值
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            titles.add("" + (i + 3));
        }
        trendTypeData.getTrendTitles().put(lotteryType + "_" + AppConstants.FAST_THREE_SUM, titles);
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_" + AppConstants.FAST_THREE_SUM, new ArrayList<>());
        trendDatasSum.addAll(getStatistic(lotteryType, AppConstants.FAST_THREE_SUM, 0, titles, missDatas));
        trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.FAST_THREE_SUM, trendDatasSum);

        //二不同
        List<String> titles2Diff = new ArrayList<>();
        for (Integer number : LotteryUtils.mFastThreeErBuTongs) {
            titles2Diff.add(number + "");
        }
        trendTypeData.getTrendTitles().put(lotteryType + "_" + AppConstants.FAST_THREE_2_DIFFERENT, titles2Diff);
        List<String> leftTitles2Diff = new ArrayList<>();
        leftTitles2Diff.add("开奖号");
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_" + AppConstants.FAST_THREE_2_DIFFERENT, leftTitles2Diff);
        trendDatas2Diff.addAll(getStatistic(lotteryType, AppConstants.FAST_THREE_2_DIFFERENT, 0, titles2Diff, missDatas));
        trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.FAST_THREE_2_DIFFERENT, trendDatas2Diff);
        iTrendView.resultData(trendTypeData);
    }

    //计算统计

    /**
     * @param lotteryType 玩法
     * @param playType    玩法
     * @param numPos      目前只适用11选5和时时彩  区分个位-万位（1-5） 跨度0 振幅-1 基本走势-2 形态-3
     */
    private List<TrendData> getStatistic(String lotteryType, int playType, int numPos, List<String> titles,
                                         List<TrendResponseData.TrendData> missDatas) {
        LogF.d(TAG, "计算底部四行 lotteryType=" + lotteryType + " playType=" + playType + " numPos=" + numPos);
        List<TrendData> trendDatas = new ArrayList<>();
        int[] showTimes = new int[titles.size()];
        int[] avgMiss = new int[titles.size()];
        int[] maxMiss = new int[titles.size()];
        int[] maxContinue = new int[titles.size()];
        int[] tempContinue = new int[titles.size()];

        //平均遗漏
        int[] num = new int[titles.size()];
        int[] sum = new int[titles.size()];
        int[] tempNumber = new int[titles.size()];

        for (int i = 0; i < missDatas.size(); i++) {
            TrendResponseData.TrendData missBean = missDatas.get(i);
            List<String> numbers = new ArrayList<>();
            int startPos = 0, endPos = 0;
            switch (lotteryType) {
                case AppConstants.LOTTERY_TYPE_FAST_3:
                case AppConstants.LOTTERY_TYPE_FAST_3_JS:
                case AppConstants.LOTTERY_TYPE_FAST_3_HB:
                case AppConstants.LOTTERY_TYPE_FAST_3_EASY:
                case AppConstants.LOTTERY_TYPE_FAST_3_NEW:
                    if (playType == AppConstants.FAST_THREE_SUM) {
                        numbers = missBean.getMissNumber().getSum();
                    } else if (playType == AppConstants.FAST_THREE_2_DIFFERENT) {
                        numbers = missBean.getMissNumber().getErbutong();
                    }
                    startPos = 0;
                    endPos = numbers.size();
                    break;
                case AppConstants.LOTTERY_TYPE_11_5:
                case AppConstants.LOTTERY_TYPE_11_5_LUCKY:
                case AppConstants.LOTTERY_TYPE_11_5_OLD:
                case AppConstants.LOTTERY_TYPE_11_5_YILE:
                case AppConstants.LOTTERY_TYPE_11_5_YUE:
                    switch (playType) {
                        case AppConstants.PLAY_11_5_FRONT_1_DIRECT:
                            if (numPos == -3) {//形态走势
                                numbers = missBean.getMissNumber().getQian1_danshi_xingtai();
                                startPos = 0;
                                endPos = numbers.size();
                            } else {//万位走势
                                numbers = missBean.getMissNumber().getQian3_zhixuan();
                                startPos = 0;
                                endPos = 11;
                            }
                            break;
                        case AppConstants.PLAY_11_5_FRONT_2_DIRECT:
                            if (numPos == 5) {//万位走势
                                numbers = missBean.getMissNumber().getQian3_zhixuan();
                                startPos = 0;
                                endPos = 11;
                            } else {//千位
                                numbers = missBean.getMissNumber().getQian3_zhixuan();
                                startPos = 11;
                                endPos = 22;
                            }
                            break;
                        case AppConstants.PLAY_11_5_FRONT_3_DIRECT:
                            if (numPos == 5) {//万位走势
                                numbers = missBean.getMissNumber().getQian3_zhixuan();
                                startPos = 0;
                                endPos = 11;
                            } else if (numPos == 4) {//千位
                                numbers = missBean.getMissNumber().getQian3_zhixuan();
                                startPos = 11;
                                endPos = 22;
                            } else {//百位
                                numbers = missBean.getMissNumber().getQian3_zhixuan();
                                startPos = 22;
                                endPos = 33;
                            }
                            break;
                        case AppConstants.PLAY_11_5_FRONT_2_GROUP:
                            numbers = missBean.getMissNumber().getQian2_zuxuan();
                            startPos = 0;
                            endPos = numbers.size();
                            break;
                        case AppConstants.PLAY_11_5_FRONT_3_GROUP:
                            numbers = missBean.getMissNumber().getQian3_zuxuan();
                            startPos = 0;
                            endPos = numbers.size();
                            break;
                    }
                    break;
                case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR:
                case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                    switch (playType) {
                        case AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                            if (numPos == -3) {//形态走势
                                numbers = missBean.getMissNumber().getDaxiaodanshuang();
                                startPos = 0;
                                endPos = numbers.size();
                            }
                            break;
                        case AppConstants.ALWAYS_COLOR_1_DIRECT:
                            if (numPos == -1) {//个位振幅
                                numbers = missBean.getMissNumber().getYixing_zhixuan_swing();
                                startPos = 0;
                                endPos = numbers.size();
                            }
                            break;
                        case AppConstants.ALWAYS_COLOR_2_GROUP:
                            if (numPos == 0) {//跨度
                                numbers = missBean.getMissNumber().getErxing_zuxuan_span();
                                startPos = 0;
                                endPos = numbers.size();
                            } else if (numPos == -2) {//基本走势
                                numbers = missBean.getMissNumber().getErxing_zuxuan();
                                startPos = 0;
                                endPos = numbers.size();
                            }
                            break;
                        case AppConstants.ALWAYS_COLOR_3_GROUP_3:
                            if (numPos == 0) {//跨度
                                numbers = missBean.getMissNumber().getSanxing_zuxuan_span();
                                startPos = 0;
                                endPos = numbers.size();
                            } else if (numPos == -2) {//基本走势
                                numbers = missBean.getMissNumber().getSanxing_zuliu();
                                startPos = 0;
                                endPos = numbers.size();
                            }
                            break;
                        case AppConstants.ALWAYS_COLOR_3_GROUP_6:
                            if (numPos == 0) {//跨度
                                numbers = missBean.getMissNumber().getSanxing_zuxuan_span();
                                startPos = 0;
                                endPos = numbers.size();
                            } else if (numPos == -2) {//基本走势
                                numbers = missBean.getMissNumber().getSanxing_zusan();
                                startPos = 0;
                                endPos = numbers.size();
                            }
                            break;
                    }
                    switch (numPos) {
                        case 1:
                            numbers = missBean.getMissNumber().getWuxing_tongxuan();
                            startPos = 40;
                            endPos = 50;
                            LogF.d(TAG, "时时彩 个位 ");
                            break;
                        case 2:
                            numbers = missBean.getMissNumber().getWuxing_tongxuan();
                            startPos = 30;
                            endPos = 40;
                            LogF.d(TAG, "时时彩 十位 ");
                            break;
                        case 3:
                            numbers = missBean.getMissNumber().getWuxing_tongxuan();
                            startPos = 20;
                            endPos = 30;
                            LogF.d(TAG, "时时彩 百位 ");
                            break;
                        case 4:
                            numbers = missBean.getMissNumber().getWuxing_tongxuan();
                            LogF.d(TAG, "时时彩 千位 ");
                            startPos = 10;
                            endPos = 20;
                            break;
                        case 5:
                            numbers = missBean.getMissNumber().getWuxing_tongxuan();
                            startPos = 0;
                            endPos = 10;
                            LogF.d(TAG, "时时彩 万位 ");
                            break;
                    }
                    break;
            }
            if (endPos <= numbers.size()) {
                for (int pos = startPos; pos < endPos; pos++) {
                    int truePos = pos - startPos;
                    //出现次数
                    if (LotteryUtils.isTrendWinNumber(numbers.get(pos))) {
                        showTimes[truePos]++;
                    }
                    //最大遗漏
                    if (Integer.parseInt(numbers.get(pos)) > maxMiss[truePos]) {
                        maxMiss[truePos] = Integer.parseInt(numbers.get(pos));
                        //                        tempAvgMiss[j] += maxMiss[j];
                    }
                    //最大连出
                    if (LotteryUtils.isTrendWinNumber(numbers.get(pos))) {
                        tempContinue[truePos]++;
                        if (maxContinue[truePos] < tempContinue[truePos]) {
                            maxContinue[truePos] = tempContinue[truePos];
                        }
                    } else {
                        tempContinue[truePos] = 0;
                    }

                    if (Integer.parseInt(numbers.get(pos)) > tempNumber[truePos]) {
                        tempNumber[truePos] = Integer.parseInt(numbers.get(pos));
                    } else if (0 == Integer.parseInt(numbers.get(pos))) {
                        sum[truePos] += tempNumber[truePos];
                        tempNumber[truePos] = 0;
                    }
                }
            }
        }//求和
        for (int i = 0; i < num.length; i++) {
            if (tempNumber[i] > 0) {
                sum[i] += tempNumber[i];
            }
            Log.d("111111", "求出的和是：" + sum[i]);
            num[i] = sum[i] / (showTimes[i] + 1);
            Log.d("111111", "求出的平均值是：" + num[i]);
        }
        //平均遗漏
        avgMiss = num;
        Log.d("111111", "出现次数==" + GsonUtil.getInstance().toJson(showTimes)
                + "最大遗漏==" + GsonUtil.getInstance().toJson(maxMiss)
                + "平均遗漏==" + GsonUtil.getInstance().toJson(avgMiss)
                + "连出=" + GsonUtil.getInstance().toJson(maxContinue));
        List<TrendData> basicData = new ArrayList<>();
        String showTime2 = "", avgMiss2 = "", maxMiss2 = "", maxContinue2 = "";
        showTime2 = arrayToString(showTime2, showTimes);
        avgMiss2 = arrayToString(avgMiss2, avgMiss);
        maxMiss2 = arrayToString(maxMiss2, maxMiss);
        maxContinue2 = arrayToString(maxContinue2, maxContinue);
        String[] tabs = {showTime2, avgMiss2, maxMiss2, maxContinue2};
        String[] types = {"dis", "avg", "mmv", "mlv"};
        for (int j = 0; j < tabs.length; j++) {
            TrendData trendData = new TrendData();
            trendData.setBlue(tabs[j]);
            trendData.setType(types[j]);
            basicData.add(trendData);
        }
        return basicData;
    }

    private String arrayToString(String name, int[] names) {
        for (int j = 0; j < names.length; j++) {
            if (j == names.length - 1) {
                name = name + names[j];
            } else {
                name = name + names[j] + ",";
            }
        }
        return name;
    }

    //11选5走势图数据
    public void setEleven5TrendData(String lotteryType, int mPlayType, List<TrendResponseData.TrendData> datas) {
        List<OpenAwardBean> awardDatas = new ArrayList<>();
        List<OpenAwardBean> formData = new ArrayList<>();
        List<BasicTrendBean> basicDatas = new ArrayList<>();
        List<TrendData> directFormData = new ArrayList<>();
        List<TrendData> wanDatas = new ArrayList<>();
        List<TrendData> qianDatas = new ArrayList<>();
        List<TrendData> baiDatas = new ArrayList<>();
        List<TrendData> g2Datas = new ArrayList<>();
        List<TrendData> g3Datas = new ArrayList<>();
        TrendTypeData trendTypeData = new TrendTypeData();
        if (datas != null && datas.size() > 0) {
            TrendResponseData.MissNumber missNumber = datas.get(datas.size() - 1).getMissNumber();
            boolean hasMissData = missNumber != null && missNumber.getRenxuan_general() != null
                    && missNumber.getRenxuan_general().size() > 0;

            for (int i = 0; i < datas.size(); i++) {
                TrendResponseData.TrendData missBean = datas.get(i);
                getOpenAwardData(mPlayType,missBean, awardDatas, formData);
                getBasicData(missBean, basicDatas);
                if (missBean != null) {
                    StringBuilder wanMiss = new StringBuilder();
                    StringBuilder qianMiss = new StringBuilder();
                    StringBuilder baiMiss = new StringBuilder();
                    String period = missBean.getPeriod().substring(missBean.getPeriod().length() - 2, missBean.getPeriod().length());
                    List<String> qian3Zhixuan = missBean.getMissNumber().getQian3_zhixuan();
                    if (qian3Zhixuan != null && qian3Zhixuan.size() == 33) {
                        for (int j = 0; j < qian3Zhixuan.size(); j++) {
                            if (j == 10) {//万位
                                wanMiss.append(qian3Zhixuan.get(j));
                            } else if (j < 11) {//万位
                                wanMiss.append(qian3Zhixuan.get(j));
                                wanMiss.append(",");
                            } else if (j == 21) { //千位
                                qianMiss.append(qian3Zhixuan.get(j));
                            } else if (j < 22) { //千位
                                qianMiss.append(qian3Zhixuan.get(j));
                                qianMiss.append(",");
                            } else if (j == 32) { //百位
                                baiMiss.append(qian3Zhixuan.get(j));
                            } else if (j < 33) { //百位
                                baiMiss.append(qian3Zhixuan.get(j));
                                baiMiss.append(",");
                            }
                        }
                    } else {
                        wanMiss.append("等待开奖");
                        qianMiss.append("等待开奖");
                        baiMiss.append("等待开奖");
                    }
                    TrendData wanData = new TrendData();
                    wanData.setPid(period);
                    wanData.setType("row");
                    wanData.setBlue(wanMiss.toString());
                    wanDatas.add(wanData);
                    TrendData qianData = new TrendData();
                    qianData.setPid(period);
                    qianData.setType("row");
                    qianData.setBlue(qianMiss.toString());
                    qianDatas.add(qianData);
                    TrendData baiData = new TrendData();
                    baiData.setPid(period);
                    baiData.setType("row");
                    baiData.setBlue(baiMiss.toString());
                    baiDatas.add(baiData);
                }

                if (mPlayType == AppConstants.PLAY_11_5_FRONT_1_DIRECT) {
                    get115DirectFormData(missBean, directFormData);
                }
                if (mPlayType == AppConstants.PLAY_11_5_FRONT_2_GROUP
                        || mPlayType == AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN) {
                    get115Group2Data(missBean, g2Datas);
                } else if (mPlayType == AppConstants.PLAY_11_5_FRONT_3_GROUP
                        || mPlayType == AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN) {
                    get115Group3Data(missBean, g3Datas);
                }
                if (hasMissData) setBasicBottomData(datas, i);
            }

            //开奖数据
            if (awardDatas.size() > 0) {
                trendTypeData.setAwardDatas(awardDatas);
            }
            //基本走势
            if (basicDatas.size() > 0) {
                trendTypeData.setBasicDatas(hasMissData ? frequencyTimes(basicDatas) : basicDatas);
            }
            //任选(除前一)形态数据
            if (formData.size() > 0) {
                trendTypeData.setFormDatas(formData);
            }
            List<String> title = Arrays.asList(LotteryUtils.m115Numbers);
            //前一形态数据
            if (mPlayType == AppConstants.PLAY_11_5_FRONT_1_DIRECT && directFormData.size() > 0) {
                List<String> titlesDirectForm = Arrays.asList(LotteryUtils.m115DirectFrom);
                trendTypeData.getTrendTitles().put(lotteryType + "_-3", titlesDirectForm);
                trendTypeData.getTrendLeftTitles().put(lotteryType + "_-3", new ArrayList<>());
                directFormData.addAll(getStatistic(lotteryType, mPlayType, -3, titlesDirectForm, datas));
                trendTypeData.getTrendDatas().put(lotteryType + "_-3", directFormData);

                //万位
                trendTypeData.getTrendTitles().put(lotteryType + "_5", title);
                trendTypeData.getTrendLeftTitles().put(lotteryType + "_5", new ArrayList<>());
                wanDatas.addAll(getStatistic(lotteryType, mPlayType, 5, title, datas));
                trendTypeData.getTrendDatas().put(lotteryType + "_5", wanDatas);
                LogF.d("north", "wanDatas.size()" + wanDatas.size());
            } else if (mPlayType == AppConstants.PLAY_11_5_FRONT_2_DIRECT) {
                //万位
                trendTypeData.getTrendTitles().put(lotteryType + "_5", title);
                trendTypeData.getTrendLeftTitles().put(lotteryType + "_5", new ArrayList<>());
                wanDatas.addAll(getStatistic(lotteryType, mPlayType, 5, title, datas));
                trendTypeData.getTrendDatas().put(lotteryType + "_5", wanDatas);
                LogF.d("north", "wanDatas.size()" + wanDatas.size());
                //千位
                trendTypeData.getTrendTitles().put(lotteryType + "_4", title);
                trendTypeData.getTrendLeftTitles().put(lotteryType + "_4", new ArrayList<>());
                qianDatas.addAll(getStatistic(lotteryType, mPlayType, 4, title, datas));
                trendTypeData.getTrendDatas().put(lotteryType + "_4", qianDatas);
                LogF.d("north", "qianDatas.size()" + qianDatas.size());
            } else if (mPlayType == AppConstants.PLAY_11_5_FRONT_3_DIRECT) {
                //万位
                trendTypeData.getTrendTitles().put(lotteryType + "_5", title);
                trendTypeData.getTrendLeftTitles().put(lotteryType + "_5", new ArrayList<>());
                wanDatas.addAll(getStatistic(lotteryType, mPlayType, 5, title, datas));
                trendTypeData.getTrendDatas().put(lotteryType + "_5", wanDatas);
                LogF.d("north", "wanDatas.size()" + wanDatas.size());
                //千位
                trendTypeData.getTrendTitles().put(lotteryType + "_4", title);
                trendTypeData.getTrendLeftTitles().put(lotteryType + "_4", new ArrayList<>());
                qianDatas.addAll(getStatistic(lotteryType, mPlayType, 4, title, datas));
                trendTypeData.getTrendDatas().put(lotteryType + "_4", qianDatas);
                LogF.d("north", "qianDatas.size()" + qianDatas.size());
                //百位
                trendTypeData.getTrendTitles().put(lotteryType + "_3", title);
                trendTypeData.getTrendLeftTitles().put(lotteryType + "_3", new ArrayList<>());
                baiDatas.addAll(getStatistic(lotteryType, mPlayType, 3, title, datas));
                trendTypeData.getTrendDatas().put(lotteryType + "_3", baiDatas);
                LogF.d("north", "baiDatas.size()" + baiDatas.size());
            } else if (mPlayType == AppConstants.PLAY_11_5_FRONT_2_GROUP
                    || mPlayType == AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN) {
                //前二组选
                trendTypeData.getTrendTitles().put(lotteryType + "_" + mPlayType + "_4", title);
                trendTypeData.getTrendLeftTitles().put(lotteryType + "_" + mPlayType + "_4", new ArrayList<>());
                g2Datas.addAll(getStatistic(lotteryType, mPlayType, 4, title, datas));
                trendTypeData.getTrendDatas().put(lotteryType + "_" + mPlayType + "_-2", g2Datas);
                LogF.d("north", "g2Datas.size()" + g2Datas.size());
            } else if (mPlayType == AppConstants.PLAY_11_5_FRONT_3_GROUP
                    || mPlayType == AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN) {
                //前三组选
                trendTypeData.getTrendTitles().put(lotteryType + "_" + mPlayType + "_4", title);
                trendTypeData.getTrendLeftTitles().put(lotteryType + "_" + mPlayType + "_4", new ArrayList<>());
                g3Datas.addAll(getStatistic(lotteryType, mPlayType, 4, title, datas));
                trendTypeData.getTrendDatas().put(lotteryType + "_" + mPlayType + "_-2", g3Datas);
                LogF.d("north", "g3Datas.size()" + g3Datas.size());
            }
        }
        iTrendView.resultData(trendTypeData);
    }

    private int[] showTimes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] avgMiss = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] maxMiss = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] maxContinue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] tempContinue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] partMaxMiss = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};//每段的最大遗漏数
    private int[] partMaxSum = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};//每段最大遗漏数相加的总数

    //计算频率
    private void setBasicBottomData(List<TrendResponseData.TrendData> datas, int i) {
        TrendResponseData.TrendData missBean = datas.get(i);
        List<String> numbers = missBean.getMissNumber().getRenxuan_general();
        for (int j = 0; j < numbers.size(); j++) {
            if (i == 0) {//reset
                partMaxSum[j] = 0;
                partMaxMiss[j] = 0;
                showTimes[j] = 0;
                maxContinue[j] = 0;
                maxMiss[j] = 0;
                tempContinue[j] = 0;
            }
            String missNumStr = numbers.get(j);
            int missNum = Integer.parseInt(missNumStr);
            //最大遗漏
            if (missNum > maxMiss[j]) {
                maxMiss[j] = missNum;
            }
            if (missNum == 0) {
                //出现次数
                showTimes[j]++;
                //每段最大遗漏数之和
                partMaxSum[j] = partMaxSum[j] + partMaxMiss[j];
                partMaxMiss[j] = 0;

                //最大连出
                tempContinue[j]++;
                if (maxContinue[j] < tempContinue[j]) {
                    maxContinue[j] = tempContinue[j];
                }
            } else {
                if (missNum >= partMaxMiss[j]) {
                    partMaxMiss[j] = missNum;
                }
                //reset连出数
                tempContinue[j] = 0;
            }
        }
    }

    //获取频次
    private List<BasicTrendBean> frequencyTimes(List<BasicTrendBean> basicDatas) {
        //平均遗漏
        for (int j = 0; j < partMaxSum.length; j++) {
            if (partMaxMiss[j] > 0) {
                partMaxSum[j] = partMaxSum[j] + partMaxMiss[j];
            }
            avgMiss[j] = partMaxSum[j] / (showTimes[j] + 1);//平均遗漏
            LogF.d(TAG, " partMaxSum[" + j + "]==>" + partMaxSum[j] + " ,avgMiss[j]==>" + avgMiss[j]);
        }
        LogF.d(TAG, "出现次数==" + Arrays.toString(showTimes) + "最大遗漏==" +
                Arrays.toString(maxMiss) + "每段最大遗漏之和==>" + Arrays.toString(partMaxSum)
                + "平均遗漏==" + Arrays.toString(avgMiss) + "连出=" + Arrays.toString(maxContinue));

        String[] tabs = {getfrequencyData(showTimes), getfrequencyData(avgMiss), getfrequencyData(maxMiss), getfrequencyData(maxContinue)};
        for (String tab : tabs) {
            BasicTrendBean basicTrendBean = new BasicTrendBean();
            basicTrendBean.setSingleNumbers(tab);
            basicTrendBean.setWaitAward(false);
            basicDatas.add(basicTrendBean);
        }
        for (BasicTrendBean basic : basicDatas) {
            LogF.d("north", "SingleNumbers==>" + basic.getSingleNumbers());
        }
        return basicDatas;
    }

    private String getfrequencyData(int[] numList) {
        StringBuilder numStr = new StringBuilder();
        if (null != numList && numList.length > 0) {
            for (int aNumList : numList) {
                String num = String.valueOf(aNumList);
                numStr.append(num);
                numStr.append(",");
            }
            if (numStr.length() > 0) {
                numStr.delete(numStr.length() - 1, numStr.length());
            }
            return numStr.toString();
        }
        return "";
    }

    //获取11选5开奖数据
    private void getOpenAwardData(int playType, TrendResponseData.TrendData missBean, List<OpenAwardBean> awardDatas,
                                  List<OpenAwardBean> formDatas) {
        String period, winNumber = "", spanNum = "", overNum = "", sizeThan = "", jiOuThan = "", zhiHeThan = "";
        int sum = 0, max = 0, min = 11, jiSum = 0, zhiSum = 0, bigNum = 0, playTypeSum = 0;
        if (missBean != null) {
            //期号
            period = missBean.getPeriod().substring(missBean.getPeriod().length() - 2, missBean.getPeriod().length());
            int size = missBean.getWinnerNumber().size();
            if (size > 0) {
                for (int j = 0; j < size; j++) {
                    int posNum = Integer.parseInt(missBean.getWinnerNumber().get(j));
                    switch (playType) {
                        case AppConstants.PLAY_11_5_FRONT_2_DIRECT://前二直选
                        case AppConstants.PLAY_11_5_FRONT_2_GROUP://前二组选
                        case AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN:
                            if (j < 2) {
                                if (posNum % 2 != 0) jiSum++;
                                if (primeNums.contains(posNum)) zhiSum++;
                                if (posNum > 5) bigNum++;
                                sum += posNum;//和值
                                //计算跨度
                                if (posNum > max) max = posNum;
                                if (posNum < min) min = posNum;
                                playTypeSum = 2;
                            }
                            break;
                        case AppConstants.PLAY_11_5_FRONT_3_DIRECT://前三直选
                        case AppConstants.PLAY_11_5_FRONT_3_GROUP://前二组选
                        case AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN:
                            if (j < 3) {
                                if (posNum % 2 != 0) jiSum++;
                                if (primeNums.contains(posNum)) zhiSum++;
                                if (posNum > 5) bigNum++;
                                sum += posNum;//和值
                                //计算跨度
                                if (posNum > max) max = posNum;
                                if (posNum < min) min = posNum;
                                playTypeSum = 3;
                            }
                            break;
                        default:
                            if (posNum % 2 != 0) jiSum++;
                            if (primeNums.contains(posNum)) zhiSum++;
                            if (posNum > 5) bigNum++;
                            sum += posNum;//和值
                            //计算跨度
                            if (posNum > max) max = posNum;
                            if (posNum < min) min = posNum;
                            playTypeSum = 5;
                            break;
                    }
                    winNumber = String.format("%s%s ", winNumber,
                            posNum < 10 ? "0" + posNum : posNum);//中奖号
                }

                //大小比
                sizeThan = bigNum + ":" + (playTypeSum - bigNum);
                jiOuThan = jiSum + ":" + (playTypeSum - jiSum);
                zhiHeThan = zhiSum + ":" + (playTypeSum - zhiSum);

                //跨度
                spanNum = String.valueOf((max - min));
                //重复号
                TrendResponseData.OverNumber overNumber = missBean.getOverNumber();
                List<String> overNums = null;
                if (overNumber != null) {
                    switch (playType) {
                        case AppConstants.PLAY_11_5_FRONT_2_DIRECT://前二直选
                        case AppConstants.PLAY_11_5_FRONT_2_GROUP://前二组选
                        case AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN:
                            overNums = overNumber.getQian2_zuxuan();
                            break;

                        case AppConstants.PLAY_11_5_FRONT_3_DIRECT://前三直选
                        case AppConstants.PLAY_11_5_FRONT_3_GROUP://前二组选
                        case AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN:
                            overNums = overNumber.getQian3_zuxuan();
                            break;

                        default:
                            overNums = overNumber.getRenxuan_general();
                            break;
                    }

                    if (overNums != null && overNums.size() > 0) {
                        String num = overNums.get(0);
                        overNum = !num.equals("-1") ? num : "--";
                    }
                }
            } else {
                winNumber = "等待开奖";
            }

            OpenAwardBean formData = new OpenAwardBean();
            formData.setPeriod(StringUtils.getInfo(period) + "期");
            formData.setAwardNums(winNumber);
            formData.setKey3(sizeThan);
            formData.setKey4(jiOuThan);
            formData.setKey5(zhiHeThan);
            formDatas.add(formData);

            OpenAwardBean awardBean = new OpenAwardBean();
            awardBean.setPeriod(StringUtils.getInfo(period) + "期");
            awardBean.setAwardNums(winNumber);
            awardBean.setKey3(String.valueOf(sum));
            awardBean.setKey4(spanNum);
            awardBean.setKey5(overNum);
            awardDatas.add(awardBean);
        }
    }

    //获取11选5的前一形态走势数据
    private void get115DirectFormData(TrendResponseData.TrendData missBean, List<TrendData> formData) {
        String times = "";
        if (missBean != null) {
            String period = missBean.getPeriod().substring(missBean.getPeriod().length() - 2, missBean.getPeriod().length());

            List<String> xingtai = missBean.getMissNumber().getQian1_danshi_xingtai();
            if (xingtai != null && xingtai.size() > 0) {
                for (int j = 0; j < xingtai.size(); j++) {
                    int endNum = xingtai.size() - 1;
                    String timesNum = times + xingtai.get(j);
                    times = j == endNum ? timesNum : timesNum + ",";
                }
            } else {
                times = "等待开奖";
            }
            TrendData data = new TrendData();
            data.setPid(period + "期");
            data.setBlue(times);
            data.setType("row");
            formData.add(data);
        }
    }

    //获取11选5的前二组选走势
    private void get115Group2Data(TrendResponseData.TrendData missBean, List<TrendData> trendData) {
        String times = "";
        if (missBean != null) {
            String period = missBean.getPeriod().substring(missBean.getPeriod().length() - 2, missBean.getPeriod().length());
            List<String> miss = missBean.getMissNumber().getQian2_zuxuan();
            if (miss != null && miss.size() > 0) {
                for (int j = 0; j < miss.size(); j++) {
                    int endNum = miss.size() - 1;
                    String timesNum = times + miss.get(j);
                    times = j == endNum ? timesNum : timesNum + ",";
                }
            } else {
                times = "等待开奖";
            }
            TrendData data = new TrendData();
            data.setPid(period + "期");
            data.setBlue(times);
            data.setType("row");
            trendData.add(data);
        }
    }

    //获取11选5的前三组选走势
    private void get115Group3Data(TrendResponseData.TrendData missBean, List<TrendData> trendData) {
        String times = "";
        if (missBean != null) {
            String period = missBean.getPeriod().substring(missBean.getPeriod().length() - 2, missBean.getPeriod().length());
            List<String> miss = missBean.getMissNumber().getQian3_zuxuan();
            if (miss != null && miss.size() > 0) {
                for (int j = 0; j < miss.size(); j++) {
                    int endNum = miss.size() - 1;
                    String timesNum = times + miss.get(j);
                    times = j == endNum ? timesNum : timesNum + ",";
                }
            } else {
                times = " 等待开奖";
            }
            TrendData data = new TrendData();
            data.setPid(period + "期");
            data.setBlue(times);
            data.setType("row");
            trendData.add(data);
        }
    }

    //获取11选5的任选走势数据
    private void getBasicData(TrendResponseData.TrendData missBean, List<BasicTrendBean> basicDatas) {
        String winNumber = "", times = "";
        if (missBean != null) {
            String period = missBean.getPeriod().substring(missBean.getPeriod().length() - 2, missBean.getPeriod().length());

            List<String> winnerNumber = missBean.getWinnerNumber();
            if (winnerNumber != null && winnerNumber.size() > 0) {
                for (int j = 0; j < winnerNumber.size(); j++) {
                    int endNum = winnerNumber.size() - 1;
                    String winNum = winNumber + winnerNumber.get(j);
                    winNumber = j == endNum ? winNum : winNum + ",";
                }
            } else {
                winNumber = "等待开奖";
            }
            List<String> renxuan = missBean.getMissNumber().getRenxuan_general();
            if (renxuan != null && renxuan.size() > 0) {
                for (int j = 0; j < renxuan.size(); j++) {
                    int endNum = renxuan.size() - 1;
                    String timesNum = times + renxuan.get(j);
                    times = j == endNum ? timesNum : timesNum + ",";
                }
            } else {
                times = "";
            }

            BasicTrendBean basicBean = new BasicTrendBean();
            basicBean.periods = period + "期";
            basicBean.numbers = winNumber;
            basicBean.singleNumbers = times;
            basicDatas.add(basicBean);
        }
    }


    //和值开奖数据
    public List<OpenAwardBean> openAwardData(List<TrendResponseData.TrendData> datas) {
        List<OpenAwardBean> awardDatas = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            TrendResponseData.TrendData missBean = datas.get(i);
            String winNumber = "", sumSize = "", sumState = "", period = "";
            int sum = 0;
            if (missBean != null) {
                period = missBean.getPeriod().substring(missBean.getPeriod().length() - 2, missBean.getPeriod().length());
                if (missBean.getWinnerNumber().size() > 0) {
                    for (int j = 0; j < missBean.getWinnerNumber().size(); j++) {
                        winNumber = winNumber + missBean.getWinnerNumber().get(j) + " ";
                        sum += Integer.parseInt(missBean.getWinnerNumber().get(j));
                    }
                    if (sum % 2 == 0) {
                        sumState = "双";
                    } else {
                        sumState = "单";
                    }
                    if (sum >= 11) {
                        sumSize = "大";
                    } else {
                        sumSize = "小";
                    }
                } else {
                    winNumber = "等待开奖";
                }

                OpenAwardBean awardBean = new OpenAwardBean();
                awardBean.setPeriod(period + "期");
                awardBean.setAwardNums(winNumber);
                awardBean.setKey3(String.valueOf(sum));
                awardBean.setKey4(sumSize);
                awardBean.setKey5(sumState);
                awardDatas.add(awardBean);
            }
        }
        return awardDatas;
    }

    //时时彩走势图数据
    //走势图  numPos（1-5） 跨度0 振幅-1 基本走势-2 形态-3 */
    public void getACTrendData(String lotteryType, int playType, List<TrendResponseData.TrendData> datas) {
        List<OpenAwardBean> awardDatas = new ArrayList<>();
        List<TrendData> trendDatasGe = new ArrayList<>();//个位走势
        List<TrendData> trendDatasShi = new ArrayList<>();//十位走势
        List<TrendData> trendDatasBai = new ArrayList<>();//百位走势
        List<TrendData> trendDatasQian = new ArrayList<>();//千位走势
        List<TrendData> trendDatasWan = new ArrayList<>();//万位走势
        List<TrendData> trendDatasGeZF = new ArrayList<>();//个位振幅
        List<TrendData> trendDatasBasic2 = new ArrayList<>();//二星组选走势
        List<TrendData> trendDatasKD2 = new ArrayList<>();//二星组选跨度
        List<TrendData> trendDatasBasic3 = new ArrayList<>();//组三走势
        List<TrendData> trendDatasBasic6 = new ArrayList<>();//组六走势
        List<TrendData> trendDatasKD3 = new ArrayList<>();//组三跨度
        List<TrendData> trendDatasKD6 = new ArrayList<>();//组六跨度
        List<TrendData> trendDatasDXDSFORM = new ArrayList<>();//大小单双形态走势
        TrendTypeData trendTypeData = new TrendTypeData();
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                //计算开奖列表数据
                OpenAwardBean openAwardBean = new OpenAwardBean();
                TrendResponseData.TrendData trendData = datas.get(i);
                List<String> winnerNums = trendData.getWinnerNumber();
                String period = "", last2WinnerNumber = "";
                if (null != winnerNums && winnerNums.size() == 5) {
                    openAwardBean.setKey3(getACNumberState(Integer.valueOf(winnerNums.get(3))));
                    openAwardBean.setKey4(getACNumberState(Integer.valueOf(winnerNums.get(4))));
                    openAwardBean.setKey5(getACNumberState(Integer.valueOf(winnerNums.get(4)),
                            Integer.valueOf(winnerNums.get(3)), Integer.valueOf(winnerNums.get(2))));
                    for (String number : winnerNums) {
                        openAwardBean.setAwardNums(String.format("%s%s ",
                                TextUtils.isEmpty(openAwardBean.getAwardNums()) ? "" : openAwardBean.getAwardNums(),
                                number));
                    }
                    last2WinnerNumber = winnerNums.get(3) + " " + winnerNums.get(4);
                } else {
                    openAwardBean.setAwardNums("等待开奖");
                }
                if (null != trendData.getPeriod()) {
                    int periodSize = trendData.getPeriod().length();
                    period = trendData.getPeriod().substring(periodSize - 3, periodSize) + "期";
                    openAwardBean.setPeriod(period);
                }
                awardDatas.add(openAwardBean);

                //计算个位走势
                StringBuilder geMiss = new StringBuilder();
                StringBuilder shiMiss = new StringBuilder();
                StringBuilder baiMiss = new StringBuilder();
                StringBuilder qianMiss = new StringBuilder();
                StringBuilder wanMiss = new StringBuilder();
                List<String> wuxingMissNums = trendData.getMissNumber().getWuxing_tongxuan();
                if (null != wuxingMissNums && wuxingMissNums.size() == 50) {//个十百千万在一起
                    for (int j = 0; j < wuxingMissNums.size(); j++) {
                        if (j == 9) {//万位
                            wanMiss.append(wuxingMissNums.get(j));
                        } else if (j < 9) {//万位
                            wanMiss.append(wuxingMissNums.get(j));
                            wanMiss.append(",");
                        } else if (j == 19) { //千位
                            qianMiss.append(wuxingMissNums.get(j));
                        } else if (j < 19) { //千位
                            qianMiss.append(wuxingMissNums.get(j));
                            qianMiss.append(",");
                        } else if (j == 29) { //百位
                            baiMiss.append(wuxingMissNums.get(j));
                        } else if (j < 29) { //百位
                            baiMiss.append(wuxingMissNums.get(j));
                            baiMiss.append(",");
                        } else if (j == 39) { //十位
                            shiMiss.append(wuxingMissNums.get(j));
                        } else if (j < 39) { //十位
                            shiMiss.append(wuxingMissNums.get(j));
                            shiMiss.append(",");
                        } else if (j == 49) { //个位
                            geMiss.append(wuxingMissNums.get(j));
                        } else if (j < 49) { //个位
                            geMiss.append(wuxingMissNums.get(j));
                            geMiss.append(",");
                        }
                    }
                } else {
                    wanMiss.append("等待开奖");
                    qianMiss.append("等待开奖");
                    baiMiss.append("等待开奖");
                    shiMiss.append("等待开奖");
                    geMiss.append("等待开奖");
                }
                TrendData wanData = new TrendData();
                wanData.setPid(period);
                wanData.setType("row");
                wanData.setBlue(wanMiss.toString());
                trendDatasWan.add(wanData);
                TrendData qianData = new TrendData();
                qianData.setPid(period);
                qianData.setType("row");
                qianData.setBlue(qianMiss.toString());
                trendDatasQian.add(qianData);
                TrendData baiData = new TrendData();
                baiData.setPid(period);
                baiData.setType("row");
                baiData.setBlue(baiMiss.toString());
                trendDatasBai.add(baiData);
                TrendData shiData = new TrendData();
                shiData.setPid(period);
                shiData.setType("row");
                shiData.setBlue(shiMiss.toString());
                trendDatasShi.add(shiData);
                TrendData geData = new TrendData();
                geData.setPid(period);
                geData.setType("row");
                geData.setBlue(geMiss.toString());
                trendDatasGe.add(geData);

                //大小单双形态走势
                StringBuilder dxdsMiss = new StringBuilder();
                List<String> dxdsMissNums = trendData.getMissNumber().getDaxiaodanshuang();
                if (null != dxdsMissNums && dxdsMissNums.size() > 0) {
                    for (int pos = 0; pos < dxdsMissNums.size(); pos++) {
                        if (pos == 9) { //最后一位不需要逗号
                            dxdsMiss.append(dxdsMissNums.get(pos));
                        } else if (pos < 9) {
                            dxdsMiss.append(dxdsMissNums.get(pos));
                            dxdsMiss.append(",");
                        }
                    }
                } else {
                    dxdsMiss.append("等待开奖");
                }
                TrendData dxdsData = new TrendData();
                dxdsData.setPid(period);
                dxdsData.setType("row");
                dxdsData.setWinNumbers(last2WinnerNumber);
                dxdsData.setBlue(dxdsMiss.toString());
                trendDatasDXDSFORM.add(dxdsData);

                //个位振幅
                StringBuilder geZFMiss = new StringBuilder();
                List<String> zfMissNums = trendData.getMissNumber().getYixing_zhixuan_swing();
                if (null != zfMissNums && zfMissNums.size() > 0) {
                    for (int pos = 0; pos < zfMissNums.size(); pos++) {
                        if (pos == 9) { //最后一位不需要逗号
                            geZFMiss.append(zfMissNums.get(pos));
                        } else if (pos < 9) {
                            geZFMiss.append(zfMissNums.get(pos));
                            geZFMiss.append(",");
                        }
                    }
                } else {
                    geZFMiss.append("等待开奖");
                }
                TrendData ZFData = new TrendData();
                ZFData.setPid(period);
                ZFData.setType("row");
                ZFData.setBlue(geZFMiss.toString());
                trendDatasGeZF.add(ZFData);

                //二星组选
                StringBuilder basic2Miss = new StringBuilder();
                List<String> basic2MissNums = trendData.getMissNumber().getErxing_zuxuan();
                if (null != basic2MissNums && basic2MissNums.size() > 0) {
                    for (int pos = 0; pos < basic2MissNums.size(); pos++) {
                        if (pos == 9) { //最后一位不需要逗号
                            basic2Miss.append(basic2MissNums.get(pos));
                        } else if (pos < 9) {
                            basic2Miss.append(basic2MissNums.get(pos));
                            basic2Miss.append(",");
                        }
                    }
                } else {
                    basic2Miss.append("等待开奖");
                }
                TrendData basic2Data = new TrendData();
                basic2Data.setPid(period);
                basic2Data.setType("row");
                basic2Data.setBlue(basic2Miss.toString());
                trendDatasBasic2.add(basic2Data);

                //二星组选跨度
                StringBuilder kd2Miss = new StringBuilder();
                List<String> kd2MissNums = trendData.getMissNumber().getErxing_zuxuan_span();
                if (null != kd2MissNums && kd2MissNums.size() > 0) {
                    for (int pos = 0; pos < kd2MissNums.size(); pos++) {
                        if (pos == 9) { //最后一位不需要逗号
                            kd2Miss.append(kd2MissNums.get(pos));
                        } else if (pos < 9) {
                            kd2Miss.append(kd2MissNums.get(pos));
                            kd2Miss.append(",");
                        }
                    }
                } else {
                    kd2Miss.append("等待开奖");
                }
                TrendData kd2Data = new TrendData();
                kd2Data.setPid(period);
                kd2Data.setType("row");
                kd2Data.setBlue(kd2Miss.toString());
                trendDatasKD2.add(kd2Data);

                //组三走势
                StringBuilder basic3Miss = new StringBuilder();
                List<String> basic3MissNums = trendData.getMissNumber().getSanxing_zusan();
                if (null != basic3MissNums && basic3MissNums.size() > 0) {
                    for (int pos = 0; pos < basic3MissNums.size(); pos++) {
                        if (pos == 9) { //最后一位不需要逗号
                            basic3Miss.append(basic3MissNums.get(pos));
                        } else if (pos < 9) {
                            basic3Miss.append(basic3MissNums.get(pos));
                            basic3Miss.append(",");
                        }
                    }
                } else {
                    basic3Miss.append("等待开奖");
                }
                TrendData basic3Data = new TrendData();
                basic3Data.setPid(period);
                basic3Data.setType("row");
                basic3Data.setBlue(basic3Miss.toString());
                trendDatasBasic3.add(basic3Data);

                //组六走势
                StringBuilder basic6Miss = new StringBuilder();
                List<String> basic6MissNums = trendData.getMissNumber().getSanxing_zusan();
                if (null != basic6MissNums && basic6MissNums.size() > 0) {
                    for (int pos = 0; pos < basic6MissNums.size(); pos++) {
                        if (pos == 9) { //最后一位不需要逗号
                            basic6Miss.append(basic6MissNums.get(pos));
                        } else if (pos < 9) {
                            basic6Miss.append(basic6MissNums.get(pos));
                            basic6Miss.append(",");
                        }
                    }
                } else {
                    basic6Miss.append("等待开奖");
                }
                TrendData basic6Data = new TrendData();
                basic6Data.setPid(period);
                basic6Data.setType("row");
                basic6Data.setBlue(basic6Miss.toString());
                trendDatasBasic6.add(basic6Data);

                //组三跨度
                StringBuilder kd3Miss = new StringBuilder();
                List<String> kd3MissNums = trendData.getMissNumber().getSanxing_zuxuan_span();
                if (null != kd3MissNums && kd3MissNums.size() > 0) {
                    for (int pos = 0; pos < kd3MissNums.size(); pos++) {
                        if (pos == 9) { //最后一位不需要逗号
                            kd3Miss.append(kd3MissNums.get(pos));
                        } else if (pos < 9) {
                            kd3Miss.append(kd3MissNums.get(pos));
                            kd3Miss.append(",");
                        }
                    }
                } else {
                    kd3Miss.append("等待开奖");
                }
                TrendData kd3Data = new TrendData();
                kd3Data.setPid(period);
                kd3Data.setType("row");
                kd3Data.setBlue(kd3Miss.toString());
                trendDatasKD3.add(kd3Data);
                //组六走势
                StringBuilder kd6Miss = new StringBuilder();
                List<String> kd6MissNums = trendData.getMissNumber().getSanxing_zuxuan_span();
                if (null != kd6MissNums && kd6MissNums.size() > 0) {
                    for (int pos = 0; pos < kd6MissNums.size(); pos++) {
                        if (pos == 9) { //最后一位不需要逗号
                            kd6Miss.append(kd6MissNums.get(pos));
                        } else if (pos < 9) {
                            kd6Miss.append(kd6MissNums.get(pos));
                            kd6Miss.append(",");
                        }
                    }
                } else {
                    kd6Miss.append("等待开奖");
                }
                TrendData kd6Data = new TrendData();
                kd6Data.setPid(period);
                kd6Data.setType("row");
                kd6Data.setBlue(kd6Miss.toString());
                trendDatasKD6.add(kd6Data);
            }
            //开奖数据
            if (awardDatas.size() > 0) {
                trendTypeData.setAwardDatas(awardDatas);
            }
            //走势图  numPos（1-5） 跨度0 振幅-1 基本走势-2 形态-3 */
            if (trendDatasWan.size() > 0) {
                trendTypeData.getTrendDatas().put(lotteryType + "_5", trendDatasWan);
            }
            if (trendDatasQian.size() > 0) {
                trendTypeData.getTrendDatas().put(lotteryType + "_4", trendDatasQian);
            }
            if (trendDatasBai.size() > 0) {
                trendTypeData.getTrendDatas().put(lotteryType + "_3", trendDatasBai);
            }
            if (trendDatasShi.size() > 0) {
                trendTypeData.getTrendDatas().put(lotteryType + "_2", trendDatasShi);
            }
            if (trendDatasGe.size() > 0) {
                trendTypeData.getTrendDatas().put(lotteryType + "_1", trendDatasGe);
            }
            if (trendDatasDXDSFORM.size() > 0) {//大小单双形态-3
                trendTypeData.getTrendDatas().put(lotteryType + "_-3", trendDatasDXDSFORM);
            }
            if (trendDatasGeZF.size() > 0) {//个位振幅-1
                trendTypeData.getTrendDatas().put(lotteryType + "_-1", trendDatasGeZF);
            }
            if (trendDatasBasic2.size() > 0) {//二星组选走势=2
                trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_2_GROUP + "_-2", trendDatasBasic2);
            }
            if (trendDatasKD2.size() > 0) {//二星组选跨度0
                trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_2_GROUP + "_0", trendDatasKD2);
            }
            if (trendDatasBasic3.size() > 0) {//三星组三走势-2
                trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_3 + "_-2", trendDatasBasic3);
            }
            if (trendDatasBasic6.size() > 0) {//三星组六走势-2
                trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_6 + "_-2", trendDatasBasic6);
            }
            if (trendDatasKD3.size() > 0) {//组三跨度0
                trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_3 + "_0", trendDatasKD3);
            }
            if (trendDatasKD6.size() > 0) {//组六跨度0
                trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_6 + "_0", trendDatasKD6);
            }
        }
        //走势图  numPos（1-5） 跨度0 振幅-1 基本走势-2 形态-3 */

        //======================标题和统计=============================
        List<String> trendTitles = new ArrayList<>();
        for (String number : LotteryUtils.mThreeDNumbers) {
            trendTitles.add(number);
        }
        //位数 不需要区分玩法
        trendTypeData.getTrendTitles().put(lotteryType + "_1", trendTitles);
        trendTypeData.getTrendTitles().put(lotteryType + "_2", trendTitles);
        trendTypeData.getTrendTitles().put(lotteryType + "_3", trendTitles);
        trendTypeData.getTrendTitles().put(lotteryType + "_4", trendTitles);
        trendTypeData.getTrendTitles().put(lotteryType + "_5", trendTitles);
        //形态走势
        List<String> dxdsFormTitles = Arrays.asList(LotteryUtils.mACFormNumbers);
        trendTypeData.getTrendTitles().put(lotteryType + "_-3", dxdsFormTitles);//大小单双 形态走势
        trendTypeData.getTrendTitles().put(lotteryType + "_-1", trendTitles);//个位振幅
        //其他需要区分玩法
        trendTypeData.getTrendTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_2_GROUP + "_-2", trendTitles);//二星组选走势
        trendTypeData.getTrendTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_2_GROUP + "_0", trendTitles);//二星组选跨度
        trendTypeData.getTrendTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_3 + "_-2", trendTitles);//三星组三走势
        trendTypeData.getTrendTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_3 + "_0", trendTitles);//三星组三跨度
        trendTypeData.getTrendTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_6 + "_-2", trendTitles);//三星组六走势
        trendTypeData.getTrendTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_6 + "_0", trendTitles);//三星组六跨度

        trendDatasGe.addAll(getStatistic(lotteryType, playType, 1, trendTitles, datas));
        trendDatasShi.addAll(getStatistic(lotteryType, playType, 2, trendTitles, datas));
        trendDatasBai.addAll(getStatistic(lotteryType, playType, 3, trendTitles, datas));
        trendDatasQian.addAll(getStatistic(lotteryType, playType, 4, trendTitles, datas));
        trendDatasWan.addAll(getStatistic(lotteryType, playType, 5, trendTitles, datas));
        trendDatasDXDSFORM.addAll(getStatistic(lotteryType, AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE,
                -3, dxdsFormTitles, datas));//个位振幅
        trendDatasGeZF.addAll(getStatistic(lotteryType, AppConstants.ALWAYS_COLOR_1_DIRECT, -1, trendTitles, datas));//个位振幅
        trendDatasBasic2.addAll(getStatistic(lotteryType, AppConstants.ALWAYS_COLOR_2_GROUP, -2, trendTitles, datas));//二星组选走势
        trendDatasKD2.addAll(getStatistic(lotteryType, AppConstants.ALWAYS_COLOR_2_GROUP, 0, trendTitles, datas));//二星组选跨度
        trendDatasBasic3.addAll(getStatistic(lotteryType, AppConstants.ALWAYS_COLOR_3_GROUP_3, -2, trendTitles, datas));//三星组三走势
        trendDatasKD3.addAll(getStatistic(lotteryType, AppConstants.ALWAYS_COLOR_3_GROUP_3, 0, trendTitles, datas));//三星组三跨度
        trendDatasBasic6.addAll(getStatistic(lotteryType, AppConstants.ALWAYS_COLOR_3_GROUP_6, -2, trendTitles, datas));//三星组三走势
        trendDatasKD6.addAll(getStatistic(lotteryType, AppConstants.ALWAYS_COLOR_3_GROUP_6, 0, trendTitles, datas));//三星组三跨度
        //======================标题和统计=============================

        //======================左侧标题=============================
        List<String> trendLeftTitles = new ArrayList<>();
        //位数 不需要区分玩法
        List<String> dxdsLeftTitles = new ArrayList<>();
        dxdsLeftTitles.add("开奖号");
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_-3", dxdsLeftTitles);
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_1", trendLeftTitles);
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_2", trendLeftTitles);
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_3", trendLeftTitles);
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_4", trendLeftTitles);
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_5", trendLeftTitles);
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_-1", trendLeftTitles);//个位振幅
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_2_GROUP + "_" + -2, trendLeftTitles);//二星组选走势
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_2_GROUP + "_" + 0, trendLeftTitles);//二星组选跨度
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_3 + "_" + -2, trendLeftTitles);//三星组三走势
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_3 + "_" + 0, trendLeftTitles);//三星组三跨度
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_6 + "_" + -2, trendLeftTitles);//三星组三走势
        trendTypeData.getTrendLeftTitles().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_6 + "_" + 0, trendLeftTitles);//三星组三跨度
        //======================左侧标题=============================

        //======================内容数据=============================
        //位数 不需要区分玩法
        trendTypeData.getTrendDatas().put(lotteryType + "_1", trendDatasGe);
        trendTypeData.getTrendDatas().put(lotteryType + "_2", trendDatasShi);
        trendTypeData.getTrendDatas().put(lotteryType + "_3", trendDatasBai);
        trendTypeData.getTrendDatas().put(lotteryType + "_4", trendDatasQian);
        trendTypeData.getTrendDatas().put(lotteryType + "_5", trendDatasWan);

        trendTypeData.getTrendDatas().put(lotteryType + "_-3", trendDatasDXDSFORM);//个位振幅
        trendTypeData.getTrendDatas().put(lotteryType + "_-1", trendDatasGeZF);//个位振幅
        trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_2_GROUP + "_" + -2, trendDatasBasic2);//二星组选走势
        trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_2_GROUP + "_" + 0, trendDatasKD2);//二星组选跨度
        trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_3 + "_" + -2, trendDatasBasic3);//三星组三走势
        trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_3 + "_" + 0, trendDatasKD3);//三星组三跨度
        trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_6 + "_" + -2, trendDatasBasic6);//三星组三走势
        trendTypeData.getTrendDatas().put(lotteryType + "_" + AppConstants.ALWAYS_COLOR_3_GROUP_6 + "_" + 0, trendDatasKD6);//三星组三跨度
        //======================内容数据=============================
        iTrendView.resultData(trendTypeData);
    }

    /**
     * @param number 个位或十位
     */
    private String getACNumberState(int number) {
        StringBuilder sb = new StringBuilder();
        if (number > 4) {
            sb.append("大");
        } else {
            sb.append("小");
        }
        if (number % 2 == 0) {
            sb.append("双");
        } else {
            sb.append("单");
        }
        return sb.toString();
    }

    /**
     * @param ge  个位
     * @param shi 十位
     * @param bai 百位
     */
    private String getACNumberState(int ge, int shi, int bai) {
        StringBuilder sb = new StringBuilder();
        if (ge == shi && ge == bai) {//组三
            sb.append("豹子");
        } else if (ge == shi || ge == bai || shi == bai) {//组三
            sb.append("组三");
        } else {//组六
            sb.append("组六");
        }
        return sb.toString();
    }

}
