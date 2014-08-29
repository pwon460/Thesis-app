package simo.transport.templates;

import java.util.ArrayList;

import simo.transport.R;
import simo.transport.backend.MockInformationExtractor;
import simo.transport.backend.TransportDAO;
import simo.transport.helpers.ButtonBuilder;
import simo.transport.helpers.CustomAdapter;
import simo.transport.helpers.DisplayedListHandler;
import simo.transport.helpers.IndexButtonHandler;
import android.annotation.TargetApi;
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
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class TripActivityTemplate extends BasicListenerActivity implements
		OnItemClickListener, OnSharedPreferenceChangeListener {

	private int numItemsShown;
	private static final int OFF = 1;
	private static final int INDEX_BTN_WIDTH_MOD = 20;
	private TransportDAO transportDAO = new MockInformationExtractor();
	private CustomAdapter adapter;
	private ListView listview;
	private IndexButtonHandler indexHandler;
	private DisplayedListHandler listHandler;
	private int originalWidth = 0;
	private int originalHeight = 0;
	private String listname = "";

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
		if (isRightHandMode()) {
			setContentView(R.layout.custom_righthand_layout);
		} else {
			setContentView(R.layout.custom_lefthand_layout);
		}
		// add index buttons to this skeleton
		addIndexButtonsToLayout();

		listview = (ListView) findViewById(R.id.list_view);
		// add click listener so clicked items lead to a response
		listview.setOnItemClickListener(this);

		// initialize handler to take care of listview buttons
		listHandler = new DisplayedListHandler();
		listHandler.setNumItemsShown(numItemsShown);

		// initialize handler to take care of index buttons
		indexHandler = new IndexButtonHandler();
		indexHandler.setNumBtns(getNumIndexBtns());

		actionStack = new ArrayList<String>();

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
		numItemsShown = getNumItemsShown();
	}

	private void addIndexButtonsToLayout() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.index_view);
		getLayoutInflater().inflate(R.layout.up_arrow, layout);
		for (int i = 0; i < getNumIndexBtns() - 2; i++) {
			getLayoutInflater().inflate(R.layout.index_button, layout);
		}
		getLayoutInflater().inflate(R.layout.down_arrow, layout);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// overridden by children that will call the onItemClick method below
	}

	/*
	 * default behaviour all subclasses of this template should do: add to stack
	 * and reset filter by default, listview will highlight items clicked so no
	 * need to do
	 */
	public boolean handleArrowButtonClick(View view) {
		Boolean isHandled = false;
		TextView tv = (TextView) view;
//		Log.d("debug", "text = " + tv.getText());
		String text = tv.getText().toString();
		if (text.equals("^")) {
			listHandler.onUpClicked();
			setAdapterToList();
			isHandled = true;
		} else if (text.equals("v")) {
			listHandler.onDownClicked();
			setAdapterToList();
			isHandled = true;
		} else {
			indexHandler.resetFilter();
		}

		return isHandled;
	}

	// filter list of stations/routes/suburbs/etc in the listview
	// also highlight button to indicate it's been clicked
	public void onIndexButtonClick(View view) {
		if (view.getId() == R.id.up_button) {
//			Log.d("debug", "up button pressed");
			indexHandler.handleUpClick();
			setIndexButtons();
		} else if (view.getId() == R.id.down_button) {
//			Log.d("debug", "down button pressed");
			indexHandler.handleDownClick();
			setIndexButtons();
		} else {
			Button btnClicked = (Button) view;
			String btnClickedText = btnClicked.getText().toString();

			// ignore the index button click if it's a blank string
			if (!btnClickedText.equals("")) {
				Log.d("debug", "adding index button to stack: "
						+ btnClickedText);

				String filter = indexHandler.getFilter();
				// if the button clicked is the same as the filter, go up one
				// level
				if (btnClickedText.equals(filter)) {
					onBackPressed();
				} else {
					if (filter.length() == 1 && btnClickedText.length() == 1) {
						indexHandler.clearList();
						onBackPressed();
					}
					actionStack.add("indexBtn");
					indexHandler.handleIndexBtnClicked(btnClickedText);
					filterList();
					setAdapterToList();
				}
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
				listHandler.getDisplayedList(), numItemsShown);
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
//		Log.d("debug", "buttons to show: " + btnsToShow.toString());
		// Log.d("debug", "setting index buttons to display");

		View indexView = findViewById(R.id.index_view);
		ViewGroup group = (ViewGroup) indexView;
		int numButtons = group.getChildCount();
		// less one button both sides because arrow buttons don't need to be set
		for (int i = 1; i < numButtons - 1; i++) {
			Button temp = (Button) group.getChildAt(i);

			if (i - 1 >= btnsToShow.size() || indexHandler.getFilter().length() == 2) {
				temp.setText("");
				temp.setContentDescription("Unused ");
				setBtnBackground(temp, ButtonBuilder.getBlankRectangle(this));
			} else {
				String text = btnsToShow.get(i - 1);
				temp.setText(text);
				temp.setContentDescription("Filter " + listname +  " by: " + text + ", this is an index ");
				temp.setGravity(Gravity.CENTER);
				temp.setTextAppearance(this, R.style.IndexBtnText);
				temp.setTextColor(getTextColor());
				temp.setBackgroundColor(getBackgroundColor());

				// make box wider to indicate different 'tier' of index
				if (text.length() > 1) {
					/*
					 * save these values to return the button size back to
					 * normal on back button pressed
					 */
					if (originalWidth == 0 || originalHeight == 0) {
						originalWidth = temp.getMeasuredWidth();
						originalHeight = temp.getMeasuredHeight();
					}

					temp.setLayoutParams(new LinearLayout.LayoutParams(
							originalWidth + INDEX_BTN_WIDTH_MOD, originalHeight));

				} else if (text.length() == 1 && originalWidth > 0
						&& originalHeight > 0) {
					/*
					 * reset the button back to original size on back pressed
					 */
					temp.setLayoutParams(new LinearLayout.LayoutParams(
							originalWidth, originalHeight));

				}

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
		Log.d("debug", "back stack =" + actionStack);
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
	
	public IndexButtonHandler getIndexHandler() {
		return indexHandler;
	}
	
	public ArrayList<String> getActionStack() {
		return actionStack;
	}
	
	public void setListName(String newName) {
		listname = newName;
	}

}
