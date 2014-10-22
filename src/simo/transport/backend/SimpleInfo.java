package simo.transport.backend;

import java.util.ArrayList;
import java.util.Date;
// last second page
public class SimpleInfo implements TimetableItem {
	
	private Date departureTime;
	private Date arrivalTime;
	private String description;
	private int privateCode;
	private int originId;
	private int destId;
	private ArrayList<Boolean> days;

	public SimpleInfo() {
		// TODO Auto-generated constructor stub
	}
	/*
	 * All dates are to be in 'dd/MM/yyyy HH:mm' format where HH is in
	 * 24 hour time
	 */
	@Override
	public Date getDepartureTime() {
		return this.departureTime;
	}

	@Override
	public Date getArrivalTime() {
		return this.arrivalTime;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getPrivateCode() {
		return privateCode;
	}

	public void setPrivateCode(int privateCode) {
		this.privateCode = privateCode;
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
	public void setDays(ArrayList<Boolean> days) {
		this.days = days;
	}

	@Override
	public ArrayList<Boolean> getDays() {
		return this.days;
	}
	
	

}
