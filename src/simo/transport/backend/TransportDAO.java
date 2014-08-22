package simo.transport.backend;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class TransportDAO implements Serializable {

	/*
	 * serializable so that it can be passed from activity to activity via
	 * intent's putExtra method
	 */
	private static final long serialVersionUID = 1L;

	// train
	public abstract ArrayList<String> getStations();

	public abstract void setTrainTrip(String start, String stop);

	// ferry
	public abstract ArrayList<String> getWharfs();

	public abstract void setFerryTrip(String start, String stop);

	// light rail
	public abstract ArrayList<String> getStops();

	public abstract void setRailTrip(String start, String stop);

	/*
	 * bus interface methods for handling bus when 'route' is clicked
	 */
	// list of all routes
	public abstract ArrayList<String> getRoutes(Boolean isRightHand);

	// given route, return the stops for that particular route
	public abstract ArrayList<String> getStopsOnRoute(String route,
			Boolean isRightHand);

	// set the stops picked
	public abstract void setStops(String route, Boolean isRightHand,
			String origin, String destination);

	/*
	 * for handling bus when 'suburb' is clicked
	 */
	public abstract ArrayList<String> getSuburbs();

	/*
	 * given the origin and/or destination suburb, return all the stops for that
	 * suburb
	 */
	public abstract ArrayList<String> getSuburbStops(String suburb);

	// set the info for the suburb trip
	public abstract void setSuburbInfo(String originSuburb, String destSuburb,
			String originStop, String destStop);

	/*
	 * for all transport types
	 */
	public abstract ArrayList<TimetableItem> getTimetable(String transport);

}
