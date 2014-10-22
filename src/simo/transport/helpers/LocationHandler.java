package simo.transport.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import org.joda.time.DateTime;

import simo.transport.activities.ViewTripActivity;
import simo.transport.backend.TransportDAO;
import simo.transport.templates.GPSLocationHandler;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationHandler implements GPSLocationHandler {

	private static final long MIN_LOCATION_REFRESH_TIME = 0;
	private static final float MIN_LOCATION_REFRESH_DIST = 0;
	private static final long SCHEDULE_LOCATION_REQUEST_TIMER = 60000;
	private ViewTripActivity activity;
	private TransportDAO transportDAO;
	private LocationManager locationManager;
	private MyLocationListener locationListener;
	private ArrayList<String> stops;
	private ArrayList<Date> times;
	private TripTimeHandler tripTimeHandler;
	private DateTime now;
	private int tripIndex;
	private Timer timer;
	private GPSTimerTask GPSTimerTask;
	private boolean scheduled = false;
	private Speaker speaker;

	public LocationHandler(ViewTripActivity activity) {
		this.activity = activity;
		this.transportDAO = activity.getDAO();
		stops = activity.getStops();
		times = activity.getTimes();
		tripTimeHandler = new TripTimeHandler();
		now = tripTimeHandler.roundToNearestMin(DateTime.now());
		locationManager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener(this);
		timer = new Timer();
		GPSTimerTask = new GPSTimerTask(locationManager, locationListener);
		initViewTripScreen();
	}

	private void initViewTripScreen() {
		Location lastKnownLocation = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		handleInitialLocation(lastKnownLocation);

	}

	private void handleInitialLocation(Location location) {
		tripIndex = activity.getTripIndex();
		tripTimeHandler.adjustIndexByTime(new DateTime(times.get(0)), now,
				tripIndex);

		boolean isValidTrip = transportDAO.isValidTrip(location);

		if (!isValidTrip || tripIndex == ViewTripActivity.TRIP_INVALID) {
			Log.d("debug", "invalid trip, removing location listener");
			locationManager.removeUpdates(locationListener);
		}

		if (!isValidTrip) {
			activity.setTripIndex(tripIndex = ViewTripActivity.TRIP_INVALID);
		} else {
			Log.d("debug", "requesting for updates");
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, MIN_LOCATION_REFRESH_TIME,
					MIN_LOCATION_REFRESH_DIST, locationListener);
		}

		activity.setViews();
	}

	/*
	 * battery save until there's only two stops left then leave the listener to
	 * be on all the time
	 */
	@Override
	public void onLocationAcquired(Location location) {
		handleLocation(location);
		if (activity.getTripIndex() < stops.size() - 2) {
			locationManager.removeUpdates(locationListener);
			Log.d("debug", "removing listener");
			if (!scheduled) {
				Log.d("debug", "scheduling location updates");
				timer.schedule(GPSTimerTask, SCHEDULE_LOCATION_REQUEST_TIMER);
				scheduled = true;
			}
		}
	}

	public void resumeLocationRequests() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_LOCATION_REFRESH_TIME, MIN_LOCATION_REFRESH_DIST,
				locationListener);
	}

	private void handleLocation(Location location) {
		Log.d("debug", "location received!");
		String nextStop = stops.get(tripIndex);
		if (transportDAO.isAtNextStop(location, nextStop)) {
			if (tripIndex == stops.size() - 1) {
				speaker.speak("Arriving at destination", speaker.getMode());
			} else if (tripIndex == stops.size() - 2) {
				speaker.speak("Arriving at second last stop: " + nextStop, speaker.getMode());
			} else {
				speaker.speak("Arriving at " + nextStop, speaker.getMode());
			}
			int tripIndex = activity.getTripIndex();
			Log.d("debug", "pre increment trip index = " + tripIndex);
			if (tripIndex < stops.size() - 1) {
				activity.setTripIndex(tripIndex + 1);
			} else {
				activity.setTripIndex(ViewTripActivity.TRIP_FINISHED);
			}
			Log.d("debug", "post increment trip index = " + activity.getTripIndex());
			activity.setViews();
		}
	}
	
	public void stopGPSRequests() {
		timer.cancel();
		scheduled = false;
		locationManager.removeUpdates(locationListener);
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}

}
