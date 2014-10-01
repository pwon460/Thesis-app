package simo.transport.helpers;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import simo.transport.activities.ViewTripActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeBroadcastReceiver extends BroadcastReceiver {

	private ArrayList<Date> times;
	private ArrayList<String> stops;
	private ViewTripActivity activity;
	private Speaker speaker;
	private DateTime now;
	private TripTimeHandler tripTimeHandler;

	public TimeBroadcastReceiver(ViewTripActivity activity) {
		this.activity = activity;
		times = activity.getTimes();
		stops = activity.getStops();
		tripTimeHandler = new TripTimeHandler();
		now = tripTimeHandler.roundToNearestMin(DateTime.now());
		tripTimeHandler.adjustIndexByTime(new DateTime(times.get(0)), now, activity.getTripIndex());
		activity.setViews();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// as soon as it hits the minute, check times
		if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
			now = tripTimeHandler.roundToNearestMin(DateTime.now());

			// check if trip has occurred and update the index accordingly
			int tripIndex = activity.getTripIndex();
			if (tripIndex != ViewTripActivity.TRIP_INVALID
					&& tripIndex != ViewTripActivity.TRIP_FINISHED) {
				tripTimeHandler.adjustIndexByTime(new DateTime(times.get(0)), now, tripIndex);
			}

			if (tripIndex != ViewTripActivity.TRIP_INVALID
					&& tripIndex != ViewTripActivity.TRIP_FINISHED) {
				progressCheck();
				DateTime nextStopArrivalTime = new DateTime(
						times.get(tripIndex));

				int result = tripTimeHandler.compareTimes(now, nextStopArrivalTime);
				if (result == 0) { // times are the same
					if (tripIndex < stops.size() - 1) {
						speaker.speak("Arriving at " + stops.get(tripIndex),
								speaker.getMode());
						activity.setTripIndex(activity.getTripIndex() + 1);
					} else {
						speaker.speak("Arriving at destination", speaker.getMode());
						activity.setTripIndex(ViewTripActivity.TRIP_FINISHED);
					}
				} else if (result > 0) { // time now, is later than the stop
											// currently looked at
					// bring the prev/next up to current 'position' in the trip
					while (result >= 0 && tripIndex < stops.size() - 1) {
						activity.setTripIndex(activity.getTripIndex() + 1);
						nextStopArrivalTime = new DateTime(times.get(tripIndex));
						result = tripTimeHandler.compareTimes(now, nextStopArrivalTime);
					}
				}
			}

			activity.setViews();
		}
	}

	private void progressCheck() {
		DateTime last = new DateTime(times.get(times.size() - 1));
		// issue warning 1-2 mins before reaching dest
		int minsBetween = Minutes.minutesBetween(now, last).getMinutes();

		if (minsBetween == 2) {
			speaker.speak("2 minutes until destination", speaker.getMode());
		} else if (minsBetween == 1) {
			speaker.speak("1 minute until destination", speaker.getMode());
		}
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}

}
