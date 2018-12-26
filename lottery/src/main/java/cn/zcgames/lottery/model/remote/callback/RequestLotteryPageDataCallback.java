package cn.zcgames.lottery.model.remote.callback;

import java.util.List;

import cn.zcgames.lottery.home.bean.ADInfo;
import cn.zcgames.lottery.home.bean.LotteryType;

/**
 * 开奖数据
 *
 * @author NorthStar
 * @date 2018/8/20 18:23
 */
public interface RequestLotteryPageDataCallback {
    void requestAdInfo(List<ADInfo> ads);

    void requestLotteryType(List<LotteryType> types);

    void requestTips(List<String> tips);

    void requestFailed(String msgStr);
}
