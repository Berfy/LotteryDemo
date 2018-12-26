package cn.zcgames.lottery.home.view.iview;

import cn.zcgames.lottery.base.IBaseView;

/**
 * Created by admin on 2017/5/24.
 */

public interface IArrange3Activity extends IBaseView {

    void onTotalCountResult(int count);

    void onCreateDirectOrder(boolean isOk, String msgStr);

    void onRequestSequence(boolean isOk, Object msg);
}
