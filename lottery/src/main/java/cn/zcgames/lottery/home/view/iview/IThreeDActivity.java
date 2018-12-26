package cn.zcgames.lottery.home.view.iview;

/**
 * Created by admin on 2017/5/24.
 */

public interface IThreeDActivity {

    void onTotalCountResult(int count);

    void onCreateDirectOrder(boolean isOk, String msgStr);

    void onRequestSequence(boolean isOk, Object msg);
}
