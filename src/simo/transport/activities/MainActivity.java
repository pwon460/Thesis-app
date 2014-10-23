package simo.transport.activities;

import java.io.File;

import simo.transport.R;
import simo.transport.helpers.DownloadHelper;
import simo.transport.helpers.StorageHelper;
import simo.transport.templates.BasicListenerActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends BasicListenerActivity {

	private DownloadHelper downloadHelper;
	private StorageHelper storageHelper;
	private File downloadDirectory;
	private File TDXFile;

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
		storageHelper = new StorageHelper(this);
		downloadDirectory = storageHelper.getDownloadDirectory();
		TDXFile = storageHelper.getTDXData(downloadDirectory);
		downloadHelper = new DownloadHelper(this);

		boolean isDownloadAvailable = false;

		if (!storageHelper.fileExists("simo.initialized")) {
			if (downloadHelper.isInitialDBAvailable()) {
				isDownloadAvailable = true;
			}
		}

		if (!downloadHelper.isPatchAvailable()) {
			File patch = storageHelper.patchFileExists();
			if (patch != null) {
				String versionTimestamp = downloadHelper.getPatchTimestamp();
				boolean isNewDataAvailable = downloadHelper.isNewDataAvailable(
						versionTimestamp, patch);
				if (isNewDataAvailable) {
					isDownloadAvailable = true;
				}
			} else {
				System.err
						.println("There's no patch flag file. Something is wrong.");
			}
		}

		if (downloadHelper.isWeeklyDataAvailable()) {
			String versionTimestamp = downloadHelper.getTDXDataTimestamp();
			boolean isNewDataAvailable = downloadHelper.isNewDataAvailable(
					versionTimestamp, TDXFile);
			if (isNewDataAvailable) {
				isDownloadAvailable = true;
			}
		} else {
			Toast.makeText(this, "Connection to server failed",
					Toast.LENGTH_SHORT).show();
		}

		if (isDownloadAvailable) {
			addDownloadButton();
		}

	}

	private void addDownloadButton() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.home);
		LinearLayout paddingLayout = (LinearLayout) layout
				.findViewById(R.id.padding);
		layout.removeView(paddingLayout);
		getLayoutInflater().inflate(R.layout.download_button, layout);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) paddingLayout
				.getLayoutParams();
		params.weight = 1.0f;
		paddingLayout.setLayoutParams(params);
		layout.addView(paddingLayout);
	}

	public void goSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void goPickTrip(View view) {
		Intent intent = new Intent(this, SelectTransportActivity.class);
		startActivity(intent);
	}

	public void onDownloadClick(View view) {
		String init = downloadHelper.getInitDBPath();
		String patch = downloadHelper.getPatchPath();
		String weekly = downloadHelper.getWeeklyDataPath();
		File initDBFile;
		File patchFile;
		File weeklyFile;

		if (init != null) {
			initDBFile = downloadHelper.downloadUpdate(downloadDirectory, init);
			Log.d("debug", "init db = " + initDBFile);
		}

		if (patch != null) {
			patchFile = downloadHelper.downloadUpdate(downloadDirectory, patch);
			Log.d("debug", "patch = " + patchFile);
		}

		if (weekly != null) {
			weeklyFile = downloadHelper.downloadUpdate(downloadDirectory,
					weekly);
			storageHelper.removeOldFiles(downloadDirectory, weeklyFile);
			Log.d("debug", "weekly = " + weeklyFile);
		}

		removeDownloadButton();
		findViewById(R.id.home).invalidate();

		// pass initDBFile to DownloadInitializer initializeDatabase(Context
		// context, String file)
		// pass patchFile to DownloadInitializer applyPatch(Context context,
		// String file)
	}

	private void removeDownloadButton() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.home);
		Button downloadBtn = (Button) layout.findViewById(R.id.download_btn);
		layout.removeView(downloadBtn);
		LinearLayout paddingLayout = (LinearLayout) layout
				.findViewById(R.id.padding);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) paddingLayout
				.getLayoutParams();
		params.weight = 2.0f;
		paddingLayout.setLayoutParams(params);
	}

}
