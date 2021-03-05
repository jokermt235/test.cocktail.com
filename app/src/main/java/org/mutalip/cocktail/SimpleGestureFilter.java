package org.mutalip.cocktail;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SimpleGestureFilter extends GestureDetector.SimpleOnGestureListener {
    public final static int SWIPE_LEFT = 1;
    private final static String TAG = "SimpleGestureFilter";
    private int swipe_Min_Distance = 100;
    private int swipe_Max_Distance = 512;
    private int swipe_Min_Velocity = 100;
    public final static int MODE_SOLID = 1;
    private boolean running = true;
    private GestureDetector detector;
    public final static int MODE_DYNAMIC = 2;
    private Activity context;
    private SimpleGestureListener listener;
    private int mode = MODE_DYNAMIC;
    public SimpleGestureFilter(Activity context,
                               SimpleGestureListener simpleGestureListener){
        this.context = context;
        listener = simpleGestureListener;
        this.detector = new GestureDetector(context, this);
        Log.d(TAG, "Gesture constructor");
    }
    public void onTouchEvent(MotionEvent event) {
        if (!this.running)
            return;

        boolean result = this.detector.onTouchEvent(event);
        if (this.mode == MODE_SOLID)
            event.setAction(MotionEvent.ACTION_CANCEL);
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        final float xDistance = Math.abs(e1.getX() - e2.getX());
        final float yDistance = Math.abs(e1.getY() - e2.getY());

        if (xDistance > this.swipe_Max_Distance
                || yDistance > this.swipe_Max_Distance)
            return false;

        velocityX = Math.abs(velocityX);

        if (velocityX > this.swipe_Min_Velocity
                && xDistance > this.swipe_Min_Distance) {
            if (e1.getX() > e2.getX()){
                listener.onSwipe(SWIPE_LEFT);
            }

        }
        return  true;
    }
    interface SimpleGestureListener {
        void onSwipe(int direction);
    }
}
