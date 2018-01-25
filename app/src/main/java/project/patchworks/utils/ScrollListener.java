package project.patchworks.utils;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import project.patchworks.activities.ControllerActivity;
import project.patchworks.views.TouchpadView;

public class ScrollListener extends GestureDetector.SimpleOnGestureListener {

    TouchpadView mView;
    Connection connection;

    public ScrollListener(TouchpadView t, Connection c) {
        mView = t;
        connection = c;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("touch", "down on touchpad");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent eDown, MotionEvent eMove, float dx, float dy) {

        Log.d("touch", "scroll on touchpad: " + dx/mView.getWidth() + "," + dy/mView.getHeight());

        if (!ControllerActivity.controllerDebug)
            connection.sendMessage(dx/mView.getWidth() + "," +dy/mView.getHeight() );

        mView.offsetX = Math.round((mView.offsetX - dx)%mView.mPattern.getWidth());
        mView.offsetY = Math.round((mView.offsetY - dy)%mView.mPattern.getHeight());
        mView.invalidate();

        return true;

    }

}