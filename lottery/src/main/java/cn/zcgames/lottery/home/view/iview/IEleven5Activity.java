package cn.zcgames.lottery.home.view.iview;

import cn.zcgames.lottery.base.IBaseView;

/**
 * Created by admin on 2017/5/24.
 */

public interface IEleven5Activity extends IBaseView {

    void onRequestSequence(boolean isOk, Object msg);

    void onTotalCount(long count);
}
