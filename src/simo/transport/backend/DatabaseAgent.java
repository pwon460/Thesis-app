package simo.transport.backend;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;

public class DatabaseAgent implements TransportDAO {

	private static String mode;
	private Context context;
	private static MySQLiteHelper helper;

	public DatabaseAgent(Context context) {
		this.context = context;
	}

	@Override
	// TODO: it requires transort mode.
	public ArrayList<String> getStations() {
		mode = MySQLiteHelper.RAIL_DATABASE;
		if (helper == null) {
			helper = new MySQLiteHelper(this.context, mode);
		} else if (!helper.getDbName().equals(mode)) {
			helper.close();
			helper = new MySQLiteHelper(this.context, mode);
		}
		return helper.getStations();
	}

	@Override
	public ArrayList<String> getWharfs() {
		mode = MySQLiteHelper.FERRY_DATABASE;
		if (helper == null) {
			helper = new MySQLiteHelper(this.context, mode);
		} else if (!helper.getDbName().equals(mode)) {
			helper.close();
			helper = new MySQLiteHelper(this.context, mode);
		}
		return helper.getWharfs();
	}

	@Override
	public ArrayList<String> getStops() {
		mode = MySQLiteHelper.TRAM_DATABASE;
		if (helper == null) {
			helper = new MySQLiteHelper(this.context, mode);
		} else if (!helper.getDbName().equals(mode)) {
			helper.close();
			helper = new MySQLiteHelper(this.context, mode);
		}
		return helper.getStops();
	}

	@Override
	public ArrayList<String> getRoutes(Boolean isRightHand) {
		mode = MySQLiteHelper.BUS_DATABASE;
		if (helper == null) {
			helper = new MySQLiteHelper(context, mode);
		} else if (!helper.getDbName().equals(mode)) {
			helper.close();
			helper = new MySQLiteHelper(this.context, mode);
		}
		return helper.getRoutes(isRightHand);
	}

	@Override
	public ArrayList<String> getStopsOnRoute(String route) {
		return helper.getStopsOnRoute(route);
	}

	@Override
	public ArrayList<String> getSuburbs() {
		mode = MySQLiteHelper.BUS_DATABASE;
		if (helper == null) {
			helper = new MySQLiteHelper(this.context, mode);
		} else if (!helper.getDbName().equals(mode)) {
			helper.close();
			helper = new MySQLiteHelper(this.context, mode);
		}
		return helper.getSuburbs();
	}

	@Override
	public ArrayList<String> getSuburbStops(String suburb) {
		return helper.getSuburbStops(suburb);
	}

	@Override
	public void setTrip(String origin, String destination) {
		helper.setTrip(origin, destination);

	}

	@Override
	public void setByStopInfo(String route, String originStop, String destStop) {
		helper.setByStopInfo(route, originStop, destStop);
	}

	@Override
	public void setBySuburbInfo(String originSuburb, String destSuburb,
			String originStop, String destStop) {
		helper.setBySuburbInfo(originSuburb, destSuburb, originStop, destStop);
	}

	@Override
	// TODO: transport parameter is not required.
	// 		 Check transport parameter values is one of MySQLiteDastabse database name.
	public ArrayList<TimetableItem> getTimetable(String transport) {
		if (transport.equals("Bus")) {
			mode = MySQLiteHelper.BUS_DATABASE;
		} else if (transport.equals("Train")) {
			mode = MySQLiteHelper.RAIL_DATABASE;
		} else if (transport.equals("Ferry")) {
			mode = MySQLiteHelper.FERRY_DATABASE;
		} else if (transport.equals("Light Rail")) {
			mode = MySQLiteHelper.TRAM_DATABASE;
		}
		if (helper == null) {
			helper = new MySQLiteHelper(this.context, mode);
		} else if (!helper.getDbName().equals(mode)) {
			helper.close();
			helper = new MySQLiteHelper(this.context, mode);
		}
		mode = transport;
		return helper.getTimetable(mode);
	}

	@Override
	// TODO: Check transport parameter values is one of MySQLiteDastabse database name.
	//		 Param transport is not required.
	public TripInfo getTrip(String transport, int privateCode, int originId,
			int destinationId) {
		// mode = transport;
		/*
		if (mode.equals("Bus")) {
			dbName = MySQLiteHelper.BUS_DATABASE;
		} else if (mode.equals("Train")) {
			dbName = MySQLiteHelper.RAIL_DATABASE;
		} else if (mode.equals("Ferry")) {
			dbName = MySQLiteHelper.FERRY_DATABASE;
		} else if (mode.equals("Light Rail")) {
			dbName = MySQLiteHelper.TRAM_DATABASE;
		}
		if (helper == null) {
			helper = new MySQLiteHelper(this.context, dbName);
		} else if (!helper.getDbName().equals(dbName)) {
			helper.close();
			helper = new MySQLiteHelper(this.context, dbName);
		}
		 */
		return helper.getTrip(mode, privateCode, originId, destinationId);
	}

	@Override
	public boolean isValidTrip(Location currLocation) {
		return helper.isValidTrip(currLocation);
	}

	@Override
	public boolean isAtNextStop(Location location, String nextStop) {
		return helper.isAtNextStop(location, nextStop);
	}

}
