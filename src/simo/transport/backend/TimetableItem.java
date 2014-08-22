package simo.transport.backend;

import java.util.Date;

public interface TimetableItem {

	/*
	 * All dates are assumed to be in 'dd/MM/yyyy HH:mm' format where HH is in 24
	 * hour time
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

}
