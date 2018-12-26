package cn.zcgames.lottery.personal.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.berfy.sdk.mvpbase.listener.ListenerItemClick;
import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.DeviceUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.personal.model.CashEntity;

/**
 * 充值界面的适配器
 *
 * @author NorthStar
 * @date 2018/8/27 11:39
 */
public class RechargeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    private LayoutInflater inflater;
    private List<CashEntity> list;
    private ListenerItemClick itemListener;


    public RechargeAdapter(Activity context, List<CashEntity> data) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.list = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RechargeHolder(inflater.inflate(R.layout.item_recharge_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RechargeHolder rechargeHolder = (RechargeHolder) holder;
        CashEntity itemData = list.get(position);
        int cash = itemData.getCash();
        rechargeHolder.cashTV.setText(cash + "元");

        //计算item的布局
        boolean isLeft = position % 3 == 0;
        boolean isMiddle = position % 3 == 1;
        boolean isRight = position % 3 == 2;
        int w = (int) (MyApplication.SCREEN_WIDTH * 111f / 375);//真实item的宽值
        int sidesInstance = (int) (MyApplication.SCREEN_WIDTH * 15f / 375);//真实两边边距的值
        int padding = (MyApplication.SCREEN_WIDTH - 2 * sidesInstance - 3 * w) / 4;
        DisplayMetrics dm = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rechargeHolder.cashTV.getLayoutParams();

        if (isLeft) {
            rechargeHolder.rl.setPadding(0, 0, DeviceUtils.dpToPx(mContext, 5), 0);
        } else if (isMiddle) {
            rechargeHolder.rl.setPadding(0, 0, 0, 0);
        } else if (isRight) {
            rechargeHolder.rl.setPadding(DeviceUtils.dpToPx(mContext, 5), 0, 0, 0);
        }

        params.width = (dm.widthPixels - 4 * padding - 2 * sidesInstance) / 3;
        params.height = params.width * 44 / 111;
        rechargeHolder.cashTV.setLayoutParams(params);

        //选中状态
        if (itemData.isSelectedFlag()) {
            rechargeHolder.cashTV.setTextColor(CommonUtil.getColor(mContext, R.color.color_FFFFFF));
            rechargeHolder.cashTV.setBackground(CommonUtil.getDrawable(mContext, R.drawable.select_bg_square));
        } else {
            rechargeHolder.cashTV.setTextColor(CommonUtil.getColor(mContext, R.color.color_red_ball));
            rechargeHolder.cashTV.setBackground(CommonUtil.getDrawable(mContext, R.drawable.normal_bg_square));
        }
        rechargeHolder.cashTV.setOnClickListener(v -> itemListener.itemListener(v, position));
    }

    @Override
    public int getItemCount() {
        return list.size() > 0 ? list.size() : 0;
    }

    public void setOnItemListener(ListenerItemClick itemListener) {
        this.itemListener = itemListener;
    }

    public class RechargeHolder extends RecyclerView.ViewHolder {
        private TextView cashTV;
        private RelativeLayout rl;

        RechargeHolder(View view) {
            super(view);
            cashTV = view.findViewById(R.id.cash_tv);
            rl = view.findViewById(R.id.rl_recharge);
        }
    }
}



