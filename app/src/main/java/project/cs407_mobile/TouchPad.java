package project.cs407_mobile;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by u1421499 on 27/11/17.
 */

public class TouchPad extends View {

    private int mHeight;
    private int mWidth;

    private Paint mPanelPaint;

    private RectF mBoundingBox;

    private GestureDetector mDetector;

    public TouchPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TouchPad, 0, 0);
        try {
            mHeight = arr.getInteger(R.styleable.TouchPad_mHeight, 50);
            mWidth = arr.getInteger(R.styleable.TouchPad_mWidth, 50);
        } finally {
            arr.recycle();
        }

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(mBoundingBox, 15.0f, 15.0f, mPanelPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
        mBoundingBox = new RectF(0, 0, w, h);
    }

    protected void init() {
        Log.d(TouchPad.class.getName(), "Constructed a TouchPad with parameters w="+mWidth+" h="+mHeight);

        mPanelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPanelPaint.setStyle(Paint.Style.FILL);
        mPanelPaint.setColor(0xffafafaf);

        mDetector = new GestureDetector(TouchPad.this.getContext(), new mListener());

    }

    public void setDetector(GestureDetector ges) {
        mDetector = ges;
    }

    class mListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent eDown, MotionEvent eMove, float dx, float dy) {
            boolean consume = false;
            int sens = 10;
            if (dx > sens) {
                Log.d(TouchPad.class.getName(), "LEFT");
                consume = true;
            }
            if (dx < -(sens)) {
                Log.d(TouchPad.class.getName(), "RIGHT");
                consume = true;
            }
            if (dy > sens) {
                Log.d(TouchPad.class.getName(), "UP");
                consume = true;
            }
            if (dy < -(sens)) {
                Log.d(TouchPad.class.getName(), "DOWN");
                consume = true;
            }

            return consume;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Let the GestureDetector interpret this event
        return mDetector.onTouchEvent(event);
    }
}
