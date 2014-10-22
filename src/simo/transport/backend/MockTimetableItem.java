package simo.transport.backend;

import java.util.ArrayList;
import java.util.Date;

public class MockTimetableItem implements TimetableItem {

	private Date departure;
	private Date arrival;
	private String description;

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
<<<<<<< HEAD
		// TODO Auto-generated method stub
=======
>>>>>>> 7656b5577bb07c1395562293fce583d95d0fa9c6
		return 0;
	}

	@Override
<<<<<<< HEAD
	public int getOriginId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDestId() {
		// TODO Auto-generated method stub
=======
	public ArrayList<Boolean> getDays() {
		ArrayList<Boolean> temp = new ArrayList<Boolean>();
		
		for (int i = 0; i < 7; i++) {
			temp.add(true);
		}
		
		return temp;
	}

	@Override
	public int getOriginId() {
>>>>>>> 7656b5577bb07c1395562293fce583d95d0fa9c6
		return 0;
	}

	@Override
<<<<<<< HEAD
	public ArrayList<Boolean> getDays() {
		// TODO Auto-generated method stub
		return null;
	}

=======
	public int getDestId() {
		return 0;
	}
>>>>>>> 7656b5577bb07c1395562293fce583d95d0fa9c6

}
