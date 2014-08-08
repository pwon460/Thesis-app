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
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class TripActivityTemplate extends ListenerActivity implements
		OnItemClickListener, OnSharedPreferenceChangeListener {

	private TransportDAO transportDAO = new MockInformationExtractor();
	private ArrayList<String> displayedList;
	private CustomAdapter adapter;
	private ListView listview;
	private IndexButtonHandler handler;
	private static final int OFF = 1;

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
		getPrefVals();
		addIndexButtonsToLayout();
		handler = new IndexButtonHandler();
		handler.setNumBtns(getNumIndexBtns());

		prevListStates = new ArrayList<ArrayList<String>>();
		stack = new ArrayList<String>();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		applySettings();
	}

	private void getPrefVals() {
		getColorsFromPrefs();
		getTextSettingsFromPrefs();
		getNumIndexBtnsFromPrefs();
		Log.d("debug", "num btns on side = " + getNumIndexBtns());
	}

	private void addIndexButtonsToLayout() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.index_view);
		getLayoutInflater().inflate(R.layout.up_arrow, layout);
		for (int i = 0; i < getNumIndexBtns() - 2; i++) {
			getLayoutInflater().inflate(R.layout.index_button, layout);
		}
		getLayoutInflater().inflate(R.layout.down_arrow, layout);
	}

	// default behaviour all subclasses of this template should do:
	// add to stack and reset filter
	// by default, listview will highlight items clicked so no need to do
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		stack.add("listview");
		handler.resetFilter();
	}

	// filter list of stations/routes/suburbs/etc in the listview
	// also highlight button to indicate it's been clicked
	public void onIndexButtonClick(View view) {
		if (view.getId() == R.id.up_button) {
			Log.d("debug", "up button pressed");
			handler.handleUpClick();
			setIndexButtons();
		} else if (view.getId() == R.id.down_button) {
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
				if (btnText.length() < 2) {
					handler.handleIndexBtnClicked(btnText); // add to current
															// filter
				} else if (btnText.length() == 2) {
					handler.handleIndexBtnClicked(btnText.substring(btnText
							.length() - 1)); // add to current filter
				}
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
		listview.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		Log.d("debug", "measured height = " + listview.getMeasuredHeight());
		// use own custom layout for the listview
		listview.setAdapter(adapter);
	}

	@Override
	public void applySettings() {
		setIndexButtons();
		setArrowColor();
		Log.d("debug", "textcolor = " + getTextColor());
		Log.d("debug", "background color = " + getBackgroundColor());
		if (getInvertedness() == OFF) {
			Log.d("debug", "inverse mode OFF");
			adapter.setTextColor(getTextColor());
			adapter.setInverseMode(false);
		} else {
			Log.d("debug", "inverse mode ON");
			adapter.setTextColor(getBackgroundColor());
			adapter.setBackgroundColor(getTextColor());
			adapter.setInverseMode(true);
		}

		adapter.setTextSettings(getTextSettings());
		LinearLayout layout = (LinearLayout) findViewById(R.id.custom_layout_view);
		layout.setBackgroundColor(getBackgroundColor());
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
				temp.setBackgroundColor(getBackgroundColor());
				temp.setGravity(Gravity.CENTER);
				setBtnBackground(temp, ButtonBuilder.getBorderedRectangle(this,
						getTextColor()));
			}
		}
	}

	private void setArrowColor() {
		// color the up button
		Button upArrow;
		upArrow = (Button) findViewById(R.id.up_button);
		Drawable d = upArrow.getBackground();
		d.setColorFilter(getTextColor(), Mode.MULTIPLY);

		// color the down button
		Button downArrow;
		downArrow = (Button) findViewById(R.id.down_button);
		d = downArrow.getBackground();
		d.setColorFilter(getTextColor(), Mode.MULTIPLY);
	}

	public void setBtnBackground(View toReplace, Drawable replaceWith) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			toReplace.setBackground(replaceWith);
		} else {
			toReplace.setBackgroundDrawable(replaceWith);
		}
	}

	public TransportDAO getDataAccessObject() {
		return transportDAO;
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
