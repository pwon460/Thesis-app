package simo.transport.templates;

import java.util.ArrayList;

import simo.transport.R;
import simo.transport.backend.MockInformationExtractor;
import simo.transport.backend.TransportDAO;
import simo.transport.helpers.ButtonBuilder;
import simo.transport.helpers.CustomAdapter;
import simo.transport.helpers.DisplayedListHandler;
import simo.transport.helpers.IndexButtonHandler;
import simo.transport.helpers.SwipeGestureListener;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class TripActivityTemplate extends BasicListenerActivity implements
		OnItemClickListener, OnSharedPreferenceChangeListener {

	private static final int NUM_ITEMS_SHOWN = 4;
	private static final int OFF = 1;
	private TransportDAO transportDAO = new MockInformationExtractor();
	private CustomAdapter adapter;
	private ListView listview;
	private IndexButtonHandler indexHandler;
	private DisplayedListHandler listHandler;
	private SwipeGestureListener gestureListener;

	// records whether an index button or a listview button was pushed
	private ArrayList<String> actionStack;

	// this create method will be utilized by all subclasses IN ADDITION
	// to the subclass' own create method
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// grab preferences from settings eg.
		loadPrefVals();
		// set the basic skeleton layout
		if (getHandedness() == 1) {
			setContentView(R.layout.custom_lefthand_layout);
		} else {
			setContentView(R.layout.custom_righthand_layout);
		}
		// add index buttons to this skeleton
		addIndexButtonsToLayout();

		listview = (ListView) findViewById(R.id.list_view);
		// add click listener so clicked items lead to a response
		listview.setOnItemClickListener(this);
		// add swipe listener to listen for swipe up/down
		// and call the setAdapterToList on swipe detected
		gestureListener = new SwipeGestureListener(this);
		listview.setOnTouchListener(gestureListener);

		// initialize handler to take care of listview buttons
		listHandler = new DisplayedListHandler();
		listHandler.setNumItemsShown(NUM_ITEMS_SHOWN);

		// initialize handler to take care of index buttons
		indexHandler = new IndexButtonHandler();
		indexHandler.setNumBtns(getNumIndexBtns());

		actionStack = new ArrayList<String>();

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		applySettings();
	}

	/*
	 * figure out which setting is currently selected and save to fields with
	 * getter methods. these settings are slightly tricky as the choices require
	 * pre-handling and thus need to be handled separately instead of a simple
	 * get statement
	 */
	private void loadPrefVals() {
		loadColorsFromPrefs();
		loadNumIndexBtnsFromPrefs();
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
		actionStack.add("listview");
		indexHandler.resetFilter();
	}

	// filter list of stations/routes/suburbs/etc in the listview
	// also highlight button to indicate it's been clicked
	public void onIndexButtonClick(View view) {
		if (view.getId() == R.id.up_button) {
			Log.d("debug", "up button pressed");
			indexHandler.handleUpClick();
			setIndexButtons();
		} else if (view.getId() == R.id.down_button) {
			Log.d("debug", "down button pressed");
			indexHandler.handleDownClick();
			setIndexButtons();
		} else {
			Button btn = (Button) view;
			String btnText = btn.getText().toString();
			// ignore the index button click if it's a blank string
			if (!btnText.equals("")) {
				Log.d("debug", "adding index button to stack: " + btnText);
				actionStack.add("indexBtn");
				if (btnText.length() < 2) {
					indexHandler.handleIndexBtnClicked(btnText); // add to
																	// current
					// filter
				} else if (btnText.length() == 2) {
					indexHandler.handleIndexBtnClicked(btnText
							.substring(btnText.length() - 1)); // add to current
																// filter
				}
				filterList();
				setAdapterToList();
			}
		}

	}

	private void filterList() {
		listHandler.saveCurrListState(); // save list current state
		Log.d("debug",
				"saving current list state: " + listHandler.getFullList());
		Log.d("debug", "filtering list by: " + indexHandler.getFilter());
		listHandler.filterList(indexHandler.getFilter());
		Log.d("debug", "remaining list: " + listHandler.getFullList());
		setAdapterToList();
	}

	public void setAdapterToList() {
		indexHandler.setListToIndex(listHandler.getFullList());
		adapter = new CustomAdapter(this, R.layout.list_row,
				listHandler.getDisplayedList(), NUM_ITEMS_SHOWN);
		applySettings(); // get and apply preference settings to the new adapter
							// and
							// index buttons on right hand side
		// use own custom layout for the listview
		listview.setAdapter(adapter);
	}

	@Override
	public void applySettings() {
		setIndexButtons();
		setArrowColor();
		if (getInvertedness() == OFF) {
			adapter.setTextColor(getTextColor());
			adapter.setInverseMode(false);
		} else {
			adapter.setTextColor(getBackgroundColor());
			adapter.setBackgroundColor(getTextColor());
			adapter.setInverseMode(true);
		}

		adapter.setTextSettings(getTextSettings());
		LinearLayout layout = (LinearLayout) findViewById(R.id.custom_layout_view);
		layout.setBackgroundColor(getBackgroundColor());
	}

	public void setIndexButtons() {
		ArrayList<String> btnsToShow = indexHandler.getIndexBtnSubset();
		Log.d("debug", "buttons to show: " + btnsToShow.toString());

		View indexView = findViewById(R.id.index_view);
		ViewGroup group = (ViewGroup) indexView;
		int numButtons = group.getChildCount();
		// less one button both sides because arrow buttons don't need to be set
		for (int i = 1; i < numButtons - 1; i++) {
			LinearLayout innerLayout = (LinearLayout) group.getChildAt(i);
			Button temp = (Button) innerLayout.getChildAt(0);
			if (i - 1 >= btnsToShow.size()
					|| listHandler.getFullList().size() == 1) {
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

	public ListView getListView() {
		return listview;
	}

	public String getPrevAction() {
		String prevAction = "";
		if (actionStack.size() > 0) {
			prevAction = actionStack.remove(actionStack.size() - 1);
		}

		Log.d("debug", "prev button clicked = " + prevAction);
		if (prevAction.equals("indexBtn")) {
			indexHandler.handleBackBtnClicked();
		}

		return prevAction;
	}

	public DisplayedListHandler getListHandler() {
		return listHandler;
	}

}
