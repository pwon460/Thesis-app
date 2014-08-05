package simo.transport.templates;

import java.util.ArrayList;
import java.util.Locale;

import simo.transport.R;
import simo.transport.backend.MockInformationExtractor;
import simo.transport.backend.TransportDAO;
import simo.transport.helpers.ButtonBuilder;
import simo.transport.helpers.CustomAdapter;
import simo.transport.helpers.IndexButtonHandler;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class TripActivityTemplate extends SettingsListenerActivity implements
		OnItemClickListener, OnSharedPreferenceChangeListener {

	private TransportDAO transportDAO = new MockInformationExtractor();
	private ArrayList<String> displayedList;
	private CustomAdapter adapter;
	private ListView listview;
	private IndexButtonHandler handler;

	// stack for storing previous listview states
	private ArrayList<ArrayList<String>> prevListStates;

	// records whether an index button or a listview button was pushed
	private ArrayList<String> stack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the basic layout
		if (getHandedness() == 1) {
			setContentView(R.layout.custom_righthand_layout);
		} else {
			setContentView(R.layout.custom_lefthand_layout);
		}
		listview = (ListView) findViewById(R.id.list_view);
		listview.setOnItemClickListener(this);
		handler = new IndexButtonHandler();

		prevListStates = new ArrayList<ArrayList<String>>();
		stack = new ArrayList<String>();
		// setArrowColor();
	}

	// private void setArrowColor() {
	// Button upArrow = (Button) findViewById(R.id.rButtonUp);
	// Drawable drawable = ButtonBuilder.getColoredArrow(upArrow,
	// getTextColor());
	// setBtnBackground(upArrow, drawable);
	// Button downArrow = (Button) findViewById(R.id.rButtonDown);
	// drawable = ButtonBuilder.getColoredArrow(upArrow, getTextColor());
	// setBtnBackground(upArrow, drawable);
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.trip_template, menu);
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
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		applySettings();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		stack.add("listview");
		handler.resetFilter();
	}

	public TransportDAO getDataAccessObject() {
		return transportDAO;
	}

	// filter list of stations/routes/suburbs/etc in the listview
	public void onIndexButtonClick(View view) {
		if (view.getId() == R.id.rButtonUp || view.getId() == R.id.lButtonUp) {
			Log.d("debug", "up button pressed");
			handler.handleUpClick();
			setIndexButtons();
		} else if (view.getId() == R.id.rButtonDown
				|| view.getId() == R.id.lButtonDown) {
			Log.d("debug", "down button pressed");
			handler.handleDownClick();
			setIndexButtons();
		} else {
			Button btn = (Button) view;
			String btnText = btn.getText().toString();
			// ignore the index button click if it's a blank string
			if (!btnText.equals("")) {
				Log.d("debug", "adding index button to stack: " + btnText);
				stack.add("indexBtn");
				handler.handleIndexBtnClicked(btnText); // add to current filter
				filterList(handler.getFilter());
				setAdapterToList();
			}
		}

	}

	private void filterList(String filter) {
		prevListStates.add(displayedList); // save list current state
		Log.d("debug", "saving current list state: " + displayedList);
		Log.d("debug", "filtering list by: " + filter);
		ArrayList<String> tempList = new ArrayList<String>();
		for (int i = 0; i < displayedList.size(); i++) {
			String item = displayedList.get(i).toUpperCase(Locale.ENGLISH);
			if (item.startsWith(filter)) {
				Log.d("debug", "adding " + item);
				tempList.add(item);
			}
		}

		displayedList = tempList;
		Log.d("debug", "remaining list: " + displayedList);
		setAdapterToList();
	}

	public void setAdapterToList() {
		handler.setListToIndex(displayedList);
		Log.d("debug", "text color = " + getTextColor());
		adapter = new CustomAdapter(this, R.layout.list_row, displayedList);
		applySettings(); // get and apply preference settings to the adapter and
							// index buttons on right hand side
		// use own custom layout for the listview
		listview.setAdapter(adapter);
	}

	@Override
	public void applySettings() {
		getColorsFromPrefs();
		getTextSettingsFromPrefs();
		setIndexButtons();
		adapter.setTextColor(getTextColor());
		Log.d("debug", "setting text color for adapter to " + getTextColor());
		adapter.setTextSettings(getTextSettings());
		Log.d("debug", "setting text settings for adapter to "
				+ getTextSettings());
		LinearLayout layout = (LinearLayout) findViewById(R.id.custom_layout_view);
		layout.setBackgroundColor(getBackgroundColor());
		Log.d("debug", "setting background color to " + getBackgroundColor());
	}

	public void setIndexButtons() {
		ArrayList<String> btnsToShow = handler.getIndexBtnSubset();
		Log.d("debug", "buttons to show: " + btnsToShow.toString());

		View indexView = findViewById(R.id.index_view);
		ViewGroup group = (ViewGroup) indexView;
		int numButtons = group.getChildCount();
		// less one button both sides because arrow buttons don't need to be set
		for (int i = 1; i < numButtons - 1; i++) {
			LinearLayout innerLayout = (LinearLayout) group.getChildAt(i);
			Button temp = (Button) innerLayout.getChildAt(0);
			if (i - 1 >= btnsToShow.size() || displayedList.size() == 1) {
				temp.setText("");
				setBtnBackground(temp, ButtonBuilder.getBlankRectangle(this));
			} else {
				temp.setText(btnsToShow.get(i - 1));
				setTextSettings(temp);
				temp.setTextColor(getTextColor());
				temp.setGravity(Gravity.CENTER);
				setBtnBackground(temp, ButtonBuilder.getBorderedRectangle(this,
						getTextColor()));
			}

		}
	}

	public void setBtnBackground(Button toReplace, Drawable replaceWith) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			toReplace.setBackground(replaceWith);
		} else {
			toReplace.setBackgroundDrawable(replaceWith);
		}

	}

	public void setListToDisplay(ArrayList<String> listToDisplay) {
		displayedList = listToDisplay;
	}

	public ArrayList<String> getDisplayedList() {
		return displayedList;
	}

	public ListView getListView() {
		return listview;
	}

	public ArrayList<String> getPrevListState() {
		ArrayList<String> prevState = new ArrayList<String>();
		if (prevListStates.size() > 0) {
			prevState = prevListStates.remove(prevListStates.size() - 1);
		}
		return prevState;
	}

	public String getPrevAction() {
		String prevAction = "";
		if (stack.size() > 0) {
			prevAction = stack.remove(stack.size() - 1);
		}

		Log.d("debug", "prev button clicked = " + prevAction);
		if (prevAction.equals("indexBtn")) {
			handler.handleBackBtnClicked();
		}

		return prevAction;
	}

}
