package simo.transport.backend;

import java.util.ArrayList;

public interface TransportDAOInterface {
	
	/*******************************/
	/*********** getters ***********/
	/*******************************/

	// returns list of all train stations
	public ArrayList<String> getStations();

	// returns list of all ferry wharves
	public ArrayList<String> getWharfs();

	// returns list of all light rail stops
	public ArrayList<String> getStops();

	/*
	 * returns list of all bus routes. isRightHand == true means the route
	 * number will be placed on the left of the route description if it is
	 * false, then the route number will be placed on the right of the route
	 * description
	 */
	public ArrayList<String> getRoutes(Boolean isRightHand);

	// given route, return the stops for that particular route
	public ArrayList<String> getStopsOnRoute(String route);

	// returns list of all suburbs that buses visit
	public ArrayList<String> getSuburbs();

	// given a suburb, returns a list of all the stops for that suburb
	public ArrayList<String> getSuburbStops(String suburb);

	
	
	/*******************************/
	/*********** setters ***********/
	/*******************************/
	
	/******************************************************************************/
	/******************************************************************************/
	/*** these setters will be called before getTimetable or getTrip are called ***/
	/******************************************************************************/
	/******************************************************************************/

	// set the origin and dest for train, ferry, light rail
	public void setTrip(String origin, String destination);

	// set the stops picked
	public void setByStopInfo(String route, String originStop, String destStop);

	// set the info for the suburb trip
	public void setBySuburbInfo(String originSuburb, String destSuburb,
			String originStop, String destStop);

	/*
	 * assumes one of the 3 setters has been correctly called - given a mode of
	 * transport, return the timetable for that transport type
	 */
	public ArrayList<TimetableItemInterface> getTimetable(String transport);

	// returns the information for the trip that the user selected from the
	// timetable
	public TripInfoInterface getTrip(String transport, String departureTime);

}
