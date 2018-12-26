package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.home.bean.FormTrendBean;

public class FormTrendAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<FormTrendBean> datas;
    private LayoutInflater inflater;
    private final int TYPE_ONE = 1;
    private final int TYPE_TWO = 2;
    private final int TYPE_THREE = 3;
    private String playType;
    private boolean isShowEmptyView;
    private boolean isShowMissNum = true;
    private boolean isHideLastFourth = false;
    private int dataSize = 0;


    public FormTrendAdapter(Context context, String type){
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.playType = type;
    }

    public void setDataFun(List<FormTrendBean> data ){
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
        if(TextUtils.equals("doubleSame",playType)){
            if(viewType == TYPE_ONE){
                View formView = inflater.inflate(R.layout.num_dis_item, parent, false);
                DisOneViewHolder disOneHolder = new DisOneViewHolder(formView);
                return disOneHolder;
            }else if(viewType == TYPE_THREE){
                View view = inflater.inflate(R.layout.bottom_empty_item_view, parent, false);
                return new BottomEmptyViewHolder(view);
            }else{
                View formViewTwo = inflater.inflate(R.layout.num_dis_two_item, parent, false);
                DisTwoViewHolder disTwoHolder = new DisTwoViewHolder(formViewTwo);
                return disTwoHolder;
            }
        }else{
            if(viewType == TYPE_ONE){
                View formView = inflater.inflate(R.layout.form_trend_item, parent, false);
                FormViewHolder formViewHolder = new FormViewHolder(formView);
                return formViewHolder;
            }else if(viewType == TYPE_THREE){
                View view = inflater.inflate(R.layout.bottom_empty_item_view, parent, false);
                return new BottomEmptyViewHolder(view);
            }else{
                View formViewTwo = inflater.inflate(R.layout.form_trend_item_two, parent, false);
                TotalViewHolder totalViewHolder = new TotalViewHolder(formViewTwo);
                return totalViewHolder;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(TextUtils.equals("doubleSame",playType)){
            if(holder instanceof DisOneViewHolder){
                FormTrendBean formBean = datas.get(position);
                DisOneViewHolder disOneHolder = (DisOneViewHolder) holder;

                if(position % 2 == 0){
                    disOneHolder.mLLBg.setBackground(mContext.getResources().getDrawable(R.color.color_white));
                    disOneHolder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_white));
                }else{
                    disOneHolder.mLLBg.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
                    disOneHolder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
                }
                TextView[] textViews = {disOneHolder.mTVFirst, disOneHolder.mTVSecond, disOneHolder.mTVThird,
                        disOneHolder.mTVFourth, disOneHolder.mTVFifth, disOneHolder.mTVSixth};
                String[] types = {"11", "22", "33", "44", "55", "66"};

                if(!TextUtils.isEmpty(formBean.getTimes())) {
                    disOneHolder.mLLBg.setVisibility(View.VISIBLE);
                    disOneHolder.mLLWaitNum.setVisibility(View.GONE);
                    disOneHolder.mTVPeriod.setText(formBean.getPeriod());
                    disOneHolder.mTVNums.setText(formBean.getNumbers());
                    String awardNums="";
                    if(!TextUtils.equals("等待开奖",formBean.getNumbers())){
                        awardNums = formBean.getNumbers().replace(" ","");
                    }
                    String[] times = formBean.getTimes().split(",");
                    for (int i = 0; i < times.length; i++) {
                        if(awardNums.contains(types[i])){
                            textViews[i].setText(types[i]);
                            setBG(textViews[i], R.drawable.sum_baisc_trend_bg, R.color.color_white);
                        }else{
                            if(isShowMissNum){
                                textViews[i].setText(times[i]);
                            }else{
                                textViews[i].setText("");
                            }
                            setBG(textViews[i], R.color.transparent, R.color.color_504f58);
                        }
                        /*if(0 == Integer.parseInt(times[i])){
                            textViews[i].setText(types[i]);
                            setBG(textViews[i], R.drawable.sum_baisc_trend_bg, R.color.color_white);
                        }else{
                            textViews[i].setText(times[i]);
                        }*/
                    }
                }else{
                    disOneHolder.mLLBg.setVisibility(View.GONE);
                    disOneHolder.mLLWaitNum.setVisibility(View.VISIBLE);
                    disOneHolder.mTVWaitPeriod.setText(formBean.getPeriod());
                }

            }else if(holder instanceof DisTwoViewHolder){
                DisTwoViewHolder disTwoHolder = (DisTwoViewHolder) holder;
                String[] textInfos = {"出现次数","平均遗漏","最大遗漏","最大连出"};

                if(position % 2 == 0){
                    disTwoHolder.mLLDisItem.setBackground(mContext.getResources().getDrawable(R.color.color_white));
                }else{
                    disTwoHolder.mLLDisItem.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
                }
                if(position == datas.size() - 4){
                    changeTextColor(mContext.getResources().getColor(R.color.color_ae68c6), disTwoHolder.mTVInfo, disTwoHolder.mTVFirst,
                            disTwoHolder.mTVsecond, disTwoHolder.mTVThird, disTwoHolder.mTVFourth, disTwoHolder.mTVfifth, disTwoHolder.mTVSixth);
                    disTwoHolder.mTVInfo.setText(textInfos[0]);
                }else if(position == datas.size() - 3){
                    changeTextColor(mContext.getResources().getColor(R.color.color_43bf8a), disTwoHolder.mTVInfo, disTwoHolder.mTVFirst,
                            disTwoHolder.mTVsecond, disTwoHolder.mTVThird, disTwoHolder.mTVFourth, disTwoHolder.mTVfifth, disTwoHolder.mTVSixth);
                    disTwoHolder.mTVInfo.setText(textInfos[1]);
                }else if(position == datas.size() - 2){
                    changeTextColor(mContext.getResources().getColor(R.color.color_f1973a), disTwoHolder.mTVInfo, disTwoHolder.mTVFirst,
                            disTwoHolder.mTVsecond, disTwoHolder.mTVThird, disTwoHolder.mTVFourth, disTwoHolder.mTVfifth, disTwoHolder.mTVSixth);
                    disTwoHolder.mTVInfo.setText(textInfos[2]);
                }else if(position == datas.size() - 1){
                    changeTextColor(mContext.getResources().getColor(R.color.color_24c4cb), disTwoHolder.mTVInfo, disTwoHolder.mTVFirst,
                            disTwoHolder.mTVsecond, disTwoHolder.mTVThird, disTwoHolder.mTVFourth, disTwoHolder.mTVfifth, disTwoHolder.mTVSixth);
                    disTwoHolder.mTVInfo.setText(textInfos[3]);
                }
                if(position == datas.size() - 4){
                    disTwoHolder.divView.setVisibility(View.VISIBLE);
                }else{
                    disTwoHolder.divView.setVisibility(View.GONE);
                }

                TextView[] textViews = {disTwoHolder.mTVFirst, disTwoHolder.mTVsecond, disTwoHolder.mTVThird, disTwoHolder.mTVFourth,
                        disTwoHolder.mTVfifth,disTwoHolder.mTVSixth};
                FormTrendBean bean2 = datas.get(position);
                String[] missTimes = bean2.getTimes().split(",");
                for (int i = 0; i < missTimes.length; i++) {
                    textViews[i].setText(missTimes[i]);
                }

                disTwoHolder.mTVInfo.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
                if(isHideLastFourth){
                    disTwoHolder.mLLDisBg.setVisibility(View.GONE);
                }else{
                    disTwoHolder.mLLDisBg.setVisibility(View.VISIBLE);
                }
            }else if(holder instanceof BottomEmptyViewHolder){
                BottomEmptyViewHolder emptyHolder = (BottomEmptyViewHolder) holder;
                if(isShowEmptyView){
                    emptyHolder.mLLEmpty.setVisibility(View.VISIBLE);
                }else{
                    emptyHolder.mLLEmpty.setVisibility(View.GONE);
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) emptyHolder.mLLShow.getLayoutParams();
                params.weight = 3f;
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) emptyHolder.mLLShow.getLayoutParams();
                params2.weight = 6f;
                if(isHideLastFourth){
                    emptyHolder.mLLEmpty.setVisibility(View.GONE);
                }else{
                    emptyHolder.mLLEmpty.setVisibility(View.VISIBLE);
                }
            }

        }else{
            if(holder instanceof FormViewHolder){
                FormTrendBean trendBean = datas.get(position);
                FormViewHolder formHolder = (FormViewHolder) holder;

                if(position % 2 == 0){
                    formHolder.mLLTrend.setBackground(mContext.getResources().getDrawable(R.color.color_white));
                    formHolder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_white));
                }else{
                    formHolder.mLLTrend.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
                    formHolder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
                }

                TextView[] textViews = {formHolder.mTVThreeSame, formHolder.mTVThreeDiffer, formHolder.mTVDoubleSame, formHolder.mTVDoubleDiffer};
                int[] drawables = {R.drawable.form_trend_bg1, R.drawable.form_trend_bg2, R.drawable.form_trend_bg3, R.drawable.form_trend_bg4};
                String[] types = {"三同号", "三不同", "二同号", "二不同"};

                if(!TextUtils.isEmpty(trendBean.getTimes())){
                    formHolder.mLLTrend.setVisibility(View.VISIBLE);
                    formHolder.mLLWaitNum.setVisibility(View.GONE);
                    formHolder.mTVPeriod.setText(trendBean.getPeriod());
                    formHolder.mTVNums.setText(trendBean.getNumbers());
                    String[] times = trendBean.getTimes().split(",");
                    for (int i = 0; i < times.length; i++) {
                        if(0 == Integer.parseInt(times[i])){
                            textViews[i].setText(types[i]);
                            setBG(textViews[i], drawables[i], R.color.color_white);
                        }else{
                            if(isShowMissNum){
                                textViews[i].setText(times[i]);
                            }else{
                                textViews[i].setText("");
                            }
                            setBG(textViews[i], R.color.transparent, R.color.color_504f58);
                        }
                    }
                }else{
                    formHolder.mLLTrend.setVisibility(View.GONE);
                    formHolder.mLLWaitNum.setVisibility(View.VISIBLE);
                    formHolder.mTVWaitPeriod.setText(trendBean.getPeriod());
                }
            }else if(holder instanceof TotalViewHolder){
                TotalViewHolder totalHolder = (TotalViewHolder) holder;
                String[] textInfos = {"出现次数","平均遗漏","最大遗漏","最大连出"};

                if(position % 2 == 0){
                    totalHolder.mLLBG.setBackground(mContext.getResources().getDrawable(R.color.color_white));
                }else{
                    totalHolder.mLLBG.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
                }
                if(position == datas.size()-4){
                    changeTextColor(mContext.getResources().getColor(R.color.color_ae68c6), totalHolder.mTVInfo, totalHolder.mTVThreeSameTime,
                            totalHolder.mTVThreeDifferTime, totalHolder.mTVDoubleSameTime, totalHolder.mTVDoubleDifferTime);
                    totalHolder.mTVInfo.setText(textInfos[0]);
                }else if(position == datas.size()-3){
                    changeTextColor(mContext.getResources().getColor(R.color.color_43bf8a), totalHolder.mTVInfo, totalHolder.mTVThreeSameTime,
                            totalHolder.mTVThreeDifferTime, totalHolder.mTVDoubleSameTime, totalHolder.mTVDoubleDifferTime);
                    totalHolder.mTVInfo.setText(textInfos[1]);
                }else if(position == datas.size()-2){
                    changeTextColor(mContext.getResources().getColor(R.color.color_f1973a), totalHolder.mTVInfo, totalHolder.mTVThreeSameTime,
                            totalHolder.mTVThreeDifferTime, totalHolder.mTVDoubleSameTime, totalHolder.mTVDoubleDifferTime);
                    totalHolder.mTVInfo.setText(textInfos[2]);
                }else if(position == datas.size()-1){
                    changeTextColor(mContext.getResources().getColor(R.color.color_24c4cb), totalHolder.mTVInfo, totalHolder.mTVThreeSameTime,
                            totalHolder.mTVThreeDifferTime, totalHolder.mTVDoubleSameTime, totalHolder.mTVDoubleDifferTime);
                    totalHolder.mTVInfo.setText(textInfos[3]);
                }
                if(position == datas.size() - 4){
                    totalHolder.divView.setVisibility(View.VISIBLE);
                }else{
                    totalHolder.divView.setVisibility(View.GONE);
                }

                TextView[] textViews = {totalHolder.mTVThreeSameTime, totalHolder.mTVThreeDifferTime, totalHolder.mTVDoubleSameTime, totalHolder.mTVDoubleDifferTime};
                FormTrendBean bean2 = datas.get(position);
                String[] missTimes = bean2.getTimes().split(",");
                for (int i = 0; i < missTimes.length; i++) {
                    textViews[i].setText(missTimes[i]);
                }
                totalHolder.mTVInfo.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
                if(isHideLastFourth){
                    totalHolder.mLLBG.setVisibility(View.GONE);
                }else{
                    totalHolder.mLLBG.setVisibility(View.VISIBLE);
                }
            }else if(holder instanceof BottomEmptyViewHolder){
                BottomEmptyViewHolder emptyHolder = (BottomEmptyViewHolder) holder;
                if(isShowEmptyView){
                    emptyHolder.mLLEmpty.setVisibility(View.VISIBLE);
                }else{
                    emptyHolder.mLLEmpty.setVisibility(View.GONE);
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) emptyHolder.mLLShow.getLayoutParams();
                params.weight = 2.5f;
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) emptyHolder.mLLShow.getLayoutParams();
                params2.weight = 4f;
                if(isHideLastFourth){
                    emptyHolder.mLLEmpty.setVisibility(View.GONE);
                }else{
                    emptyHolder.mLLEmpty.setVisibility(View.VISIBLE);
                }
            }

        }

    }

    //设置字体颜色
    public void changeTextColor(int color, TextView... textViews){
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setTextColor(color);
        }
    }

    //设置背景
    public void setBG(TextView textView, int bgColor, int textColor){
        textView.setBackground(mContext.getResources().getDrawable(bgColor));
        textView.setTextColor(mContext.getResources().getColor(textColor));
    }


    @Override
    public int getItemCount() {
        /*if(isHideLastFourth){
            if(isShowEmptyView){
                isShowEmptyView = false;
            }else{
                for (int i = 0; i < 4; i++) {
                    datas.remove(datas.size()-1);
                }
            }
        }
        return isShowEmptyView ? datas.size()+1 : datas.size();*/
        return dataSize;
    }

    @Override
    public int getItemViewType(int position) {
        if(position < dataSize - 4){
            return TYPE_ONE;
        }else if(position  == datas.size()){
            return TYPE_THREE;
        }else{
            int type = isShowEmptyView ? TYPE_ONE : TYPE_TWO;
            if(isHideLastFourth){
                type = TYPE_ONE;
            }
            return type;
        }
    }

    class FormViewHolder extends RecyclerView.ViewHolder{
        TextView mTVPeriod, mTVNums, mTVThreeSame, mTVThreeDiffer,
                    mTVDoubleSame, mTVDoubleDiffer, mTVWaitPeriod, mTVWaitText;
        LinearLayout mLLTrend, mLLWaitNum;

        public FormViewHolder(View itemView) {
            super(itemView);
            this.mLLTrend = itemView.findViewById(R.id.ll_form_trend_item);
            this.mTVPeriod = itemView.findViewById(R.id.tv_form_trend_period);
            this.mTVNums = itemView.findViewById(R.id.tv_form_trend_nums);
            this.mTVThreeSame = itemView.findViewById(R.id.tv_three_num_same);
            this.mTVThreeDiffer = itemView.findViewById(R.id.tv_three_num_differ);
            this.mTVDoubleSame = itemView.findViewById(R.id.tv_double_num_same);
            this.mTVDoubleDiffer = itemView.findViewById(R.id.tv_double_num_differ);
            mLLWaitNum = itemView.findViewById(R.id.ll_wait_num);
            mTVWaitPeriod = itemView.findViewById(R.id.tv_wait_item_period);
            mTVWaitText = itemView.findViewById(R.id.tv_wait_num_text);
        }
    }

    class TotalViewHolder extends RecyclerView.ViewHolder{
        private TextView mTVInfo, mTVThreeSameTime, mTVThreeDifferTime,
                mTVDoubleSameTime, mTVDoubleDifferTime;
        private View divView;
        private LinearLayout mLLBG;

        public TotalViewHolder(View itemView) {
            super(itemView);
            this.mTVInfo = itemView.findViewById(R.id.tv_form_trend_info);
            this.mTVThreeSameTime = itemView.findViewById(R.id.tv_three_num_same_two);
            this.mTVThreeDifferTime = itemView.findViewById(R.id.tv_three_num_differ_two);
            this.mTVDoubleSameTime = itemView.findViewById(R.id.tv_double_num_same_two);
            this.mTVDoubleDifferTime = itemView.findViewById(R.id.tv_double_num_differ_two);
            this.divView = itemView.findViewById(R.id.view_form_div);
            this.mLLBG = itemView.findViewById(R.id.ll_form_item_two);
        }
    }

    class DisOneViewHolder extends RecyclerView.ViewHolder{
        TextView mTVPeriod, mTVNums, mTVFirst, mTVSecond,
                mTVThird, mTVFourth, mTVFifth, mTVSixth, mTVWaitPeriod, mTVWaitText;
        LinearLayout mLLBg, mLLWaitNum;
        public DisOneViewHolder(View itemView) {
            super(itemView);
            this.mLLBg = itemView.findViewById(R.id.ll_dis_one_bg);
            this.mTVPeriod = itemView.findViewById(R.id.tv_dis_period);
            this.mTVNums = itemView.findViewById(R.id.tv_dis_nums);
            this.mTVFirst = itemView.findViewById(R.id.tv_dis_first);
            this.mTVSecond = itemView.findViewById(R.id.tv_dis_second);
            this.mTVThird = itemView.findViewById(R.id.tv_dis_third);
            this.mTVFourth = itemView.findViewById(R.id.tv_dis_fourth);
            this.mTVFifth = itemView.findViewById(R.id.tv_dis_fifth);
            this.mTVSixth = itemView.findViewById(R.id.tv_dis_sixth);
            mLLWaitNum = itemView.findViewById(R.id.ll_wait_num);
            mTVWaitPeriod = itemView.findViewById(R.id.tv_wait_item_period);
            mTVWaitText = itemView.findViewById(R.id.tv_wait_num_text);
        }
    }

    class DisTwoViewHolder extends RecyclerView.ViewHolder{
        TextView mTVInfo, mTVFirst, mTVsecond, mTVThird,
                mTVFourth, mTVfifth, mTVSixth;
        View divView;
        LinearLayout mLLDisItem, mLLDisBg;

        public DisTwoViewHolder(View itemView) {
            super(itemView);
            this.divView = itemView.findViewById(R.id.view_dis_div);
            this.mLLDisItem = itemView.findViewById(R.id.ll_dis_item_two);
            this.mTVInfo = itemView.findViewById(R.id.tv_dis_trend_info);
            this.mTVFirst = itemView.findViewById(R.id.tv_dis_two_first);
            this.mTVsecond = itemView.findViewById(R.id.tv_dis_two_second);
            this.mTVThird = itemView.findViewById(R.id.tv_dis_two_third);
            this.mTVFourth = itemView.findViewById(R.id.tv_dis_two_fourth);
            this.mTVfifth = itemView.findViewById(R.id.tv_dis_two_fifth);
            this.mTVSixth = itemView.findViewById(R.id.tv_dis_two_sixth);
            this.mLLDisBg = itemView.findViewById(R.id.ll_dis_bg);
        }
    }

    class BottomEmptyViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout mRLWait;
        LinearLayout mLLEmpty, mLLShow;
        public BottomEmptyViewHolder(View itemView) {
            super(itemView);
            this.mLLEmpty = itemView.findViewById(R.id.ll_bottom_empty);
            this.mLLShow = itemView.findViewById(R.id.ll_empty_show_text);
            this.mRLWait = itemView.findViewById(R.id.rl_empty_show_wait);
        }
    }
}
