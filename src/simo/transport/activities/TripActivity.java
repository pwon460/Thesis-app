package simo.transport.activities;

import java.util.ArrayList;

import simo.transport.R;
import simo.transport.helpers.ViewHolder;
import simo.transport.templates.TripActivityTemplate;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class TripActivity extends TripActivityTemplate {

	private String startingPoint;
	private String transport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		transport = intent.getStringExtra("transport");

		Resources r = getResources();
		String title = "Select origin ";
		if (transport.equals(r.getString(R.string.train))) {
			title += "station";
			getListHandler().setFullList(getDataAccessObject().getStations());
		} else if (transport.equals(r.getString(R.string.ferry))) {
			title += "wharf";
			getListHandler().setFullList(getDataAccessObject().getWharfs());
		} else { // light rail
			title += "stop";
			getListHandler().setFullList(getDataAccessObject().getStops());
		}
		setTitle(title);
		setAdapterToList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		boolean handled = handleItemClick(view);

		if (!handled) {
			ViewHolder holder = (ViewHolder) view.getTag();
			TextView tv = holder.getTextView();
			if (startingPoint == null) {
				startingPoint = tv.getText().toString();
				/*
				 * case statement to handle title and reset the list back to
				 * normal for person to choose destination
				 */
				Resources r = getResources();
				ArrayList<String> tempList;
				String title = "Select destination ";
				if (transport.equals(r.getString(R.string.train))) {
					title += "station";
					tempList = getDataAccessObject().getStations();
				} else if (transport.equals(r.getString(R.string.ferry))) {
					title += "wharf";
					tempList = getDataAccessObject().getWharfs();
				} else {
					title += "stop";
					tempList = getDataAccessObject().getStops();
				}
				setTitle(title);

				// remove starting point from the soon-to-be list of destinations
				Log.d("debug", "removing " + startingPoint + " from the list");
				tempList.remove(startingPoint);
				getListHandler().setFullList(tempList);

				// set the list to the listview
				setAdapterToList();
			} else {
				String destination = tv.getText().toString();
				getDataAccessObject().setTrainTrip(startingPoint, destination);
				Intent intent = new Intent(this, ShowTripActivity.class);
				// TODO: figure out what to pass onwards
				startActivity(intent);
			}
		}
	}

	@Override
	public void onBackPressed() {
		String result = getPrevAction();
		if (result.equals("indexBtn")) {
			// undo the action of the index button being pressed
			getListHandler().restorePrevState();
			setAdapterToList();
		} else if (result.equals("listview")) {
			// button previously pressed was from listview
			// return dest list back to origin list
			Resources r = getResources();
			String title = "Select origin ";
			if (transport.equals(r.getString(R.string.train))) {
				title += "station";
				getListHandler().setFullList(getDataAccessObject().getStations());
			} else if (transport.equals(r.getString(R.string.ferry))) {
				title += "wharf";
				getListHandler().setFullList(getDataAccessObject().getWharfs());
			} else {
				title += "stop";
				getListHandler().setFullList(getDataAccessObject().getStops());
			}
			setTitle(title);
			startingPoint = null;
			setAdapterToList();
		} else {
			super.onBackPressed();
		}
	}

}