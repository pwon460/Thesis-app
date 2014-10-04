package simo.transport.helpers;

import simo.transport.templates.GPSLocationHandler;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {

	private GPSLocationHandler activity;

	public MyLocationListener (GPSLocationHandler activity) {
		this.activity = activity;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		activity.onLocationAcquired(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
