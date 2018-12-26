package cn.zcgames.lottery.personal.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.response.ResponseNormalBean;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.event.UserInfoUpdateEvent;
import cn.zcgames.lottery.utils.okhttp.callback.JsonGenericsSerializer;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.utils.GsonUtils;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.utils.okhttp.OkHttpUtils;
import cn.zcgames.lottery.utils.okhttp.callback.GenericsCallback;
import okhttp3.Call;

import static cn.zcgames.lottery.app.HttpHelper.ACCOUNT_UPDATE_NICK_NAME;
import static cn.zcgames.lottery.app.HttpHelper.PARAMS_NICK_NAME;

public class SetNicknameActivity extends BaseActivity {

    private static final String TAG = "SetNicknameActivity";

    private static final int MSG_UPDATE_FAIL = 0;
    private static final int MSG_UPDATE_OK = 1;

    @BindView(R.id.nickName_et_input)
    EditText mNickNameEt;

    @BindView(R.id.nickName_tv_ok)
    TextView mOkTv;

    private String mNickName;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_FAIL:
                    if (msg.obj instanceof Exception) {
                        String errorStr = msg.obj.toString();
                        UIHelper.showToast(HttpHelper.getErrorTipsByResponseCode(errorStr));
                    } else if (msg.obj instanceof String) {
                        ToastUtil.getInstances().showShort(msg.obj.toString());
                    } else {
                        ToastUtil.getInstances().showShort(R.string.tips_update_fail);
                    }
                    break;
                case MSG_UPDATE_OK:
                    UIHelper.showToast(R.string.tips_update_ok);
                    UserBean user = MyApplication.getCurrLoginUser();
                    user.setNickname(mNickName);
                    MyApplication.updateCurrLoginUser(user);
                    EventBus.getDefault().post(new UserInfoUpdateEvent(user));
                    goBack(SetNicknameActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_update_nickname);
        setButterKnife(this);
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.mine_update_nickname);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
    }

    @OnClick({R.id.title_back, R.id.nickName_tv_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(SetNicknameActivity.this);
                break;
            case R.id.nickName_tv_ok:
                if (!AppUtils.checkJump()) return;
                setNickname();
                break;
        }
    }

    private void setNickname() {
        mNickName = mNickNameEt.getText().toString().trim();
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(mNickName)) {
            UIHelper.showToast(R.string.tips_nickname_null);
            return;
        }
        if (TextUtils.isEmpty(tokenStr)) {
            UIHelper.showToast("token is null");
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put(PARAMS_NICK_NAME, mNickName);
        String paramStr = GsonUtils.getGsonInstance().toJson(param);
        Log.e(TAG, "setNickname: paramStr is " + paramStr);
        OkHttpUtils.postString()
                .url(ACCOUNT_UPDATE_NICK_NAME)
                .content(paramStr)
                .build()
                .execute(new GenericsCallback<ResponseNormalBean>(new JsonGenericsSerializer()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        Message msg = new Message();
                        msg.obj = e;
                        msg.what = MSG_UPDATE_FAIL;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(ResponseNormalBean response, int id) {
                        Log.e(TAG, "onError: error is " + response.toString());
                        if (response.isOk()) {
                            mHandler.sendEmptyMessage(MSG_UPDATE_OK);
                        } else {
                            Message msg = new Message();
                            msg.obj = response.getMsg();
                            msg.what = MSG_UPDATE_FAIL;
                            mHandler.sendMessage(msg);
                        }
                    }
                });
    }
}
