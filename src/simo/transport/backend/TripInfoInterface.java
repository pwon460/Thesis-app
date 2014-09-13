package simo.transport.backend;

import java.util.ArrayList;
import java.util.Date;

public interface TripInfoInterface {

	/*****************************************************************
	 ** return string containing trip's information ******************
	 ** (NB. these are subject to change, only a rough outline atm) **
	 ** platform/bus stop description ********************************
	 ** eg. ABC to DEF ***********************************************
	 *****************************************************************/
	public String getTripInfo();

	// returns an arraylist of stops ordered by the order they are visited
	public ArrayList<String> getOrderedStops();

	// TODO: returns an arraylist of timestamps(?) ordered by the ordered stops
	// list
	public ArrayList<Date> getOrderedTimes();
}
