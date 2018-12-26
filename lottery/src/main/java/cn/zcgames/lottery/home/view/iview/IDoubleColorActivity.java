package cn.zcgames.lottery.home.view.iview;

/**
 * Created by admin on 2017/5/11.
 */

public interface IDoubleColorActivity {

    void createNormalOrderResult(boolean isOk, String msgStr);

    void createDantuoOrderResult(boolean isOk, String msgStr);

    void getNormalTotalResult(long totalCount);

    void getDantuoTotalCountResul(long totalCount);

    void showDialog(String msgStr);

    void hiddenDialog();

    void requestSequenceResult(boolean isOK,Object str);
}
