package simo.transport.helpers;

import java.util.TimerTask;

import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;

public class GPSTimerTask extends TimerTask {

	private LocationManager locationManager;
	private MyLocationListener locationListener;
	private Handler handler = new Handler();

	public GPSTimerTask(LocationManager locationManager,
			MyLocationListener locationListener) {
		this.locationManager = locationManager;
		this.locationListener = locationListener;
	}

	/*
	 * can only access android UI toolkit from inside the UI thread
	 * needs handler otherwise get error: "can't create handler inside
	 * thread that has not called looper.prepare()"
	 */
	@Override
	public void run() {
		handler.post(new Runnable() {
			public void run() {
				Log.d("debug", "timertask: requesting for location updates");
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			}
		});

	}

}
