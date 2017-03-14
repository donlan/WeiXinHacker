package dong.lan.weixinhacker.ui.custom;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import dong.lan.weixinhacker.R;


/**
 * Created by 梁桂栋 on 16-10-31 ： 下午11:42.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: map
 */

public class LabelTextView extends android.support.v7.widget.AppCompatTextView {

    private int shadowColor;
    private int strokeWidth;
    private int bgColor;
    private float roundRadius = 0;
    private boolean clickAnimation = true;
    private Paint bgPaint;
    private float shadowRadius = 0;
    private RectF bgRect;

    private boolean refresh = false;

    public LabelTextView(Context context) {
        this(context, null);
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bgColor = 0xFF4CAF50;
        shadowColor = 0XFF777777;
        strokeWidth = 0;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);
            bgColor = ta.getColor(R.styleable.LabelTextView_bg_color, bgColor);
            roundRadius = ta.getDimension(R.styleable.LabelTextView_radius, roundRadius);
            clickAnimation = ta.getBoolean(R.styleable.LabelTextView_clickAnimation, clickAnimation);
            shadowRadius = ta.getDimension(R.styleable.LabelTextView_shadowRadius, shadowRadius);
            shadowColor = ta.getColor(R.styleable.LabelTextView_shadowColor, shadowColor);
            strokeWidth = (int) ta.getDimension(R.styleable.LabelTextView_strokeWidth, 0);
            ta.recycle();
        }
        bgPaint = new Paint();
        if (strokeWidth > 0) {
            bgPaint.setStyle(Paint.Style.STROKE);
            bgPaint.setStrokeWidth(strokeWidth);
            bgPaint.setStrokeCap(Paint.Cap.ROUND);
            bgPaint.setStrokeJoin(Paint.Join.ROUND);
        }
        bgPaint.setColor(bgColor);
        bgPaint.setAntiAlias(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        bgPaint.setShadowLayer(shadowRadius, 0, 0, shadowColor);
    }


    public float getRoundRadius() {
        return roundRadius;
    }

    public void setRoundRadius(float roundRadius) {
        this.roundRadius = roundRadius;
        invalidate();
    }

    public boolean isClickAnimation() {
        return clickAnimation;
    }

    public void setClickAnimation(boolean clickAnimation) {
        this.clickAnimation = clickAnimation;
    }


    @Override
    public void invalidate() {
        refresh = true;
        super.invalidate();
    }

    @Override
    public float getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
        bgPaint.setShadowLayer(shadowRadius,0,0,shadowColor);
        invalidate();
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        bgPaint.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        bgPaint.setColor(bgColor);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float r = shadowRadius / 2+strokeWidth;
        if (bgRect == null) {
            bgRect = new RectF(r, r, getWidth() - r, getHeight() - r);
        } else if (refresh) {
            bgRect.set(r, r, getWidth() - r, getHeight() - r);
            refresh = false;
        }
        canvas.drawRoundRect(bgRect, roundRadius, roundRadius, bgPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (clickAnimation && event.getAction() == MotionEvent.ACTION_DOWN) {
            ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.9f, 1.0f).setDuration(200).start();
            ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.9f, 1.0f).setDuration(200).start();
            ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.5f, 1.0f).setDuration(200).start();
        }
        return super.onTouchEvent(event);
    }
}
