package simo.transport.backend;

import java.util.Date;

public class MockTimetableItem implements TimetableItem {

	private Date departure;
	private Date arrival;
	private String description;

	@Override
	public Date getDepartureTime() {
		return departure;
	}

	@Override
	public Date getArrivalTime() {
		return arrival;
	}

	@Override
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


}
