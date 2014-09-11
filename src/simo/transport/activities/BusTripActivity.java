package simo.transport.activities;

import java.util.ArrayList;

import simo.transport.R;
import simo.transport.helpers.ViewHolder;
import simo.transport.templates.TripActivityTemplate;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class BusTripActivity extends TripActivityTemplate {

	private Boolean isBySuburb;
	private String startSuburb;
	private String destSuburb;
	private String originStop;
	private String destStop;
	private String route;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		checkRouteType(intent.getStringExtra("routeType"));

		String title = "Select ";
		if (isBySuburb) {
			title += "origin suburb";
			getListHandler().setFullList(getDataAccessObject().getSuburbs());
		} else { // route
			title += "route";
			getListHandler().setFullList(
					getDataAccessObject().getRoutes(isRightHandMode()));
		}
		setTitle(title);
		setAdapterToList();
	}

	private void checkRouteType(String s) {
		isBySuburb = false;
		if (s.equals(getResources().getString(R.string.btn_suburb))) {
			isBySuburb = true;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		boolean handled = handleArrowButtonClick(view);

		
		if (!handled) {
			ViewHolder holder = (ViewHolder) view.getTag();
			TextView tv = holder.getTextView();
			// clear index buttons from action stack as choice has been locked in
			cleanActionStack();
			if (isBySuburb) {
				if (startSuburb == null || originStop == null
						|| destSuburb == null) {
					getActionStack().add(LIST_BTN);
					String title = "";
					String btnText = tv.getText().toString();
					ArrayList<String> temp = null;
					if (startSuburb == null) {
						startSuburb = btnText;
						title = "Select stop for " + startSuburb;
						temp = getDataAccessObject()
								.getSuburbStops(startSuburb);
					} else if (originStop == null) {
						originStop = btnText;
						title = "Select destination suburb";
						temp = getDataAccessObject().getSuburbs();
					} else { // destSuburb == null
						destSuburb = btnText;
						title = "Select stop for " + destSuburb;
						temp = getDataAccessObject().getSuburbStops(destSuburb);
						if (startSuburb.equals(destSuburb)) {
							temp.remove(originStop);
						}
					}
					setTitle(title);
					getListHandler().setFullList(temp);
					setAdapterToList();
				} else {
					destStop = tv.getText().toString();
					getDataAccessObject().setSuburbInfo(startSuburb,
							destSuburb, originStop, destStop);
					goTimetableActivity();
				}
			} else {
				if (route == null) {
					getActionStack().add(LIST_BTN);
					route = tv.getText().toString();
					setTitle("Select origin stop");
					getListHandler().setFullList(
							getDataAccessObject().getStopsOnRoute(route,
									isRightHandMode()));
					setAdapterToList();
				} else if (originStop == null) {
					getActionStack().add(LIST_BTN);
					originStop = tv.getText().toString();
					setTitle("Select destination stop");
					ArrayList<String> tempList = getDataAccessObject()
							.getStopsOnRoute(route, isRightHandMode());
					tempList.remove(originStop);
					getListHandler().setFullList(tempList);
					setAdapterToList();
				} else {
					destStop = tv.getText().toString();
					getDataAccessObject().setStops(route, isRightHandMode(),
							originStop, destStop);
					goTimetableActivity();
				}
			}
		}
	}

	private void goTimetableActivity() {
		Intent intent = new Intent(this, ShowTimetableActivity.class);
		String transport = getResources().getString(R.id.bus_btn);
		intent.putExtra("timetable",
				getDataAccessObject().getTimetable(transport));
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		String result = getPrevAction();
		if (result.equals(INDEX_BTN)) {
			boolean hasRestored = getListHandler().restorePrevState();
			if (hasRestored == false) {
				super.onBackPressed();
			} else {
				setAdapterToList();
			}
		} else if (result.equals(LIST_BTN)) {
			if (isBySuburb) {
				if (destSuburb != null) {
					setTitle("Select destination suburb");
					destSuburb = null;
					getListHandler().setFullList(
							getDataAccessObject().getSuburbs());
				} else if (originStop != null) {
					setTitle("Select stop for " + startSuburb);
					originStop = null;
					getListHandler().setFullList(
							getDataAccessObject().getSuburbStops(startSuburb));
				} else if (startSuburb != null) {
					setTitle("Select origin suburb");
					startSuburb = null;
					getListHandler().setFullList(
							getDataAccessObject().getSuburbs());
				}
			} else {
				if (originStop != null) {
					setTitle("Select origin stop");
					originStop = null;
					getListHandler().setFullList(
							getDataAccessObject().getStopsOnRoute(route,
									isRightHandMode()));
				} else if (route != null) {
					setTitle("Select route");
					route = null;
					getListHandler().setFullList(
							getDataAccessObject().getRoutes(isRightHandMode()));

				}
			}
			setAdapterToList();
		} else {
			super.onBackPressed();
		}
	}
}
