package simo.transport;

import android.annotation.TargetApi;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MainActivity extends ActionBarActivity implements
		OnSharedPreferenceChangeListener {

	private static final String DEFAULT_PREF_VALUE = "1";
	private SharedPreferences prefs; // the preferences in the xml file

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.activity_main);
		applySettings();
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	private void applySettings() {
		// The attributes you want retrieved
		int[] attrs = { android.R.attr.textStyle, android.R.attr.textColor,
				android.R.attr.background };
		TypedArray ta;
		

		// TODO: remove debug prints
		// grab the current preference color settings
		int colorScheme = Integer.valueOf(prefs.getString("pref_color_key", DEFAULT_PREF_VALUE));
		Log.d("debug", "color scheme is " + colorScheme);
		// get it for preference text settings too
		int textSettings = Integer.valueOf(prefs.getString("pref_text_size_key", DEFAULT_PREF_VALUE));
		Log.d("debug", "text setting is " + textSettings);
		
		// grab the appropriate style from the style.xml using Context.obtainStyledAttributes()
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
		// TODO: for testing purposes only
		// should turn up as blank white screen if not working
		int textColor = ta.getColor(1, Color.WHITE); 
		int background = ta.getColor(2, Color.WHITE);
		Log.d("debug", "text color is " + textColor);
		Log.d("debug", "background is " + background);
		
		// apply preference settings to buttons on screen and the layout
		LinearLayout home = (LinearLayout) findViewById(R.id.home);
		Button btn;

		for (int i = 0; i < home.getChildCount(); i++) {
			btn = (Button) home.getChildAt(i);
			setBtnColor(btn, textColor);
			setTextSettings(btn, textSettings);
		}
		home.setBackgroundColor(background);
		
		// Recycle typed array
		ta.recycle();

	}

	private void setTextSettings(Button btn, int textSettings) {
		btn.setGravity(Gravity.CENTER);
		if (textSettings == 2) {
			btn.setTextAppearance(getApplicationContext(), R.style.MediumText);
		} else if (textSettings == 3) {
			btn.setTextAppearance(getApplicationContext(), R.style.LargeText);
		} else {
			btn.setTextAppearance(getApplicationContext(), R.style.SmallText);
		}
	}

	
	private void setBtnColor(Button btn, int textColor) {

		// apply them to the button
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			btn.setBackground(ButtonBuilder.getBorderedRectangle(getApplicationContext(),
					textColor));
		} else {
			btn.setBackgroundDrawable(ButtonBuilder.getBorderedRectangle(
					getApplicationContext(), textColor));
		}

		btn.setTextColor(textColor);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	public void goSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		applySettings();
	}

}
