package simo.transport.backend;

import java.util.ArrayList;
import java.util.Date;

public interface TimetableItem {

	/*
	 * All dates are to be in 'dd/MM/yyyy HH:mm' format where HH is in 24 hour
	 * time
	 */
	public Date getDepartureTime();

	public void setDepartureTime(Date departure);

	public Date getArrivalTime();

	public void setArrivalTime(Date arrival);

	/*
	 * store station platform number, bus stand number, wharf number etc
	 */
	public String getDescription();

	public void setDescription(String description);

	public int getPrivateCode();

	/*
	 * returns an arraylist containing the days that this particular timetable
	 * item occurs on
	 */
	public ArrayList<Boolean> getDays();

}
