package simo.transport.helpers;

import simo.transport.templates.TripActivityTemplate;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeGestureListener extends SimpleOnGestureListener implements
		OnTouchListener {
	private TripActivityTemplate activity;
	private GestureDetector gDetector;
	private static final int SWIPE_MIN_DISTANCE = 140;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 150;

	public SwipeGestureListener() {
		super();
	}

	public SwipeGestureListener(Context context) {
		this(context, null);
	}

	public SwipeGestureListener(Context context, GestureDetector gDetector) {

		if (gDetector == null) {
			gDetector = new GestureDetector(context, this);
		}

		activity = (TripActivityTemplate) context;
		this.gDetector = gDetector;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
			if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH
					|| Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY) {
				return false;
			}
			if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) { // swipe up
				activity.getListHandler().handleUpSwipe();
				activity.setAdapterToList();
			} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) { // swipe
																		// down
				activity.getListHandler().handleDownSwipe();
				activity.setAdapterToList();
			}
		}

		return super.onFling(e1, e2, velocityX, velocityY);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.performClick();
		return gDetector.onTouchEvent(event);
	}

}