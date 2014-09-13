package simo.transport.activities;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import simo.transport.R;
import simo.transport.backend.MockTransportDAO;
import simo.transport.backend.TimetableItemInterface;
import simo.transport.backend.TransportDAOInterface;
import simo.transport.helpers.CustomAdapter;
import simo.transport.helpers.DisplayedListHandler;
import simo.transport.templates.BasicListenerActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ShowTimetableActivity extends BasicListenerActivity implements
		OnItemClickListener {

	private int numItemsShown;
	private ListView listview;
	private ArrayList<TimetableItemInterface> timetable;
	private CustomAdapter adapter;
	private DisplayedListHandler listHandler;
	private TransportDAOInterface transportDAO = new MockTransportDAO(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPrefSettings();
		setContentView(R.layout.activity_timetable);
		
		Intent intent = getIntent();
		String transport = intent.getStringExtra("transport");
		Log.d("debug", "transport type is " + transport);

		Resources res = getResources();
		if (transport.equals(res.getString(R.string.bus))) {
			Boolean isBySuburb = intent.getBooleanExtra("isBySuburb", false);
			if (isBySuburb) {
				String originSuburb = intent.getStringExtra("originSuburb");
				String destSuburb = intent.getStringExtra("destSuburb");
				String originStop = intent.getStringExtra("originStop");
				String destStop = intent.getStringExtra("destStop");
				transportDAO.setBySuburbInfo(originSuburb, destSuburb, originStop, destStop);
			} else {
				String route = intent.getStringExtra("route");
				String originStop = intent.getStringExtra("originStop");
				String destStop = intent.getStringExtra("destStop");
				transportDAO.setByStopInfo(route, originStop, destStop);
			}
		} else {
			String origin = intent.getStringExtra("origin");
			String destination = intent.getStringExtra("destination");
			transportDAO.setTrip(origin, destination);
		}
		
		timetable = transportDAO.getTimetable(transport);
		unpackTimetable(transport);

		listHandler = new DisplayedListHandler();
		listHandler.setNumItemsShown(numItemsShown);
		listHandler.setFullList(unpackTimetable(transport));

		listview = (ListView) findViewById(R.id.timetable_list);
		listview.setOnItemClickListener(this);

		setAdapterToList();
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

	@Override
	public void applySettings() {
		adapter.setTextColor(getTextColor());
		LinearLayout layout = (LinearLayout) findViewById(R.id.timetable_layout);
		layout.setBackgroundColor(getBackgroundColor());
	}

	private ArrayList<String> unpackTimetable(String transport) {
		ArrayList<String> list = new ArrayList<String>();

		for (TimetableItemInterface item : timetable) {
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
			diff = minDiff + " mins";
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
			startActivity(intent);
		}
	}

}
