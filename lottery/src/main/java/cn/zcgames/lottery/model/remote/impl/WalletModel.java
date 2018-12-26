package cn.zcgames.lottery.model.remote.impl;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.bean.response.ResponseBaseBean;
import cn.zcgames.lottery.bean.response.ResponseNormalBean;
import cn.zcgames.lottery.bean.response.ResultAccountDetail;
import cn.zcgames.lottery.bean.response.ResultRecharge;
import cn.zcgames.lottery.model.remote.callback.CommonCallback;
import cn.zcgames.lottery.personal.model.WithdrawRecordInfo;
import cn.zcgames.lottery.personal.view.iview.IWalletModel;
import cn.zcgames.lottery.utils.okhttp.callback.JsonGenericsSerializer;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.utils.GsonUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.utils.okhttp.OkHttpUtils;
import cn.zcgames.lottery.utils.okhttp.callback.GenericsCallback;
import okhttp3.Call;

import static cn.zcgames.lottery.app.HttpHelper.*;

/**
 * Created by admin on 2017/5/18.
 */

public class WalletModel implements IWalletModel {

    private static final String TAG = "WalletModel";

    @Override
    public void recharge(String goods_name, long amount, int payType, final CommonCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(HttpHelper.RESPONSE_CODE_TOKEN_INVALID, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put(PARAMS_GOODS_NAME, goods_name);
        params.put(PARAMS_AMOUNT, amount);
        params.put(PARAMS_CHANNEL, payType);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        Log.e(TAG, "recharge: jsonParam is " + jsonParam);
        Log.e(TAG, "recharge: url is " + WALLET_RECHARGE);
        Log.e(TAG, "recharge: tokenId is " + tokenStr);
        OkHttpUtils.postString()
                .url(WALLET_RECHARGE)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResultRecharge>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(300, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResultRecharge response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getData());
                        } else {
                            callback.responseFail(response.getCode(), response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void pay(String orderId, long amount, String channel, final NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put(PARAMS_AMOUNT, amount);
        params.put(PARAMS_CHANNEL, channel);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);

        OkHttpUtils.postString()
                .url(WALLET_RECHARGE)
                //                .addHeader(HEADER_PARAMS_AUTHORIZATION, "Bearer " + tokenStr)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResponseNormalBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseNormalBean response, int id) {
                        if (response.isOk()) {
                            callback.responseOk("支付成功");
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestRemain(final NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }

        OkHttpUtils.postString()
                .url(WALLET_REMAIN)
                //                .addHeader(HEADER_PARAMS_AUTHORIZATION, "Bearer " + tokenStr)
                .content("")
                .build()
                .execute(new GenericsCallback<ResponseBaseBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseBaseBean response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response);
                        } else if (response.getStatus().equals(RESPONSE_STATUS_UN_LOGIN)) {
                            callback.responseFail(false, response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestAccountDetail(int startPageIndex, int pageSize, int type, final NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }
        Log.e(TAG, "requestAccountDetail: url is " + WALLET_ACCOUNT_DETAIL);

        HashMap<String, Object> params = new HashMap<>();
        params.put("page", startPageIndex);
        params.put(PARAMS_PAGE_SIZE, pageSize);
        params.put(PARAMS_STATUS, type);
        String paramStr = GsonUtils.getGsonInstance().toJson(params);
        Log.e(TAG, "requestAccountDetail: param is " + paramStr);
        OkHttpUtils.postString()
                .url(WALLET_ACCOUNT_DETAIL)
                //                .addHeader(HEADER_PARAMS_AUTHORIZATION, "Bearer " + tokenStr)
                .content(paramStr)
                .build()
                .execute(new GenericsCallback<ResultAccountDetail>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResultAccountDetail response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getData());
                        } else if (response.getStatus().equals(RESPONSE_STATUS_UN_LOGIN)) {
                            callback.responseFail(false, response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void withdraw(long amount, int payType,String payPassword, NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put(PARAMS_AMOUNT, amount);
        params.put(PARAMS_WITHDRAW_CHANNEL, payType);
        params.put(PARAMS_PAY_PSWD, payPassword);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        Log.e(TAG, "recharge: jsonParam is " + jsonParam);
        OkHttpUtils.postString()
                .url(WALLET_WITHDRAW)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResultRecharge>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResultRecharge response, int id) {
                        //                        Log.e(TAG, "onResponse: data is " + response.getData().getUrl() );
                        if (response.isOk()) {
                            callback.responseOk(response.getData());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestWithdrawRecord(long startPageIdx, long ts, NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("page", startPageIdx);
        if (startPageIdx == 1) param.put(PARAMS_TS, 0);
        param.put(PARAMS_PAGE_SIZE, 20);
        String paramStr = GsonUtils.getGsonInstance().toJson(param);

        OkHttpUtils.postString()
                .url(WALLET_WITHDRAW_RECORD)
                .content(paramStr)
                .build()
                .execute(new GenericsCallback<WithdrawRecordInfo>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(WithdrawRecordInfo response, int id) {
                        if (response.isOk()) {
                            if (response.getData() == null) {
                                callback.responseFail(false, response.getMsg());
                            } else {
                                callback.responseOk(response.getData());
                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }


}
