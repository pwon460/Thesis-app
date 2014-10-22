package simo.transport.backend;

import java.util.ArrayList;
import java.util.Date;

public class CalendarDTO {

	private int privateCode;
	private int routeID;
	private int dayId;
	private ArrayList<Boolean> days;
	private int departureTime;
	private int originArrival;
	private int destArrival;
	private Date originTime;
	private Date destTime;
	private int originId;
	private int destId;

	public CalendarDTO() {
		// TODO Auto-generated constructor stub
	}

	public int getPrivateCode() {
		return privateCode;
	}

	public void setPrivateCode(int privateCode) {
		this.privateCode = privateCode;
	}

	public int getRouteID() {
		return routeID;
	}

	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}

	public int getDayId() {
		return dayId;
	}

	public void setDayId(int dayId) {
		this.dayId = dayId;
	}

	public ArrayList<Boolean> getDays() {
		return this.days;
	}

	public void setDays(ArrayList<Boolean> days) {
		this.days = days;
	}

	public int getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(int departureTime) {
		this.departureTime = departureTime;
	}

	public int getOriginArrival() {
		return originArrival;
	}

	public void setOriginArrival(int originArrival) {
		this.originArrival = originArrival;
	}

	public int getDestArrival() {
		return destArrival;
	}

	public void setDestArrival(int destArrival) {
		this.destArrival = destArrival;
	}

	public Date getOriginTime() {
		return originTime;
	}

	public void setOriginTime(Date originTime) {
		this.originTime = originTime;
	}

	public Date getDestTime() {
		return destTime;
	}

	public void setDestTime(Date destTime) {
		this.destTime = destTime;
	}

	public int getOriginId() {
		return originId;
	}

	public void setOriginId(int originId) {
		this.originId = originId;
	}

	public int getDestId() {
		return destId;
	}

	public void setDestId(int destId) {
		this.destId = destId;
	}

}
