package cn.zcgames.lottery.model.remote.impl;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import cn.zcgames.lottery.model.remote.callback.NormalCallback;

import static org.junit.Assert.*;

/**
 * Created by admin on 2017/7/20.
 */
public class WalletModelTest {

    private static final String TAG = "WalletModelTest";

    WalletModel walletModel;

    @Before
    public void setUp() throws Exception {
        walletModel = new WalletModel();
    }

    @Test
    public void recharge() throws Exception {
//        walletModel.recharge(12, "zhifubao", new NormalCallback() {
//            @Override
//            public void responseOk(Object msgStr) {
//
//            }
//
//            @Override
//            public void responseFail(String errorStr) {
//            }
//        });
    }

    @Test
    public void pay() throws Exception {

    }

    @Test
    public void requestRemain() throws Exception {

    }

    @Test
    public void requestAccountDetail() throws Exception {

    }

    @Test
    public void addOrListBankCards() throws Exception {

    }

    @Test
    public void updateBankCard() throws Exception {

    }

}