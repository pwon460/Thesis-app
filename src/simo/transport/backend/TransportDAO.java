package simo.transport.backend;

import java.util.ArrayList;

public interface TransportDAO {

	// train
	public ArrayList<String> getStations();
	public void setTrainTrip(String start, String stop);

	// ferry
	public ArrayList<String> getWharfs();
	public void setFerryTrip(String start, String stop);
	
	// light rail
	public ArrayList<String> getStops();
	public void setRailTrip(String start, String stop);
	
	/* bus interface methods */
	/* for handling bus when 'route' is clicked */
    public ArrayList<String> getRoutes(Boolean isRightHand); // list of all routes
    // given route, return the stops for that particular route
    public ArrayList<String> getStopsOnRoute(String route, Boolean isRightHand);
    // set the stops picked
    public void setStops(String route, Boolean isRightHand, String origin, String destination);

    /* for handling bus when 'suburb' is clicked */
    public ArrayList<String> getSuburbs();
    // given the origin and/or destination suburb, return all the stops for that suburb
    public ArrayList<String> getSuburbStops(String suburb);
    // set the info for the suburb trip
    public void setSuburbInfo(String originSuburb, String destSuburb, String originStop, String destStop);

    // for all transport types
    public Timetable getTimetable(String transport);

		
}
