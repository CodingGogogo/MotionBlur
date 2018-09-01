package com.example.lingye.motionblurdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * a textView which text bitmap will make a motion blur filter.
 */
public class MotionBlurTextView extends TextView {
    /**
     * the offline canvas to draw the text bitmap before motion blur.
     */
    private Canvas mOffCanvas = new Canvas();

    /**
     * the text bitmap before motion blur.
     */
    private Bitmap mTextBmp;

    private Paint mPaint = new Paint();
    public MotionBlurTextView(Context context) {
        super(context);
    }

    public MotionBlurTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MotionBlurTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(mOffCanvas);
        Bitmap bitmap = MotionBlur.bitmapMotionBlur(getContext(), mTextBmp, 7, 2);
        if(null != bitmap) {
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTextBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mOffCanvas.setBitmap(mTextBmp);
    }
}
