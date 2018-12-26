package cn.zcgames.lottery.message.view.iview;

//import com.lottery.entity.LotteryOrder;

import cn.zcgames.lottery.bean.response.ResultBuyLotteryBeanNew;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.callback.RequestLotteryPageDataCallback;

/**
 * 首页-开奖的iView
 *
 * @author NorthStar
 * @date 2018/8/20 18:23
 */
public interface ILotteryModel {

    /**
     * 从服务器获取双色球当前期号
     *
     * @param mLotteryType
     * @param callback
     */
    void requestCurrentSequence(String mLotteryType, NormalCallback callback);

    /**
     * 请求购彩首页的全部数据
     *
     * @param callback
     */
    void requestLotteryData(RequestLotteryPageDataCallback callback);

    /**
     * 请求开奖公告
     *
     * @param callback
     */
    void requestLotteryWinningNotice(RequestLotteryPageDataCallback callback);

    /**
     * 请求所有彩票的开奖结果
     *
     * @param callback
     */
    void requestWinningInfo(NormalCallback callback);

    /**
     * 请求开奖历史
     *
     * @param urlParam
     * @param callback
     */
    void requestHistoryResult(String urlParam, NormalCallback callback);

    /**
     * 请求开奖详情
     *
     * @param sequence 期号
     * @param type     彩票类型
     * @param callback
     */
    void requestResultDetail(String sequence, String type, NormalCallback callback);

    /**
     * 购买福彩
     *
     * @param lotteryType
     * @param jsonStr
     * @param amount         订单金额
     * @param normalCallback
     */
    void buyLottery(String lotteryType, String jsonStr, String amount, String payPassword, NormalCallback normalCallback);

    /**
     * 购买福彩
     *
     * @param lotteryType             彩票类型
     * @param resultBuyLotteryBeanNew 订单
     * @param amount                  订单付款金额
     */
    void pay(String lotteryType, ResultBuyLotteryBeanNew resultBuyLotteryBeanNew, String amount, String payPassword, final NormalCallback callback);

    /**
     * 查询订单详情
     *
     * @param lotteryName 彩种名称
     * @param orderId     订单id
     * @param callback
     */
    void requestOrderDetail(String lotteryName, String orderId, NormalCallback callback);

    /**
     * 查询快三开奖历史 新的
     *
     * @param pageSize 一页显示的数据
     * @param page     页码
     * @param callback
     */
    void requestHistoryNew(String lotteryType, int pageSize, int page, long ts, NormalCallback callback);

    /**
     * 查询时时彩开奖历史
     *
     * @param pageSize     一页显示的数据
     * @param startPageIdx 页码
     * @param callback
     */
    void requestAlwaysColorHistory(int pageSize, int startPageIdx, NormalCallback callback);

    /**
     * 查询七乐彩开奖历史
     *
     * @param pageSize
     * @param startPageIdx
     * @param callback
     */
    void requestSevenHappyHistory(int pageSize, int startPageIdx, NormalCallback callback);

    /**
     * @param lotteryType 彩种
     * @param url
     * @param mPageSize
     * @param mStartPage
     * @param callback
     */
    void requestResultHistory(String lotteryType, String url, int mPageSize, int mStartPage, NormalCallback callback);

    /**
     * @param lotteryName 彩种名称
     * @param mPageSize
     * @param mStartPage
     * @param callback
     */
    void requestResultHistoryNew(String lotteryName, int mPageSize, int mStartPage, NormalCallback callback);

    /**
     * 获取走势图数据
     *
     * @param lotteryType 彩种名称
     * @param callback
     */
    void requestTrend(String lotteryType, NormalCallback callback);
}
