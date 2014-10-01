package simo.transport.helpers;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;

import simo.transport.activities.ViewTripActivity;
import simo.transport.backend.TransportDAO;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSHandler implements LocationListener {

	private static final long LOCATION_REFRESH_TIME = 3000; // every 3 secs
	private static final float LOCATION_REFRESH_DIST = 10; // every 10 m
	private ViewTripActivity activity;
	private TransportDAO transportDAO;
	private LocationManager locationManager;
	private ArrayList<String> stops;
	private ArrayList<Date> times;
	private TripTimeHandler tripTimeHandler;
	private DateTime now;

	public GPSHandler(ViewTripActivity activity) {
		this.activity = activity;
		this.transportDAO = activity.getDAO();
		stops = activity.getStops();
		times = activity.getTimes();
		tripTimeHandler = new TripTimeHandler();
		now = tripTimeHandler.roundToNearestMin(DateTime.now());
		locationManager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				LOCATION_REFRESH_TIME, LOCATION_REFRESH_DIST, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		int tripIndex = activity.getTripIndex();
		tripTimeHandler.adjustIndexByTime(new DateTime(times.get(0)), now, tripIndex);
		if (transportDAO.isValidTrip(location) && tripIndex != ViewTripActivity.TRIP_INVALID) {
			String nextStop = stops.get(tripIndex);
			if (transportDAO.isAtNextStop(location, nextStop)) {
				activity.setTripIndex(activity.getTripIndex() + 1);
				activity.setViews();
			}
		} else {
			activity.setTripIndex(tripIndex = ViewTripActivity.TRIP_INVALID);
			activity.setViews();
			locationManager.removeUpdates(this);
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// do nothing
	}

	@Override
	public void onProviderDisabled(String provider) {
		// do nothing
	}

}
