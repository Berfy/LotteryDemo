package cn.zcgames.lottery.home.view.iview;

import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.home.bean.TrendTypeData;

/**
 * 用于传递相应彩种各类走势图数据的回调接口
 *
 * @author NorthStar
 * @date 2018/10/18 16:43
 */
public interface ITrendActivity extends IBaseView {
    void resultData(TrendTypeData trendTypeData);
}
