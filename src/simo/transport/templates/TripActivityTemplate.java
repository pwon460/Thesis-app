package simo.transport.templates;

import java.util.ArrayList;

import simo.transport.R;
import simo.transport.ViewHolder;
import simo.transport.backend.MockInformationExtractor;
import simo.transport.backend.TransportDAO;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class TripActivityTemplate extends SettingsListenerActivity implements
		OnItemClickListener, OnSharedPreferenceChangeListener {

	private TransportDAO extractor = new MockInformationExtractor();
	private ArrayList<String> clickedItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		clickedItems = new ArrayList<String>();
		setContentView(R.layout.custom_righthand_layout);
		ListView listview = (ListView) findViewById(R.id.list_view);
		listview.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trip_template, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public TransportDAO getExtractor() {
		return extractor;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		applySettings();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ViewHolder holder = (ViewHolder) view.getTag();
		TextView tv = holder.getTextView();
		clickedItems.add((String) tv.getText());
		Log.d("debug", "textview text is = " + tv.getText());
	}
}
