package cn.zcgames.lottery.model.remote.impl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;

import static cn.zcgames.lottery.app.AppConstants.*;

import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.home.bean.AlwaysColorHistory;
import cn.zcgames.lottery.home.bean.LotteryWinningNoticeResponseBean;
import cn.zcgames.lottery.home.bean.ResultHistoryListData;
import cn.zcgames.lottery.bean.LotteryResultHistory;
import cn.zcgames.lottery.bean.response.DoubleColorDetailBean;
import cn.zcgames.lottery.home.bean.LotteryPageDataResponseBean;
import cn.zcgames.lottery.bean.response.LotteryWinningBean;
import cn.zcgames.lottery.home.bean.LotteryPageDataBean;
import cn.zcgames.lottery.bean.response.LotteryWinningDataBean;
import cn.zcgames.lottery.bean.response.LotteryWinningResponseBean;
import cn.zcgames.lottery.bean.response.ResponseBillDetailFastThree;
import cn.zcgames.lottery.bean.response.ResultAlwaysColorHistory;
import cn.zcgames.lottery.bean.response.ResultBeanData;
import cn.zcgames.lottery.bean.response.ResultBuyLotteryBean;
import cn.zcgames.lottery.bean.response.ResultBuyLotteryBeanNew;
import cn.zcgames.lottery.bean.response.ResultHistoryListResponseNew;
import cn.zcgames.lottery.bean.response.ResultSequenceBean;
import cn.zcgames.lottery.bean.response.ResultSequenceBeanNew;
import cn.zcgames.lottery.home.bean.TrendDataResponse;
import cn.zcgames.lottery.home.bean.TrendResponseData;
import cn.zcgames.lottery.model.local.SharedPreferencesUtils;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.callback.RequestLotteryPageDataCallback;
import cn.zcgames.lottery.utils.GsonUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;
import cn.zcgames.lottery.utils.okhttp.OkHttpUtils;
import cn.zcgames.lottery.utils.okhttp.builder.PostFormBuilder;
import cn.zcgames.lottery.utils.okhttp.builder.PostStringBuilder;
import cn.zcgames.lottery.utils.okhttp.callback.GenericsCallback;
import cn.zcgames.lottery.utils.okhttp.callback.JsonGenericsSerializer;
import cn.zcgames.lottery.utils.okhttp.callback.StringCallback;
import okhttp3.Call;

//import com.lottery.entity.LotteryOrder;

/**
 * Created by admin on 2017/5/16.
 */

public class LotteryModel implements ILotteryModel {

    private static final String TAG = "LotteryModel";

    @Override
    public void requestCurrentSequence(String type, final NormalCallback callback) {
        String url = "";
        PostStringBuilder builder = OkHttpUtils.postString();
        GenericsCallback resultSequenceBeanGenericsCallback = new GenericsCallback<ResultSequenceBean>(new JsonGenericsSerializer()) {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError: error is " + e.getMessage());
                callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
            }

            @Override
            public void onResponse(ResultSequenceBean response, int id) {
                if (response.isOk()) {
                    callback.responseOk(response.getData());
                } else {
                    callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.tips_request_fail));
                }
            }
        };
        //TODO 新增彩种需要适配 Y
        if (type.equals(LOTTERY_TYPE_2_COLOR)) {
            url = HttpHelper.DOUBLE_COLOR_SEQUENCE;
        } else if (type.equals(LOTTERY_TYPE_FAST_3)
                || type.equals(LOTTERY_TYPE_FAST_3_EASY)//易快三
                || type.equals(LOTTERY_TYPE_FAST_3_HB)//湖北快三
                || type.equals(LOTTERY_TYPE_FAST_3_JS)//江苏快三
                || type.equals(LOTTERY_TYPE_FAST_3_NEW)
                || type.equals(LOTTERY_TYPE_11_5)
                || type.equals(LOTTERY_TYPE_11_5_LUCKY)
                || type.equals(LOTTERY_TYPE_11_5_OLD)
                || type.equals(LOTTERY_TYPE_11_5_YUE)
                || type.equals(LOTTERY_TYPE_11_5_YILE)
                || type.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                || type.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
            builder.addHeader("LotteryName", type);
            if (type.equals(LOTTERY_TYPE_FAST_3)
                    || type.equals(LOTTERY_TYPE_FAST_3_EASY)//易快三
                    || type.equals(LOTTERY_TYPE_FAST_3_HB)//湖北快三
                    || type.equals(LOTTERY_TYPE_FAST_3_JS)//江苏快三
                    || type.equals(LOTTERY_TYPE_FAST_3_NEW)) {
                url = HttpHelper.FAST_SEQUENCE;
            } else if (type.equals(LOTTERY_TYPE_11_5)
                    || type.equals(LOTTERY_TYPE_11_5_LUCKY)
                    || type.equals(LOTTERY_TYPE_11_5_OLD)
                    || type.equals(LOTTERY_TYPE_11_5_YUE)
                    || type.equals(LOTTERY_TYPE_11_5_YILE)) {
                url = HttpHelper.ELEVEN5_SEQUENCE;
            } else if (type.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                    || type.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                url = HttpHelper.SHI_SHI_CAI_SEQUENCE;
            }
            resultSequenceBeanGenericsCallback = new GenericsCallback<ResultSequenceBeanNew>(new JsonGenericsSerializer()) {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.e(TAG, "onError: error is " + e.getMessage());
                    callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                }

                @Override
                public void onResponse(ResultSequenceBeanNew response, int id) {
                    if (response.isOk()) {
                        callback.responseOk(response.getData());
                    } else {
                        callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.tips_request_fail));
                    }
                }
            };
        } else if (type.equals(LOTTERY_TYPE_7_HAPPY)) {
            url = HttpHelper.SEVEN_HAPPY_SEQUENCE;
        } else if (type.equals(LOTTERY_TYPE_ARRANGE_3)) {
            url = HttpHelper.ARRANGE3_SEQUENCE;
        } else if (type.equals(LOTTERY_TYPE_ARRANGE_5)) {
            url = HttpHelper.ARRANGE5_SEQUENCE;
        } else if (type.equals(LOTTERY_TYPE_7_STAR)) {
            url = HttpHelper.STAR7_SEQUENCE;
        } else {
            url = HttpHelper.THREE_D_SEQUENCE;
        }

        builder.url(url)
                .content("")
                .build()
                .execute(resultSequenceBeanGenericsCallback);
    }

    @Override
    public void requestLotteryData(final RequestLotteryPageDataCallback callback) {
        OkHttpUtils.get()
                .url(HttpHelper.LOTTERY_FRAGMENT_URL)
                .build()
                .execute(new GenericsCallback<LotteryPageDataResponseBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        formatLotteryData(SharedPreferencesUtils.getLotteryPageDataInfo(), callback);
                        callback.requestFailed(HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(LotteryPageDataResponseBean response, int id) {
                        formatLotteryData(response, callback);
                    }
                });
    }

    private void formatLotteryData(LotteryPageDataResponseBean lotteryPageDataResponseBean, RequestLotteryPageDataCallback callback) {
        LogF.d(TAG, "首页数据" + GsonUtil.getInstance().toJson(lotteryPageDataResponseBean));
        if (null != lotteryPageDataResponseBean && null != lotteryPageDataResponseBean.getPayload()) {
            LotteryPageDataBean bean = lotteryPageDataResponseBean.getPayload();
            callback.requestAdInfo(bean.getBanners());
            callback.requestLotteryType(bean.getLotteries());
            //            callback.requestTips(bean.getTips());
            SharedPreferencesUtils.saveLotteryPageData(lotteryPageDataResponseBean);
        } else {
            //数据失败取缓存  缓存也不存在再失败提示出错
            lotteryPageDataResponseBean = SharedPreferencesUtils.getLotteryPageDataInfo();
            if (null == lotteryPageDataResponseBean || null == lotteryPageDataResponseBean.getPayload()) {
                callback.requestFailed(StaticResourceUtils.getStringResourceById(R.string.tips_request_fail));
            } else {
                formatLotteryData(lotteryPageDataResponseBean, callback);
            }
        }
    }

    //中奖信息
    @Override
    public void requestLotteryWinningNotice(RequestLotteryPageDataCallback callback) {
        Map<String, Object> param = new HashMap<>();
        param.put("count", 30);
        String paramStr = GsonUtils.getGsonInstance().toJson(param);
        OkHttpUtils.postString()
                .url(HttpHelper.LOTTERY_WINNING_NOTICE)
                .content(paramStr)
                .build()
                .execute(new GenericsCallback<LotteryWinningNoticeResponseBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.requestFailed(HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(LotteryWinningNoticeResponseBean response, int id) {
                        if (null != response && null != response.getPayload())
                            callback.requestTips(response.getPayload().getWinners_info());
                    }
                });
    }

    @Override
    public void requestWinningInfo(final NormalCallback callback) {
        OkHttpUtils.get()
                .url(HttpHelper.RESULT_FRAGMENT_URL)
                .build()
                .execute(new GenericsCallback<ResultBeanData>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResultBeanData response, int id) {
                        if (response != null) {
                            callback.responseOk(response);
                        } else {
                            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.tips_request_fail));
                        }
                    }
                });
    }

    @Override
    public void requestHistoryResult(final String urlParam, final NormalCallback callback) {
        OkHttpUtils.postString()
                .url(urlParam)
                .content("")
                .build()
                .execute(new GenericsCallback<LotteryWinningDataBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(LotteryWinningDataBean response, int id) {
                        if (response.isOk()) {
                            List<LotteryResultHistory> dataList = response.getList();
                            if (dataList == null || dataList.size() <= 0) {
                                callback.responseFail(false, response.getMsg());
                            } else {
                                callback.responseOk(dataList);
                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestResultDetail(String sequence, String type, final NormalCallback callback) {
        if (TextUtils.isEmpty(sequence)) {
            callback.responseFail(false, "期次信息获取失败");
            return;
        }

        String url;
        //TODO 新增彩种需要适配的地方 Y
        if (type.equals(LOTTERY_TYPE_2_COLOR)) {
            url = HttpHelper.DOUBLE_COLOR_DETAIL;
            //            param.put("lottery_name", LOTTERY_NAME_2_COLOR);
        } else if (type.equals(LOTTERY_TYPE_7_HAPPY)) {
            url = HttpHelper.SEVEN_HAPPY_WINNING_DETAIL;
            //            param.put("lottery_name", LOTTERY_NAME_7_HAPPY);
        } else if (type.equals(LOTTERY_TYPE_7_STAR)) {
            url = HttpHelper.STAR7_WINNING_DETAIL;
            //            param.put("lottery_name", LOTTERY_NAME_7_STAR);
        } else if (type.equals(LOTTERY_TYPE_ARRANGE_3)) {
            url = HttpHelper.ARRANGE3_WINNING_DETAIL;
            //            param.put("lottery_name", LOTTERY_NAME_ARRANGE_3);
        } else if (type.equals(LOTTERY_TYPE_ARRANGE_5)) {
            url = HttpHelper.ARRANGE5_WINNING_DETAIL;
            //            param.put("lottery_name", LOTTERY_NAME_ARRANGE_5);
        } else {
            url = HttpHelper.THREE_D_WINNING_DETAIL;
        }

        Map<String, String> param = new HashMap<>();
        param.put(HttpHelper.PARAMS_SEQUENCE, sequence);
        String paramStr = GsonUtils.getGsonInstance().toJson(param);
        OkHttpUtils.postString()
                .url(url)
                .content(paramStr)
                .build()
                .execute(new GenericsCallback<DoubleColorDetailBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(DoubleColorDetailBean response, int id) {
                        if (response.isOk()) {
                            if (response.getData() == null) {
                                callback.responseFail(false, response.getMsg());
                            } else {
                                callback.responseOk(response.getData());
                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    /**
     * 之前的支付
     * 现在的下单+支付
     */
    @Override
    public void buyLottery(String lotteryType, String paramStr, String amount, String payPassword, final NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (!SHOW_LOTTERY_PAY_TEST) {
            if (TextUtils.isEmpty(tokenStr)) {
                callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
                return;
            }
        }
        PostStringBuilder builder = OkHttpUtils.postString();
        GenericsCallback genericsCallback = new GenericsCallback<ResultBuyLotteryBean>(new JsonGenericsSerializer()) {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "下单失败" + e.getMessage());
                callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
            }

            @Override
            public void onResponse(ResultBuyLotteryBean response, int id) {
                Log.e(TAG, "下单返回" + response.getCode());
                if (response.isOk()) {
                    callback.responseOk(response.getData());
                } else if (response.isStolen()) {
                    setStolen(callback);
                } else {
                    callback.responseFail(false, response.getMsg());
                }
            }
        };
        String url = "";
        //TODO 新增彩种需要适配 Y
        if (lotteryType.equals(LOTTERY_TYPE_2_COLOR)) {
            url = HttpHelper.DOUBLE_COLOR_BUY_LOTTERY;
        } else if (lotteryType.equals(LOTTERY_TYPE_3_D)) {
            url = HttpHelper.THREE_D_BUY_LOTTERY;
        } else if (lotteryType.equals(LOTTERY_TYPE_FAST_3)
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)//易快三
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_HB)//湖北快三
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_JS)//江苏快三
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                || lotteryType.equals(LOTTERY_TYPE_11_5)
                || lotteryType.equals(LOTTERY_TYPE_11_5_OLD)
                || lotteryType.equals(LOTTERY_TYPE_11_5_LUCKY)
                || lotteryType.equals(LOTTERY_TYPE_11_5_YUE)
                || lotteryType.equals(LOTTERY_TYPE_11_5_YILE)
                || lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                || lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {//新快三
            builder.addHeader("LotteryName", lotteryType);
            if (lotteryType.equals(LOTTERY_TYPE_FAST_3)
                    || lotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)//易快三
                    || lotteryType.equals(LOTTERY_TYPE_FAST_3_HB)//湖北快三
                    || lotteryType.equals(LOTTERY_TYPE_FAST_3_JS)//江苏快三
                    || lotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)) {
                url = HttpHelper.FAST_THREE_BUY_ORDER;
            } else if (lotteryType.equals(LOTTERY_TYPE_11_5)
                    || lotteryType.equals(LOTTERY_TYPE_11_5_OLD)
                    || lotteryType.equals(LOTTERY_TYPE_11_5_LUCKY)
                    || lotteryType.equals(LOTTERY_TYPE_11_5_YUE)
                    || lotteryType.equals(LOTTERY_TYPE_11_5_YILE)) {
                url = HttpHelper.ELEVEN5_BUY_ORDER;
            } else if (lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                    || lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                url = HttpHelper.SHI_SHI_CAI_BUY_ORDER;
            }
            genericsCallback = new GenericsCallback<ResultBuyLotteryBeanNew>(new JsonGenericsSerializer()) {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.e(TAG, "onError: error is " + e.getMessage());
                    callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                }

                @Override
                public void onResponse(ResultBuyLotteryBeanNew response, int id) {
                    if (response.isOk()) {
                        pay(lotteryType, response, amount, payPassword, callback);
                    } else if (response.isStolen()) {
                        setStolen(callback);
                    } else {
                        callback.responseFail(false, response.getMsg());
                    }
                }
            };
        } else if (lotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
            url = HttpHelper.ARRANGE3_BUY;
        } else if (lotteryType.equals(LOTTERY_TYPE_ARRANGE_5)) {
            url = HttpHelper.ARRANGE5_BUY;
        } else if (lotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {
            url = HttpHelper.SEVEN_HAPPY_BUY;
        } else if (lotteryType.equals(LOTTERY_TYPE_7_STAR)) {
            url = HttpHelper.STAR7_BUY;
        }
        Log.e(TAG, "buyLottery: url is " + url);
        Log.e(TAG, "buyLottery: paramStr is " + paramStr);

        builder.url(url)
                .content(paramStr)
                .build()
                .execute(genericsCallback);
    }

    private void setStolen(NormalCallback callback) {
        MyApplication.getInstance().unSubscribeAllTopic();
        callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
    }

    @Override
    public void pay(String lotteryType, ResultBuyLotteryBeanNew resultBuyLotteryBeanNew, String amount, String payPassword, NormalCallback callback) {
        if (TextUtils.isEmpty(lotteryType)) {
            LogF.d(TAG, "没有匹配的彩票名称");
            callback.responseFail(false, "没有匹配的彩票名称");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("order_id", resultBuyLotteryBeanNew.getData().getOrder_id());
        param.put("amount", Integer.valueOf(StringUtils.clearZero(Double.valueOf(amount) * 100)));
        param.put("pay_pswd", payPassword);
        OkHttpUtils.postString()
                .url(HttpHelper.PAY)
                .content(GsonUtil.getInstance().toJson(param))
                .addHeader("LotteryName", lotteryType)
                .build()
                .execute(new GenericsCallback<ResultBuyLotteryBeanNew>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResultBuyLotteryBeanNew response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(resultBuyLotteryBeanNew.getData());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    //    @Override
    //    public void requestOrderDetail(final String lotteryType, String url, String orderId, String sequence, final NormalCallback callback) {
    //        String tokenStr = MyApplication.getTokenId();
    //        if (TextUtils.isEmpty(tokenStr)) {
    //            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
    //            return;
    //        }
    //
    //        Map<String, Object> param = new HashMap<>();
    //        param.put(HttpHelper.PARAMS_SEQUENCE, sequence);
    //        param.put(HttpHelper.PARAMS_ID, orderId);
    //        String paramStr = GsonUtils.getGsonInstance().toJson(param);
    //        Log.e(TAG, "requestOrderDetail: lotteryType is " + lotteryType);
    //        Log.e(TAG, "requestOrderDetail: jsonStr is " + paramStr);
    //        Log.e(TAG, "requestOrderDetail: url is " + url);
    //        OkHttpUtils.postString()
    //                .url(url)
    ////                .addHeader(HEADER_PARAMS_AUTHORIZATION, "Bearer " + tokenStr)
    //                .content(paramStr)
    //                .build()
    //                .execute(new StringCallback() {
    //                    @Override
    //                    public void onError(Call call, Exception e, int id) {
    //                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
    //                    }
    //
    //                    @Override
    //                    public void onResponse(String response, int id) {
    //                        Log.e(TAG, "onResponse: response is " + response);
    //                        //TODO 新增彩种需要适配的地方 Y
    //                        if (lotteryType.equals(LOTTERY_TYPE_2_COLOR)) {
    //                            ResponseBillDetailDC bean = GsonUtils.getGsonInstance().fromJson(response, new TypeToken<ResponseBillDetailDC>() {
    //                            }.getType());
    //                            if (bean.isOk()) {
    //                                callback.responseOk(bean.getData());
    //                            } else if (bean.getStatus().equals(HttpHelper.RESPONSE_STATUS_UN_LOGIN)) {
    //                                callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
    //                            } else {
    //                                callback.responseFail(false, bean.getMsg());
    //                            }
    //                        } else if (lotteryType.equals(LOTTERY_TYPE_3_D) || lotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
    //                            ResponseBillDetailThreeD bean = GsonUtils.getGsonInstance().fromJson(response, new TypeToken<ResponseBillDetailThreeD>() {
    //                            }.getType());
    //                            if (bean.isOk()) {
    //                                callback.responseOk(bean.getData());
    //                            } else if (bean.getStatus().equals(HttpHelper.RESPONSE_STATUS_UN_LOGIN)) {
    //                                callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
    //                            } else {
    //                                callback.responseFail(false, bean.getMsg());
    //                            }
    //                        } else if (lotteryType.equals(LOTTERY_TYPE_FAST_3)) {
    //                            ResponseBillDetailFastThree bean = GsonUtils.getGsonInstance().fromJson(response, new TypeToken<ResponseBillDetailFastThree>() {
    //                            }.getType());
    //                            if (bean.isOk()) {
    //                                callback.responseOk(bean.getData());
    //                            } else if (bean.getStatus().equals(HttpHelper.RESPONSE_STATUS_UN_LOGIN)) {
    //                                callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
    //                            } else {
    //                                callback.responseFail(false, bean.getMsg());
    //                            }
    //                        } else if (lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)) {
    //                            ResponseBillDetailAlwaysColor bean = GsonUtils.getGsonInstance().fromJson(response, new TypeToken<ResponseBillDetailAlwaysColor>() {
    //                            }.getType());
    //                            if (bean.isOk()) {
    //                                callback.responseOk(bean.getData());
    //                            } else if (bean.getStatus().equals(HttpHelper.RESPONSE_STATUS_UN_LOGIN)) {
    //                                callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
    //                            } else {
    //                                callback.responseFail(false, bean.getMsg());
    //                            }
    //                        } else if (lotteryType.equals(LOTTERY_TYPE_ARRANGE_5)) {
    //                            ResponseBillDetailArrange5 bean = GsonUtils.getGsonInstance().fromJson(response, new TypeToken<ResponseBillDetailArrange5>() {
    //                            }.getType());
    //                            if (bean.isOk()) {
    //                                callback.responseOk(bean.getData());
    //                            } else if (bean.getStatus().equals(HttpHelper.RESPONSE_STATUS_UN_LOGIN)) {
    //                                callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
    //                            } else {
    //                                callback.responseFail(false, bean.getMsg());
    //                            }
    //                        } else if (lotteryType.equals(LOTTERY_TYPE_7_STAR)) {
    //                            ResponseBillDetail7Star bean = GsonUtils.getGsonInstance().fromJson(response, new TypeToken<ResponseBillDetail7Star>() {
    //                            }.getType());
    //                            if (bean.isOk()) {
    //                                callback.responseOk(bean.getData());
    //                            } else if (bean.getStatus().equals(HttpHelper.RESPONSE_STATUS_UN_LOGIN)) {
    //                                callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
    //                            } else {
    //                                callback.responseFail(false, bean.getMsg());
    //                            }
    //                        } else if (lotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {
    //                            ResponseBillDetailSevenHappy bean = GsonUtils.getGsonInstance().fromJson(response, new TypeToken<ResponseBillDetailSevenHappy>() {
    //                            }.getType());
    //                            if (bean.isOk()) {
    //                                callback.responseOk(bean.getData());
    //                            } else if (bean.getStatus().equals(HttpHelper.RESPONSE_STATUS_UN_LOGIN)) {
    //                                callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
    //                            } else {
    //                                callback.responseFail(false, bean.getMsg());
    //                            }
    //                        }
    //
    //                    }
    //                });
    //
    //    }


    @Override//新订单详情接口
    public void requestOrderDetail(String lotteryName, String orderId, final NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put(HttpHelper.PARAMS_ORDER_ID, orderId);
        String paramStr = GsonUtils.getGsonInstance().toJson(param);

        String url = "";
        switch (lotteryName) {
            case LOTTERY_TYPE_FAST_3:
            case LOTTERY_TYPE_FAST_3_JS:
            case LOTTERY_TYPE_FAST_3_HB:
            case LOTTERY_TYPE_FAST_3_NEW:
            case LOTTERY_TYPE_11_5:
            case LOTTERY_TYPE_11_5_OLD:
            case LOTTERY_TYPE_11_5_LUCKY:
            case LOTTERY_TYPE_11_5_YUE:
            case LOTTERY_TYPE_11_5_YILE:
            case LOTTERY_TYPE_ALWAYS_COLOR://重庆时时彩
            case LOTTERY_TYPE_ALWAYS_COLOR_NEW://新时时彩
                url = HttpHelper.PRE_ORDER_DETAIL;
                break;
        }

        Log.e(TAG, "requestOrderDetail: jsonStr is " + paramStr);
        Log.e(TAG, "requestOrderDetail: url is " + url);
        Log.e(TAG, "lotteryType: is " + lotteryName);
        OkHttpUtils.postString()
                .url(url)
                //                .addHeader(HEADER_PARAMS_AUTHORIZATION, "Bearer " + tokenStr)
                .content(paramStr)
                .addHeader("LotteryName", lotteryName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: response is " + response);
                        ResponseBillDetailFastThree bean = GsonUtils.getGsonInstance().fromJson(response, new TypeToken<ResponseBillDetailFastThree>() {
                        }.getType());
                        if (bean.isOk()) {
                            callback.responseOk(bean.getData());
                        } else if (bean.getStatus().equals(HttpHelper.RESPONSE_STATUS_UN_LOGIN)) {
                            setStolen(callback);
                        } else {
                            callback.responseFail(false, bean.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestHistoryNew(String lotteryType, int pageSize, int startPageIdx, long ts, final NormalCallback callback) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("page_size", pageSize);
        paramMap.put("page", startPageIdx);
        if (startPageIdx > 1) {
            paramMap.put("ts", ts);
        }
        String url = "";
        switch (lotteryType) {
            case LOTTERY_TYPE_FAST_3:
            case LOTTERY_TYPE_FAST_3_JS:
            case LOTTERY_TYPE_FAST_3_HB:
            case LOTTERY_TYPE_FAST_3_EASY:
            case LOTTERY_TYPE_FAST_3_NEW:
                url = HttpHelper.FAST_THREE_LATEST_WINNING;
                break;
            case LOTTERY_TYPE_11_5:
            case LOTTERY_TYPE_11_5_LUCKY:
            case LOTTERY_TYPE_11_5_OLD:
            case LOTTERY_TYPE_11_5_YUE:
            case LOTTERY_TYPE_11_5_YILE:
                url = HttpHelper.ELEVEN5_LATEST_WINNING;
                break;
            case LOTTERY_TYPE_ALWAYS_COLOR:
            case LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                url = HttpHelper.SHI_SHI_CAI_LATEST_WINNING;
                break;
        }

        String urlParam = GsonUtils.getGsonInstance().toJson(paramMap);
        Log.e(TAG, "requestFastThreeHistory: urlParam is " + urlParam);
        OkHttpUtils.postString()
                .url(url)
                .addHeader("LotteryName", lotteryType)
                .content(urlParam)
                .build()
                .execute(new GenericsCallback<ResultHistoryListResponseNew>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResultHistoryListResponseNew response, int id) {
                        if (response.isOk()) {
                            ResultHistoryListData data = response.getData();
                            if (data == null) {
                                callback.responseFail(false, response.getMsg());
                            } else {
                                callback.responseOk(data);
                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestAlwaysColorHistory(int pageSize, int startPageIdx, final NormalCallback callback) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(HttpHelper.PARAMS_PAGE_SIZE, pageSize);
        paramMap.put(HttpHelper.PARAMS_START_PAGE, startPageIdx);

        String urlParam = GsonUtils.getGsonInstance().toJson(paramMap);
        Log.e(TAG, "requestAlwaysColorHistory: urlParam is " + urlParam);
        Log.e(TAG, "requestAlwaysColorHistory: url is " + HttpHelper.SHI_SHI_CAI_LATEST_WINNING);
        OkHttpUtils.postString()
                .url(HttpHelper.SHI_SHI_CAI_LATEST_WINNING)
                .content(urlParam)
                .build()
                .execute(new GenericsCallback<ResultAlwaysColorHistory>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResultAlwaysColorHistory response, int id) {
                        if (response.isOk()) {
                            List<AlwaysColorHistory> dataList = response.getData();
                            if (dataList == null || dataList.size() <= 0) {
                                callback.responseFail(false, response.getMsg());
                            } else {
                                callback.responseOk(dataList);
                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestSevenHappyHistory(int pageSize, int startPageIdx, final NormalCallback callback) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(HttpHelper.PARAMS_PAGE_SIZE, pageSize);
        paramMap.put(HttpHelper.PARAMS_START_PAGE, startPageIdx);

        String urlParam = GsonUtils.getGsonInstance().toJson(paramMap);
        Log.e(TAG, "requestSevenHappyHistory: urlParam is " + urlParam);
        Log.e(TAG, "requestSevenHappyHistory: url is " + HttpHelper.SEVEN_HAPPY_LATEST_WINNING);
        OkHttpUtils.postString()
                .url(HttpHelper.SEVEN_HAPPY_LATEST_WINNING)
                .content(urlParam)
                .build()
                .execute(new GenericsCallback<LotteryWinningBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(LotteryWinningBean response, int id) {
                        if (response.isOk()) {
                            List<LotteryResultHistory> dataList = response.getData();
                            if (dataList == null || dataList.size() <= 0) {
                                callback.responseFail(false, response.getMsg());
                            } else {
                                callback.responseOk(dataList);
                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestResultHistory(String lotteryType, String url, int mPageSize, int mStartPage, final NormalCallback callback) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(HttpHelper.PARAMS_PAGE_SIZE, mPageSize);
        paramMap.put(HttpHelper.PARAMS_START_PAGE, mStartPage);
        PostStringBuilder builder = OkHttpUtils.postString();
        String urlParam = GsonUtils.getGsonInstance().toJson(paramMap);

        Log.e(TAG, "requestResultHistory: url is " + url);
        GenericsCallback genericsCallback;
        switch (lotteryType) {
            case LOTTERY_TYPE_11_5:
            case LOTTERY_TYPE_11_5_LUCKY:
            case LOTTERY_TYPE_11_5_OLD:
            case LOTTERY_TYPE_11_5_YUE:
            case LOTTERY_TYPE_11_5_YILE:
                url = HttpHelper.FAST_SEQUENCE;
                builder.addHeader("LotteryName", lotteryType);
                genericsCallback = new GenericsCallback<ResultHistoryListResponseNew>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResultHistoryListResponseNew response, int id) {
                        if (response.isOk()) {
                            //                            List<LotteryResultHistory> dataList = response.getData().getList();
                            //                            if (dataList == null || dataList.size() <= 0) {
                            //                                callback.responseFail(false, response.getMsg());
                            //                            } else {
                            //                                callback.responseOk(dataList);
                            //                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                };
                break;

            default:
                genericsCallback = new GenericsCallback<LotteryWinningBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(LotteryWinningBean response, int id) {
                        if (response.isOk()) {
                            List<LotteryResultHistory> dataList = response.getData();
                            if (dataList == null || dataList.size() <= 0) {
                                callback.responseFail(false, response.getMsg());
                            } else {
                                callback.responseOk(dataList);
                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                };
                break;
        }
        builder
                .url(url)
                .content(urlParam)
                .build()
                .execute(genericsCallback);
    }

    @Override
    public void requestResultHistoryNew(String lotteryName, int mPageSize, int mStartPage, final NormalCallback callback) {
        Map<String, Object> paramMap = new HashMap<>();
        if (mStartPage == 1) {
            paramMap.put("ts", System.currentTimeMillis());
        } else {
            paramMap.put("ts", "");
        }
        paramMap.put("lottery_name", lotteryName);
        paramMap.put("page_size", mPageSize);
        paramMap.put("page", mStartPage);

        String urlParam = GsonUtils.getGsonInstance().toJson(paramMap);

        Log.e(TAG, "requestResultHistory: url is " + lotteryName);

        OkHttpUtils.postString()
                .url(HttpHelper.LOTTERY_WINNING_HISTORY)
                .content(urlParam)
                .build()
                .execute(new GenericsCallback<LotteryWinningResponseBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(LotteryWinningResponseBean response, int id) {
                        if (null != response && response.isOk() && null != response.getPayload()) {
                            List<LotteryResultHistory> dataList = response.getPayload().getList();
                            if (dataList == null || dataList.size() <= 0) {
                                callback.responseFail(false, response.getMsg());
                            } else {
                                callback.responseOk(dataList);
                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestTrend(String lotteryType, NormalCallback callback) {
        Log.e(TAG, "获取走势图: type is " + lotteryType);
        //        PostFormBuilder postFormBuilder = OkHttpUtils.post();
        //        switch (lotteryType) {
        //            case AppConstants.LOTTERY_TYPE_FAST_3:
        //                postFormBuilder.addParams("gameEn", "kuai3");
        //                break;
        //            case AppConstants.LOTTERY_TYPE_FAST_3_JS:
        //                postFormBuilder.addParams("gameEn", "oldkuai3");
        //                break;
        //            case AppConstants.LOTTERY_TYPE_FAST_3_HB:
        //                postFormBuilder.addParams("gameEn", "hbkuai3");
        //                break;
        //            case AppConstants.LOTTERY_TYPE_FAST_3_EASY:
        //                postFormBuilder.addParams("gameEn", "nmgkuai3");
        //                break;
        //            case AppConstants.LOTTERY_TYPE_FAST_3_NEW:
        //                postFormBuilder.addParams("gameEn", "gxkuai3");
        //                break;
        //            case AppConstants.LOTTERY_TYPE_11_5:
        //                postFormBuilder.addParams("gameEn", "d11");
        //                break;
        //            case AppConstants.LOTTERY_TYPE_11_5_OLD:
        //                postFormBuilder.addParams("gameEn", "jxd11");
        //                break;
        //            case AppConstants.LOTTERY_TYPE_11_5_YUE:
        //                postFormBuilder.addParams("gameEn", "gdd11");
        //                break;
        //            case AppConstants.LOTTERY_TYPE_11_5_YILE:
        //                postFormBuilder.addParams("gameEn", "zjd11");
        //                break;
        //            case AppConstants.LOTTERY_TYPE_11_5_LUCKY:
        //                postFormBuilder.addParams("gameEn", "hljd11");
        //                break;
        //            case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR:
        //                postFormBuilder.addParams("gameEn", "ssc");
        //                break;
        //            case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW:
        //                postFormBuilder.addParams("gameEn", "jxssc");
        //                break;
        //        }
        //        postFormBuilder
        //                .url(HttpHelper.LOTTERY_TREND_OLD)
        //                .build()
        //                .execute(new GenericsCallback<TrendResponseData>(new JsonGenericsSerializer()) {
        //                    @Override
        //                    public void onError(Call call, Exception e, int id) {
        //                        Log.e(TAG, "onError: error is " + e.getMessage());
        //                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
        //                    }
        //
        //                    @Override
        //                    public void onResponse(TrendResponseData response, int id) {
        //                        if (null != response) {
        //                            callback.responseOk(response);
        //                        } else {
        //                            callback.responseFail(false, "走势图获取失败");
        //                        }
        //                    }
        //                });

        PostStringBuilder postFormBuilder = OkHttpUtils.postString();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("lottery_name", lotteryType);
        postFormBuilder
                .url(HttpHelper.LOTTERY_TREND)
                .content(GsonUtil.getInstance().toJson(paramMap))
                .build()
                .execute(new GenericsCallback<TrendDataResponse>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(TrendDataResponse response, int id) {
                        if (null != response && null != response.getPayLoad() && null != response.getPayLoad().getTrend() && response.isOk()) {
                            callback.responseOk(response);
                        } else {
                            callback.responseFail(false, "走势图获取失败");
                        }
                    }
                });
    }
}
