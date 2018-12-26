package cn.zcgames.lottery.personal.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.event.UserInfoUpdateEvent;
import cn.zcgames.lottery.personal.presenter.IdentityInfoPresenter;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.view.iview.IIdentityInfoActivity;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 实名认证界面
 *
 * @author NorthStar
 * @date 2018/8/20 17:12
 */
public class IdentityInfoActivity extends BaseActivity implements IIdentityInfoActivity {

    private static final String TAG = "IdentityInfoActivity";
    public static final String TYPE_ACTIVITY = "TYPE_ACTIVITY";
    public static final String TYPE_ACTIVITY_WITHDRAW = "TYPE_ACTIVITY_WITHDRAW";
    public static final String TYPE_ACTIVITY_USER_INFO = "TYPE_ACTIVITY_USER_INFO";

    private static final int MSG_SET_IDENTIFY_INFO_OK = 0;
    private static final int MSG_SHOW_FAIL_TIP = 1;

    @BindView(R.id.identify_ed_realName)
    EditText mRealNameEd;

    @BindView(R.id.identify_ed_id)
    EditText mIdEd;

    @BindView(R.id.identify_btn_next)
    Button mNextBtn;


    private IdentityInfoPresenter mPresenter;
    private String mActivityType = "";
    private boolean mIsRealNameOk;
    private boolean mIsIdOk;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_IDENTIFY_INFO_OK:
                    UIHelper.showToast(R.string.tips_update_ok);
                    UserBean user = MyApplication.getCurrLoginUser();
                    user.setIsIdentSet("2");
                    MyApplication.updateCurrLoginUser(user);
                    if (TYPE_ACTIVITY_USER_INFO.equals(mActivityType)) {
                        EventBus.getDefault().post(new UserInfoUpdateEvent(new UserBean()));
                    }
                    goBack(IdentityInfoActivity.this);
                    break;
                case MSG_SHOW_FAIL_TIP:
                    if (msg.obj instanceof String) {
                        String errorStr = msg.obj.toString();
                        showMessageDialog(errorStr, v -> {
                        });
                    } else {
                        UIHelper.showToast(R.string.tips_request_fail);
                    }
                    break;
            }
        }
    };

    /**
     * 进入此activity的入口
     *
     * @param fromActivity
     * @param typeStr
     */
    public static void intoThisActivity(Activity fromActivity, String typeStr) {
        Intent i = new Intent(fromActivity, IdentityInfoActivity.class);
        i.putExtra(TYPE_ACTIVITY, typeStr);
        fromActivity.startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_info);
        initData();
        initView();
    }

    private void initData() {
        setButterKnife(this);
        mActivityType = getIntent().getStringExtra(TYPE_ACTIVITY);
        mPresenter = new IdentityInfoPresenter(this, this);
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.withdraw_identity_info);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        setListener();
    }

    private void setListener() {
        setChangeListener(mRealNameEd, 0);
        setChangeListener(mIdEd, 1);
    }


    private void setChangeListener(EditText et, int type) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean notEmpty = !TextUtils.isEmpty(getElement(et));
                switch (type) {
                    case 0:
                        mIsRealNameOk = notEmpty;
                        break;
                    case 1:
                        mIsIdOk = notEmpty;
                        break;
                }
                mNextBtn.setEnabled(mIsRealNameOk && mIsIdOk);
            }
        });
    }

    //获取所需参数
    private String getElement(EditText et) {
        return et.getText().toString();
    }

    @OnClick({R.id.title_back, R.id.identify_btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                backToPreActivity();
                break;
            case R.id.identify_btn_next:
                //设置身份信息
                mPresenter.setIdentityInfo(getElement(mRealNameEd), getElement(mIdEd));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backToPreActivity();
        }
        return true;
    }

    private void backToPreActivity() {
        if (mIsIdOk || mIsRealNameOk) {
            AlertDialog alertDialog = new AlertDialog(this)
                    .builder()
                    .setCancelable(false)
                    .setMsg("是否确定放弃身份信息编辑?")
                    .setNegativeButton(StaticResourceUtils.getStringResourceById(R.string.btn_ok),
                            v -> goBack(IdentityInfoActivity.this))
                    .setPositiveButton(StaticResourceUtils.getStringResourceById(R.string.btn_cancel),
                            v -> {
                            });
            alertDialog.show();
        } else {
            goBack(IdentityInfoActivity.this);
        }
    }


    @Override
    public void setIdentityInfoResult(boolean isOk, Object errorStr) {
        if (isOk) {
            mHandler.sendEmptyMessage(MSG_SET_IDENTIFY_INFO_OK);
        } else {
            Message msg = new Message();
            msg.obj = errorStr;
            msg.what = MSG_SHOW_FAIL_TIP;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void showWaitingDialog(String msgStr) {
        showWaitingDialog(IdentityInfoActivity.this, msgStr, false);
    }

    @Override
    public void hiddenWaitingDialog() {
        runOnUiThread(this::hideWaitingDialog);
    }
}
