package simo.transport.activities;

import java.io.File;

import simo.transport.R;
import simo.transport.helpers.DownloadHelper;
import simo.transport.helpers.StorageHelper;
import simo.transport.templates.BasicListenerActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends BasicListenerActivity {

	private DownloadHelper downloadHelper;
	private StorageHelper storageHelper = new StorageHelper();
	private File downloadDirectory;
	private File TDXFile;
	private boolean hasDownloadButton;

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
		hasDownloadButton = false;
		downloadDirectory = storageHelper.getDownloadDirectory();
		TDXFile = storageHelper.getTDXData(downloadDirectory);
		downloadHelper = new DownloadHelper(this);

		// TODO: Better to suggest only one database update button. 
		if (!downloadHelper.fileExistance("simo.initialized")) {
			if (downloadHelper.isInitialDBAvailable()) {
				// download initial data file
				// pass it to DownloadInitializer initializeDatabase(Context context, String filePath)
			}
		}
		if (!downloadHelper.isPatchAvailable()) {
			File patch = downloadHelper.patchFileExistance();
			if (patch != null) {
				String versionTimestamp = downloadHelper.getPatchTimestamp();
				boolean isNewDataAvailalbe = downloadHelper.isNewDataAvailable(versionTimestamp, patch);
				// download patch file
				// pass it to DownloadInitializer applyPatch(Context context, String filePath)
				//if (isNewDataAvailable && !hasDownloadButton) {
				//	addDownloadButton();
				//}				
			} else {
				System.err.println("There's no patch flag fils. Something goes wrong.");
			}
		}
		if (downloadHelper.isDownloadAvailable()) {
			String versionTimestamp = downloadHelper.getTDXDataTimestamp();
			boolean isNewDataAvailable = downloadHelper.isNewDataAvailable(
					versionTimestamp, TDXFile);
			if (isNewDataAvailable && !hasDownloadButton) {
				addDownloadButton();
			}
		} else {
			if (hasDownloadButton) {
				removeDownloadButton();
			}
			Toast.makeText(this, "Connection to server failed",
					Toast.LENGTH_SHORT).show();
		}

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
		hasDownloadButton = false;
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
		hasDownloadButton = true;
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
		File newFile = downloadHelper.downloadUpdate(downloadDirectory);
		if (newFile != null) {
			removeDownloadButton();
			findViewById(R.id.home).invalidate();
			storageHelper.removeOldFiles(downloadDirectory, newFile);
		}
	}

}
