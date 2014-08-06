package simo.transport.templates;

import java.util.ArrayList;

import simo.transport.R;
import simo.transport.helpers.ButtonBuilder;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.TypedArray;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
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
public class ListenerActivity extends ActionBarActivity implements
		OnSharedPreferenceChangeListener, OnGesturePerformedListener {

	private static final String DEFAULT_PREF_VALUE = "1";
	private SharedPreferences prefs; // the preferences in the xml file
	private int textColor; // text color currently selected by user
	private int background; // background color selected by user
	private int textSettings;
	private int id;
	private GestureLibrary gestureLib;
	private GestureOverlayView gestureOverlayView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gestureOverlayView = new GestureOverlayView(this);
		gestureOverlayView.addOnGesturePerformedListener(this);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	// used to revert the button colour back to original un-highlighted state
	@Override
	protected void onResume() {
		super.onResume();
		applySettings();
	}

	protected void applySettings() {
		// grab current color setting
		getColorsFromPrefs();
		Log.d("debug", "text color is " + textColor);
		Log.d("debug", "background is " + background);

		// get it for preference text settings too
		getTextSettingsFromPrefs();
		Log.d("debug", "text setting is " + textSettings);

		// apply preference settings to buttons on screen and the layout
		LinearLayout home = (LinearLayout) findViewById(id);
		Button btn;

		for (int i = 0; i < home.getChildCount(); i++) {
			btn = (Button) home.getChildAt(i);
			setBtnColor(btn);
			setTextSettings(btn);
		}
		home.setBackgroundColor(background);

	}

	public void getTextSettingsFromPrefs() {
		textSettings = Integer.valueOf(prefs.getString("pref_text_size_key",
				DEFAULT_PREF_VALUE));
	}

	public void getColorsFromPrefs() {
		// The attributes you want retrieved
		int[] attrs = { android.R.attr.textColor, android.R.attr.background };
		TypedArray ta;

		// grab the current preference color settings
		int colorScheme = Integer.valueOf(prefs.getString("pref_color_key",
				DEFAULT_PREF_VALUE));
		Log.d("debug", "color scheme is " + colorScheme);

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

	public void setTextSettings(Button btn) {
		btn.setGravity(Gravity.CENTER);
		if (textSettings == 2) {
			btn.setTextAppearance(getApplicationContext(), R.style.MediumText);
		} else if (textSettings == 3) {
			btn.setTextAppearance(getApplicationContext(), R.style.LargeText);
		} else {
			btn.setTextAppearance(getApplicationContext(), R.style.SmallText);
		}
	}

	private void setBtnColor(Button btn) {

		// apply them to the button
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			btn.setBackground(ButtonBuilder.getBorderedRectangle(
					getApplicationContext(), textColor));
		} else {
			btn.setBackgroundDrawable(ButtonBuilder.getBorderedRectangle(
					getApplicationContext(), textColor));
		}

		btn.setTextColor(textColor);

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
		return textSettings;
	}

	public void setID(int layoutID) {
		this.id = layoutID;
	}

	public int getHandedness() {
		return Integer.valueOf(prefs.getString("pref_main_hand_key",
				DEFAULT_PREF_VALUE));
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		Log.d("debug", "gesture detected!!!");
		for (Prediction prediction : predictions) {
			if (prediction.score > 0 && prediction.name.equals("Back")) {
				onBackPressed();
			}
		}
	}
	
	public void setGestureOverlayView (int id) {
		View inflate = getLayoutInflater().inflate(id, null);
	    gestureOverlayView.addView(inflate);
	}

}