package cn.zcgames.lottery.home.view.iview;

import cn.zcgames.lottery.base.IBaseView;

/**
 * Created by admin on 2017/6/20.
 */

public interface IAlwaysColorActivity extends IBaseView {

    void requestCurrentSequenceResult(boolean isOk, Object obj);

    void onCreateLocalOrderResult(boolean isOk, String msgStr);
}
