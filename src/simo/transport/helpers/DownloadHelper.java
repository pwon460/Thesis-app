package simo.transport.helpers;

import java.util.concurrent.ExecutionException;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

public class DownloadHelper {

	private Context ctx;
	private static final String PROTOCOL = "http://";
	private static final String SERVER_AUTH = "10.10.1.216:8080";
	private static final String PATH = "/Thesis/IntermediaryServlet";
	private static String responseString;

	public DownloadHelper(Context ctx) {
		this.ctx = ctx;
		IntentFilter intentFilter = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		BroadcastReceiver broadcastReceiver = new DownloadBroadcastReceiver();
		ctx.registerReceiver(broadcastReceiver, intentFilter);
	}

	public boolean isDownloadAvailable() {
//		DownloadManager downloadManager = (DownloadManager) ctx
//				.getSystemService(Context.DOWNLOAD_SERVICE);

		String serverURL = PROTOCOL + SERVER_AUTH + PATH;

		Toast.makeText(ctx, "Please wait, connecting to server",
				Toast.LENGTH_LONG).show();
		
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
		
		Log.d("debug", "response string is: " + responseString);
		
		if (responseString.contains("/")) {
			return true;
		} else {
			return false;
		}
	}

}
