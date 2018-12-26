package cn.zcgames.lottery.home.view.fragment.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Picture;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.DisplayUtil;
import cn.berfy.sdk.mvpbase.util.DeviceUtils;
import cn.zcgames.lottery.R;

/**
 * 走势图图表类
 *
 * @author hjh berfy xzh
 * @date 2018/10/25 14:13
 */
public abstract class ATrendChart implements ITrendChart {

    protected static final boolean DEBUG = false;
    protected static final String TAG = "ASixPartTrendChart";
    int mBottomMargin;
    int mCApCount;
    int mCAvgYilou;
    int mCJO;
    int mCZH;
    int mCAvgYilouBg;
    int mCBallBlue;
    int mCBallPurple;
    int mCBallRed;
    int mCBallSelectedBlue;
    int mCBallSelectedRed;
    int mCBallSelectedStroke;
    int mCDiv;
    int mCEvenY;
    int mCLianchu;
    int mCLianchuBg;
    int mCMaxYilou;
    int mCOddContent;
    int mCOddY;
    int mCQihaoText;
    int mCTextSize;
    int mCXTitleBg;
    int mCXbottomTextBlue;
    int mCXbottomTextRed;
    int mCYText;
    int mCYilou;
    boolean mCanScale = false;
    boolean[] mCanScroll = new boolean[]{false, false};
    int mDefBallSize;
    final float mDefaultMaxScale = 2.0f;
    final float mDefaultMinScale = 0.1f;
    int mDivHeight;
    int mDivWidth;
    protected final float mHalfSize = 0.5f;
    boolean mInitOk = false;
    int mLcTextSize;
    Paint mPaint = new Paint(1);
    Paint mPaintRight = new Paint(1);
    List<FormPath> formPaths = new ArrayList<>();//存放各分类画笔及路径
    final Picture mPicContent = new Picture();
    final Picture mPicLeftBottom = new Picture();
    final Picture mPicLeftTop = new Picture();
    final Picture mPicXBottom = new Picture();
    final Picture mPicXTop = new Picture();
    final Picture mPicY = new Picture();
    Rect mRect = new Rect();
    Rect formmRect = new Rect();
    protected final int mScaleCount = 4;
    float[] mScaleRange = new float[]{0.1f, 2.0f};
    protected final int mScaleRangeMaxY = 3;
    protected final int mScaleRangeMinY = 2;
    int[] mScrollRange = new int[4];
    final int mStatCount = 4;
    int innerDev = 0;
    int mTimeHeight;
    LottoTrendView mTrendView;
    String[] countsName;
    int mXItemHeight;
    int mXItemWidth;
    int mXTextSize;
    int mYItemHeight;
    int mYItemWidth;
    int mYTextSize;
    int mAddItemWidth;
    int mAddSecondItemWidth;
    int mScreenWidth;
    TextPaint tp = new TextPaint(1);

    protected abstract void drawContent();

    //    protected abstract void drawLeftBottom();

    protected abstract void drawLeftTop();

    //    protected abstract void drawXBottom();

    protected abstract void drawXTop();

    protected abstract void drawY();

    protected abstract CharSequence getKuaiPingLeftTime();

    public ATrendChart(Context context, LottoTrendView lottoTrendView) {
        this(context, lottoTrendView, context.getResources().getDimensionPixelSize(R.dimen.trend_x_item_width),
                context.getResources().getDimensionPixelSize(R.dimen.trend_y_item_width), 0);
    }

    public ATrendChart(Context context, LottoTrendView lottoTrendView, int xItemWidth, int leftTitleItemWidth, int leftSecondWidth) {
        this.mTrendView = lottoTrendView;
        LottoTrendView.setHardwareAccelerated(lottoTrendView, false);
        Resources resources = context.getResources();
        this.mCYText = resources.getColor(R.color.lottery_title_color);
        this.mCOddY = resources.getColor(R.color.trend_list_odd);
        this.mCEvenY = resources.getColor(R.color.trend_list_even);
        this.mCOddContent = resources.getColor(R.color.trend_line_odd_bg);
        this.mCBallRed = resources.getColor(R.color.trend_ball_red);
        this.mCBallBlue = resources.getColor(R.color.trend_ball_blue);
        this.mCBallPurple = resources.getColor(R.color.trend_ball_purple);
        this.mCBallSelectedRed = resources.getColor(R.color.trend_x_ball_red);
        this.mCBallSelectedBlue = resources.getColor(R.color.trend_x_ball_blue);
        this.mCBallSelectedStroke = resources.getColor(R.color.trend_x_ball_stroke);
        this.mCXbottomTextRed = resources.getColor(R.color.trend_x_red_text);
        this.mCXbottomTextBlue = resources.getColor(R.color.trend_x_blue_text);
        this.mCApCount = resources.getColor(R.color.trend_max_count_color);
        this.mCAvgYilou = resources.getColor(R.color.trend_avg_yilou_color);
        this.mCJO = resources.getColor(R.color.trend_avg_jiou_color);
        this.mCZH = resources.getColor(R.color.trend_avg_zhihe_color);
        this.mCAvgYilouBg = resources.getColor(R.color.trend_avg_yilou_bg);
        this.mCMaxYilou = resources.getColor(R.color.trend_max_yilou_color);
        this.mCLianchu = resources.getColor(R.color.trend_max_lianchu_color);
        this.mCLianchuBg = resources.getColor(R.color.trend_max_lianchu_bg);
        this.mCDiv = resources.getColor(R.color.trend_divider);
        this.mCXTitleBg = resources.getColor(R.color.trend_xtitle_bg);
        this.mCQihaoText = resources.getColor(R.color.trend_qihao_text);
        this.mCYilou = resources.getColor(R.color.trend_content_yilou);
        this.mXItemWidth = xItemWidth;
        this.mXItemHeight = resources.getDimensionPixelSize(R.dimen.trend_x_item_height);
        //        this.mYItemWidth = leftTitleItemWidth;
        this.mYItemWidth = context.getResources().getDimensionPixelSize(R.dimen.trend_y_item_width);
        this.mAddItemWidth = leftTitleItemWidth;
        //        this.mAddItemWidth = context.getResources().getDimensionPixelSize(R.dimen.trend_y_item_width);
        this.mYItemHeight = resources.getDimensionPixelSize(R.dimen.trend_y_item_height);
        this.mXTextSize = resources.getDimensionPixelSize(R.dimen.trend_x_text_size);
        this.mYTextSize = resources.getDimensionPixelSize(R.dimen.trend_y_text_size);
        this.mCTextSize = resources.getDimensionPixelSize(R.dimen.trend_content_text_size);
        this.mLcTextSize = resources.getDimensionPixelSize(R.dimen.trend_corner_text_size);
        this.mDefBallSize = resources.getDimensionPixelSize(R.dimen.trend_ball_radius);
        this.mDivWidth = resources.getDimensionPixelSize(R.dimen.trend_sulcus_width);
        this.mDivHeight = resources.getDimensionPixelSize(R.dimen.trend_sulcus_height);
        this.mBottomMargin = resources.getDimensionPixelSize(R.dimen.trend_sulcus_bottom);
        this.mTimeHeight = resources.getDimensionPixelSize(R.dimen.trend_time_height);
        this.mAddSecondItemWidth = leftSecondWidth;
        this.innerDev = DeviceUtils.dpToPx(context, 3);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.mScreenWidth = DeviceUtils.pxToDp(context, wm.getDefaultDisplay().getWidth());
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        mPaintRight.setColor(-1);
        mPaintRight.setTextAlign(Paint.Align.CENTER);
        mPaintRight.setStyle(Style.FILL);
        mPaintRight.setColor(ContextCompat.getColor(context, R.color.white_normal));
        mPaintRight.setTextSize(DisplayUtil.dip2px(context, 5));
    }

    public void initChart(Context context, int i, int i2, float f) {
        if (i != 0 && i2 != 0) {
            System.currentTimeMillis();
            drawXTop();
            drawLeftTop();
            drawY();
            //            drawXBottom();
            drawContent();
            //            drawLeftBottom();
            this.mInitOk = true;
        }
    }

    public void setInitOk(boolean z) {
        this.mInitOk = z;
    }

    public void draw(Canvas canvas, int i, int i2, int i3, int i4, float f) {
        if (i3 > 0 && i4 > 0 && initOk()) {
            CharSequence kuaiPingLeftTime = getKuaiPingLeftTime();
            int width = (int) (((float) this.mPicY.getWidth()) * f);
            int height = (int) (((float) this.mPicY.getHeight()) * f);
            int width2 = (int) (((float) this.mPicXTop.getWidth()) * f);
            int height2 = (int) (((float) this.mPicXTop.getHeight()) * f);
            int width3 = (int) (((float) this.mPicXBottom.getWidth()) * f);
            int height3 = (int) (((float) this.mPicXBottom.getHeight()) * f);
            int width4 = (int) (((float) this.mPicContent.getWidth()) * f);
            int height4 = (int) (((float) this.mPicContent.getHeight()) * f);
            //            canvas.save();
            this.mRect.set(width, height2, i3, i4 - height3);
            //            canvas.clipRect(this.mRect);
            this.mRect.set(i, i2, width4 + i, height4 + i2);
            canvas.drawPicture(this.mPicContent, this.mRect);
            //            canvas.restore();
            this.mRect.set(0, i2, width, height + i2);
            canvas.drawPicture(this.mPicY, this.mRect);
            this.mRect.set(i, 0, width2 + i, height2);
            canvas.drawPicture(this.mPicXTop, this.mRect);
            this.mRect.set(i, i4 - height3, width3 + i, i4);
            canvas.drawPicture(this.mPicXBottom, this.mRect);
            this.mRect.set(0, 0, width, height2);
            canvas.drawPicture(this.mPicLeftTop, this.mRect);
            this.mRect.set(0, i4 - height3, width, i4);
            canvas.drawPicture(this.mPicLeftBottom, this.mRect);
            if (kuaiPingLeftTime != null) {
                this.mRect.set(0, (i4 - height3) - ((int) (((float) this.mTimeHeight) * f)), i3, i4 - height3);
                drawKuaiPingTime(canvas, this.mRect, f);
            }
        }
    }

    private void drawKuaiPingTime(Canvas canvas, Rect rect, float f) {
        this.mPaint.setColor(-1);
        this.mPaint.setStyle(Style.FILL);
        canvas.drawRect(rect, this.mPaint);
        this.mPaint.setColor(this.mCDiv);
        canvas.drawLine(0.0f, (float) (rect.top + 1), (float) rect.right, (float) (rect.top + 1), this.mPaint);
        this.mPaint.setColor(this.mCYText);
        this.mPaint.setTextSize(((float) this.mCTextSize) * f);
        drawSpanText2Rect(getKuaiPingLeftTime(), canvas, rect, this.mPaint);
    }

    public void reCalcScroll(float f, int i, int i2) {
        if (this.mPicY != null && this.mPicXTop != null && this.mPicXBottom != null) {
            int width = this.mPicY.getWidth();
            int height = this.mPicY.getHeight();
            int width2 = this.mPicXTop.getWidth();
            int height2 = this.mPicXTop.getHeight();
            int height3 = this.mPicXBottom.getHeight();
            Object obj = getKuaiPingLeftTime() != null ? 1 : null;
            this.mCanScroll[1] = ((float) ((height2 + height3) + height)) * f > ((float) i2);
            this.mScrollRange[2] = (int) (((float) i2) - (((float) ((obj != null ? this.mTimeHeight : 0) + (height3 + height))) * f));
            this.mScrollRange[3] = (int) (((float) height2) * f);
            float f2 = (((float) i2) * 1.0f) / ((float) ((height2 + height3) + height));
            if (f2 > this.mScaleRange[0]) {
                this.mScaleRange[0] = f2;
            }
            this.mCanScroll[0] = ((float) (width + width2)) * f > ((float) i);
            this.mScrollRange[0] = (int) (((float) i) - (((float) width2) * f));
            this.mScrollRange[1] = (int) (((float) width) * f);
            f2 = (((float) i) * 1.0f) / ((float) (width + width2));
            if (f2 > this.mScaleRange[0]) {
                this.mScaleRange[0] = f2;
            }
        }
    }

    public boolean[] getCanScroll() {
        return this.mCanScroll;
    }

    public int[] getScrollRange() {
        return this.mScrollRange;
    }

    public float[] getScaleRange() {
        return this.mScaleRange;
    }

    public boolean getCanScale() {
        return this.mCanScale;
    }

    public boolean onClick(MotionEvent motionEvent, float f, float f2, int i, int i2, float f3) {
        return false;
    }

    public boolean onLongClick(MotionEvent motionEvent, float f, float f2, int i, int i2, float f3) {
        return false;
    }

    protected int dp2px(float f, int i) {
        return (int) ((((float) i) * f) + 0.5f);
    }

    protected void drawText2Rect(String str, Canvas canvas, Rect rect, Paint paint) {
        if (!TextUtils.isEmpty(str)) {
            FontMetrics fontMetrics = paint.getFontMetrics();
            canvas.drawText(str, (float) rect.centerX(), (((float) rect.top) + (((((float) (rect.bottom - rect.top)) - fontMetrics.bottom) + fontMetrics.top) / 2.0f)) - fontMetrics.top, paint);
        }
    }

    protected void drawTextFormRect(String str, Canvas canvas, Rect rect, Paint paint) {
        if (!TextUtils.isEmpty(str)) {
            FontMetrics fontMetrics = paint.getFontMetrics();
            float textWidth = paint.measureText(str);
            float Textx = (float) rect.centerX() - textWidth / 2;
            float Texty = (((float) rect.top) + (((((float) (rect.bottom - rect.top)) - fontMetrics.bottom) + fontMetrics.top) / 2.0f)) - fontMetrics.top;
            canvas.drawText(str, Textx, Texty, paint);
        }
    }

    protected void drawSpanText2Rect(CharSequence charSequence, Canvas canvas, Rect rect, Paint paint) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (charSequence instanceof String) {
                drawText2Rect(charSequence.toString(), canvas, rect, paint);
                return;
            }
            this.tp.setTextSize(paint.getTextSize());
            StaticLayout staticLayout = new StaticLayout(charSequence, this.tp, rect.width(), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
            FontMetrics fontMetrics = paint.getFontMetrics();
            float centerX = ((float) rect.centerX()) - (paint.measureText(charSequence, 0, charSequence.length()) / 2.0f);
            float f = ((fontMetrics.top + (((float) (rect.bottom - rect.top)) - fontMetrics.bottom)) / 2.0f) + ((float) rect.top);
            //            canvas.save();
            canvas.translate(centerX, f);
            staticLayout.draw(canvas);
            //            canvas.restore();
        }
    }

    protected void drawBitmap2Rect(Bitmap bitmap, Canvas canvas, Rect rect, Paint paint) {
        canvas.drawBitmap(bitmap, (float) (rect.centerX() - (bitmap.getWidth() / 2)), (float) (rect.centerY() - (bitmap.getHeight() / 2)), paint);
    }

    protected void drawBall2Rect(int i, Canvas canvas, Rect rect, float f, Paint paint) {
        if (i < 0) {
            paint.setStyle(Style.STROKE);
            paint.setColor(-i);
        } else {
            paint.setStyle(Style.FILL);
            paint.setColor(i);
        }
        canvas.drawCircle(rect.exactCenterX(), (float) rect.centerY(), f, paint);
    }

    public boolean initOk() {
        return this.mInitOk;
    }


}
