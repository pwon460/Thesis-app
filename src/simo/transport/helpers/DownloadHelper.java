package simo.transport.helpers;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class DownloadHelper {

	private static final String PATCH_FILENAME = "/.*/simo\\.patch\\.\\d{8}\\.zip";
	private static final String INIT_DB_FILENAME = "/.*/simo\\.init\\.zip";
	private static final String WEEKLY_DATA_FILENAME = "/.*/simo\\.\\d{8}\\.zip";
	private static final String PATCH_TAG_REGEX = "<p id=2>(/.*?)</p>";
	private static final String INITDB_TAG_REGEX = "<p id=1>(/.*?)</p>";
	private static final String WEEKLY_TAG_REGEX = "<p>(/.*?)</p>";
	private static final String PROTOCOL = "http://";
	private static final String SERVER_AUTHORITY = "10.10.1.216:8080";
	private static final String PATH = "/Thesis/IntermediaryServlet";
	private Context ctx;
	private String responseString;
	private String weeklyDataPath;
	private String TDXTimestamp;
	private String patchTimestamp;
	private String initDBPath;
	private String patchPath;

	public DownloadHelper(Context ctx) {
		this.ctx = ctx;
		checkServer();
	}

	private void checkServer() {
		String serverURL = PROTOCOL + SERVER_AUTHORITY + PATH;

		RequestTask task = new RequestTask();
		task.execute(serverURL);
		responseString = "";
		try {
			responseString = task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public boolean isWeeklyDataAvailable() {
		Pattern pattern = Pattern.compile(WEEKLY_TAG_REGEX);
		Matcher matcher = pattern.matcher(responseString);
		boolean isAvailable = false;
		weeklyDataPath = null;
		
		if (matcher.find()) {
			weeklyDataPath = matcher.group(1);
			if (weeklyDataPath.matches(WEEKLY_DATA_FILENAME)) {
				isAvailable = true;
				TDXTimestamp = extractTimestamp(weeklyDataPath);
			}
		}
		Log.d("debug", "weekly available? " + isAvailable);
		return isAvailable;
	}

	public boolean isInitialDBAvailable() {
		// if id is 1 then the content of p tags include a initial database file
		// link
		Pattern pattern = Pattern.compile(INITDB_TAG_REGEX);
		Matcher matcher = pattern.matcher(responseString);
		boolean isAvailable = false;
		initDBPath = null;
		
		if (matcher.find()) {
			initDBPath = matcher.group(1);
			if (initDBPath.matches(INIT_DB_FILENAME)) {
				isAvailable = true;
			}
		}

		Log.d("debug", "init db available? " + isAvailable);
		return isAvailable;
	}

	public boolean isPatchAvailable() {
		// if id is 2 then the content of p tags include a initial database
		// patch file
		Pattern pattern = Pattern.compile(PATCH_TAG_REGEX);
		Matcher matcher = pattern.matcher(responseString);
		boolean isAvailable = false;

		if (matcher.find()) {
			patchPath = matcher.group(1);
			if (patchPath.matches(PATCH_FILENAME)) {
				isAvailable = true;
				patchTimestamp = extractTimestamp(patchPath);
			}
		}

		Log.d("debug", "patch available? " + isAvailable);
		return isAvailable;
	}

	public String extractTimestamp(String temp) {
		String[] path = temp.split("/");
		String fileName = path[path.length - 1];
		String[] parts = fileName.split("\\.");
		return parts[parts.length - 2];
	}

	public boolean isNewDataAvailable(String serverTimestamp, File file) {
		boolean isAvailable = false;
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd");
		LocalDate serverDate = dtf.parseLocalDate(serverTimestamp);

		if (file == null) { // no prior file exists
			Log.d("debug", "tdx file is null");
			isAvailable = true;
		} else {
			String fileName = file.getName();
			String[] parts = fileName.split("\\.");
			String fileTimestamp = parts[parts.length - 2];
			Log.d("debug", "file timestamp = " + fileTimestamp);
			LocalDate localCopyTimestamp = dtf.parseLocalDate(fileTimestamp);
			Log.d("debug", "server timestamp = " + serverDate.toString());
			Log.d("debug",
					"local copy timestamp = " + localCopyTimestamp.toString());
			if (serverDate.isAfter(localCopyTimestamp)) {
				isAvailable = true;
			}
		}

		return isAvailable;
	}

	public File downloadUpdate(File downloadDirectory, String path) {
		File f = null;
		String url = PROTOCOL + SERVER_AUTHORITY + path;
		
		Log.d("debug", "downloading from " + url);

		ProgressDialog progressDialog;

		// instantiate it within the onCreate method
		progressDialog = new ProgressDialog(ctx);
		progressDialog.setMessage("Downloading file...");
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);

		final DownloadFileTask task = new DownloadFileTask(ctx,
				downloadDirectory, progressDialog);
		task.execute(url);

		progressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						task.cancel(true);
					}
				});

		try {
			f = new File(task.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return f;
	}
	
	public String getWeeklyDataPath() {
		return weeklyDataPath;
	}
	
	public String getInitDBPath() {
		return initDBPath;
	}
	
	public String getPatchPath() {
		return patchPath;
	}

	public String getTDXDataTimestamp() {
		return TDXTimestamp;
	}
	
	public String getPatchTimestamp() {
		return patchTimestamp;
	}

}
