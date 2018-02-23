package patchworks.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;

import patchworks.R;

public class TouchpadView extends View {

    private int mHeight;
    private int mWidth;

    public int offsetX;
    public int offsetY;

    private NinePatchDrawable mNpd;

    private Paint mPanelPaint;

    public Bitmap mPattern;

    private Rect mBoundingBox;

    private GestureDetector mDetector;

    public TouchpadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TouchpadView, 0, 0);
        try {
            mHeight = arr.getInteger(R.styleable.TouchpadView_mHeight, 50);
            mWidth = arr.getInteger(R.styleable.TouchpadView_mWidth, 50);
        } finally {
            arr.recycle();
        }

        Resources res = context.getResources();

        mPattern = BitmapFactory.decodeResource(res, R.drawable.sample_tile);



        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Path path = new Path();
        path.addRoundRect(new RectF(canvas.getClipBounds()), 40f, 40f, Path.Direction.CCW);
        canvas.clipPath(path);   // give touchpad rounded corners

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
        Log.d(TouchpadView.class.getName(), "Constructed a TouchPad with parameters w="+mWidth+" h="+mHeight);

        mPanelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPanelPaint.setStyle(Paint.Style.FILL);
        mPanelPaint.setColor(0xffafafaf);

        offsetX = 0;
        offsetY = 0;

        mNpd = (NinePatchDrawable) getResources().getDrawable(R.drawable.tx_ui_stitchbox);
    }

    public void setDetector(GestureDetector ges) {
        mDetector = ges;
        mDetector.setIsLongpressEnabled(false);   // don't consume long press events (otherwise scroll not consumed)
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);   // let the GestureDetector interpret this event
    }

}