package simo.transport.activities;

import java.util.ArrayList;

import simo.transport.R;
import simo.transport.helpers.ViewHolder;
import simo.transport.templates.TripActivityTemplate;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class TrainTripActivity extends TripActivityTemplate {

	private String startingPoint;
	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Select origin station");
		setListToDisplay(getDataAccessObject().getStations());
		Log.d("debug", "first call to set adapter list adapter");
		setAdapterToList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.train_origin, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id); 
		ViewHolder holder = (ViewHolder) view.getTag();
		TextView tv = holder.getTextView();
		if (startingPoint == null) {
			startingPoint = tv.getText().toString();
			index = position;
			Log.d("debug", "position is = " + position);
			Log.d("debug", "starting point is = " + startingPoint);
			getDisplayedList().remove(startingPoint);
			Log.d("debug", "removed " + startingPoint + " from the list");
			setTitle("Select destination station");
			// reset the list back to normal for person to choose destination
			ArrayList<String> listToDisplay = getDataAccessObject()
					.getStations();
			// remove starting point from the list
			listToDisplay.remove(startingPoint);
			setListToDisplay(listToDisplay);
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

	@Override
	public void onBackPressed() {
		if (getPrevAction().equals("indexBtn")) {
			// undo the action of the index button being pressed
			Log.d("debug", "restoring list to original");
			setListToDisplay(getPrevListState());
			setAdapterToList();
		} else if (getPrevAction().equals("listview")) {
			// button previously pressed was from listview
			// return dest list back to origin list
			Log.d("debug", "restoring list to original");
			getDisplayedList().add(index, startingPoint);
			startingPoint = null;
			setTitle("Select origin station");
			setAdapterToList();
		} else {
			super.onBackPressed();
		}
	}

}
