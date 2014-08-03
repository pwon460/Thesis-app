package simo.transport.ui;

import java.util.ArrayList;

import simo.transport.CustomAdapter;
import simo.transport.R;
import simo.transport.templates.TripActivityTemplate;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

public class TrainTripActivity extends TripActivityTemplate {

	private ArrayList<String> stations;
	private CustomAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Origin station");
		stations = getExtractor().getStations();
		ListView listView = (ListView) findViewById(R.id.list_view);
		adapter = new CustomAdapter(this, R.layout.list_row, stations);
		applySettings();
		// use own custom layout
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.train_trip, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void applySettings() {
		getColorsFromPrefs();
		getTextSettingsFromPrefs();
		adapter.setTextColor(getTextColor());
		Log.d("debug", "setting text color for adapter to " + getTextColor());
		adapter.setTextSettings(getTextSettings());
		Log.d("debug", "setting text settings for adapter to " + getTextSettings());
		LinearLayout layout = (LinearLayout) findViewById(R.id.custom_layout_view);
		layout.setBackgroundColor(getBackgroundColor());
		Log.d("debug", "setting background color to " + getBackgroundColor());
	}

}
