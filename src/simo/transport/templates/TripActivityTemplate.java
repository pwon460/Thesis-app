package simo.transport.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import simo.transport.R;
import simo.transport.backend.MockTransportDAO;
import simo.transport.backend.TransportDAO;
import simo.transport.helpers.ButtonBuilder;
import simo.transport.helpers.CustomAdapter;
import simo.transport.helpers.DisplayedListHandler;
import simo.transport.helpers.IndexButtonHandler;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public abstract class TripActivityTemplate extends BasicListenerActivity
		implements OnItemClickListener {

	public static final String LIST_BTN = "listview";
	public static final String INDEX_BTN = "index";
	private int numItemsShown;
	private static final int OFF = 1;
	private TransportDAO transportDAO = new MockTransportDAO(this);
	private CustomAdapter adapter;
	private ListView listview;
	private IndexButtonHandler indexHandler;
	private DisplayedListHandler listHandler;

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
	 * only this template and its children needs these values so set them up
	 * during onCreate() of this activity
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

		for (int i = 0; i < getNumIndexBtns(); i++) {
			View temp = layout.getChildAt(i);
			if (hasHoverListener()) {
				temp.setOnHoverListener(this);
			}
		}
	}

	@Override
	public abstract void onItemClick(AdapterView<?> parent, View view,
			int position, long id);

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

		LinearLayout layout = (LinearLayout) findViewById(R.id.custom_layout_view);
		layout.setBackgroundColor(getBackgroundColor());
	}

	/*
	 * default behaviour all subclasses of this template should do: add to stack
	 * and reset filter by default, listview will highlight items clicked so no
	 * need to do
	 */
	public boolean handleArrowButtonClick(View view) {
		Boolean isHandled = false;
		TextView tv = (TextView) view;
		// Log.d("debug", "text = " + tv.getText());
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
	public void onIndexButtonClick(View view) {
		if (view.getId() == R.id.up_button) {
			// Log.d("debug", "up button pressed");
			indexHandler.handleUpClick();
			setIndexButtons();
			dispatchAccessibilityEvent(view);
		} else if (view.getId() == R.id.down_button) {
			// Log.d("debug", "down button pressed");
			indexHandler.handleDownClick();
			setIndexButtons();
			dispatchAccessibilityEvent(view);
		} else {
			Button btnClicked = (Button) view;
			String btnClickedText = btnClicked.getText().toString();

			// ignore the index button click if it's a blank string
			if (!btnClickedText.equals("")) {
				// Log.d("debug", "adding index button to stack: "
				// + btnClickedText);

				actionStack.add(INDEX_BTN);
				indexHandler.handleIndexBtnClicked(btnClickedText);
				view.announceForAccessibility("filtering list");
				filterList();
				setAdapterToList();
				dispatchAccessibilityEvent(view);
			}
		}
	}

	private void dispatchAccessibilityEvent(View view) {
		view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
	}

	private void filterList() {
		listHandler.saveCurrListState(); // save list current state
		// Log.d("debug",
		// "saving current list state: " + listHandler.getFullList());
		// Log.d("debug", "filtering list by: " + indexHandler.getFilter());
		listHandler.filterList(indexHandler.getFilter());
		// Log.d("debug", "remaining list: " + listHandler.getFullList());
		setAdapterToList();
	}

	public void setAdapterToList() {
		indexHandler.setListToIndex(listHandler.getFullList(), numItemsShown);
		adapter = new CustomAdapter(this, R.layout.list_row,
				listHandler.getDisplayedList(), numItemsShown);
		/*
		 * get and apply preference settings to the new adapter and index
		 * buttons on right hand side
		 */
		applySettings();
		// use own custom layout for the listview
		listview.setAdapter(adapter);
	}

	public void setIndexButtons() {
		ArrayList<String> btnsToShow = indexHandler.getIndexBtnSubset();
		// Log.d("debug", "buttons to show: " + btnsToShow.toString());
		// Log.d("debug", "setting index buttons to display");

		View view = findViewById(R.id.up_button);

		if (!indexHandler.canGoUp()) {
			view.setContentDescription("No more up");
			view.setClickable(false);
		} else {
			view.setContentDescription("Scroll up indices");
			view.setClickable(true);
		}

		view = findViewById(R.id.down_button);

		if (!indexHandler.canGoDown()) {
			view.setContentDescription("No more down");
			view.setClickable(false);
		} else {
			view.setContentDescription("Scroll down indices");
			view.setClickable(true);
		}

		View indexView = findViewById(R.id.index_view);
		ViewGroup group = (ViewGroup) indexView;
		int numButtons = group.getChildCount();
		// less one button both sides because arrow buttons don't need to be set
		for (int i = 1; i < numButtons - 1; i++) {
			Button temp = (Button) group.getChildAt(i);

			// handle empty index buttons
			if (i - 1 >= btnsToShow.size()
					|| indexHandler.getFilter().length() == 2) {
				temp.setText("");
				temp.setContentDescription("");
				temp.setClickable(false);
				temp.setFocusable(false);
				setBtnBackground(temp, true);
			} else { // handle index buttons with text
				String text = btnsToShow.get(i - 1);
				temp.setText(text.replace(" to ", "-"));
				temp.setContentDescription(assembleDescription(text));
				temp.setClickable(true);
				temp.setFocusable(true);
				temp.setGravity(Gravity.CENTER);
				temp.setTextAppearance(this, R.style.IndexBtnText);
				temp.setTextColor(getTextColor());
				setBtnBackground(temp, false);
			}
		}
	}

	private String assembleDescription(String text) {
		StringBuilder sb = new StringBuilder();
		String twoLetters = "\\w{2}";
		char[] chars = text.toUpperCase(Locale.ENGLISH).toCharArray();

		if (text.matches(twoLetters)) {
			sb.append(chars[0]).append(",").append(chars[1]);
		} else {
			/*
			 * string will be in the form "ab to cd" or single letter
			 */
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
				sb.append(c);

				if (i == 0 || i == chars.length - 2) {
					sb.append(",");
				} else if (i == 1 || i == chars.length - 4) {
					sb.append(" ");
				}
			}
		}

		sb.append(", index ");
		Log.d("debug", sb.toString());
		return sb.toString();
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

	public void setBtnBackground(View toReplace, boolean isBlank) {
		GradientDrawable drawable;
		StateListDrawable states = new StateListDrawable();

		if (isBlank) {
			drawable = ButtonBuilder.getBlankRectangle(this);
			states.addState(new int[] {}, drawable);
		} else {
			drawable = ButtonBuilder.getHighlightedBorderedRectangle(this,
					getTextColor(), getBackgroundColor());
			states.addState(new int[] { android.R.attr.state_pressed },
					drawable);
			states.addState(new int[] { android.R.attr.state_selected },
					drawable);

			drawable = ButtonBuilder.getBorderedRectangle(this, getTextColor());
			states.addState(new int[] { -android.R.attr.state_selected },
					drawable);
		}

		toReplace.setBackground(states);
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
		if (prevAction.equals(INDEX_BTN)) {
			indexHandler.handleBackBtnClicked();
		}

		return prevAction;
	}

	public void cleanActionStack() {
		actionStack.removeAll(Collections.singleton(INDEX_BTN));
		listHandler.clearPrevListStates();
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

	@Override
	protected void onTitleChanged(CharSequence title, int color) {
		super.onTitleChanged(title, color);
		getWindow().getDecorView().sendAccessibilityEvent(
				AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
	}

}
