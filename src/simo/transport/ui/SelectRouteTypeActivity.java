package simo.transport.ui;

import simo.transport.R;
import simo.transport.helpers.ButtonBuilder;
import simo.transport.templates.ListenerActivity;
import simo.transport.templates.TripActivityTemplate;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class SelectRouteTypeActivity extends ListenerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setGestureOverlayView(R.layout.activity_select_route_type);
		setContentView(R.layout.activity_select_route_type);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_route_type, menu);
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

	public void goSelectRoute(View view) {
		setViewClickedBackground(view);
		Button temp = (Button) view;
		String text = temp.getText().toString();
		Intent intent = new Intent(this, TripActivityTemplate.class);
		intent.putExtra("btn_text", text);
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
