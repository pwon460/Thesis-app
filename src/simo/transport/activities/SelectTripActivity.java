package simo.transport.activities;

import simo.transport.R;
import simo.transport.helpers.ButtonBuilder;
import simo.transport.templates.ListenerActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class SelectTripActivity extends ListenerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transport_choice);
		getActionBar().setTitle("Select Trip");
		getSupportActionBar().setTitle("Select Trip"); // provide compatibility
														// to all the versions
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
		// int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	public void goSelectTrip(View view) {
		setViewClickedBackground(view);
		Button temp = (Button) view;
		String text = temp.getText().toString();
		Intent intent;

		if (text.equals(getResources().getString(R.string.train))) {
			intent = new Intent(this, TrainTripActivity.class);
		} else if (text.equals(getResources().getString(R.string.ferry))) {
			intent = new Intent(this, FerryTripActivity.class);
		} else if (text.equals(getResources().getString(R.string.light_rail))) {
			intent = new Intent(this, LightRailTripActivity.class);
		} else {
			intent = new Intent(this, SelectRouteTypeActivity.class);
		}

		startActivity(intent);
	}

	private void setViewClickedBackground(View view) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(ButtonBuilder.getHighlightedBorderedRectangle(
					getApplicationContext(), getTextColor(),
					getBackgroundColor()));
		} else {
			view.setBackgroundDrawable(ButtonBuilder.getHighlightedBorderedRectangle(
					getApplicationContext(), getTextColor(),
					getBackgroundColor()));
		}
	}

}
