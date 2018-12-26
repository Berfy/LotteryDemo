package cn.zcgames.lottery.model.local;

import cn.zcgames.lottery.bean.BankCardBean;

/**
 * Created by admin on 2017/6/15.
 */

public class BankCardDbUtils {

    /**
     * 插入一条记录
     *
     * @param bean
     */
    public static void addBankCard(BankCardBean bean) {
        DaoManager.getInstance().getDaoSession().insert(bean);
    }

    /**
     * 删除全部
     */
    public static void deleteAll() {
        DaoManager.getInstance().getDaoSession().deleteAll(BankCardBean.class);
    }

}
