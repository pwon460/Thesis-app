package simo.transport.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

public class RequestTask extends AsyncTask<String, Void, String> {

	private static final int CONNETION_TIMEOUT_VALUE = 1000;
	private static final int SOCKET_TIMEOUT_VALUE = 1500;
	
	@Override
	protected String doInBackground(String... uri) {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNETION_TIMEOUT_VALUE);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT_VALUE);
		HttpClient httpClient = new DefaultHttpClient(params);
		String responseString = "";

		Log.d("debug", "server url = " + uri[0]);
		try {
			HttpResponse response = httpClient.execute(new HttpGet(uri[0]));
			
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();

				Log.d("debug", "response string = " + responseString);

			} else { // close connection
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}

		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return responseString;
	}

	@Override
	protected void onPostExecute(String responseString) {
		super.onPostExecute(responseString);
	}

}
