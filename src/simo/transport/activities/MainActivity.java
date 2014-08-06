package simo.transport.activities;

import simo.transport.R;
import simo.transport.helpers.ButtonBuilder;
import simo.transport.templates.ListenerActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MainActivity extends ListenerActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		setContentView(R.layout.activity_main);
		setID(R.id.home);
		applySettings();
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
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	public void goSettings(View view) {
		setViewClickedBackground(view);
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void goPickTrip(View view) {
		setViewClickedBackground(view);
		Intent intent = new Intent(this, SelectTripActivity.class);
		startActivity(intent);
	}
	
	private void setViewClickedBackground(View view) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(ButtonBuilder.getHighlightedBorderedRectangle(
					getApplicationContext(), getTextColor(), getBackgroundColor()));
		} else {
			view.setBackgroundDrawable(ButtonBuilder.getHighlightedBorderedRectangle(
					getApplicationContext(), getTextColor(), getBackgroundColor()));
		}
	}
	
}
