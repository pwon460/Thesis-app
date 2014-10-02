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

	private static final String PROTOCOL = "http://";
	private static final String SERVER_AUTHORITY = "10.10.1.216:8080";
	private static final String PATH = "/Thesis/IntermediaryServlet";
	private static String responseString;
	private Context ctx;

	public DownloadHelper(Context ctx) {
		this.ctx = ctx;
	}

	public boolean isDownloadAvailable() {
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

		Log.d("debug", "pre formatted response string is: " + responseString);
		Pattern pattern = Pattern.compile("<p>(/.*?)</p>");
		Matcher matcher = pattern.matcher(responseString);
		if (matcher.find()) {
			responseString = matcher.group(1);
		}
		Log.d("debug", "post formatted response string is: " + responseString);

		if (responseString.matches("/.*/\\d{4}-\\d{2}-\\d{2}\\.txt")) {
			Log.d("debug", "download available");
			return true;
		} else {
			Log.d("debug", "download not available");
			return false;
		}
	}

	public String getResponseString() {
		return responseString;
	}

	public String getTDXDataTimestamp() {
		String[] path = responseString.split("/");
		String fileName = path[path.length - 1];
		String[] parts = fileName.split("\\.");
		return parts[0];
	}

	public boolean isNewDataAvailable(String serverTimestamp, File TDXFile) {
		boolean isAvailable = false;
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
		LocalDate serverDate = dtf.parseLocalDate(serverTimestamp);

		if (TDXFile == null) { // no prior file exists
			Log.d("debug", "tdx file is null");
			isAvailable = true;
		} else {
			String fileName = TDXFile.getName();
			String[] parts = fileName.split("\\.");
			String fileTimestamp = parts[0];
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

	public File downloadUpdate(File downloadDirectory) {
		File f = null;
		String url = PROTOCOL + SERVER_AUTHORITY + responseString;

		ProgressDialog progressDialog;

		// instantiate it within the onCreate method
		progressDialog = new ProgressDialog(ctx);
		progressDialog.setMessage("Downloading file...");
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(true);

		final DownloadFileTask task = new DownloadFileTask(ctx, downloadDirectory, progressDialog);
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

}
