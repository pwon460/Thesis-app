package simo.transport.activities;

import java.util.ArrayList;

import simo.transport.R;
import simo.transport.backend.TransportDAO;
import simo.transport.backend.TripInfo;
import simo.transport.helpers.DAOBuilder;
import simo.transport.helpers.Speaker;
import simo.transport.helpers.TimeBroadcastReceiver;
import simo.transport.templates.BasicListenerActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ViewTripActivity extends BasicListenerActivity {

	public static final int TRIP_INVALID = -1;
	public static final int TRIP_FINISHED = -2;
	private int tripIndex = 0;
	private TransportDAO transportDAO;
	private TripInfo info;
	private TimeBroadcastReceiver broadcastReceiver;
	private Speaker speaker;

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
		setupTripInfo();
		if (!isGPSEnabled()) {
			broadcastReceiver = new TimeBroadcastReceiver(this);
		} else {
			// TODO: GPS receiver?
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

	public TripInfo getTripInfo() {
		return info;
	}

	public int getTripIndex() {
		return tripIndex;
	}

	public void setTripIndex(int num) {
		tripIndex = num;
	}

	public void setNextStop(int tripIndex) {
		ArrayList<String> stops = info.getOrderedStops();
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

	public void setPrevStop(int tripIndex) {
		ArrayList<String> stops = info.getOrderedStops();
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

	public void setNumStopsLeft(int tripIndex) {
		ArrayList<String> stops = info.getOrderedStops();
		View v = findViewById(R.id.num_trips_left_btn);
		Button b = (Button) v;
		int endIndex = stops.size();
		int stopsLeft = endIndex - tripIndex;
		String text;
		if (tripIndex == TRIP_FINISHED) {
			text = "Trip finished";
		} else if (tripIndex == TRIP_INVALID) {
			text = "Stops left: N/A";
		} else {
			text = "Stops left: " + stopsLeft;
		}
		b.setText(text);
		b.setContentDescription(text);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, SelectTransportActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public Speaker getSpeaker() {
		return speaker;
	}
}
