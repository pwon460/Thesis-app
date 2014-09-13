package simo.transport.activities;

import simo.transport.R;
import simo.transport.templates.BasicListenerActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MainActivity extends BasicListenerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		setContentView(R.layout.activity_main);
		// tell superclass id of page being used for applySettings to work on
		// that page
		setID(R.id.home);
	}

	public void goSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void goPickTrip(View view) {
		Intent intent = new Intent(this, SelectTransportActivity.class);
		startActivity(intent);
	}

}
