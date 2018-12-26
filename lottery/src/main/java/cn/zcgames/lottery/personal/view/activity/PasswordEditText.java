package cn.zcgames.lottery.personal.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import cn.berfy.sdk.mvpbase.util.DeviceUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.MyApplication;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * 自定义密码输入框
 */
public class PasswordEditText extends AppCompatEditText {

    private int textLength;

    private int borderColor;
    private float borderWidth;
    private float borderRadius;

    private int passwordLength;
    private int passwordColor;
    private float passwordWidth;
    private float passwordRadius;

    private Paint passwordPaint = new Paint(ANTI_ALIAS_FLAG);
    private Paint borderPaint = new Paint(ANTI_ALIAS_FLAG);

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        final Resources res = getResources();

        final int defaultBorderColor = res.getColor(R.color.color_CCCCCC);
        final float defaultBorderWidth = res.getDimension(R.dimen.default_ev_border_width);
        final float defaultBorderRadius = res.getDimension(R.dimen.default_ev_border_radius);

        final int defaultPasswordLength = res.getInteger(R.integer.default_ev_password_length);
        final int defaultPasswordColor = res.getColor(R.color.black);
        final float defaultPasswordWidth = res.getDimension(R.dimen.default_ev_password_width);
        final float defaultPasswordRadius = res.getDimension(R.dimen.default_ev_password_radius);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordEditText, 0, 0);
        try {
            borderColor = a.getColor(R.styleable.PasswordEditText_borderColor, defaultBorderColor);
            borderWidth = a.getDimension(R.styleable.PasswordEditText_borderWidth, defaultBorderWidth);
            borderRadius = a.getDimension(R.styleable.PasswordEditText_borderRadius, defaultBorderRadius);
            passwordLength = a.getInt(R.styleable.PasswordEditText_passwordLength, defaultPasswordLength);
            passwordColor = a.getColor(R.styleable.PasswordEditText_passwordColor, defaultPasswordColor);
            passwordWidth = a.getDimension(R.styleable.PasswordEditText_passwordWidth, defaultPasswordWidth);
            passwordRadius = a.getDimension(R.styleable.PasswordEditText_passwordRadius, defaultPasswordRadius);
        } finally {
            a.recycle();
        }

        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        passwordPaint.setStrokeWidth(passwordWidth);
        passwordPaint.setStyle(Paint.Style.FILL);
        passwordPaint.setColor(passwordColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        int defaultContMargin = 5;
        int defaultSplitLineWidth = DeviceUtils.dpToPx(MyApplication.getAppContext(), 1);

        @SuppressLint("DrawAllocation")//设置输入框边框
        RectF rect = new RectF(0, 0, width, height);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        canvas.drawRoundRect(rect, borderRadius, borderRadius, borderPaint);

        @SuppressLint("DrawAllocation")
        RectF rectIn = new RectF(rect.left + defaultContMargin, rect.top + defaultContMargin,
                rect.right - defaultContMargin, rect.bottom - defaultContMargin);
        borderPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectIn, borderRadius, borderRadius, borderPaint);

        //设置输入框分割线
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(defaultSplitLineWidth);
        for (int i = 1; i < passwordLength; i++) {
            float x = width * i / passwordLength;
            canvas.drawLine(x, 0, x, height, borderPaint);
        }

        float cx, cy = height / 2;
        float half = width / passwordLength / 2;
        for (int i = 0; i < textLength; i++) {
            cx = width * i / passwordLength + half;
            canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.textLength = text.toString().length();
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        borderPaint.setStrokeWidth(borderWidth);
        invalidate();
    }

    public float getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(float borderRadius) {
        this.borderRadius = borderRadius;
        invalidate();
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        invalidate();
    }

    public int getPasswordColor() {
        return passwordColor;
    }

    public void setPasswordColor(int passwordColor) {
        this.passwordColor = passwordColor;
        passwordPaint.setColor(passwordColor);
        invalidate();
    }

    public float getPasswordWidth() {
        return passwordWidth;
    }

    public void setPasswordWidth(float passwordWidth) {
        this.passwordWidth = passwordWidth;
        passwordPaint.setStrokeWidth(passwordWidth);
        invalidate();
    }

    public float getPasswordRadius() {
        return passwordRadius;
    }

    public void setPasswordRadius(float passwordRadius) {
        this.passwordRadius = passwordRadius;
        invalidate();
    }
}
