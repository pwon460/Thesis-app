package simo.transport.activities;

import simo.transport.R;
import simo.transport.templates.BasicListenerActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class BusOptionsActivity extends BasicListenerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_options);
		setID(R.id.bus_options);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public void goBusTripActivity(View view) {
		Button temp = (Button) view;
		String text = temp.getText().toString();
		Intent intent;
		intent = new Intent(this, SelectBusTripActivity.class);
		intent.putExtra("routeType", text);
		startActivity(intent);
	}
}
