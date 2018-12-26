package cn.zcgames.lottery.home.view.fragment.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.nfc.Tag;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.berfy.sdk.mvpbase.util.DeviceUtils;
import cn.zcgames.lottery.home.bean.TrendData;
import cn.zcgames.lottery.model.local.LotteryUtils;

public class DDTrendChart extends ATrendChart {
    private static final boolean DEBUG = false;
    private static final String TAG = "DDTrendChart";
    final int mDefDLTBlueCount = 12;
    final int mDefDLTRedCount = 35;
    final int mDefSSQBlueCount = 16;
    final int mDefSSQRedCount = 33;
    final int mMaxSignleNum = 9;
    private int blueCount = 16;
    private boolean mDrawLine = true;
    private String mTrendViewType;
    private Path mPathPoint = new Path();
    //    private Path jPathPoint = new Path();//奇偶
    //    private Path zPathPoint = new Path();//质合
    //    private Path lPathPoint = new Path();//0,1,2路
    private TreeSet<Integer> mSelectedBlue = new TreeSet();
    private TreeSet<Integer> mSelectedRed = new TreeSet();
    private ISelectedChangeListener mSelectedChangeListener;
    private boolean mShowYilou = true;
    private ArrayList<TrendData> mTrendData;
    private int redCount = 0;
    private String titles;
    protected String[] titleCount = new String[]{};
    private List<String> selectDatas = new ArrayList<>();
    private boolean hideLastFourth = false;
    private int leftOneCount = 0;
    private int leftSecondCount = 0;
    private Context mContext;
    //画笔颜色
    private List<Integer> paintColors = new ArrayList<>();
    private int selectSize;//形态分类个数
    private int colorPos = 0;
    private boolean mIsShowTitle = true;//是否显示标题

    public interface ISelectedChangeListener {
        void onSelectedChange(TreeSet<Integer> treeSet, TreeSet<Integer> treeSet2);
    }

    public DDTrendChart(Context context, LottoTrendView lottoTrendView) {
        super(context, lottoTrendView);
        mContext = context;
        this.mPaint.setTextAlign(Align.CENTER);
    }

    public DDTrendChart(Context context, LottoTrendView lottoTrendView, int xItemWidth, int leftTitleItemWidth, int leftSecondWidth) {
        super(context, lottoTrendView, xItemWidth, leftTitleItemWidth, leftSecondWidth);
        mContext = context;
        this.mPaint.setTextAlign(Align.CENTER);
    }

    public DDTrendChart(Context context, LottoTrendView lottoTrendView, int xItemWidth, int leftTitleItemWidth,
                        int leftSecondWidth, List<Integer> paintColors) {
        super(context, lottoTrendView, xItemWidth, leftTitleItemWidth, leftSecondWidth);
        mContext = context;
        if (paintColors != null && paintColors.size() > 0) {
            formPaths.clear();
            this.paintColors = paintColors;
            selectSize = paintColors.size();
            for (int i = 0; i < selectSize; i++) {
                FormPath selectPath = new FormPath(new Path(), new Paint(i));
                formPaths.add(selectPath);
            }
        }
        this.mPaint.setTextAlign(Align.CENTER);
    }

    public void setBlueCount(int blueCount) {
        this.blueCount = blueCount;
    }

    //绘制的颜色
    public void setLineColor(int drawColor) {
        this.mCBallBlue = drawColor;
    }

    public void setAddCountString(String[] names) {
        if (null == names) {
            this.titleCount = new String[]{};
        } else {
            this.titleCount = names;
        }
        if (this.titleCount.length > 1) {
            leftOneCount = 1;
            leftSecondCount = 1;
        } else if (this.titleCount.length == 1) {
            leftOneCount = 1;
            leftSecondCount = 0;
        } else {
            leftOneCount = 0;
            leftSecondCount = 0;
        }
    }

    public void setXTitles(String title) {
        this.titles = title;
    }

    public void setSmallGridWidth(int width) {
        this.mXItemWidth = width;
    }

    public void setSmallGridhHeight(int height) {
        this.mXItemHeight = height;
    }

    public void setBigGridhWidth(int addWidth) {
        this.mAddItemWidth = addWidth;
    }

    public void setPeriodWidth(int width) {
        this.mYItemWidth = width;
    }

    public void setHideLastFourth(boolean hide) {
        hideLastFourth = hide;
    }

    public void updateData(String str, ArrayList<TrendData> arrayList) {
        if (arrayList != null && arrayList.size() != 0) {
            this.mTrendData = arrayList;
            setOption(str);
            for (int i = 0; i < arrayList.size(); i++) {
                TrendData trendData = arrayList.get(i);
                if ("row".equals(trendData.getType())) {
                    String[] missNum = trendData.getBlue().split(",");

                    for (int j = 0; j < missNum.length; j++) {
                        switch (mTrendViewType) {
                            case AppConstants.TREND_STYLE_BASIC:
                                if (LotteryUtils.isTrendWinNumber(missNum[j])) {
                                    //画连线
                                    int addItemWidths = mAddItemWidth * titleCount.length;
                                    float x = ((0.5f + (float) j) * (float) mXItemWidth)//遗漏数itemWidth
                                            + ((float) this.mDivWidth)//间隔
                                            + addItemWidths;//新增列宽

                                    float y = (((float) i) + 0.5f) * ((float) this.mXItemHeight);//行高

                                    if (i == 0) {
                                        this.mPathPoint.moveTo(x, y);
                                    } else {
                                        if(TextUtils.equals("等待开奖", mTrendData.get(i - 1).getBlue())){
                                            this.mPathPoint.moveTo(x, y);
                                        }else{
                                            this.mPathPoint.lineTo(x, y);
                                        }
                                    }
                                }
                                break;
                            case AppConstants.TREND_STYLE_FORM:
                                //确定起点坐标
                                int addItemWidths = mAddItemWidth * titleCount.length;
                                float x = ((0.5f + (float) j) * (float) mXItemWidth)//遗漏数itemWidth
                                        + ((float) this.mDivWidth)//间隔
                                        + addItemWidths;//新增列宽

                                float y = (((float) i) + 0.5f) * ((float) this.mXItemHeight);//行高
                                //画连线
                                if (missNum[j].equals("0")) {//起点
                                    if (paintColors != null && paintColors.size() > 0) {
                                        int color = CommonUtil.getColor(mContext, paintColors.get(colorPos));
                                        formPaths.get(colorPos).getPaint().setColor(color);
                                        if (i == 0) {
                                            formPaths.get(colorPos).getPath().moveTo(x, y);
                                        } else {
                                            if(TextUtils.equals("等待开奖", mTrendData.get(i - 1).getBlue())){
                                                formPaths.get(colorPos).getPath().moveTo(x, y);
                                            }else{
                                                formPaths.get(colorPos).getPath().lineTo(x, y);
                                            }
                                        }
                                        colorPos++;
                                        colorPos = colorPos == selectSize ? 0 : colorPos;
                                    } else {
                                        LogF.d(TAG, "没有传色值");
                                    }
                                    break;
                                }
                        }
                    }
                }
            }

            if (this.mTrendView != null) {
                initChart(this.mTrendView.getContext(), this.mTrendView.getWidth(), this.mTrendView.getHeight(), this.mTrendView.getScale());
                this.mTrendView.invalidate();
            }
            if (this.mSelectedChangeListener != null) {
                this.mSelectedChangeListener.onSelectedChange(mSelectedRed, mSelectedBlue);
            }
        }
    }

    private void setOption(String str) {
        boolean isBasic = AppConstants.TREND_STYLE_BASIC.equals(str) || AppConstants.TREND_STYLE_FORM.equals(str);
        this.mTrendViewType = isBasic ? str : AppConstants.TREND_STYLE_BASIC;
        this.mSelectedRed.clear();
        this.mSelectedBlue.clear();
        this.mPathPoint.reset();
        if (formPaths != null) {
            for (FormPath formPath : formPaths) {
                formPath.getPath().reset();
            }
        }
        this.mScaleRange = new float[]{1.0f, 1.0f};//控制缩放
    }

    public void initChart(Context context, int i, int i2, float f) {
        if (i != 0 && i2 != 0 && this.mTrendData != null && this.mTrendData.size() >= 4) {
            super.initChart(context, i, i2, f);
            if (this.mTrendView != null) {
                this.mTrendView.setNowY((float) (-this.mPicY.getHeight()));
                this.mTrendView.setNowX((float) (this.mPicY.getWidth()));
            }
        }
    }

    public void setSelectedChangeListener(ISelectedChangeListener iSelectedChangeListener) {
        this.mSelectedChangeListener = iSelectedChangeListener;
    }

    public void setDrawLine(boolean isShow) {
        if ((this.mDrawLine != isShow ? 1 : null) != null) {
            this.mDrawLine = isShow;
            if (this.mTrendData != null) {
                drawContent();
            }
            if (this.mTrendView != null) {
                this.mTrendView.invalidate();
            }
        }
        LogF.d(TAG, "设置显示line" + mDrawLine);
    }

    public void setShowYilou(boolean z) {
        if ((this.mShowYilou != z ? 1 : null) != null) {
            this.mShowYilou = z;
            if (this.mTrendData != null) {
                drawContent();
            }
            if (this.mTrendView != null) {
                this.mTrendView.invalidate();
            }
        }
    }

    public boolean onClick(MotionEvent motionEvent, float f, float f2, int i, int i2, float f3) {
        if (motionEvent.getY() <= ((float) i2) - (((float) this.mXItemHeight) * f3) || motionEvent.getX() <= ((float) this.mYItemWidth) * f3) {
            return false;
        }
        int x = (int) ((motionEvent.getX() - f) / (((float) this.mXItemWidth) * f3));
        if (x >= this.redCount) {
            x = ((int) (((motionEvent.getX() - f) - ((float) this.mDivWidth)) / (((float) this.mXItemWidth) * f3))) - this.redCount;
            if (this.mSelectedBlue.contains(x)) {
                this.mSelectedBlue.remove(x);
            } else {
                this.mSelectedBlue.add(x);
            }
            if (this.mSelectedChangeListener != null) {
                this.mSelectedChangeListener.onSelectedChange(this.mSelectedRed, this.mSelectedBlue);
            }
        } else {
            if (this.mSelectedRed.contains(x)) {
                this.mSelectedRed.remove(x);
            } else {
                this.mSelectedRed.add(x);
            }
            if (this.mSelectedChangeListener != null) {
                this.mSelectedChangeListener.onSelectedChange(this.mSelectedRed, this.mSelectedBlue);
            }
        }
        //        drawXBottom();
        return true;
    }

    //添加列  在此修改
    public void drawY() {
        if (this.mTrendData != null && this.mTrendData.size() >= 4) {
            Canvas beginRecording = this.mPicY.beginRecording(this.mYItemWidth, (this.mYItemHeight * this.mTrendData.size()) + this.mDivHeight);
            this.mPaint.setStyle(Style.FILL);
            int size = this.mTrendData.size();
            for (int i = 0; i < size; i++) {
                int i2 = i * this.mYItemHeight;
                /*if (i == size - 4) {
                    this.mRect.set(0, this.mYItemHeight * i, this.mYItemWidth, (this.mYItemHeight * i) + this.mDivHeight);
                    this.mPaint.setColor(-1);
                    beginRecording.drawRect(this.mRect, this.mPaint);
                    this.mPaint.setColor(this.mCDiv);
                    beginRecording.drawLine(0.0f, (float) i2, (float) this.mYItemWidth, (float) i2, this.mPaint);
                    beginRecording.drawLine(0.0f, (float) ((this.mDivHeight + i2) - 1), (float) this.mYItemWidth, (float) ((this.mDivHeight + i2) - 1), this.mPaint);
                    this.mRect.set(0, this.mDivHeight + i2, this.mYItemWidth+mAddItemWidth * titleCount.length, (this.mYItemHeight + i2) + this.mDivHeight);
                } else */
                if (i >= size - 4) {
                    if (hideLastFourth) {
                        //画期数
                        this.mRect.set(0, i2, this.mYItemWidth, this.mYItemHeight + i2);
                        String type1 = this.mTrendData.get(i).getType();
                        if (type1.equals("row")) {
                            if (i % 2 == 0) {
                                this.mPaint.setColor(this.mCOddY);
                            } else {
                                this.mPaint.setColor(this.mCEvenY);
                            }
                            beginRecording.drawRect(this.mRect, this.mPaint);
                            this.mPaint.setColor(Color.parseColor("#85817d"));
                        }
                        this.mPaint.setTextSize((float) this.mYTextSize);
                        String num = this.mTrendData.get(i).getPid();
                        drawText2Rect(num, beginRecording, this.mRect, this.mPaint);

                        //画分隔线
                        //                    this.mPaint.setColor(Color.parseColor("#ff0000"));
                        this.mPaint.setColor(this.mCDiv);
                        beginRecording.drawLine(this.mYItemWidth, 0.0f + i2, this.mYItemWidth, mXItemHeight + i2, this.mPaint);
                        beginRecording.drawLine(this.mYItemWidth + mDivHeight, 0f + i2, this.mYItemWidth + mDivHeight,
                                mXItemHeight + i2, this.mPaint);

                        //画增加的第一列
                        this.mRect.set(mYItemWidth + mDivHeight, i2, this.mYItemWidth + mDivHeight + mAddItemWidth * leftOneCount, this.mYItemHeight + i2);
                        if (titleCount.length > 1) {
                            if (type1.equals("row")) {
                                setBGColor(i);
                                beginRecording.drawRect(this.mRect, this.mPaint);
                                this.mPaint.setColor(Color.parseColor("#85817d"));
                            }
                            this.mPaint.setTextSize((float) this.mYTextSize);
                            String num2 = ((TrendData) this.mTrendData.get(i)).getPid();
                            drawText2Rect(num2, beginRecording, this.mRect, this.mPaint);

                            //画分隔线
                            //                      this.mPaint.setColor(Color.parseColor("#ff0000"));
                            this.mPaint.setColor(this.mCDiv);
                            beginRecording.drawLine(this.mYItemWidth + mDivHeight + mAddItemWidth * leftOneCount, 0.0f + i2,
                                    this.mYItemWidth + mDivHeight + mAddItemWidth * leftOneCount, mXItemHeight + i2, this.mPaint);

                            beginRecording.drawLine(this.mYItemWidth + mAddItemWidth * leftOneCount + mDivHeight * 2, 0f + i2,
                                    this.mYItemWidth + mAddItemWidth * leftOneCount + mDivHeight * 2, mXItemHeight + i2, this.mPaint);

                            //画增加的第二列
                            this.mRect.set(this.mYItemWidth + mAddItemWidth * leftOneCount + mDivHeight * 2, i2,
                                    this.mYItemWidth + mAddItemWidth * leftOneCount + mDivHeight * 2 + mAddSecondItemWidth * leftSecondCount,
                                    this.mYItemHeight + i2);
                        }
                    } else {
                        this.mRect.set(0, this.mDivHeight + i2, this.mYItemWidth + mAddItemWidth * titleCount.length,
                                (this.mYItemHeight + i2) + this.mDivHeight);
                    }
                } else {
                    //画期数
                    this.mRect.set(0, i2, this.mYItemWidth, this.mYItemHeight + i2);
                    String type1 = mTrendData.get(i).getType();
                    if (type1.equals("row")) {
                        if (i % 2 == 0) {
                            this.mPaint.setColor(this.mCOddY);
                        } else {
                            this.mPaint.setColor(this.mCEvenY);
                        }
                        beginRecording.drawRect(this.mRect, this.mPaint);
                        this.mPaint.setColor(Color.parseColor("#85817d"));
                    }
                    this.mPaint.setTextSize((float) this.mYTextSize);
                    String num = this.mTrendData.get(i).getPid();
                    drawText2Rect(num, beginRecording, this.mRect, this.mPaint);

                    //画分隔线
                    //                    this.mPaint.setColor(Color.parseColor("#ff0000"));
                    this.mPaint.setColor(this.mCDiv);
                    beginRecording.drawLine(this.mYItemWidth, 0.0f + i2, this.mYItemWidth, mXItemHeight + i2, this.mPaint);
                    beginRecording.drawLine(this.mYItemWidth + mDivHeight, 0f + i2, this.mYItemWidth + mDivHeight, mXItemHeight + i2, this.mPaint);

                    //画增加的第一列
                    this.mRect.set(mYItemWidth + mDivHeight, i2, this.mYItemWidth + mDivHeight + mAddItemWidth * leftOneCount, this.mYItemHeight + i2);

                    if (titleCount.length > 1) {
                        if (type1.equals("row")) {
                            setBGColor(i);
                            beginRecording.drawRect(this.mRect, this.mPaint);
                            this.mPaint.setColor(Color.parseColor("#85817d"));
                        }
                        this.mPaint.setTextSize((float) this.mYTextSize);
                        String num2 = ((TrendData) this.mTrendData.get(i)).getPid();
                        drawText2Rect(num2, beginRecording, this.mRect, this.mPaint);

                        //画分隔线
                        //                      this.mPaint.setColor(Color.parseColor("#ff0000"));
                        this.mPaint.setColor(this.mCDiv);
                        beginRecording.drawLine(this.mYItemWidth + mDivHeight + mAddItemWidth * leftOneCount, 0.0f + i2, this.mYItemWidth + mDivHeight + mAddItemWidth * leftOneCount, mXItemHeight + i2, this.mPaint);
                        beginRecording.drawLine(this.mYItemWidth + mAddItemWidth * leftOneCount + mDivHeight * 2, 0f + i2,
                                this.mYItemWidth + mAddItemWidth * leftOneCount + mDivHeight * 2, mXItemHeight + i2, this.mPaint);
                        //画增加的第二列
                        this.mRect.set(this.mYItemWidth + mAddItemWidth * leftOneCount + mDivHeight * 2, i2,
                                this.mYItemWidth + mAddItemWidth * leftOneCount + mDivHeight * 2 + mAddSecondItemWidth * leftSecondCount, this.mYItemHeight + i2);
                    }
                }
                String type = this.mTrendData.get(i).getType();
                switch (type) {
                    case "row":
                        type = this.mTrendData.get(i).getWinNumbers();
                        setBGColor(i);
                        beginRecording.drawRect(this.mRect, this.mPaint);
                        this.mPaint.setColor(Color.parseColor("#85817d"));
                        break;
                    case "dis":
                        type = "出现次数";
                        this.mPaint.setColor(-1);
                        beginRecording.drawRect(this.mRect, this.mPaint);
                        this.mPaint.setColor(Color.parseColor("#ae68c6"));
                        break;
                    case "avg":
                        type = "平均遗漏";
                        this.mPaint.setColor(this.mCAvgYilouBg);
                        beginRecording.drawRect(this.mRect, this.mPaint);
                        this.mPaint.setColor(Color.parseColor("#43bf8a"));
                        break;
                    case "mmv":
                        type = "最大遗漏";
                        this.mPaint.setColor(-1);
                        beginRecording.drawRect(this.mRect, this.mPaint);
                        this.mPaint.setColor(Color.parseColor("#f1973a"));
                        break;
                    case "mlv":
                        type = "最大连出";
                        this.mPaint.setColor(this.mCLianchuBg);
                        beginRecording.drawRect(this.mRect, this.mPaint);
                        this.mPaint.setColor(Color.parseColor("#24c4cb"));
                        break;
                    default:
                        type = "??";
                        break;
                }
                this.mPaint.setTextSize((float) this.mYTextSize);
                drawText2Rect(type, beginRecording, this.mRect, this.mPaint);
            }
            this.mPicY.endRecording();
        }
    }

    public void setBGColor(int num) {
        if (num % 2 == 0) {
            this.mPaint.setColor(Color.parseColor("#ffffff"));
        } else {
            this.mPaint.setColor(Color.parseColor("#f6f5f4"));
        }
    }

    public void setShowTitle(boolean isShowTitle) {
        mIsShowTitle = isShowTitle;
    }

    public void drawLeftBottom() {
        int i = this.mXItemHeight + this.mBottomMargin;
        Canvas beginRecording = this.mPicLeftBottom.beginRecording(this.mYItemWidth, i);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(-1);
        this.mRect.set(0, 0, this.mYItemWidth + mAddItemWidth * titleCount.length, i);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setColor(this.mCDiv);
        beginRecording.drawLine(0.0f, 2.0f, (float) this.mYItemWidth, 2.0f, this.mPaint);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setTextSize((float) this.mLcTextSize);
        this.mRect.set(0, this.mBottomMargin, this.mYItemWidth + mAddItemWidth * titleCount.length, i);
        drawText2Rect("预选区", beginRecording, this.mRect, this.mPaint);
        this.mPicLeftBottom.endRecording();
    }

    //添加列  在此修改
    public void drawLeftTop() {
        if (!mIsShowTitle) {
            return;
        }
        Canvas beginRecording = this.mPicLeftTop.beginRecording(this.mYItemWidth, this.mXItemHeight);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(this.mCQihaoText);
        this.mRect.set(0, 0, this.mYItemWidth, this.mXItemHeight);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setTextSize((float) this.mLcTextSize);
        drawText2Rect("期号", beginRecording, this.mRect, this.mPaint);

        /*for (int i = 0; i < titleCount.length; i++) {

            //画分隔线
//        this.mPaint.setColor(Color.parseColor("#ff0000"));
            this.mPaint.setColor(this.mCDiv);
            beginRecording.drawLine(this.mYItemWidth + mAddItemWidth * i + mAddSecondItemWidth, 0.0f, this.mYItemWidth + mAddItemWidth * i + mAddSecondItemWidth, mXItemHeight, this.mPaint);
            beginRecording.drawLine(this.mYItemWidth + mDivHeight + mAddItemWidth * i + mAddSecondItemWidth, 0f, this.mYItemWidth + mDivHeight + mAddItemWidth * i + mAddSecondItemWidth, mXItemHeight, this.mPaint);

            this.mPaint.setStyle(Style.FILL);
//            this.mPaint.setColor(this.mCXTitleBg);
            this.mPaint.setColor(Color.parseColor("#ffdad2ca"));
            this.mRect.set(mYItemWidth + mAddItemWidth * i + mAddSecondItemWidth, 0, this.mYItemWidth + this.mAddItemWidth * (i + 1) + mAddSecondItemWidth, this.mXItemHeight);
            beginRecording.drawRect(this.mRect, this.mPaint);
            this.mPaint.setColor(-1);
            this.mPaint.setTextSize((float) this.mLcTextSize);
            drawText2Rect(titleCount[i], beginRecording, this.mRect, this.mPaint);
        }*/

        //画分隔线
        int leftTempCount = 0, leftSecondTempCount = 0;
        if (titleCount.length >= 1) {
            leftTempCount = leftOneCount - 1;
        }
        if (titleCount.length > 1) {
            leftSecondTempCount = leftSecondCount - 1;
        }
        if (titleCount.length >= 1) {
            this.mPaint.setColor(this.mCDiv);
            beginRecording.drawLine(this.mYItemWidth + mAddItemWidth * leftTempCount, 0.0f, this.mYItemWidth + mAddItemWidth * leftTempCount, mXItemHeight, this.mPaint);
            beginRecording.drawLine(this.mYItemWidth + mDivHeight + mAddItemWidth * leftTempCount, 0f, this.mYItemWidth + mDivHeight + mAddItemWidth * leftTempCount, mXItemHeight, this.mPaint);

            this.mPaint.setStyle(Style.FILL);
            //            this.mPaint.setColor(this.mCXTitleBg);
            this.mPaint.setColor(Color.parseColor("#ffdad2ca"));

            this.mRect.set(mYItemWidth + mAddItemWidth * leftTempCount, 0, this.mYItemWidth + this.mAddItemWidth * (leftTempCount + 1), this.mXItemHeight);
            beginRecording.drawRect(this.mRect, this.mPaint);
            this.mPaint.setColor(-1);
            this.mPaint.setTextSize((float) this.mLcTextSize);
            if (titleCount.length >= 1) {
                drawText2Rect(titleCount[0], beginRecording, this.mRect, this.mPaint);
            }
        }
        //画分隔线
        if (titleCount.length > 1) {
            this.mPaint.setColor(this.mCDiv);
            beginRecording.drawLine(this.mYItemWidth + mAddItemWidth * leftTempCount + mAddSecondItemWidth * leftSecondTempCount, 0.0f,
                    this.mYItemWidth + mAddItemWidth * leftTempCount + mAddSecondItemWidth * leftSecondTempCount, mXItemHeight, this.mPaint);
            beginRecording.drawLine(this.mYItemWidth + mDivHeight + mAddItemWidth * leftTempCount + mAddSecondItemWidth * leftSecondTempCount, 0f,
                    this.mYItemWidth + mDivHeight + mAddItemWidth * leftTempCount + mAddSecondItemWidth * leftSecondTempCount, mXItemHeight, this.mPaint);

            this.mPaint.setStyle(Style.FILL);
            //            this.mPaint.setColor(this.mCXTitleBg);
            this.mPaint.setColor(Color.parseColor("#ffdad2ca"));
            this.mRect.set(mYItemWidth + mAddItemWidth * leftTempCount + mAddSecondItemWidth * leftSecondTempCount, 0,
                    this.mYItemWidth + this.mAddItemWidth * (leftTempCount + 1) + mAddSecondItemWidth * (leftSecondTempCount + 1), this.mXItemHeight);
            beginRecording.drawRect(this.mRect, this.mPaint);
            this.mPaint.setColor(-1);
            this.mPaint.setTextSize((float) this.mLcTextSize);
            if (titleCount.length > 1) {
                drawText2Rect(titleCount[1], beginRecording, this.mRect, this.mPaint);
            }
        }

        this.mPicLeftTop.endRecording();
    }

    public void drawXTop() {
        if (!mIsShowTitle) {
            return;
        }
        int i;
        int i2 = this.mDivWidth + (this.mXItemWidth * (this.redCount + this.blueCount)) + mAddItemWidth * leftOneCount + mAddSecondItemWidth * leftSecondCount;
        int i3 = this.mXItemHeight;
        Canvas beginRecording = this.mPicXTop.beginRecording(i2, i3);
        this.mRect.set(0, 0, i2, i3);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(this.mCXTitleBg);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setColor(-1);
        this.mRect.set(this.redCount * this.mXItemWidth, 0, (this.redCount * this.mXItemWidth) + this.mDivWidth, i3);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setTextSize((float) this.mLcTextSize);
        for (i = 1; i <= this.redCount; i++) {
            String str;
            int i4 = i * this.mXItemWidth;
            this.mPaint.setColor(this.mCDiv);
            beginRecording.drawLine((float) i4, 0.0f, (float) i4, (float) i3, this.mPaint);
            this.mRect.set(i4 - this.mXItemWidth, 0, i4, i3);
            this.mPaint.setColor(-1);
            if (i <= 9) {
                str = "0" + i;
            } else {
                str = "" + i;
            }
            drawText2Rect(str, beginRecording, this.mRect, this.mPaint);
        }
        String[] xTitle = titles.split(",");
        for (i = 0; i < this.blueCount; i++) {
            String str;
            int i4 = ((this.redCount + i) * this.mXItemWidth) + this.mDivWidth + mAddItemWidth * leftOneCount + mAddSecondItemWidth * leftSecondCount;
            beginRecording.drawLine((float) i4, 0.0f, (float) i4, (float) i3, this.mPaint);
            this.mRect.set(i4, 0, this.mXItemWidth + i4, i3);
            this.mPaint.setColor(-1);
            /*if (i < 9) {
                str = "0" + (i + 1);
            } else {
                str = "" + (i + 1);
            }*/
            drawText2Rect(xTitle[i], beginRecording, this.mRect, this.mPaint);
        }

        this.mPicXTop.endRecording();
    }

    public void drawXBottom() {
        int i = 1;
        int i2 = (this.mXItemWidth * (this.redCount + this.blueCount)) + this.mDivWidth + mAddItemWidth * titleCount.length;
        int i3 = this.mXItemHeight + this.mBottomMargin;
        Canvas beginRecording = this.mPicXBottom.beginRecording(i2, i3);
        this.mRect.set(0, 0, i2, i3);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(-1);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setColor(this.mCDiv);
        beginRecording.drawLine(0.0f, 2.0f, (float) i2, 2.0f, this.mPaint);
        this.mPaint.setTextSize((float) this.mXTextSize);
        for (int i4 = 1; i4 <= this.redCount; i4++) {
            String str;
            this.mRect.set((i4 - 1) * this.mXItemWidth, this.mBottomMargin, this.mXItemWidth * i4, i3);
            if (this.mSelectedRed.contains(i4 - 1)) {
                this.mPaint.setStyle(Style.FILL);
                this.mPaint.setColor(this.mCBallSelectedRed);
                beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
                this.mPaint.setColor(-1);
            } else {
                this.mPaint.setStyle(Style.STROKE);
                this.mPaint.setColor(this.mCBallSelectedStroke);
                beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
                this.mPaint.setColor(this.mCXbottomTextRed);
                this.mPaint.setStyle(Style.FILL);
            }
            if (i4 <= 9) {
                str = "0" + i4;
            } else {
                str = "" + i4;
            }
            drawText2Rect(str, beginRecording, this.mRect, this.mPaint);
        }
        while (i <= this.blueCount) {
            String str2;
            this.mRect.set((((this.redCount + i) - 1) * this.mXItemWidth) + this.mDivWidth + mAddItemWidth * titleCount.length, this.mBottomMargin, ((this.redCount + i) * this.mXItemWidth) + this.mDivWidth + mAddItemWidth * titleCount.length, i3);
            if (this.mSelectedBlue.contains(i - 1)) {
                this.mPaint.setStyle(Style.FILL);
                this.mPaint.setColor(this.mCBallSelectedBlue);
                beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
                this.mPaint.setColor(-1);
            } else {
                this.mPaint.setStyle(Style.STROKE);
                this.mPaint.setColor(this.mCBallSelectedStroke);
                beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
                this.mPaint.setColor(this.mCXbottomTextBlue);
                this.mPaint.setStyle(Style.FILL);
            }
            if (i <= 9) {
                str2 = "0" + i;
            } else {
                str2 = "" + i;
            }
            drawText2Rect(str2, beginRecording, this.mRect, this.mPaint);
            i++;
        }
        this.mPicXBottom.endRecording();
    }

    public void drawContent() {
        int i;
        int i2 = (this.mXItemWidth * (this.redCount + this.blueCount)) + this.mDivWidth + mAddItemWidth * leftOneCount + mAddSecondItemWidth * leftSecondCount;
        Canvas beginRecording = this.mPicContent.beginRecording(i2, (this.mYItemHeight * this.mTrendData.size()) + this.mDivHeight);
        this.mPaint.setTextSize((float) this.mCTextSize);
        this.mPaint.setStyle(Style.FILL);
        int i3 = this.redCount + this.blueCount;
        int size = this.mTrendData.size();
        for (i = 0; i <= size; i++) {
            int i4 = i * this.mXItemHeight;
            if (i != size) {
                if (i < size - 4) {
                    this.mRect.set(0, i4, i2, this.mXItemHeight + i4);
                    if (i % 2 == 0) {
                        this.mPaint.setColor(-1);
                    } else {
                        this.mPaint.setColor(this.mCOddContent);
                    }
                    beginRecording.drawRect(this.mRect, this.mPaint);
                    this.mPaint.setColor(this.mCDiv);
                    beginRecording.drawLine(0.0f, (float) i4, (float) i2, (float) i4, this.mPaint);
                } else {
                    this.mRect.set(0, this.mDivHeight + i4, i2, (i4 + this.mDivHeight) + this.mXItemHeight);
                    if (i == (size - 4) + 1) {
                        this.mPaint.setColor(this.mCAvgYilouBg);
                    } else if (i == size - 1) {
                        this.mPaint.setColor(this.mCLianchuBg);
                    } else {
                        this.mPaint.setColor(-1);
                    }
                    beginRecording.drawRect(this.mRect, this.mPaint);
                }
            }
        }

        //画内容区域的竖行分隔线
        int size2 = this.mTrendData.size() * this.mXItemHeight;
        for (i = 0; i <= i3; i++) {
            int i5 = i * this.mXItemWidth + mAddItemWidth * leftOneCount + mAddSecondItemWidth * leftSecondCount;
            if (i == this.redCount) {
                this.mPaint.setColor(-1);
                this.mRect.set(i5, 0, this.mDivWidth + i5, size2);
                beginRecording.drawRect(this.mRect, this.mPaint);
                this.mPaint.setColor(this.mCDiv);
                beginRecording.drawLine((float) i5, 0.0f, (float) i5, (float) size2, this.mPaint);
                beginRecording.drawLine((float) (this.mDivWidth + i5), 0.0f, (float) (this.mDivWidth + i5), (float) size2, this.mPaint);
            } else if (i < this.redCount) {
                this.mPaint.setColor(this.mCDiv);
                beginRecording.drawLine((float) i5, 0.0f, (float) i5, (float) size2, this.mPaint);
            } else {
                this.mPaint.setColor(this.mCDiv);
                boolean drawLastFour = true;
                //根据情况去掉某一行分隔线
                for (int j = 0; j < mTrendData.size(); j++) {
                    if (!TextUtils.equals("等待开奖", mTrendData.get(j).getBlue())) {
                        if (j >= mTrendData.size() - 4) {
                            if (drawLastFour) {
                                beginRecording.drawLine((float) (this.mDivWidth + i5), j * mXItemHeight, (float) (this.mDivWidth + i5), (j + 1) * mXItemHeight, this.mPaint);
                            }
                        } else {
                            beginRecording.drawLine((float) (this.mDivWidth + i5), j * mXItemHeight, (float) (this.mDivWidth + i5), (j + 1) * mXItemHeight, this.mPaint);
                        }
                    } else {
                        drawLastFour = false;
                    }

                }
                //                beginRecording.drawLine((float) (this.mDivWidth + i5), 0.0f, (float) (this.mDivWidth + i5), (float) size2, this.mPaint);
            }
        }

        //画横着的一条有大宽度的分隔线 现在宽度设为了0
        i = (size - 4) * this.mXItemHeight;
        this.mPaint.setColor(-1);
        this.mRect.set(0, i, i2, this.mDivHeight + i);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setColor(this.mCDiv);
        beginRecording.drawLine(0.0f, (float) i, (float) i2, (float) i, this.mPaint);
        beginRecording.drawLine(0.0f, (float) ((this.mDivHeight + i) - 1), (float) i2, (float) ((this.mDivHeight + i) - 1), this.mPaint);

        if (AppConstants.TREND_STYLE_BASIC.equals(this.mTrendViewType) && this.mDrawLine) {
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setColor(this.mCBallBlue);
            beginRecording.drawPath(this.mPathPoint, this.mPaint);
            this.mPaint.setStyle(Style.FILL);
        }

        //TODO初始化路径及画笔
        if (AppConstants.TREND_STYLE_FORM.equals(this.mTrendViewType) && this.mDrawLine) {
            if (formPaths != null && formPaths.size() > 0) {
                for (FormPath formPath : formPaths) {
                    formPath.getPaint().setStrokeWidth(DeviceUtils.dpToPx(mContext, 2));
                    formPath.getPaint().setStyle(Style.STROKE);
                    formPath.getPaint().setDither(true);//柔和
                    beginRecording.drawPath(formPath.getPath(), formPath.getPaint());
                }
            }
        }

        //填充遗漏数据
        i = this.mTrendData.size();
        this.mPaint.setStyle(Style.FILL);
        boolean drawEmptyText = false;

        for (int i6 = 0; i6 < i; i6++) {
            int i7;
            String str;
            TrendData trendData = this.mTrendData.get(i6);
            //            String[] split = trendData.getRed().split(",");
            String[] split2 = trendData.getBlue().split(",");
            int i8 = this.mXItemHeight * i6;
            int redCount = 0;
            /*if (i6 >= i - 4) {
                i8 += this.mDivHeight;
            }*/
            //去掉红球
            //            int redCount = split.length;
            /*for (i7 = 0; i7 < redCount; i7++) {
                this.mRect.set(this.mXItemWidth * i7, i8, (i7 + 1) * this.mXItemWidth, this.mXItemHeight + i8);
                if (!"row".equals(trendData.getType())) {
                    this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
                    drawText2Rect(split[i7], beginRecording, this.mRect, this.mPaint);
                } else if (split[i7].equals("0")) {
                    this.mPaint.setColor(this.mCBallRed);
                    beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
                    this.mPaint.setColor(-1);
                    if (i7 < 9) {
                        str = "0" + (i7 + 1);
                    } else {
                        str = "" + (i7 + 1);
                    }
                    drawText2Rect(str, beginRecording, this.mRect, this.mPaint);
                } else if (this.mShowYilou) {
                    this.mPaint.setColor(this.mCYilou);
                    drawText2Rect(split[i7], beginRecording, this.mRect, this.mPaint);
                }
            }*/
            String[] xTitles = titles.split(",");
            //蓝球
            for (i7 = 0; i7 < split2.length; i7++) {
                int left = ((redCount + i7) * this.mXItemWidth) + this.mDivWidth + mAddItemWidth * leftOneCount + mAddSecondItemWidth * leftSecondCount;
                int right = (((redCount + i7) + 1) * this.mXItemWidth) + this.mDivWidth + mAddItemWidth * leftOneCount + mAddSecondItemWidth * leftSecondCount;
                int bottom = this.mXItemHeight + i8;
                this.mRect.set(left, i8, right, bottom);
                this.formmRect.set(left + innerDev, i8 + innerDev, right - innerDev, bottom - innerDev);

                if (!"row".equals(trendData.getType())) {
                    switch (trendData.getType()) {
                        case "dis":
                            this.mPaint.setColor(Color.parseColor("#ae68c6"));
                            break;
                        case "avg":
                            this.mPaint.setColor(Color.parseColor("#43bf8a"));
                            break;
                        case "mmv":
                            this.mPaint.setColor(Color.parseColor("#f1973a"));
                            break;
                        case "mlv":
                            this.mPaint.setColor(Color.parseColor("#24c4cb"));
                            break;
                    }

                    //添加四行空布局
                    if (i6 >= i - 4) {
                        if (drawEmptyText) {
                            this.mRect.set(this.mDivWidth + mAddItemWidth * leftOneCount + mAddSecondItemWidth * leftSecondCount, (mTrendData.size() - 4) * mXItemHeight,
                                    this.mScreenWidth, mTrendData.size() * mXItemHeight);
                            this.mPaint.setColor(Color.parseColor("#ff9e9e9e"));
                            mPaint.setFakeBoldText(false);
                            drawText2Rect("等待开奖", beginRecording, this.mRect, this.mPaint);
                        } else {
                            drawText2Rect(split2[i7], beginRecording, this.mRect, this.mPaint);
                        }
                    } else {
                        drawText2Rect(split2[i7], beginRecording, this.mRect, this.mPaint);
                    }
                    //                    drawText2Rect(split2[i7], beginRecording, this.mRect, this.mPaint);
                } else if (LotteryUtils.isTrendWinNumber(split2[i7])) {
                    if (AppConstants.TREND_STYLE_FORM.equals(this.mTrendViewType)) {
                        str = xTitles[i7];
                        if (paintColors != null && paintColors.size() > 0) {
                            if (formPaths != null && formPaths.size() > 0) {
                                FormPath formPath = formPaths.get(colorPos);
                                Paint paint = formPath.getPaint();
                                paint.setStyle(Style.FILL);
                                int color = CommonUtil.getColor(mContext, paintColors.get(colorPos));
                                paint.setColor(color);
                                paint.setTextSize(DeviceUtils.sp2px(mContext, 14));
                                beginRecording.drawRect(formmRect, paint);
                                paint.setColor(-1);
                                drawTextFormRect(str, beginRecording, this.formmRect, paint);
                                colorPos++;
                                colorPos = colorPos == selectSize ? 0 : colorPos;

                            }
                        } else {
                            LogF.d(TAG, "没有设置色值");
                        }
                    } else {
                        this.mPaint.setColor(this.mCBallBlue);
                        beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
                        if (LotteryUtils.isTrendSameWinNumber(split2[i7])) {
                            int num = Math.abs(Integer.valueOf(split2[i7]));
                            this.mPaint.setColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.color_trend_same_number_bg));
                            float rightX = mRect.exactCenterX() + mXItemWidth / 4;
                            float rightY = mRect.centerY() - mXItemWidth / 3;
                            beginRecording.drawCircle(rightX, rightY, (float) this.mDefBallSize / 3, this.mPaint);
                            beginRecording.drawText(num + "",
                                    mRect.centerX() + mXItemWidth / 4, (((float) mRect.top) + (((((float) (mRect.bottom - mRect.top))
                                            - mPaintRight.getFontMetrics().bottom) + mPaintRight.getFontMetrics().top) / 2.0f)) - mPaintRight.getFontMetrics().top - mXItemWidth / 3, mPaintRight);
                        }
                        this.mPaint.setColor(-1);
                        str = xTitles[i7];
                        drawText2Rect(str, beginRecording, this.mRect, this.mPaint);
                    }
                } else if (this.mShowYilou) {
                    this.mPaint.setColor(this.mCYilou);
                    if (trendData.getBlue().contains("等待开奖")) {
                        this.mRect.set(this.mDivWidth + mAddItemWidth * leftOneCount + mAddSecondItemWidth * leftSecondCount, i8,
                                this.mScreenWidth, this.mXItemHeight + i8);
                        drawText2Rect(trendData.getBlue(), beginRecording, this.mRect, this.mPaint);
                        if (i6 == i - 5) {//除了底部四行数据的最后一行是等待开奖才需要画空布局
                            drawEmptyText = true;
                        }
                    } else {
                        drawText2Rect(split2[i7], beginRecording, this.mRect, this.mPaint);
                    }
                }
                //
            }
        }
        this.mPicContent.endRecording();
    }

    private void setPaint(Canvas beginRecording, String str, Paint paint) {
        paint.setStyle(Style.FILL);
        int color = CommonUtil.getColor(mContext, paintColors.get(colorPos));
        paint.setColor(color);
        paint.setTextSize(DeviceUtils.sp2px(mContext, 14));
        beginRecording.drawRect(formmRect, paint);
        paint.setColor(-1);
        drawTextFormRect(str, beginRecording, this.formmRect, paint);
        colorPos++;
        colorPos = colorPos == selectSize ? 0 : colorPos;
    }

    protected CharSequence getKuaiPingLeftTime() {
        return null;
    }
}
