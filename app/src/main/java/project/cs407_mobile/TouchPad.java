package project.cs407_mobile;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.NinePatchDrawable;
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

    public int offsetX;
    public int offsetY;

    private NinePatchDrawable mNpd;

    private Paint mPanelPaint;

    public Bitmap mPattern;

    private Rect mBoundingBox;

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

        mPattern = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.sample_tile);

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int bmWidth = mPattern.getWidth();
        final int bmHeight = mPattern.getHeight();

        for (int y = offsetY-mPattern.getHeight(), height = mBoundingBox.height(); y < height; y += bmHeight) {
            for (int x = offsetX-mPattern.getWidth(), width = mBoundingBox.width(); x < width; x += bmWidth) {
                canvas.drawBitmap(mPattern, x, y, null);
            }
        }

        mNpd.setBounds(mBoundingBox);
        mNpd.draw(canvas);

        //canvas.drawRoundRect(mBoundingBox, 15.0f, 15.0f, mPanelPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
        mBoundingBox = new Rect(0, 0, w, h);
        invalidate();
    }

    protected void init() {
        Log.d(TouchPad.class.getName(), "Constructed a TouchPad with parameters w="+mWidth+" h="+mHeight);

        mPanelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPanelPaint.setStyle(Paint.Style.FILL);
        mPanelPaint.setColor(0xffafafaf);

        offsetX = 0;
        offsetY = 0;

        mDetector = new GestureDetector(TouchPad.this.getContext(), new mListener());

        mNpd = (NinePatchDrawable) getResources().getDrawable(R.drawable.tx_ui_stitchbox);


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

            offsetX = Math.round((offsetX - dx)%mPattern.getWidth());
            offsetY = Math.round((offsetY - dy)%mPattern.getHeight());
            invalidate();
            Log.d("DEBUG", offsetX+", "+offsetY);
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Let the GestureDetector interpret this event
        boolean out = mDetector.onTouchEvent(event);
        Log.d("touch", "event: "+event.toString()+" "+out);
        return true;
    }
}
