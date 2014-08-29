package simo.transport.activities;

import java.util.ArrayList;

import simo.transport.R;
import simo.transport.helpers.ViewHolder;
import simo.transport.templates.TripActivityTemplate;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
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
			setListName("origin suburbs");
			getListHandler().setFullList(getDataAccessObject().getSuburbs());
		} else { // route
			title += "route";
			setListName("routes");
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

			if (isBySuburb) {
				if (startSuburb == null || originStop == null
						|| destSuburb == null) {
					getActionStack().add("listview");
					getIndexHandler().clearList();
					String title = "";
					String listName = "";
					ArrayList<String> temp = null;
					if (startSuburb == null) {
						startSuburb = tv.getText().toString();
						title = "Select stop for " + startSuburb;
						listName = "bus stops in " + startSuburb;
						temp = getDataAccessObject()
								.getSuburbStops(startSuburb);
					} else if (originStop == null) {
						originStop = tv.getText().toString();
						title = "Select destination suburb";
						listName = "destination suburbs";
						temp = getDataAccessObject().getSuburbs();
					} else { // destSuburb == null
						destSuburb = tv.getText().toString();
						title = "Select stop for " + destSuburb;
						listName = "bus stops in " + destSuburb;
						temp = getDataAccessObject().getSuburbStops(destSuburb);
						if (startSuburb.equals(destSuburb)) {
							temp.remove(originStop);
						}
					}
					setTitle(title);
					setListName(listName);
					getListHandler().setFullList(temp);
					dispatchAccessibilityNotification();
					setAdapterToList();
				} else {
					destStop = tv.getText().toString();
					getDataAccessObject().setSuburbInfo(startSuburb,
							destSuburb, originStop, destStop);
					goTimetableActivity();
				}
			} else {
				if (route == null) {
					getActionStack().add("listview");
					getIndexHandler().clearList();
					route = tv.getText().toString();
					setTitle("Select origin stop");
					setListName("origin bus stops");
					getListHandler().setFullList(
							getDataAccessObject().getStopsOnRoute(route,
									isRightHandMode()));
					dispatchAccessibilityNotification();
					setAdapterToList();
				} else if (originStop == null) {
					getActionStack().add("listview");
					getIndexHandler().clearList();
					originStop = tv.getText().toString();
					setTitle("Select destination stop");
					setListName("destination bus stops");
					ArrayList<String> tempList = getDataAccessObject()
							.getStopsOnRoute(route, isRightHandMode());
					tempList.remove(originStop);
					getListHandler().setFullList(tempList);
					dispatchAccessibilityNotification();
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
	
	private void dispatchAccessibilityNotification() {
		getWindow().getDecorView().sendAccessibilityEvent(
				AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
	}

	private void goTimetableActivity() {
		Intent intent = new Intent(this, ShowTimetableActivity.class);
		intent.putExtra("DAO", getDataAccessObject());
		intent.putExtra("transport", getResources().getString(R.id.bus_btn));
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		String result = getPrevAction();
		if (result.equals("indexBtn")) {
			boolean hasRestored = getListHandler().restorePrevState();
			if (hasRestored == false) {
				super.onBackPressed();
			} else {
				setAdapterToList();
			}
		} else if (result.equals("listview")) {
			if (isBySuburb) {
				if (destSuburb != null) {
					setTitle("Select destination suburb");
					setListName("destination suburbs");
					destSuburb = null;
					getListHandler().setFullList(
							getDataAccessObject().getSuburbs());
				} else if (originStop != null) {
					setTitle("Select stop for " + startSuburb);
					setListName("bus stops in " + startSuburb);
					originStop = null;
					getListHandler().setFullList(
							getDataAccessObject().getSuburbStops(startSuburb));
				} else if (startSuburb != null) {
					setTitle("Select origin suburb");
					setListName("origin suburbs");
					startSuburb = null;
					getListHandler().setFullList(
							getDataAccessObject().getSuburbs());
				}
			} else {
				if (originStop != null) {
					setTitle("Select origin stop");
					setListName("origin stops");
					originStop = null;
					getListHandler().setFullList(
							getDataAccessObject().getStopsOnRoute(route,
									isRightHandMode()));
				} else if (route != null) {
					setTitle("Select route");
					setListName("routes");
					route = null;
					getListHandler().setFullList(
							getDataAccessObject().getRoutes(isRightHandMode()));

				}
			}
			dispatchAccessibilityNotification();
			setAdapterToList();
		} else {
			super.onBackPressed();
		}
	}
}
