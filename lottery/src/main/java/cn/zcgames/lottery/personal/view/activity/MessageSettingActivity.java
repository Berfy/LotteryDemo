package cn.zcgames.lottery.personal.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.service.mqtt.MQTTManager;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.bean.LotteryType;
import cn.zcgames.lottery.home.bean.LotteryPageDataResponseBean;
import cn.zcgames.lottery.model.local.SharedPreferencesUtils;
import cn.zcgames.lottery.personal.model.NotifyToggleInfo;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.view.adapter.NotifySetAdapter;

/**
 * 通知中心
 *
 * @author NorthStar
 * @date 2018/9/27 13:37
 */
public class MessageSettingActivity extends BaseActivity {

    @BindView(R.id.notify_rv_list)
    RecyclerView notifyRv;//通知开关设置列表
    private NotifyToggleInfo notifyToggleInfo;//配置信息
    private List<LotteryType> lotteryNotify = new ArrayList<>();//打开消息通知的彩种
    private Map<String, NotifyToggleInfo> mTogglesMap; //保存通知开关配置
    private String userId;//用于保存中奖及各彩种开奖通知开关配置的KEY
    public static final String TAG = "MessageSettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_message_setting);
        setButterKnife(this);
        ((TextView) findViewById(R.id.title_tv)).setText("通知设置");
        View backView = findViewById(R.id.title_back);
        backView.setOnClickListener(v -> MessageSettingActivity.this.goBack(MessageSettingActivity.this));
        UIHelper.showWidget(backView, true);
    }

    private void initData() {
        UserBean user = MyApplication.getCurrLoginUser();
        if (user != null) userId = user.getPlayerId();
        LotteryPageDataResponseBean lotterys = SharedPreferencesUtils.getLotteryPageDataInfo();
        if (null != lotterys && null != lotterys.getPayload()) {
            lotteryNotify = lotterys.getPayload().getLotteries();
        }
        if (lotteryNotify.size() > 0 && !TextUtils.isEmpty(userId)) {
            mTogglesMap = SharedPreferenceUtil.getHashMapData(
                    this, AppConstants.NOTIFY_OPTION, NotifyToggleInfo.class);
            if (mTogglesMap != null) notifyToggleInfo = mTogglesMap.get(userId);
            if (notifyToggleInfo != null) {
                List<LotteryType> tempToggles = notifyToggleInfo.getToggles();
                if (tempToggles != null && tempToggles.size() > 0) {
                    int size = lotteryNotify.size();//首页彩种
                    int tempsize = tempToggles.size();//订阅的彩种
                    LogF.d(TAG, "lotteryNotify.size==>" + size + " ,tempToggles.size()==>" + tempsize);
                    for (LotteryType tempType : tempToggles) {//订阅彩种缓存
                        for (LotteryType lotteryType : lotteryNotify) {//首页彩种缓存
                            if (tempType.getName().equals(lotteryType.getName())) {
                                lotteryType.setChecked(tempType.isChecked());
                            } else {
                                setPushTopic(false, tempType.getName());
                            }
                        }
                    }
                    setNotifyToggles(false, "");
                }
            } else {
                notifyToggleInfo = new NotifyToggleInfo();
                notifyToggleInfo.setOpenWin(true);
                //订阅中奖通知
                String winTopic = String.format(Locale.CHINA, "%s%s/%s",
                        AppConstants.NOTIFY_WIN_TYPE, AppConstants.getChannelId(), userId);
                setNotifyToggles(true, winTopic);
                LogF.d(TAG, "size**==>" + lotteryNotify.size());
            }
            initAdapter();
        }
    }

    private void initAdapter() {
        notifyRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notifyRv.setItemAnimator(null);
        NotifySetAdapter mAdapter = new NotifySetAdapter(this, lotteryNotify);
        notifyRv.setAdapter(mAdapter);
        mAdapter.setWinBtnState(notifyToggleInfo.isOpenWin());
        mAdapter.setItemListener(new NotifySetAdapter.ItemListenerClick() {
            @Override
            public void itemListener(int position, boolean isChecked) {
                lotteryNotify.get(position).setChecked(isChecked);
                //订阅开奖通知
                String lotteryTopic = String.format(Locale.CHINA, "%s%s",
                        AppConstants.NOTIFY_LOTTERY_TYPE, lotteryNotify.get(position).getName());
                LogF.d(TAG, "lotteryName==>" + lotteryNotify.get(position).getName() + ", LotteryChecked==>" + isChecked);
                setNotifyToggles(isChecked, lotteryTopic);
            }

            @Override
            public void winSetListener(boolean isOpenWin) {
                if (notifyToggleInfo == null) notifyToggleInfo = new NotifyToggleInfo();
                notifyToggleInfo.setOpenWin(isOpenWin);
                LogF.d(TAG, "中奖通知, isOpenWin==>" + isOpenWin);
                //订阅中奖通知
                String winTopic = String.format(Locale.CHINA, "%s%s/%s",
                        AppConstants.NOTIFY_WIN_TYPE, AppConstants.getChannelId(), userId);
                setNotifyToggles(isOpenWin, winTopic);
            }
        });
    }


    //更新订阅话题
    private void setNotifyToggles(boolean isSubscribe, @Nullable String topic) {
        if (notifyToggleInfo != null) {
            notifyToggleInfo.setToggles(lotteryNotify);
            if (!TextUtils.isEmpty(userId)) {
                if (mTogglesMap == null) mTogglesMap = new LinkedHashMap<>();
                mTogglesMap.put(userId, notifyToggleInfo);//订阅或注销对应话题
                SharedPreferenceUtil.putHashMapData(MessageSettingActivity.this, AppConstants.NOTIFY_OPTION, mTogglesMap);
                if (!TextUtils.isEmpty(topic)) {
                    setPushTopic(isSubscribe, topic);//设置订阅
                }
            }
        }
    }

    private void setPushTopic(boolean isSubscribe, String topic) {
        if (isSubscribe) {
            MQTTManager.getInstance().subscribeToTopic(topic);//订阅推送话题
        } else {
            MQTTManager.getInstance().unSubscribeTopic(topic);//注销该话题订阅推送
        }
    }
}
