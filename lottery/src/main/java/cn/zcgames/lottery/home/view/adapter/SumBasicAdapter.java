package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.BasicTrendBean;

public class SumBasicAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private final int TYPE_ONE = 1;
    private final int TYPE_TWO = 2;
    private final int TYPE_THREE = 3;
    private LayoutInflater inflater;
    private List<BasicTrendBean> datas;
    private int firstRepeatCount;
    private boolean isSelected;
    private boolean isShowEmptyView;
    private boolean isShowMissNum = true;
    private boolean isHideLastFourth = false;
    private int dataSize = 0;

    public SumBasicAdapter(Context context){
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setDataFun(List<BasicTrendBean> data ){
        this.datas = data;
    }

    public void setDataSize(int size){
        dataSize = size;
    }

    public void showLastView(boolean isShow){
        isShowEmptyView = isShow;
    }

    public void showMissNum(boolean isShow){
        isShowMissNum = isShow;
    }

    public void hideLastFourth(boolean isShow){
        isHideLastFourth = isShow;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ONE){
            View view = inflater.inflate(R.layout.sum_basic_item, parent, false);
            return new BasicViewHolder(view);
        }else if(viewType == TYPE_THREE){
            View view = inflater.inflate(R.layout.bottom_empty_item_view, parent, false);
            return new BottomEmptyViewHolder(view);
        }else{
            View view = inflater.inflate(R.layout.sum_basic_item_two, parent, false);
            return new BasicTwoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BasicViewHolder){
            BasicViewHolder basicHolder = (BasicViewHolder) holder;
           /* int realPosition = 0;
            if(isShowEmptyView){
                realPosition = position - 1;
            }else{
                realPosition = position;
            }*/
            if(position % 2 == 0){
                basicHolder.mLLBg.setBackground(mContext.getResources().getDrawable(R.color.color_white));
                basicHolder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_white));
            }else{
                basicHolder.mLLBg.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
                basicHolder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
            }
            //中奖号码

            BasicTrendBean bean = datas.get(position);
            basicHolder.mTVOpenPeriods.setText(bean.periods);
            if(TextUtils.equals("等待开奖", bean.numbers)){
                basicHolder.mLLWaitNum.setVisibility(View.VISIBLE);
                basicHolder.mLLBg.setVisibility(View.GONE);
                basicHolder.mTVWaitPeriod.setText(bean.periods);
//                basicHolder.mTVWaitText.setText(bean.numbers);
            }else{
                basicHolder.mLLWaitNum.setVisibility(View.GONE);
                basicHolder.mLLBg.setVisibility(View.VISIBLE);
                if(bean.numbers != null){
                    String[] numbers = bean.numbers.split(",");
                    basicHolder.mTVAwardNum.setText(bean.numbers.replace(","," "));
                    if(0 == bean.sum){
                        basicHolder.mTVAwardSum.setText("");
                    }else{
                        basicHolder.mTVAwardSum.setText(String.valueOf(bean.sum));
                    }
                    if((0 == numbers.length) && (0 == bean.differ)){
                        basicHolder.mTVAwardDiffer.setText("");
                    }else{
                        basicHolder.mTVAwardDiffer.setText(String.valueOf(bean.differ));
                    }

                    TextView[] textViews = {basicHolder.mTVFirstNum, basicHolder.mTVSecondNum, basicHolder.mTVThirdNum, basicHolder.mTVFourthNum,
                            basicHolder.mTVFifthNum, basicHolder.mTVSixthNum};
                    ImageView[] imageViews = {basicHolder.mIVFirst, basicHolder.mIVSecond, basicHolder.mIVThird, basicHolder.mIVFourth,
                            basicHolder.mIVFifth, basicHolder.mIVSixth};
                    //设置数据
                    if(!TextUtils.isEmpty(bean.singleNumbers)){
                        String[] missTimes = bean.singleNumbers.split(",");
                        for (int i = 0; i < missTimes.length; i++) {
                            if(isShowMissNum){
                                textViews[i].setText(missTimes[i]);
                            }else{
                                textViews[i].setText("");
                            }
                        }

                        //设置中奖情况
                        for (int i = 0; i < missTimes.length; i++) {
                            for (int j = 0; j < numbers.length; j++) {
                                if(i+1 == Integer.parseInt(numbers[j])){
                                    firstRepeatCount++;
                                    isSelected = true;
                                    textViews[i].setText(numbers[j]);
                                }
                            }
                            if(isSelected){
                                setBG(textViews[i], R.drawable.sum_baisc_trend_bg, R.color.color_white);
                            }else{
                                setBG(textViews[i], R.color.transparent, R.color.color_a29e9a);
                            }
                            setRightIcon(imageViews[i], firstRepeatCount);
                            firstRepeatCount = 0;
                            isSelected = false;
                        }
                    }
                }else{
                    bean.numbers = "等待开奖";
                }

            }

        }else if(holder instanceof BasicTwoViewHolder){
            /*if(isShowEmptyView){
                return;
            }*/
            BasicTwoViewHolder basicTwoHolder = (BasicTwoViewHolder) holder;
            if(position % 2 == 0){
                basicTwoHolder.mLLTwoBG.setBackground(mContext.getResources().getDrawable(R.color.color_white));
            }else{
                basicTwoHolder.mLLTwoBG.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
            }
            if(position == datas.size() -4){
                basicTwoHolder.mTVInfo.setText("出现次数");
                changeTextColor(mContext.getResources().getColor(R.color.color_ae68c6), basicTwoHolder.mTVInfo, basicTwoHolder.mTVFirstCount,
                        basicTwoHolder.mTVSecondCount, basicTwoHolder.mTVThirdCount, basicTwoHolder.mTVFourthCount, basicTwoHolder.mTVFifthCount, basicTwoHolder.mTVSixthCount);
            }else if(position == datas.size() -3){
                basicTwoHolder.mTVInfo.setText("平均遗漏");
                changeTextColor(mContext.getResources().getColor(R.color.color_43bf8a), basicTwoHolder.mTVInfo, basicTwoHolder.mTVFirstCount,
                        basicTwoHolder.mTVSecondCount, basicTwoHolder.mTVThirdCount, basicTwoHolder.mTVFourthCount, basicTwoHolder.mTVFifthCount, basicTwoHolder.mTVSixthCount);
            }else if(position == datas.size() -2){
                basicTwoHolder.mTVInfo.setText("最大遗漏");
                changeTextColor(mContext.getResources().getColor(R.color.color_f1973a), basicTwoHolder.mTVInfo, basicTwoHolder.mTVFirstCount,
                        basicTwoHolder.mTVSecondCount, basicTwoHolder.mTVThirdCount, basicTwoHolder.mTVFourthCount, basicTwoHolder.mTVFifthCount, basicTwoHolder.mTVSixthCount);
            }else if(position == datas.size() -1){
                basicTwoHolder.mTVInfo.setText("最大连出");
                changeTextColor(mContext.getResources().getColor(R.color.color_24c4cb), basicTwoHolder.mTVInfo, basicTwoHolder.mTVFirstCount,
                        basicTwoHolder.mTVSecondCount, basicTwoHolder.mTVThirdCount, basicTwoHolder.mTVFourthCount, basicTwoHolder.mTVFifthCount, basicTwoHolder.mTVSixthCount);
            }
            TextView[] textViews = {basicTwoHolder.mTVFirstCount, basicTwoHolder.mTVSecondCount, basicTwoHolder.mTVThirdCount, basicTwoHolder.mTVFourthCount,
                    basicTwoHolder.mTVFifthCount, basicTwoHolder.mTVSixthCount};
            BasicTrendBean bean2 = datas.get(position);
            String[] missTimes = bean2.singleNumbers.split(",");
            for (int i = 0; i < missTimes.length; i++) {
                textViews[i].setText(missTimes[i]);
            }
            if(isHideLastFourth){
                basicTwoHolder.mLLTwoBG.setVisibility(View.GONE);
            }else{
                basicTwoHolder.mLLTwoBG.setVisibility(View.VISIBLE);
            }

        }else if(holder instanceof BottomEmptyViewHolder){
            BottomEmptyViewHolder emptyHolder = (BottomEmptyViewHolder) holder;
            if(isShowEmptyView){
                emptyHolder.mLLEmpty.setVisibility(View.VISIBLE);
            }else{
                emptyHolder.mLLEmpty.setVisibility(View.GONE);
            }
            if(isHideLastFourth){
                emptyHolder.mLLEmpty.setVisibility(View.GONE);
            }else{
                emptyHolder.mLLEmpty.setVisibility(View.VISIBLE);
            }
        }

    }


    //设置字体颜色
    private void changeTextColor(int color, TextView... textViews){
        for (TextView textView : textViews) {
            textView.setTextColor(color);
        }
    }

    //设置背景
    private void setBG(TextView textView, int bgColor, int textColor){
        textView.setBackground(mContext.getResources().getDrawable(bgColor));
        textView.setTextColor(mContext.getResources().getColor(textColor));
    }

    //设置右上角标
    private void setRightIcon(ImageView imageView, int num){
        if(num == 2){
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.double_same_num));
        }else if(num == 3){
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.three_same_num));
        }else{
            imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {

//        Log.d("111111", "条目数量==="+dataSize);
        return dataSize;
    }

    @Override
    public int getItemViewType(int position) {
//        Log.d("111111", "类型");

        if(position < datas.size() - 4){
            return TYPE_ONE;
        }else if(position == datas.size()){
            return TYPE_THREE;
        }else{
            int type = isShowEmptyView ? TYPE_ONE : TYPE_TWO;
            if(isHideLastFourth){
                type = TYPE_ONE;
            }
            return type;
        }
    }



    class BasicViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout mLLBg, mLLWaitNum;
        private TextView mTVOpenPeriods, mTVAwardNum, mTVAwardSum, mTVAwardDiffer,
                        mTVFirstNum, mTVSecondNum, mTVThirdNum, mTVFourthNum, mTVFifthNum, mTVSixthNum, mTVWaitPeriod, mTVWaitText;
        private ImageView mIVFirst, mIVSecond, mIVThird, mIVFourth, mIVFifth, mIVSixth;
        public BasicViewHolder(View itemView) {
            super(itemView);
            mLLBg = itemView.findViewById(R.id.ll_sum_basic_item);
            mTVOpenPeriods = itemView.findViewById(R.id.tv_basic_item_periods);
            mTVAwardNum = itemView.findViewById(R.id.tv_award_nums);
            mTVAwardSum = itemView.findViewById(R.id.tv_awards_sum);
            mTVAwardDiffer = itemView.findViewById(R.id.tv_awards_num_differ);
            mTVFirstNum = itemView.findViewById(R.id.tv_first_num);
            mTVSecondNum = itemView.findViewById(R.id.tv_second_num);
            mTVThirdNum = itemView.findViewById(R.id.tv_third_num);
            mTVFourthNum = itemView.findViewById(R.id.tv_fourth_num);
            mTVFifthNum = itemView.findViewById(R.id.tv_fifth_num);
            mTVSixthNum = itemView.findViewById(R.id.tv_sixth_num);
            mIVFirst = itemView.findViewById(R.id.iv_first_num);
            mIVSecond = itemView.findViewById(R.id.iv_second_num);
            mIVThird = itemView.findViewById(R.id.iv_third_num);
            mIVFourth = itemView.findViewById(R.id.iv_fourth_num);
            mIVFifth = itemView.findViewById(R.id.iv_fifth_num);
            mIVSixth = itemView.findViewById(R.id.iv_sixth_num);
            mLLWaitNum = itemView.findViewById(R.id.ll_wait_num);
            mTVWaitPeriod = itemView.findViewById(R.id.tv_wait_item_period);
            mTVWaitText = itemView.findViewById(R.id.tv_wait_num_text);
        }
    }

    class BasicTwoViewHolder extends RecyclerView.ViewHolder{
        private TextView mTVInfo, mTVFirstCount, mTVSecondCount, mTVThirdCount,
                        mTVFourthCount, mTVFifthCount, mTVSixthCount;
        private LinearLayout mLLTwoBG;

        public BasicTwoViewHolder(View itemView) {
            super(itemView);
            mLLTwoBG = itemView.findViewById(R.id.ll_item_two);
            mTVInfo = itemView.findViewById(R.id.tv_basic_item_info);
            mTVFirstCount = itemView.findViewById(R.id.tv_first_count);
            mTVSecondCount = itemView.findViewById(R.id.tv_second_count);
            mTVThirdCount = itemView.findViewById(R.id.tv_third_count);
            mTVFourthCount = itemView.findViewById(R.id.tv_fourth_count);
            mTVFifthCount = itemView.findViewById(R.id.tv_fifth_count);
            mTVSixthCount = itemView.findViewById(R.id.tv_sixth_count);
        }
    }

    class BottomEmptyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout mLLEmpty;
        public BottomEmptyViewHolder(View itemView) {
            super(itemView);
            this.mLLEmpty = itemView.findViewById(R.id.ll_bottom_empty);
        }
    }

}
