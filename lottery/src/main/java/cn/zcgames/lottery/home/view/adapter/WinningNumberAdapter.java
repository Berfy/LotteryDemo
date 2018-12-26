package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.home.bean.OpenAwardBean;

public class WinningNumberAdapter extends RecyclerView.Adapter<WinningNumberAdapter.WinningViewHolder> {
    private Context mContext;
    private String mLotteryType;//彩种
    private int mPlayType;//玩法
    private List<OpenAwardBean> datas;
    private LayoutInflater inflater;

    public WinningNumberAdapter(Context context, String lotteryType, int playType) {
        mContext = context;
        mLotteryType = lotteryType;
        mPlayType = playType;
        inflater = LayoutInflater.from(context);
        datas = new ArrayList<>();
    }

    public void update(String lotteryType, int playType) {
        mLotteryType = lotteryType;
        mPlayType = playType;
    }

    public void setData(List<OpenAwardBean> data) {
        this.datas.clear();
        this.datas = data;
        notifyDataSetChanged();
    }

    @Override
    public WinningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View winView = inflater.inflate(R.layout.win_number_item, parent, false);
        WinningViewHolder winHolder = new WinningViewHolder(winView);
        return winHolder;
    }

    @Override
    public void onBindViewHolder(WinningViewHolder holder, int position) {
        OpenAwardBean openBean = datas.get(position);
        if (position % 2 == 0) {
            holder.mLLNumBg.setBackground(mContext.getResources().getDrawable(R.color.color_white));
            holder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_white));
        } else {
            holder.mLLNumBg.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
            holder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
        }
        if (TextUtils.equals("等待开奖", openBean.getAwardNums())) {
            holder.mLLWaitNum.setVisibility(View.VISIBLE);
            holder.mLLNumBg.setVisibility(View.GONE);
            holder.mTVWaitPeriod.setText(openBean.getPeriod());
            holder.mTVWaitText.setText(openBean.getAwardNums());
        } else {
            holder.mLLWaitNum.setVisibility(View.GONE);
            holder.mLLNumBg.setVisibility(View.VISIBLE);
            holder.mTVPeriod.setText(openBean.getPeriod());
            LogF.d("走势图-开奖", "玩法 mLotteryType=" + mLotteryType + "  mPlayType=" + mPlayType);
            switch (mLotteryType) {
                case AppConstants.LOTTERY_TYPE_11_5:
                case AppConstants.LOTTERY_TYPE_11_5_LUCKY:
                case AppConstants.LOTTERY_TYPE_11_5_OLD:
                case AppConstants.LOTTERY_TYPE_11_5_YILE:
                case AppConstants.LOTTERY_TYPE_11_5_YUE:
                    switch (mPlayType) {
                        case AppConstants.PLAY_11_5_FRONT_1_DIRECT:
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 0, 0));
                            break;
                        case AppConstants.PLAY_11_5_FRONT_2_DIRECT:
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 0, 1));
                            break;
                        case AppConstants.PLAY_11_5_FRONT_3_DIRECT:
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 0, 2));
                            break;
                        case AppConstants.PLAY_11_5_FRONT_2_GROUP:
                        case AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN:
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 0, 1));
                            break;
                        case AppConstants.PLAY_11_5_FRONT_3_GROUP:
                        case AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN:
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 0, 2));
                            break;
                        default:
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 0, 4));
                            break;

                    }
                    break;
                case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR:
                case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                    switch (mPlayType) {
                        case AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE://大小单双 后两位高亮
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 3, 4));
                            break;
                        case AppConstants.ALWAYS_COLOR_1_DIRECT://一星直选
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 4, 4));
                            break;
                        case AppConstants.ALWAYS_COLOR_2_DIRECT://二星直选
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 3, 4));
                            break;
                        case AppConstants.ALWAYS_COLOR_2_GROUP://二星组选
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 3, 4));
                            break;
                        case AppConstants.ALWAYS_COLOR_3_DIRECT:
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 2, 4));
                            break;
                        case AppConstants.ALWAYS_COLOR_3_GROUP_3:
                        case AppConstants.ALWAYS_COLOR_3_GROUP_6:
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 2, 4));
                            break;
                        case AppConstants.ALWAYS_COLOR_5_ALL:
                        case AppConstants.ALWAYS_COLOR_5_DIRECT:
                            holder.mTVWinNum.setText(getLightWinnerNum(openBean.getAwardNums(), 0, 4));
                            break;
                    }
                    break;
                default:
                    holder.mTVWinNum.setText(openBean.getAwardNums());
                    break;
            }
            holder.mTVSum.setText(openBean.getKey3());
            holder.mTVNumSize.setText(openBean.getKey4());
            holder.mTVSumSize.setText(openBean.getKey5());
        }
    }

    private Spanned getLightWinnerNum(String oldWinNum, int startPos, int endPos) {
        LogF.d("走势图-开奖", "计算高亮" + oldWinNum + "  startPos=" + startPos + "  endPos=" + endPos);
        String[] nums = oldWinNum.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < nums.length; i++) {
            if (i < startPos || i > endPos) {
                stringBuilder.append(nums[i]);
                stringBuilder.append(" ");
            } else if (i >= startPos && i <= endPos) {
                stringBuilder.append("<font color=\"#dd3048\">" + nums[i] + "</font> ");
            }
        }
        Spanned spanned = Html.fromHtml(stringBuilder.toString());
        return spanned;
    }

    @Override
    public int getItemCount() {
        return datas.size() > 0 ? datas.size() : 0;
    }

    class WinningViewHolder extends RecyclerView.ViewHolder {
        private TextView mTVPeriod, mTVWinNum, mTVSum, mTVNumSize, mTVSumSize, mTVWaitPeriod, mTVWaitText;
        private LinearLayout mLLNumBg, mLLWaitNum;

        public WinningViewHolder(View itemView) {
            super(itemView);
            mTVPeriod = itemView.findViewById(R.id.tv_win_item_period);
            mTVWinNum = itemView.findViewById(R.id.tv_win_item_num);
            mTVSum = itemView.findViewById(R.id.tv_win_item_key3);
            mTVNumSize = itemView.findViewById(R.id.tv_win_item_key4);
            mTVSumSize = itemView.findViewById(R.id.tv_win_item_key5);
            mLLNumBg = itemView.findViewById(R.id.ll_win_num_item);
            mLLWaitNum = itemView.findViewById(R.id.ll_wait_num);
            mTVWaitPeriod = itemView.findViewById(R.id.tv_wait_item_period);
            mTVWaitText = itemView.findViewById(R.id.tv_wait_num_text);
        }
    }

}
