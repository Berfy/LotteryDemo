package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.home.view.adapter.leftSwipeRv.OnItemActionListener;
import cn.zcgames.lottery.home.listener.DoubleColorOrderChangedListener;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_1_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_2_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_2_GROUP;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_GROUP_3;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_GROUP_6;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_5_ALL;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_5_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE;
import static cn.zcgames.lottery.app.AppConstants.ARRANGE_3_PLAY_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ARRANGE_3_PLAY_GROUP_SIX;
import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_ORDER_TYPE_FUSHI;
import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_PLAY_STYLE_NORMAL;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_DIFFERENT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_SAME_MORE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_SAME_ONE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_DIFFERENT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_SAME_ALL;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_SAME_ONE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_SUM;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_7_STAR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ALWAYS_COLOR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_3;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_5;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_2_COLOR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_7_HAPPY;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_3_D;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_EASY;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_HB;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_JS;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_NEW;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_2;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_2_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_3;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_3_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_4;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_4_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_5;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_5_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_6;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_6_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_7;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_7_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_8;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_8_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_1_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_2_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_2_GROUP;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_3_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_3_GROUP;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN;
import static cn.zcgames.lottery.app.AppConstants.THREE_D_PLAY_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.THREE_D_PLAY_GROUP_SIX;

/**
 * Created by admin on 2017/4/12.
 * Berfy修改
 * 投注单适配器
 */
public class LotteryOrderAdapter extends RecyclerView.Adapter<LotteryOrderAdapter.OrderViewHolder> {

    private static final String TAG = "LotteryOrderAdapter";

    private List<LotteryOrder> mOrderList;
    private Context mContext;
    private DoubleColorOrderChangedListener mChangedListener;
    private OnItemActionListener mOnItemActionListener;

    public LotteryOrderAdapter(Context context, DoubleColorOrderChangedListener changedListener, OnItemActionListener onItemActionListener) {
        mContext = context;
        mOrderList = new ArrayList<>();
        mChangedListener = changedListener;
        mOnItemActionListener = onItemActionListener;
    }

    public void setOrderList(List<LotteryOrder> list) {
        if (list == null) {
            return;
        }
        mOrderList = list;
        updateAdapter();
    }

    public void addOneOrder(LotteryOrder order) {
        if (mOrderList == null) {
            return;
        }
        mOrderList.add(0, order);
        updateAdapter();
    }

    private void updateAdapter() {
        notifyDataSetChanged();
        long totalCount = 0;
        double totalMoney = 0;
        for (int i = 0; i < mOrderList.size(); i++) {
            LotteryOrder bean = mOrderList.get(i);
            totalCount = totalCount + bean.getTotalCount();
            totalMoney += bean.getTotalCount() * (float) SharedPreferenceUtil.get(mContext, bean.getLotteryType() + "_price", AppConstants.LOTTERY_DEFAULT_PRICE);
        }
        mChangedListener.OrderChangedListener(totalCount, StringUtils.getNumberNoZero(totalMoney));
    }

    public void deleteOrder(LotteryOrder order) {
        if (order != null && mOrderList != null && mOrderList.size() > 0) {
            mOrderList.remove(order);
            updateAdapter();
        } else {
        }
    }

    public LotteryOrder getOrderByPosition(int orderPosition) {
        LogF.d(TAG, "删除订单" + orderPosition + "   " + mOrderList.size());
        if (mOrderList != null && mOrderList.size() > orderPosition) {
            return mOrderList.get(orderPosition);
        }
        return null;
    }

    public LotteryOrder getLastestOrder() {
        if (mOrderList != null && mOrderList.size() > 0) {
            return mOrderList.get(0);
        }
        return null;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_double_color_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        final LotteryOrder order = mOrderList.get(position);
        String moneyStr = order.getTotalCount()
                + StaticResourceUtils.getStringResourceById(R.string.lottery_zhu)
                + order.getTotalMoney()
                + StaticResourceUtils.getStringResourceById(R.string.lottery_yuan);
        holder.mMoneyTv.setText(moneyStr);

        String playStyle = "";

        //TODO 新增彩种需要适配
        LogF.d(TAG, "投注单列表   " + GsonUtil.getInstance().toJson(order));
        if (order.getLotteryType().equals(LOTTERY_TYPE_FAST_3)
                || order.getLotteryType().equals(LOTTERY_TYPE_FAST_3_JS)
                || order.getLotteryType().equals(LOTTERY_TYPE_FAST_3_HB)
                || order.getLotteryType().equals(LOTTERY_TYPE_FAST_3_NEW)
                || order.getLotteryType().equals(LOTTERY_TYPE_FAST_3_EASY)) {
            if (order.getPlayMode() == FAST_THREE_SUM) {
                playStyle = "和值";
            } else if (order.getPlayMode() == FAST_THREE_2_DIFFERENT) {
                playStyle = "二不同号";
            } else if (order.getPlayMode() == FAST_THREE_3_DIFFERENT) {
                playStyle = "三不同号";
            } else if (order.getPlayMode() == FAST_THREE_2_SAME_MORE) {
                playStyle = "二同号复选";
            } else if (order.getPlayMode() == FAST_THREE_2_SAME_ONE) {
                playStyle = "二同号单选";
            } else if (order.getPlayMode() == FAST_THREE_3_SAME_ALL) {
                playStyle = "三同号通选";
            } else if (order.getPlayMode() == FAST_THREE_3_SAME_ONE) {
                playStyle = "三同号单选";
            } else {
                playStyle = "三连号通选";
            }
            String balStr = LotteryUtils.getSpannable3dDirectString(order, order.getPlayMode());
            holder.mNumberTv.setText(balStr);
        } else if (order.getLotteryType().equals(LOTTERY_TYPE_ALWAYS_COLOR)
                || order.getLotteryType().equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)
                || order.getLotteryType().equals(LOTTERY_TYPE_ARRANGE_5)) {
            switch (order.getPlayMode()) {
                case ALWAYS_COLOR_5_ALL:
                    playStyle = "五星通选";
                    break;
                case ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                    playStyle = "大小单双";
                    break;
                case ALWAYS_COLOR_1_DIRECT:
                    playStyle = "一星直选";
                    break;
                case ALWAYS_COLOR_2_GROUP:
                    playStyle = "二星组选";
                    break;
                case ALWAYS_COLOR_3_DIRECT:
                    playStyle = "三星直选";
                    break;
                case ALWAYS_COLOR_3_GROUP_3:
                    playStyle = "三星组三";
                    break;
                case ALWAYS_COLOR_3_GROUP_6:
                    playStyle = "三星组六";
                    break;
                case ALWAYS_COLOR_5_DIRECT:
                    playStyle = "五星直选";
                    break;
                case ALWAYS_COLOR_2_DIRECT:
                    playStyle = "二星直选";
                    break;
            }
            String numberStr = LotteryUtils.getSpannableACString(order, order.getPlayMode());
            holder.mNumberTv.setText(numberStr);
        } else if (order.getLotteryType().equals(AppConstants.LOTTERY_TYPE_11_5)
                || order.getLotteryType().equals(AppConstants.LOTTERY_TYPE_11_5_OLD)
                || order.getLotteryType().equals(AppConstants.LOTTERY_TYPE_11_5_LUCKY)
                || order.getLotteryType().equals(AppConstants.LOTTERY_TYPE_11_5_YUE)
                || order.getLotteryType().equals(AppConstants.LOTTERY_TYPE_11_5_YILE)) {
            switch (order.getPlayMode()) {
                case PLAY_11_5_FRONT_1_DIRECT:
                    playStyle = "前一直选";
                    break;
                case PLAY_11_5_ANY_2:
                    playStyle = "任选二";
                    break;
                case PLAY_11_5_FRONT_2_GROUP:
                    playStyle = "前二组选";
                    break;
                case PLAY_11_5_ANY_3:
                    playStyle = "任选三";
                    break;
                case PLAY_11_5_FRONT_3_GROUP:
                    playStyle = "前三组选";
                    break;
                case PLAY_11_5_ANY_4:
                    playStyle = "任选四";
                    break;
                case PLAY_11_5_ANY_5:
                    playStyle = "任选五";
                    break;
                case PLAY_11_5_ANY_6:
                    playStyle = "任选六";
                    break;
                case PLAY_11_5_ANY_7:
                    playStyle = "任选七";
                    break;
                case PLAY_11_5_ANY_8:
                    playStyle = "任选八";
                    break;
                case PLAY_11_5_FRONT_2_DIRECT:
                    playStyle = "前二直选";
                    break;
                case PLAY_11_5_FRONT_3_DIRECT:
                    playStyle = "前三直选";
                    break;
                case PLAY_11_5_ANY_2_DAN:
                    playStyle = "任选二-胆拖";
                    break;
                case PLAY_11_5_ANY_3_DAN:
                    playStyle = "任选三-胆拖";
                    break;
                case PLAY_11_5_ANY_4_DAN:
                    playStyle = "任选四-胆拖";
                    break;
                case PLAY_11_5_ANY_5_DAN:
                    playStyle = "任选五-胆拖";
                    break;
                case PLAY_11_5_ANY_6_DAN:
                    playStyle = "任选六-胆拖";
                    break;
                case PLAY_11_5_ANY_7_DAN:
                    playStyle = "任选七-胆拖";
                    break;
                case PLAY_11_5_ANY_8_DAN:
                    playStyle = "任选八-胆拖";
                    break;
                case PLAY_11_5_FRONT_2_GROUP_DAN:
                    playStyle = "前二组选-胆拖";
                    break;
                case PLAY_11_5_FRONT_3_GROUP_DAN:
                    playStyle = "前三组选-胆拖";
                    break;
            }
            String numberStr = LotteryUtils.getSpannable11_5String(order, order.getPlayMode());
            holder.mNumberTv.setText(numberStr);
        } else if (order.getLotteryType().equals(LOTTERY_TYPE_7_HAPPY)) {
            SpannableString ss = LotteryUtils.getSpannableNormalString(order);
            holder.mNumberTv.setText(ss);
        } else if (order.getLotteryType().equals(LOTTERY_TYPE_7_STAR)) {
            String ss = LotteryUtils.getSpannable7StarString(order);
            holder.mNumberTv.setText(ss);
        } else if (order.getLotteryType().equals(LOTTERY_TYPE_2_COLOR)) {
            SpannableString ss;
            if (order.getPlayMode() == DOUBLE_COLOR_PLAY_STYLE_NORMAL) {
                ss = LotteryUtils.getSpannableNormalString(order);
            } else {
                ss = LotteryUtils.getSpannableDantuoString(order);
            }
            holder.mNumberTv.setText(ss);
        } else if (order.getLotteryType().equals(LOTTERY_TYPE_3_D)) {
            if (order.getPlayMode() == THREE_D_PLAY_DIRECT) {
                playStyle = "直选";
            } else if (order.getPlayMode() == THREE_D_PLAY_GROUP_SIX) {
                playStyle = "组选六";
            } else {
                playStyle = "组选三";
            }
            String balStr = LotteryUtils.getSpannable3dDirectString(order, order.getPlayMode());
            holder.mNumberTv.setText(balStr);
        } else if (order.getLotteryType().equals(LOTTERY_TYPE_ARRANGE_3)) {
            if (order.getPlayMode() == ARRANGE_3_PLAY_DIRECT) {
                playStyle = "直选";
            } else if (order.getPlayMode() == ARRANGE_3_PLAY_GROUP_SIX) {
                playStyle = "组选六";
            } else {
                playStyle = "组选三";
            }
            String balStr = LotteryUtils.getSpannable3dDirectString(order, order.getPlayMode());
            holder.mNumberTv.setText(balStr);
        }
        if (order.getOrderType() == DOUBLE_COLOR_ORDER_TYPE_FUSHI) {
            playStyle += TextUtils.isEmpty(playStyle) ? "" : "  " + StaticResourceUtils.getStringResourceById(R.string.lottery_fushi);
        } else {
            playStyle += TextUtils.isEmpty(playStyle) ? "" : "  " + StaticResourceUtils.getStringResourceById(R.string.lottery_danshi);
        }
        holder.mTypeTv.setText(playStyle);

        holder.ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemActionListener) {
                    mOnItemActionListener.OnItemDelete(position);
                }
            }
        });

        if (position == getItemCount() - 1) {
            holder.vBottom.setVisibility(View.VISIBLE);
        } else {
            holder.vBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView mMoneyTv, mNumberTv, mTypeTv;
        private View mParentView, vBottom;
        public LinearLayout order_ll;
        public TextView mDeleteTv;
        public LinearLayout ll_delete;

        public OrderViewHolder(View itemView) {
            super(itemView);
            mMoneyTv = (TextView) itemView.findViewById(R.id.order_item_money);
            mNumberTv = (TextView) itemView.findViewById(R.id.order_item_number);
            mTypeTv = (TextView) itemView.findViewById(R.id.order_item_type);
            mParentView = itemView.findViewById(R.id.order_rl_doubleColor);
            vBottom = itemView.findViewById(R.id.v_list_bottom);
            order_ll = (LinearLayout) itemView.findViewById(R.id.order_ll);
            mDeleteTv = (TextView) itemView.findViewById(R.id.delete_tv);
            ll_delete = itemView.findViewById(R.id.ll_delete);
        }
    }

}
