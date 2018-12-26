package cn.zcgames.lottery.home.view.fragment.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Scroller;

import cn.berfy.sdk.mvpbase.util.LogF;


public class LottoTrendView extends View implements OnDoubleTapListener, OnGestureListener, OnTouchListener {
    private ITrendChart mChart;
    private GestureDetector mGestureDetector;
    private float mLastDistance;
    final int mMinDistance;
    private float mNowX;
    private float mNowY;
    final int mPosition3;
    private float mScale;
    private Scroller mScroller;
    private SetMoveXPositionListener moveXPositionListener;

    public LottoTrendView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMinDistance = 10;
        this.mNowX = 0.0f;
        this.mNowY = 0.0f;
        this.mScale = 1.0f;
        this.mLastDistance = 0.0f;
        this.mPosition3 = 3;
        initView();
    }

    public LottoTrendView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LottoTrendView(Context context) {
        this(context, null, 0);
    }

    public void setChart(ITrendChart iTrendChart) {
        this.mChart = iTrendChart;
    }

    public void setNowX(float f) {
        this.mNowX = f;
        refreshPos();
    }

    public void setNowY(float f) {
        this.mNowY = f;
        refreshPos();
    }

    public void setScale(float f) {
        this.mScale = f;
        if (this.mChart != null && this.mChart.getCanScale()) {
            float[] scaleRange = this.mChart.getScaleRange();
            this.mScale = Math.max(scaleRange[0], Math.min(this.mScale, scaleRange[1]));
        }
        refreshPos();
    }

    public float getScale() {
        return this.mScale;
    }

    @SuppressLint({"NewApi"})
    public static void setHardwareAccelerated(View view, boolean z) {
        if (VERSION.SDK_INT < 11) {
            return;
        }
        if (z) {
            view.setLayerType(2, null);
        } else {
            view.setLayerType(1, null);
        }
    }

    private void initView() {
        this.mGestureDetector = new GestureDetector(getContext(), this);
        this.mGestureDetector.setOnDoubleTapListener(this);
        this.mScroller = new Scroller(getContext());
        setOnTouchListener(this);
    }

    public boolean performClick() {
        return super.performClick();
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        if (this.mChart != null) {
            this.mChart.initChart(getContext(), i, i2, this.mScale);
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mChart != null && this.mChart.initOk()) {
            if (this.mScroller.computeScrollOffset()) {
                boolean[] canScroll = this.mChart.getCanScroll();
                if (canScroll[0]) {
                    this.mNowX = (float) this.mScroller.getCurrX();
                }
                if (canScroll[1]) {
                    this.mNowY = (float) this.mScroller.getCurrY();
                }
                postInvalidate();
            }
            Log.d("111111", "mNowX="+mNowX+",mNowY= "+mNowY);
            this.mChart.draw(canvas, (int) this.mNowX, (int) this.mNowY, getWidth(), getHeight(), this.mScale);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (this.mChart == null || !this.mChart.initOk()) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsCurrentTouch = true;
                LogF.d("走势图 滑动", "按下");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsCurrentTouch = false;
                LogF.d("走势图 滑动", "抬起");
                break;
        }
        if (this.mChart.getCanScale() && motionEvent.getPointerCount() == 2 && motionEvent.getAction() == 2) {
            float x = motionEvent.getX(0) - motionEvent.getX(1);
            float y = motionEvent.getY(0) - motionEvent.getY(1);
            x = (float) Math.sqrt((double) ((x * x) + (y * y)));
            if (this.mLastDistance == 0.0f) {
                this.mLastDistance = x;
                return true;
            } else if (Math.abs(x - this.mLastDistance) <= 10) {
                return true;
            } else {
                float[] scaleRange = this.mChart.getScaleRange();
                this.mScale = Math.max(scaleRange[0], Math.min((this.mScale * x) / this.mLastDistance, scaleRange[1]));
                this.mLastDistance = x;
                refreshPos();
                return true;
            }
        }
        if (motionEvent.getPointerCount() < 2) {
            this.mLastDistance = 0.0f;
        }
        return this.mGestureDetector.onTouchEvent(motionEvent);
    }

    public boolean onDown(MotionEvent motionEvent) {
        if (!this.mScroller.isFinished()) {
            this.mScroller.forceFinished(true);
        }
        this.mLastDistance = 0.0f;
        return true;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        if (!this.mChart.onClick(motionEvent, this.mNowX, this.mNowY, getWidth(), getHeight(), this.mScale)) {
            return false;
        }
        invalidate();
        return true;
    }

    private float mLastX;
    private boolean mIsCurrentTouch;//当前走势图滑动，不允许外部控制
    private boolean mIsControllerScroll;//是否被控制

    /**
     * 控制走势图滑动
     *
     * @param isScrolling 是否正在滑动  停止滑动=faslt
     */
    public void setMoveX(boolean isScrolling, int dx) {
//        mIsControllerScroll = isScrolling;
        //没想好怎么滑动走势图
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mIsControllerScroll && !mIsCurrentTouch) {
            return;
        }
        int dx = (int) (mScroller.getCurrX() - mLastX);
        if (dx != 0) {
            if (null != moveXPositionListener)
                moveXPositionListener.setMoveX((int) (mScroller.getCurrX() - mLastX));
            LogF.d("scroll滑动computeScroll   ", (mScroller.getCurrX() - mLastX) + " ");
        }
        mLastX = mScroller.getCurrX();
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (null != moveXPositionListener && (!mIsControllerScroll || mIsCurrentTouch)) {
            LogF.d("scroll滑动onScroll   ", -f + " ");
            moveXPositionListener.setMoveX((int) (-f));
        }
        boolean[] canScroll = this.mChart.getCanScroll();
        if (!canScroll[0] && !canScroll[1]) {
            return false;
        }
        int[] scrollRange = this.mChart.getScrollRange();
        if (canScroll[0]) {
            this.mNowX -= f;
            this.mNowX = Math.max((float) scrollRange[0], Math.min(this.mNowX, (float) scrollRange[1]));
        }
        if (canScroll[1]) {
            this.mNowY -= f2;
            this.mNowY = Math.max((float) scrollRange[2], Math.min(this.mNowY, (float) scrollRange[3]));
        }
        float mLastX = motionEvent.getX();
        LogF.d("111111", "2222滑动的mLastX距离====" + mLastX);
        float x = motionEvent2.getX();
        LogF.d("111111", "2222滑动的x距离====" + x);
        int dx = (int) (x - mLastX);
        LogF.d("111111", "2滑动的x距离====" + dx);
        invalidate();
        return true;
    }

    public void onLongPress(MotionEvent motionEvent) {
        if (this.mChart.onLongClick(motionEvent, this.mNowX, this.mNowY, getWidth(), getHeight(), this.mScale)) {
            invalidate();
        }
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.mScroller.forceFinished(true);
        int[] scrollRange = this.mChart.getScrollRange();
        this.mScroller.fling((int) this.mNowX, (int) this.mNowY, (int) f, (int) f2, scrollRange[0], scrollRange[1], scrollRange[2], scrollRange[3]);
        return true;
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    public boolean onDoubleTap(MotionEvent motionEvent) {
        this.mScale = 1.0f;
        refreshPos();
        return false;
    }

    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    public void refreshPos() {
        this.mScroller.forceFinished(true);
        if (this.mChart != null) {
            this.mChart.reCalcScroll(this.mScale, getWidth(), getHeight());
            int[] scrollRange = this.mChart.getScrollRange();
            this.mNowX = Math.min((float) scrollRange[1], Math.max((float) scrollRange[0], this.mNowX));
            this.mNowY = Math.min((float) scrollRange[3], Math.max((float) scrollRange[2], this.mNowY));
            invalidate();
        }
    }

    //水平方向移动
    public interface SetMoveXPositionListener {
        void setMoveX(int xPosition);
    }

    public void setMoveXListener(SetMoveXPositionListener listener) {
        this.moveXPositionListener = listener;
    }
}
