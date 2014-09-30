package simo.transport.helpers;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import simo.transport.activities.ViewTripActivity;

public class TripTimeHandler {

	private static final int EQUAL_TO = 0;
	private static final int LESS_THAN = -1;
	private static final int GREATER_THAN = 1;
	
	// set trip index to be invalid if the transport has already supposedly left
	public void adjustIndexByTime(DateTime firstStopTime, DateTime now, int tripIndex) {
		int result = compareTimes(now, firstStopTime);
		if (result > 0) {
			tripIndex = ViewTripActivity.TRIP_INVALID;
		}
	}
	
	public int compareTimes(DateTime now, DateTime nextStopArrivalTime) {
		int t1Hour = now.getHourOfDay();
		int t1Mins = now.getMinuteOfHour();
		int t2Hour = nextStopArrivalTime.getHourOfDay();
		int t2Mins = nextStopArrivalTime.getMinuteOfHour();

		if (t1Hour > t2Hour) {
			return GREATER_THAN;
		} else if (t1Hour < t2Hour) {
			return LESS_THAN;
		} else {
			if (t1Mins > t2Mins) {
				return GREATER_THAN;
			} else if (t1Mins < t2Mins) {
				return LESS_THAN;
			} else {
				return EQUAL_TO;
			}
		}
	}
	
	public DateTime roundToNearestMin(DateTime dt) {
		DateTime hour = dt.hourOfDay().roundFloorCopy();
		long millisSinceHour = new Duration(hour, dt).getMillis();
		int roundedMinutes = ((int) Math.round(millisSinceHour / 60000.0));
		return hour.plusMinutes(roundedMinutes);
	}
}
