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
	
	// bus - refer to tripview
	public ArrayList<String> getRoutes(Boolean isRightHand);
	public ArrayList<String> getRouteOriginStops(String route, Boolean isRightHand);
	public void setStops(String route, Boolean isRightHand, String origin, String destination);
	
	// TODO: fix these later
//	public ArrayList<String> getSuburbs();
//	public ArrayList<String> getSuburbStops(String originStop);
		
}
