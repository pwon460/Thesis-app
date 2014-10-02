package simo.transport.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.widget.Toast;

public class DownloadFileTask extends AsyncTask<String, Integer, String> {

	private File downloadDirectory;
	private Context ctx;
	private PowerManager.WakeLock wakeLock;
	private ProgressDialog progressDialog;

	public DownloadFileTask(Context ctx, File downloadDirectory,
			ProgressDialog mProgressDialog) {
		this.ctx = ctx;
		this.downloadDirectory = downloadDirectory;
		this.progressDialog = mProgressDialog;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// take CPU lock to prevent CPU from going off if the user
		// presses the power button during download
		PowerManager pm = (PowerManager) ctx
				.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass()
				.getName());
		wakeLock.acquire();
		progressDialog.show();
	}

	@Override
	protected String doInBackground(String... urls) {
		String filePath = null;
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urls[0]);
			String[] parts = url.getPath().split("/");
			String fileName = parts[parts.length - 1];
			String newFilePath = downloadDirectory + "/" + fileName;
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			// expect HTTP 200 OK, so we don't mistakenly save error report
			// instead of the file
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// this will be useful to display download percentage
				// might be -1: server did not report the length
				int fileLength = connection.getContentLength();

				// download the file
				input = connection.getInputStream();
				output = new FileOutputStream(newFilePath);

				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					if (isCancelled()) {
						break;
					}
					total += count;

					if (fileLength > 0) {
						publishProgress((int) (total * 100 / fileLength));
					}
					output.write(data, 0, count);
				}

				output.close();
				input.close();
				connection.disconnect();
				filePath = newFilePath;
			}

		} catch (Exception e) {
			return e.toString();
		}

		return filePath;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		progressDialog.setIndeterminate(false);
		progressDialog.setMax(100);
		progressDialog.setProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		wakeLock.release();
		progressDialog.dismiss();
		if (isCancelled()) {
			Toast.makeText(ctx, "Download cancelled", Toast.LENGTH_SHORT)
			.show();
		} else if (result != null) {
			Toast.makeText(ctx, "File downloaded", Toast.LENGTH_SHORT).show();
//			Log.d("debug", result);
		} else {
			Toast.makeText(ctx, "Error downloading", Toast.LENGTH_SHORT)
			.show();
		}
	}

}
