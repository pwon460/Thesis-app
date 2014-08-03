package simo.transport.ui;

import simo.transport.ButtonBuilder;
import simo.transport.R;
import simo.transport.templates.SettingsListenerActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class SelectTripActivity extends SettingsListenerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transport_choice);
		getActionBar().setTitle("Select Trip");   
		getSupportActionBar().setTitle("Select Trip");  // provide compatibility to all the versions
		setID(R.id.pick_transport);
		applySettings();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pick_trip, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	public void goTrainActivity (View view) {
		setViewClickedBackground(view);
		Intent intent = new Intent(this, TrainTripActivity.class);
		startActivity(intent);
	}
	
	private void setViewClickedBackground(View view) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(ButtonBuilder.getHighlightedRectangle(
					getApplicationContext(), getTextColor(), getBackgroundColor()));
		} else {
			view.setBackgroundDrawable(ButtonBuilder.getHighlightedRectangle(
					getApplicationContext(), getTextColor(), getBackgroundColor()));
		}
	}

}
