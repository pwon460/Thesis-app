package simo.transport.activities;

import simo.transport.R;
import simo.transport.helpers.ButtonBuilder;
import simo.transport.templates.BasicListenerActivity;
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
public class BusRouteTypeActivity extends BasicListenerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_route_type);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

}
