package simo.transport.helpers;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import simo.transport.activities.ViewTripActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

public class TimeBroadcastReceiver extends BroadcastReceiver {

	private int tripIndex = 0;
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
		tripTimeHandler.adjustIndexByTime(new DateTime(times.get(0)), now, tripIndex);
		setViews();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// as soon as it hits the minute, check times
		if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
			now = tripTimeHandler.roundToNearestMin(DateTime.now());

			// check if trip has occurred and update the index accordingly
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
								getMode());
						tripIndex++;
					} else {
						speaker.speak("Arriving at destination", getMode());
						tripIndex = ViewTripActivity.TRIP_FINISHED;
					}
				} else if (result > 0) { // time now, is later than the stop
											// currently looked at
					// bring the prev/next up to current 'position' in the trip
					while (result >= 0 && tripIndex < stops.size() - 1) {
						tripIndex++;
						nextStopArrivalTime = new DateTime(times.get(tripIndex));
						result = tripTimeHandler.compareTimes(now, nextStopArrivalTime);
					}
				}
			}

			setViews();
		}
	}

	private void progressCheck() {
		DateTime last = new DateTime(times.get(times.size() - 1));
		// issue warning 1-2 mins before reaching dest
		int minsBetween = Minutes.minutesBetween(now, last).getMinutes();

		if (minsBetween == 2) {
			speaker.speak("2 minutes until destination", getMode());
		} else if (minsBetween == 1) {
			speaker.speak("1 minute until destination", getMode());
		}
	}

	private int getMode() {
		int mode;

		// if (speaker.isSpeaking()) {
		// mode = TextToSpeech.QUEUE_FLUSH;
		// } else {
		// mode = TextToSpeech.QUEUE_ADD;
		// }

		mode = TextToSpeech.QUEUE_ADD;

		return mode;
	}

	private void setViews() {
		activity.setNextStop(tripIndex);
		activity.setPrevStop(tripIndex);
		activity.setNumStopsLeft(tripIndex);
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}

}
