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

	private static final int DEFAULT_VALUE = -1;
	public static final int TRIP_INVALID = -1;
	public static final int TRIP_FINISHED = -2;
	private int tripIndex = 0;
	private TransportDAO transportDAO;
	private TripInfo info;
	private ArrayList<String> stops;
	private ArrayList<Date> times;
	private TimeBroadcastReceiver broadcastReceiver;
	private LocationHandler locationHandler;
	private Speaker speaker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_trip);
		setID(R.id.view_trip);

		Intent intent = getIntent();
		String transport = intent.getStringExtra("transport");
		ArrayList<Integer> codes = intent.getIntegerArrayListExtra("codes");

		DAOBuilder builder = new DAOBuilder(this);
		builder.rebuildDAO(intent, transport);
		transportDAO = builder.getDAO();
		info = transportDAO.getTrip(transport, codes.get(0), codes.get(1),
				codes.get(2));
		stops = info.getOrderedStops();
		times = info.getOrderedTimes();
		setupTripInfo();
		if (stops == null || stops.isEmpty() || times == null
				|| times.isEmpty()) {
			tripIndex = TRIP_INVALID;
			setViews();
		} else {
			if (!isGPSEnabled()) {
				broadcastReceiver = new TimeBroadcastReceiver(this);
			} else {
				locationHandler = new LocationHandler(this);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (tripIndex != TRIP_INVALID) {
			if (broadcastReceiver != null) {
				registerReceiver(broadcastReceiver, new IntentFilter(
						Intent.ACTION_TIME_TICK));
			}
			if (isAccessibilityEnabled()) {
				Log.d("debug", "creating speaker");
				speaker = new Speaker(this);
				if (!isGPSEnabled()) {
					broadcastReceiver.setSpeaker(speaker);
				} else {
					locationHandler.setSpeaker(speaker);
				}
			}
			if (locationHandler != null) {
				locationHandler.resumeLocationRequests();
			}
		}
	}

	/*
	 * override text appearance for large text as it is otherwise too large to
	 * fit in the text boxes on the screen
	 */
	@Override
	public int getTextStyleID() {
		int id = super.getTextStyleID();

		if (id == R.style.LargeText) {
			id = R.style.MediumText;
		}

		return id;
	}

	@Override
	public void onPause() {
		if (speaker != null) {
			speaker.shutdown();
		}
		if (broadcastReceiver != null) {
			unregisterReceiver(broadcastReceiver);
		}
		if (locationHandler != null) {
			locationHandler.stopGPSRequests();
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
				message = "Transport nearing your destination";
			} else if (tripIndex + 1 == stops.size()) {
				message = "Next stop is your destination";
			} else if (tripIndex == TRIP_FINISHED) {
				message = "Destination reached, trip finished";
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
