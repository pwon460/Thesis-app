package simo.transport.backend;

import java.util.ArrayList;
import java.util.Date;

// last page
public class DetailedInfo implements TripInfo {

	private ArrayList<String> paths;
	//private String origin;
	//private String destination;
	private ArrayList<Date> orderedTimes;
	// TODO: check whether it needs to be hashMap if not, change to ArrayList<GPSValues> in order of paths
	//private HashMap<String, GPSValues> stopInfo;
	private ArrayList<GPSValues> stopInfo;
	private String tripDescription;
	/*****************************************************************
	 ** return string containing trip's information ******************
	 ** (NB. these are subject to change, only a rough outline atm) **
	 ** platform/bus stop description ********************************
	 ** eg. ABC to DEF ***********************************************
	 *****************************************************************/
	@Override
	public String getTripInfo() {
		return this.tripDescription;
	}
	
	public void setTripDescription(String description) {
		this.tripDescription = description;
	}
	// returns an arraylist of stops ordered by the order they are visited
	public ArrayList<String> getOrderedStops() {
		return this.paths;
	}

	public void setPaths(ArrayList<String> paths) {
		this.paths = paths;
	}

	// TODO: returns an arraylist of timestamps(?) ordered by the ordered stops
	// list
	public ArrayList<Date> getOrderedTimes() {
		return this.orderedTimes;
	}

	public void setOrderedTimes(ArrayList<Date> orderedTimes) {
		this.orderedTimes = orderedTimes;
	}

	/*
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	*/
	public ArrayList<GPSValues> getStopInfo() {
		return stopInfo;
	}

	public void setStopInfo(ArrayList<GPSValues> stopInfo) {
		this.stopInfo = stopInfo;
	}

}
