package simo.transport.helpers;

import java.io.File;
import java.io.FileFilter;
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

		if (responseString.matches("/.*/simo\\.\\d{8}\\.zip")) {
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
		return parts[parts.length - 2];
	}

	public boolean isNewDataAvailable(String serverTimestamp, File TDXFile) {
		boolean isAvailable = false;
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd");
		LocalDate serverDate = dtf.parseLocalDate(serverTimestamp);

		if (TDXFile == null) { // no prior file exists
			Log.d("debug", "tdx file is null");
			isAvailable = true;
		} else {
			String fileName = TDXFile.getName();
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

	public boolean fileExistance(String fname){
	    File file = ctx.getFileStreamPath(fname);
	    return file.exists();
	}
	
	public File patchFileExistance() {
		File f = this.ctx.getFilesDir();
		if (f.exists() && f.isDirectory()){
		    final Pattern p = Pattern.compile("simo\\.patch\\.\\d{8}\\.zip");
		    File[] flists = f.listFiles(new FileFilter() {
		        @Override
		        public boolean accept(File file) {
		            return p.matcher(file.getName()).matches();

		        }
		    });
		    if (flists.length == 1) {
		    	return flists[0];
		    } else {
		    	System.err.println("multiple patch file records exists");
		    }
		}
		return null;
	}

	public String getPatchTimestamp() {
		String[] path = responseString.split("/");
		String fileName = path[path.length - 1];
		String[] parts = fileName.split("\\.");
		return parts[parts.length - 2];
	}

	public boolean isInitialDBAvailable() {
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
		// if id is 1 then the content of p tags include a initial database file link
		Pattern pattern = Pattern.compile("<p id=1>(/.*?)</p>");
		Matcher matcher = pattern.matcher(responseString);
		if (matcher.find()) {
			responseString = matcher.group(1);
		}
		Log.d("debug", "post formatted response string is: " + responseString);

		if (responseString.matches("/.*/simo\\.init\\.zip")) {
			Log.d("debug", "download available");
			return true;
		} else {
			Log.d("debug", "download not available");
			return false;
		}
	}

	public boolean isPatchAvailable() {
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
		// if id is 2 then the content of p tags include a initial database patch file
		Pattern pattern = Pattern.compile("<p id=2>(/.*?)</p>");
		Matcher matcher = pattern.matcher(responseString);
		if (matcher.find()) {
			responseString = matcher.group(1);
		}
		Log.d("debug", "post formatted response string is: " + responseString);

		if (responseString.matches("/.*/simo\\.patch\\.\\d{8}\\.zip")) {
			Log.d("debug", "download available");
			return true;
		} else {
			Log.d("debug", "download not available");
			return false;
		}
	}

}
