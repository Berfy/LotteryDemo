package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.BasicTrendBean;

/**
 * 11选5任选及组选基本走势适配器
 *
 * @author NorthStar
 * @date 2018/10/16 18:48
 */
public class AnyBasicAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private final int TYPE_ONE = 1;
    private final int TYPE_TWO = 2;
    private final int TYPE_THREE = 3;
    private LayoutInflater inflater;
    private List<BasicTrendBean> datas = new ArrayList<>();
    private int firstRepeatCount;
    private boolean isSelected;
    public static final String TAG = "走势图频率";
    private boolean isShowEmptyView = false;
    private int dataSize = 0;
    private boolean isShowMissNum = true;
    private boolean isHideLastFourth = false;


    public AnyBasicAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<BasicTrendBean> data) {
        this.datas = data;
        if (data != null && data.size() > 0) {
            isShowEmptyView = data.get(data.size() - 1).isWaitAward();
        }
    }

    public void setDataSize(int size) {
        dataSize = size;
    }

    public void showLastView(boolean isShow) {
        isShowEmptyView = isShow;
    }

    public void showMissNum(boolean isShow) {
        isShowMissNum = isShow;
    }

    public void hideLastFourth(boolean isShow) {
        isHideLastFourth = isShow;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            View view = inflater.inflate(R.layout.any_basic_item, parent, false);
            return new BasicViewHolder(view);
        } else if (viewType == TYPE_TWO) {
            View view = inflater.inflate(R.layout.any_basic_item_two, parent, false);
            return new BasicTwoViewHolder(view);
        } else if (viewType == TYPE_THREE) {
            View view = inflater.inflate(R.layout.bottom_empty_11_5_view, parent, false);
            return new BottomEmptyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BasicViewHolder) {
            BasicViewHolder basicHolder = (BasicViewHolder) holder;
            if (position % 2 == 0) {
                basicHolder.mLLBg.setBackground(mContext.getResources().getDrawable(R.color.color_white));
                basicHolder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_white));
            } else {
                basicHolder.mLLBg.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
                basicHolder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
            }

            //中奖号码
            BasicTrendBean bean = datas.get(position);
            basicHolder.mTVOpenPeriods.setText(bean.periods);
            if (TextUtils.equals("等待开奖", bean.numbers)) {
                basicHolder.mLLWaitNum.setVisibility(View.VISIBLE);
                basicHolder.mLLBg.setVisibility(View.GONE);
                basicHolder.mTVWaitPeriod.setText(bean.periods);
                //                basicHolder.mTVWaitText.setText(bean.numbers);
            } else {
                basicHolder.mLLWaitNum.setVisibility(View.GONE);
                basicHolder.mLLBg.setVisibility(View.VISIBLE);
                if (bean.numbers != null) {
                    String[] numbers = bean.numbers.split(",");
                    TextView[] textViews = {basicHolder.mTVFirstNum, basicHolder.mTVSecondNum, basicHolder.mTVThirdNum,
                            basicHolder.mTVFourthNum, basicHolder.mTVFifthNum, basicHolder.mTVSixthNum, basicHolder.mTVSeventhNum,
                            basicHolder.mTVEighthNum, basicHolder.mTVNinthNum, basicHolder.mTVTenthNum, basicHolder.mTVEleventhNum};

                    ImageView[] imageViews = {basicHolder.mIVFirst, basicHolder.mIVSecond, basicHolder.mIVThird,
                            basicHolder.mIVFourth, basicHolder.mIVFifth, basicHolder.mIVSixth, basicHolder.mIVSeventh,
                            basicHolder.mIVEighth, basicHolder.mIVNinth, basicHolder.mIVTenth, basicHolder.mIVEleventh};

                    //设置数据
                    if (!TextUtils.isEmpty(bean.singleNumbers)) {
                        String[] missTimes = bean.singleNumbers.split(",");
                        for (int i = 0; i < missTimes.length; i++) {
                            if (isShowMissNum) {
                                textViews[i].setText(missTimes[i]);
                            } else {
                                textViews[i].setText("");
                            }
                        }

                        //设置中奖情况
                        for (int i = 0; i < missTimes.length; i++) {
                            for (String number : numbers) {
                                if (i + 1 == Integer.parseInt(number)) {
                                    firstRepeatCount++;
                                    isSelected = true;
                                    textViews[i].setText(number);
                                }
                            }
                            if (isSelected) {
                                setBG(textViews[i], R.drawable.sum_baisc_trend_bg, R.color.color_white);
                            } else {
                                setBG(textViews[i], R.color.transparent, R.color.color_a29e9a);
                            }
                            setRightIcon(imageViews[i], firstRepeatCount);
                            firstRepeatCount = 0;
                            isSelected = false;
                        }
                    }
                } else {
                    bean.numbers = "等待开奖";
                }
            }

        } else if (holder instanceof BasicTwoViewHolder) {
            BasicTwoViewHolder basicTwoHolder = (BasicTwoViewHolder) holder;

            TextView[] textViews = {basicTwoHolder.mTVFirstCount, basicTwoHolder.mTVSecondCount,
                    basicTwoHolder.mTVThirdCount, basicTwoHolder.mTVFourthCount, basicTwoHolder.mTVFifthCount,
                    basicTwoHolder.mTVSixthCount, basicTwoHolder.mTVSeventhCount, basicTwoHolder.mTVEighthCount,
                    basicTwoHolder.mTVNinthCount, basicTwoHolder.mTVTenthCount, basicTwoHolder.mTVEleventhCount};

            BasicTrendBean bean2 = datas.get(position);
            String singleNumbers = bean2.singleNumbers;
            LogF.d(TAG, "anybasicAdapter-singleNumbers==>" + singleNumbers);
            String[] missTimes = singleNumbers.split(",");
            for (int i = 0; i < missTimes.length; i++) {
                textViews[i].setText(missTimes[i]);
            }

            if (position % 2 == 0) {
                basicTwoHolder.mLLTwoBG.setBackground(mContext.getResources().getDrawable(R.color.color_white));
            } else {
                basicTwoHolder.mLLTwoBG.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
            }

            if (position == datas.size() - 4) {
                basicTwoHolder.mTVInfo.setText(R.string.show_num);
                basicTwoHolder.mTVInfo.setTextColor(CommonUtil.getColor(mContext, R.color.color_ae68c6));
                changeTextColor(CommonUtil.getColor(mContext, R.color.color_ae68c6), textViews);
            } else if (position == datas.size() - 3) {
                basicTwoHolder.mTVInfo.setText(R.string.average_miss_num);
                basicTwoHolder.mTVInfo.setTextColor(CommonUtil.getColor(mContext, R.color.color_43bf8a));
                changeTextColor(CommonUtil.getColor(mContext, R.color.color_43bf8a), textViews);
            } else if (position == datas.size() - 2) {
                basicTwoHolder.mTVInfo.setText(R.string.max_miss_num);
                basicTwoHolder.mTVInfo.setTextColor(CommonUtil.getColor(mContext, R.color.color_f1973a));
                changeTextColor(CommonUtil.getColor(mContext, R.color.color_f1973a), textViews);
            } else if (position == datas.size() - 1) {
                basicTwoHolder.mTVInfo.setText(R.string.max_continuously_appear);
                basicTwoHolder.mTVInfo.setTextColor(CommonUtil.getColor(mContext, R.color.color_24c4cb));
                changeTextColor(CommonUtil.getColor(mContext, R.color.color_24c4cb), textViews);
            }

            basicTwoHolder.mLLTwoBG.setVisibility(isHideLastFourth ? View.GONE : View.VISIBLE);
        } else if (holder instanceof BottomEmptyViewHolder) {
            BottomEmptyViewHolder emptyHolder = (BottomEmptyViewHolder) holder;
            emptyHolder.mLLEmpty.setVisibility(isShowEmptyView || isHideLastFourth ? View.VISIBLE : View.GONE);

        }

    }

    //设置字体颜色
    private void changeTextColor(int textColor, TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setTextColor(textColor);
        }
    }

    //设置背景
    private void setBG(TextView textView, int bgColor, int textColor) {
        textView.setBackground(mContext.getResources().getDrawable(bgColor));
        textView.setTextColor(mContext.getResources().getColor(textColor));
    }

    //设置右上角标
    private void setRightIcon(ImageView imageView, int num) {
        if (num == 2) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.double_same_num));
        } else if (num == 3) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.three_same_num));
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {

        return dataSize;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < datas.size() - 4) {
            return TYPE_ONE;
        } else if (position == datas.size() && !isHideLastFourth) {
            return TYPE_THREE;
        } else {
            return isShowEmptyView || isHideLastFourth ? TYPE_ONE : TYPE_TWO;
        }
    }

    class BasicViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLLBg, mLLWaitNum;
        private TextView mTVOpenPeriods, mTVFirstNum, mTVSecondNum, mTVThirdNum, mTVFourthNum, mTVFifthNum,
                mTVSixthNum, mTVSeventhNum, mTVEighthNum, mTVNinthNum, mTVTenthNum, mTVEleventhNum, mTVWaitPeriod;

        private ImageView mIVFirst, mIVSecond, mIVThird, mIVFourth, mIVFifth, mIVSixth, mIVSeventh,
                mIVEighth, mIVNinth, mIVTenth, mIVEleventh;

        BasicViewHolder(View itemView) {
            super(itemView);
            mLLBg = itemView.findViewById(R.id.ll_sum_basic_item);
            mLLWaitNum = itemView.findViewById(R.id.ll_wait_num);
            mTVOpenPeriods = itemView.findViewById(R.id.tv_basic_item_periods);
            mTVFirstNum = itemView.findViewById(R.id.tv_first_num);
            mTVSecondNum = itemView.findViewById(R.id.tv_second_num);
            mTVThirdNum = itemView.findViewById(R.id.tv_third_num);
            mTVFourthNum = itemView.findViewById(R.id.tv_fourth_num);
            mTVFifthNum = itemView.findViewById(R.id.tv_fifth_num);
            mTVSixthNum = itemView.findViewById(R.id.tv_sixth_num);
            mTVSeventhNum = itemView.findViewById(R.id.tv_seventh_num);
            mTVEighthNum = itemView.findViewById(R.id.tv_eighth_num);
            mTVNinthNum = itemView.findViewById(R.id.tv_ninth_num);
            mTVTenthNum = itemView.findViewById(R.id.tv_tenth_num);
            mTVEleventhNum = itemView.findViewById(R.id.tv_eleventh_num);
            mTVWaitPeriod = itemView.findViewById(R.id.tv_wait_item_period);
            mIVFirst = itemView.findViewById(R.id.iv_first_num);
            mIVSecond = itemView.findViewById(R.id.iv_second_num);
            mIVThird = itemView.findViewById(R.id.iv_third_num);
            mIVFourth = itemView.findViewById(R.id.iv_fourth_num);
            mIVFifth = itemView.findViewById(R.id.iv_fifth_num);
            mIVSixth = itemView.findViewById(R.id.iv_sixth_num);
            mIVSeventh = itemView.findViewById(R.id.iv_seventh_num);
            mIVEighth = itemView.findViewById(R.id.iv_eighth_num);
            mIVNinth = itemView.findViewById(R.id.iv_ninth_num);
            mIVTenth = itemView.findViewById(R.id.iv_tenth_num);
            mIVEleventh = itemView.findViewById(R.id.iv_eleveth_num);
        }
    }

    class BasicTwoViewHolder extends RecyclerView.ViewHolder {
        private TextView mTVInfo, mTVFirstCount, mTVSecondCount, mTVThirdCount, mTVFourthCount, mTVFifthCount,
                mTVSixthCount, mTVSeventhCount, mTVEighthCount, mTVNinthCount, mTVTenthCount, mTVEleventhCount;
        private LinearLayout mLLTwoBG;

        BasicTwoViewHolder(View itemView) {
            super(itemView);
            mLLTwoBG = itemView.findViewById(R.id.ll_item_two);
            mTVInfo = itemView.findViewById(R.id.tv_basic_item_info);
            mTVFirstCount = itemView.findViewById(R.id.tv_first_count);
            mTVSecondCount = itemView.findViewById(R.id.tv_second_count);
            mTVThirdCount = itemView.findViewById(R.id.tv_third_count);
            mTVFourthCount = itemView.findViewById(R.id.tv_fourth_count);
            mTVFifthCount = itemView.findViewById(R.id.tv_fifth_count);
            mTVSixthCount = itemView.findViewById(R.id.tv_sixth_count);
            mTVSeventhCount = itemView.findViewById(R.id.tv_seventh_count);
            mTVEighthCount = itemView.findViewById(R.id.tv_eighth_count);
            mTVNinthCount = itemView.findViewById(R.id.tv_ninth_count);
            mTVTenthCount = itemView.findViewById(R.id.tv_tenth_count);
            mTVEleventhCount = itemView.findViewById(R.id.tv_eleventh_count);
        }
    }

    class BottomEmptyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLLEmpty;

        public BottomEmptyViewHolder(View itemView) {
            super(itemView);
            this.mLLEmpty = itemView.findViewById(R.id.ll_bottom_empty);
        }
    }

}
