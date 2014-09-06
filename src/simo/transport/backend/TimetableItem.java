package simo.transport.backend;

import java.io.Serializable;
import java.util.Date;

public abstract class TimetableItem implements Serializable {

	/*
	 * Serializable to allow arraylist of timetable items to be passed from one
	 * activity to the next
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * All dates are assumed to be in 'dd/MM/yyyy HH:mm' format where HH is in
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
