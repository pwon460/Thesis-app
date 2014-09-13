package simo.transport.activities;

import simo.transport.R;
import simo.transport.templates.BasicListenerActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class SelectTransportActivity extends BasicListenerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transport_choice);
		setID(R.id.pick_transport);
	}

	public void goSelectTrip(View view) {
		Button temp = (Button) view;
		String text = temp.getText().toString();
		Intent intent;

		if (text.equals(getResources().getString(R.string.bus))) {
			intent = new Intent(this, BusOptionsActivity.class);
		} else {
			intent = new Intent(this, SelectTripActivity.class);
			intent.putExtra("transport", text);
		}

		startActivity(intent);
	}

}
