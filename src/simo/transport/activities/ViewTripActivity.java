package simo.transport.activities;

import java.util.ArrayList;
import java.util.Date;

import simo.transport.R;
import simo.transport.backend.TransportDAO;
import simo.transport.backend.TripInfo;
import simo.transport.helpers.DAOBuilder;
import simo.transport.helpers.LocationHandler;
import simo.transport.helpers.Speaker;
import simo.transport.helpers.TimeBroadcastReceiver;
import simo.transport.templates.BasicListenerActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

public class ViewTripActivity extends BasicListenerActivity {

	public static final int TRIP_INVALID = -1;
	public static final int TRIP_FINISHED = -2;
	private int tripIndex = 0;
	private TransportDAO transportDAO;
	private TripInfo info;
	private ArrayList<String> stops;
	private ArrayList<Date> times;
	private TimeBroadcastReceiver broadcastReceiver;
	private Speaker speaker;
	private LocationHandler locationHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_trip);
		setID(R.id.view_trip);

		Intent intent = getIntent();
		String transport = intent.getStringExtra("transport");
		String departureTime = intent.getStringExtra("departure");

		DAOBuilder builder = new DAOBuilder(this);
		builder.rebuildDAO(intent, transport);
		transportDAO = builder.getDAO();
		info = transportDAO.getTrip(transport, departureTime);
		stops = info.getOrderedStops();
		times = info.getOrderedTimes();
		setupTripInfo();
		if (!isGPSEnabled()) {
			broadcastReceiver = new TimeBroadcastReceiver(this);
		} else {
			locationHandler = new LocationHandler(this);
		}
	}

	@Override
	protected void onResume() {
		if (broadcastReceiver != null) {
			registerReceiver(broadcastReceiver, new IntentFilter(
					Intent.ACTION_TIME_TICK));
		}
		if (isAccessibilityEnabled()) {
			speaker = new Speaker(this);
			broadcastReceiver.setSpeaker(speaker);
		}
		locationHandler.resume();
		super.onResume();
	}

	@Override
	public void onPause() {
		if (broadcastReceiver != null) {
			unregisterReceiver(broadcastReceiver);
		}
		if (speaker != null) {
			speaker.shutdown();
		}
		locationHandler.stopGPSRequests();
		super.onPause();
	}

	private void setupTripInfo() {
		View v = findViewById(R.id.trip_info_btn);
		Button b = (Button) v;
		// System.out.println("'" + info.getTripInfo() + "'");
		String text = info.getTripInfo();
		b.setText(text);
		b.setContentDescription(text);
	}

	public int getTripIndex() {
		return tripIndex;
	}

	public void setTripIndex(int num) {
		tripIndex = num;
	}

	private void setNextStop(int tripIndex) {
		View v = findViewById(R.id.next_stop_btn);
		Button b = (Button) v;
		String text;
		if (tripIndex == TRIP_INVALID) {
			text = "Next stop: None";
		} else if (tripIndex == TRIP_FINISHED) {
			text = "Destination reached";
		} else if (tripIndex < stops.size()) {
			text = "Next stop: " + stops.get(tripIndex);
		} else {
			Log.d("error", "tripIndex error");
			text = "Next stop: None";
		}
		b.setText(text);
		b.setContentDescription(text);
	}

	private void setPrevStop(int tripIndex) {
		View v = findViewById(R.id.prev_stop_btn);
		Button b = (Button) v;
		String text;
		if (tripIndex > 0) {
			text = "Previous stop: " + stops.get(tripIndex - 1);
		} else {
			text = "Previous stop: None";
		}
		b.setText(text);
		b.setContentDescription(text);
	}

	private void setNumStopsLeft(int tripIndex) {
		View v = findViewById(R.id.num_trips_left_btn);
		Button b = (Button) v;
		int stopsLeft = stops.size() - tripIndex;
		String text;
		if (tripIndex == TRIP_FINISHED) {
			text = "Trip finished";
		} else if (tripIndex == TRIP_INVALID) {
			text = "Invalid trip";
		} else {
			text = "Stops left: " + stopsLeft;
		}
		b.setText(text);
		b.setContentDescription(text);
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder adb = new AlertDialog.Builder(
				new ContextThemeWrapper(this, android.R.style.Theme_Holo_Dialog));

		adb.setMessage("Click OK to go back")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						Intent intent = new Intent(((Dialog) dialog)
								.getContext(), SelectTransportActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = adb.create();
		alertDialog.show();

	}

	/*
	 * called on clicking stops left button
	 */
	public void checkDestProximity(View view) {
		if (speaker != null) {
			String message = "";
			Log.d("debug", "tripindex = " + tripIndex);
			if (tripIndex + 1 == stops.size() - 1) {
				message = "You are approaching your destination";
			} else if (tripIndex + 1 == stops.size()) {
				message = "Next stop is your destination";
			}
			speaker.speak(message, speaker.getMode());
		}
	}

	public void setViews() {
		setPrevStop(tripIndex);
		setNextStop(tripIndex);
		setNumStopsLeft(tripIndex);
		applySettings();
	}

	public Speaker getSpeaker() {
		return speaker;
	}

	public TransportDAO getDAO() {
		return transportDAO;
	}

	public ArrayList<Date> getTimes() {
		return times;
	}

	public ArrayList<String> getStops() {
		return stops;
	}

}
