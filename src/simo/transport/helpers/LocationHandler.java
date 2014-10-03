package simo.transport.helpers;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;

import simo.transport.activities.ViewTripActivity;
import simo.transport.backend.TransportDAO;
import simo.transport.templates.GPSActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationHandler implements GPSActivity {

	private static final long LOCATION_REFRESH_TIME = 0;
	private static final float LOCATION_REFRESH_DIST = 0;
	private ViewTripActivity activity;
	private TransportDAO transportDAO;
	private LocationManager locationManager;
	private MyLocationListener locationListener;
	private ArrayList<String> stops;
	private ArrayList<Date> times;
	private TripTimeHandler tripTimeHandler;
	private DateTime now;
	private int tripIndex;

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
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					LOCATION_REFRESH_TIME, LOCATION_REFRESH_DIST, locationListener);
		}
		
		activity.setViews();
	}
	
	@Override
	public void onLocationAcquired(Location location) {
		handleLocation(location);
	}

	private void handleLocation(Location location) {
		String nextStop = stops.get(tripIndex);
		if (transportDAO.isAtNextStop(location, nextStop)) {
			activity.setTripIndex(activity.getTripIndex() + 1);
			activity.setViews();
		}
	}

}
