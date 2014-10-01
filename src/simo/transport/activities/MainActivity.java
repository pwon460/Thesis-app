package simo.transport.activities;

import java.io.File;

import simo.transport.R;
import simo.transport.helpers.DownloadHelper;
import simo.transport.helpers.StorageHelper;
import simo.transport.templates.BasicListenerActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MainActivity extends BasicListenerActivity {

	private DownloadHelper downloadHelper;
	private StorageHelper storageHelper = new StorageHelper();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		setContentView(R.layout.activity_main);
		/*
		 * tell parent the id of page being used in order for applySettings to
		 * work on that page
		 */
		setID(R.id.home);

	}

	@Override
	protected void onResume() {
		super.onResume();
		downloadHelper = new DownloadHelper(this);

		if (downloadHelper.isDownloadAvailable()) {
			File downloadDirectory = storageHelper.getDownloadDirectory();
			File TDXFile = storageHelper.getTDXData(downloadDirectory);

			if (TDXFile == null) { // no prior file exists

			} else { // replace existing file

			}

		} else {
			Toast.makeText(this, "Connection to server failed",
					Toast.LENGTH_SHORT).show();
		}
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
