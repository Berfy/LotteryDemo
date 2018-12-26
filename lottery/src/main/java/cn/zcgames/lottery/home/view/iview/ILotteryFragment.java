package cn.zcgames.lottery.home.view.iview;

import android.widget.TextView;

import java.util.List;

import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.home.bean.ADInfo;
import cn.zcgames.lottery.home.bean.LotteryType;

/**
 * Created by admin on 2017/4/12.
 */

public interface ILotteryFragment extends IBaseView {

    /**
     * 获取轮播广告成功
     *
     * @param adInfoList
     */
    void requestAdList(List<ADInfo> adInfoList);

    /**
     * 获取lotteryType列表成功
     *
     * @param typeList
     */
    void requestLotteryType(List<LotteryType> typeList);

    /**
     * 显示公告消息
     *
     * @param tipTvs
     */
    void requestNoticeTips(List<TextView> tipTvs);

    /**
     * 请求失败
     *
     * @param errorStr
     */
    void requestFailed(String errorStr);

    /**
     * 正在请求
     *
     * @param msgStr
     */
    void requestingData(String msgStr);

}
