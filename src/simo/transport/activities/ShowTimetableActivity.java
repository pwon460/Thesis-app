package simo.transport.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import simo.transport.R;
import simo.transport.backend.TimetableItem;
import simo.transport.backend.TransportDAO;
import simo.transport.helpers.CustomAdapter;
import simo.transport.helpers.DAOBuilder;
import simo.transport.helpers.DisplayedListHandler;
import simo.transport.helpers.GPSTimerTask;
import simo.transport.helpers.MyLocationListener;
import simo.transport.templates.BasicListenerActivity;
import simo.transport.templates.GPSLocationHandler;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ShowTimetableActivity extends BasicListenerActivity implements
		OnItemClickListener, GPSLocationHandler {

	private static final int SCHEDULE_LOCATION_REFRESH_TIMER = 120000;
	private int numItemsShown;
	private ListView listview;
	private ArrayList<TimetableItem> timetable;
	private CustomAdapter adapter;
	private DisplayedListHandler listHandler;
	private TransportDAO transportDAO;
	private Bundle bundle;
	private LocationManager locationManager;
	private MyLocationListener locationListener;
	private GPSTimerTask GPSTimerTask;
	private Timer timer;
	private boolean scheduled = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPrefSettings();
		setContentView(R.layout.activity_timetable);

		Intent intent = getIntent();
		bundle = intent.getExtras();
		String transport = intent.getStringExtra("transport");
		// Log.d("debug", "transport type is " + transport);

		DAOBuilder builder = new DAOBuilder(this);
		builder.rebuildDAO(intent, transport);
		transportDAO = builder.getDAO();

		timetable = transportDAO.getTimetable(transport);
		unpackTimetable(transport);

		listHandler = new DisplayedListHandler();
		listHandler.setNumItemsShown(numItemsShown);
		listHandler.setFullList(unpackTimetable(transport));

		listview = (ListView) findViewById(R.id.timetable_list);
		listview.setOnItemClickListener(this);

		setAdapterToList();

		if (isGPSEnabled()) {
			locationListener = new MyLocationListener(this);
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			timer = new Timer();
			GPSTimerTask = new GPSTimerTask(locationManager, locationListener);
		}
	}

	private void getPrefSettings() {
		loadColorsFromPrefs();
		numItemsShown = getNumItemsShown();
	}

	private void setAdapterToList() {
		adapter = new CustomAdapter(this, R.layout.list_row,
				listHandler.getDisplayedList(), numItemsShown);
		applySettings();
		listview.setAdapter(adapter);
	}

	private ArrayList<String> unpackTimetable(String transport) {
		ArrayList<String> list = new ArrayList<String>();

		for (TimetableItem item : timetable) {
			String timeDiff = calcDiff(item.getDepartureTime());
			String temp = timeDiff + item.getDescription() + ", "
					+ getTime(item.getDepartureTime()) + " - "
					+ getTime(item.getArrivalTime());
			list.add(temp);
		}

		return list;
	}

	private String getTime(Date d) {
		String time = "";
		DateTime dt = new DateTime(d);
		int hourInt = dt.getHourOfDay();
		int minInt = dt.getMinuteOfHour();
		boolean isAM = true;

		if (hourInt > 11) {
			if (hourInt > 12) {
				hourInt %= 12;
			}
			isAM = false;
		}

		time += hourInt;

		if (dt.getMinuteOfHour() < 10) {
			time += ":0";
		} else {
			time += ":";
		}

		time += minInt;

		if (isAM) {
			time += " AM";
		} else {
			time += " PM";
		}

		return time;
	}

	private String calcDiff(Date departureTime) {
		DateTime now = DateTime.now();
		DateTime departure = new DateTime(departureTime);
		int hourDiff = Hours.hoursBetween(now, departure).getHours() % 24;
		int minDiff = Minutes.minutesBetween(now, departure).getMinutes() % 60;
		boolean isNegative = false;

		if (hourDiff < 0) {
			hourDiff = 0 - hourDiff;
			isNegative = true;
		}

		if (minDiff < 0) {
			minDiff = 0 - minDiff;
			isNegative = true;
		}

		String diff;
		if (hourDiff > 0) {
			diff = hourDiff + " hrs, " + minDiff + " mins";
		} else {
			if (minDiff == 0) {
				diff = "< 1 min";
			} else {
				diff = minDiff + " mins";
			}
		}

		if (isNegative) {
			diff += " ago\n";
		} else {
			diff += " left\n";
		}

		return diff;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TextView tv = (TextView) view;
		String text = tv.getText().toString();
		if (text.equals("^")) {
			listHandler.onUpClicked();
			setAdapterToList();
		} else if (text.equals("v")) {
			listHandler.onDownClicked();
			setAdapterToList();
		} else {
			Intent intent = new Intent(this, ViewTripActivity.class);
			intent.putExtras(bundle);
			String time = getDepartureTime(text);
			intent.putExtra("departure", time);
			startActivity(intent);
		}
	}

	private String getDepartureTime(String text) {
		Log.d("debug", text);
		String[] parts = text.split(", ");
		String timeDelimiter = " - ";
		int i = 0;
		for (String s : parts) {
			// Log.d("debug", "index " + i + "=" + s);
			if (s.contains(timeDelimiter)) {
				// Log.d("debug", "break");
				break;
			}
			i++;
		}
		String[] times = parts[i].split(timeDelimiter);
		// Log.d("debug", times[0] + ", " + times[1]);
		return times[0];
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isGPSEnabled()) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}

	}

	@Override
	public void applySettings() {
		adapter.setTextColor(getTextColor());
		LinearLayout layout = (LinearLayout) findViewById(R.id.timetable_layout);
		layout.setBackgroundColor(getBackgroundColor());
	}

	/*
	 * battery-saver: every 2 mins, start listening for updated location instead
	 * of constantly listening for location forever
	 */
	@Override
	public void onLocationAcquired(Location location) {
		locationManager.removeUpdates(locationListener);
		Log.d("debug", "removing listener");
		if (!scheduled) {
			Log.d("debug", "scheduling location updates");
			timer.schedule(GPSTimerTask, SCHEDULE_LOCATION_REFRESH_TIMER);
			scheduled = true;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		timer.cancel();
		scheduled = false;
		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
			Log.d("debug", "recurring task cancelled, listener removed");
		}
	}
}
