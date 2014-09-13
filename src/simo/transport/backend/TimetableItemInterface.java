package simo.transport.backend;

import java.util.Date;

public interface TimetableItemInterface {

	/*
	 * All dates are to be in 'dd/MM/yyyy HH:mm' format where HH is in
	 * 24 hour time
	 */
	public abstract Date getDepartureTime();

	public abstract void setDepartureTime(Date departure);

	public abstract Date getArrivalTime();

	public abstract void setArrivalTime(Date arrival);

	/*
	 * store station platform number, bus stand number, wharf number etc
	 */
	public abstract String getDescription();

	public abstract void setDescription(String description);

}
