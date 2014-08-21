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

			if (isBySuburb) {
				if (startSuburb == null) {
					getActionStack().add("listview");
					getIndexHandler().clearList();
					startSuburb = tv.getText().toString();
					setTitle("Select destination suburb");
					ArrayList<String> tempList = getDataAccessObject()
							.getSuburbs();
					tempList.remove(startSuburb);
					getListHandler().setFullList(tempList);
					setAdapterToList();
				} else if (destSuburb == null) {
					getActionStack().add("listview");
					getIndexHandler().clearList();
					destSuburb = tv.getText().toString();
					setTitle("Select stop for " + startSuburb);
					getListHandler().setFullList(
							getDataAccessObject().getSuburbStops(startSuburb));
					setAdapterToList();
				} else if (originStop == null) {
					getActionStack().add("listview");
					getIndexHandler().clearList();
					originStop = tv.getText().toString();
					setTitle("Select stop for " + destSuburb);
					getListHandler().setFullList(
							getDataAccessObject().getSuburbStops(destSuburb));
					setAdapterToList();
				} else {
					destStop = tv.getText().toString();
					getDataAccessObject().setSuburbInfo(startSuburb,
							destSuburb, originStop, destStop);
					Intent intent = new Intent(this, ShowTripActivity.class);
					// TODO: figure out what to pass onwards
					startActivity(intent);
				}
			} else {
				if (route == null) {
					getActionStack().add("listview");
					getIndexHandler().clearList();
					route = tv.getText().toString();
					setTitle("Select origin stop");
					getListHandler().setFullList(
							getDataAccessObject().getStopsOnRoute(route,
									isRightHandMode()));
					setAdapterToList();
				} else if (originStop == null) {
					getActionStack().add("listview");
					getIndexHandler().clearList();
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
					Intent intent = new Intent(this, ShowTripActivity.class);
					// TODO: figure out what to pass onwards
					startActivity(intent);
				}
			}
		}
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
				if (originStop != null) {
					setTitle("Select stop for " + startSuburb);
					originStop = null;
					getListHandler().setFullList(
							getDataAccessObject().getSuburbStops(startSuburb));
				} else if (destSuburb != null) {
					setTitle("Select destination suburb");
					destSuburb = null;
					ArrayList<String> tempList = getDataAccessObject()
							.getSuburbs();
					tempList.remove(startSuburb);
					getListHandler().setFullList(tempList);
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
