package cn.zcgames.lottery.home.view.iview;

import cn.zcgames.lottery.base.IBaseView;

/**
 * Created by admin on 2017/7/4.
 */

public interface ISevenActivity extends IBaseView {
    void onSelectedOrderCount(int count);

    void onResultCurrentSequence(boolean b, Object msgStr);

    void onResultCreateLocalOrder(boolean b, String msgStr);
}
