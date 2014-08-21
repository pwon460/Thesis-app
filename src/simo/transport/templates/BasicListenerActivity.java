package simo.transport.templates;

import simo.transport.R;
import simo.transport.helpers.ButtonBuilder;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class BasicListenerActivity extends ActionBarActivity implements
		OnSharedPreferenceChangeListener {

	private static final String DEFAULT_PREF_VALUE = "1";
	private static final int NUM_ARROW_BTNS = 2;
	private SharedPreferences prefs; // the preferences in the xml file
	private int textColor; // text color currently selected by user
	private int background; // background color selected by user
	private int id;
	private int numBtns;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
//		Log.d("debug", "on resume!");
		applySettings(); // revert colour back to normal state
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		prefs.unregisterOnSharedPreferenceChangeListener(this);
	}

	protected void applySettings() {
		// grab current color setting
		loadColorsFromPrefs();
		// Log.d("debug", "text color is " + textColor);
		// Log.d("debug", "background is " + background);

		// apply preference settings to buttons on screen and the layout
		LinearLayout layout = (LinearLayout) findViewById(id);
		Button btn;

		// Log.d("debug", "layout = " + layout);
		for (int i = 0; i < layout.getChildCount(); i++) {
			btn = (Button) layout.getChildAt(i);
			if (!btn.getText().equals("")) {
				setViewNormalBackground(btn);
				btn.setTextColor(textColor);
				btn.setGravity(Gravity.CENTER);
				btn.setTextAppearance(getApplicationContext(),
						R.style.SmallText);
			}
		}
		layout.setBackgroundColor(background);

	}

	public void loadColorsFromPrefs() {
		// The attributes you want retrieved
		int[] attrs = { android.R.attr.textColor, android.R.attr.background };
		TypedArray ta;

		// grab the current preference color settings
		int colorScheme = Integer.valueOf(prefs.getString("pref_color_key",
				DEFAULT_PREF_VALUE));
//		Log.d("debug", "color scheme is " + colorScheme);

		// grab the appropriate style from the style.xml using
		// Context.obtainStyledAttributes()
		if (colorScheme == 2) { // black on white color scheme
			ta = obtainStyledAttributes(R.style.BlackOnWhite, attrs);
		} else if (colorScheme == 3) { // yellow on black color scheme
			ta = obtainStyledAttributes(R.style.WhiteOnPurple, attrs);
		} else if (colorScheme == 4) { // white on purple color scheme
			ta = obtainStyledAttributes(R.style.YellowOnBlack, attrs);
		} else { // else use white on black scheme
			ta = obtainStyledAttributes(R.style.WhiteOnBlack, attrs);
		}

		// Fetch the colors from style
		// should turn up as blank white screen if not working
		textColor = ta.getColor(0, Color.WHITE);
		background = ta.getColor(1, Color.WHITE);

		// Recycle typed array
		ta.recycle();

	}

	public void loadNumIndexBtnsFromPrefs() {
		int tempVal = Integer.valueOf(prefs.getString("pref_index_btns_key",
				DEFAULT_PREF_VALUE));
		Log.d("debug", "pref val = " + tempVal);
		if (tempVal == 1) {
			numBtns = 8 + NUM_ARROW_BTNS;
		} else if (tempVal == 2) {
			numBtns = 9 + NUM_ARROW_BTNS;
		} else if (tempVal == 3) {
			numBtns = 10 + NUM_ARROW_BTNS;
		} else if (tempVal == 4) {
			numBtns = 11 + NUM_ARROW_BTNS;
		} else {
			numBtns = 12 + NUM_ARROW_BTNS;
		}
	}

	public void setViewClickedBackground(View view) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(ButtonBuilder.getHighlightedBorderedRectangle(
					this, textColor, background));
		} else {
			view.setBackgroundDrawable(ButtonBuilder
					.getHighlightedBorderedRectangle(this, textColor,
							background));
		}
	}

	private void setViewNormalBackground(View view) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(ButtonBuilder.getBorderedRectangle(this,
					textColor, background));
		} else {
			view.setBackgroundDrawable(ButtonBuilder.getBorderedRectangle(this,
					textColor, background));
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		applySettings();
	}

	public int getBackgroundColor() {
		return background;
	}

	public int getTextColor() {
		return textColor;
	}

	public int getTextSettings() {
		return Integer.valueOf(prefs.getString("pref_text_size_key",
				DEFAULT_PREF_VALUE));
	}

	public void setID(int layoutID) {
		this.id = layoutID;
	}

	public boolean isRightHandMode() {
		int mode = Integer.valueOf(prefs.getString("pref_main_hand_key",
				DEFAULT_PREF_VALUE));
		boolean isRightHandMode = true;
		
		if (mode != Integer.valueOf(DEFAULT_PREF_VALUE)) {
			isRightHandMode = false;
		}
		
		return isRightHandMode;
	}

	public int getInvertedness() {
		return Integer.valueOf(prefs.getString("pref_invert_color_key",
				DEFAULT_PREF_VALUE));
	}

	public int getNumIndexBtns() {
		return numBtns;
	}

}