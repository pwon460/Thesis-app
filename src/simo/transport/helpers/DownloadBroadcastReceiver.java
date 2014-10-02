package simo.transport.helpers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class DownloadBroadcastReceiver extends BroadcastReceiver {

	private long downloadID = -1;

	@Override
	public void onReceive(Context context, Intent intent) {
		long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
		if (id == downloadID) {
			DownloadManager downloadManager = (DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE);
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(id);
			Cursor cursor = downloadManager.query(query);
			if (cursor.moveToFirst()) {
				int statusIndex = cursor
						.getColumnIndex(DownloadManager.COLUMN_STATUS);
				if (DownloadManager.STATUS_SUCCESSFUL == cursor
						.getInt(statusIndex)) {
					int uriIndex = cursor
							.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
					String downloadedPackageUriString = cursor.getString(uriIndex);
				} else {
					Log.d("debug", "download failed");
				}

			}

		}

	}

	public void setID(long downloadID) {
		this.downloadID = downloadID;
	}
}
