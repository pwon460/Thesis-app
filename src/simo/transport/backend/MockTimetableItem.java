package simo.transport.backend;

import java.util.Date;

public class MockTimetableItem implements TimetableItem {

	private Date departure;
	private Date arrival;
	private String description;
	private int privateCode;

	public Date getDepartureTime() {
		return departure;
	}

	public Date getArrivalTime() {
		return arrival;
	}

	public String getDescription() {
		return description;
	}

	public void setDepartureTime(Date departure) {
		this.departure = departure;
	}

	public void setArrivalTime(Date arrival) {
		this.arrival = arrival;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int getPrivateCode() {
		return 0;
	}


}
