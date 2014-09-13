package simo.transport.backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MockTripInfo implements TripInfoInterface {

	private String tripInfo;
	private ArrayList<String> orderedStops;
	private ArrayList<Date> orderedTimes;
	private String departure;

	public MockTripInfo(String transport, String departure, String origin,
			String dest, String route, String originStop, String destStop) {
		this.departure = departure;
		if (originStop == null) {
			tripInfo = "From " + origin + " to " + dest;
		} else {
			tripInfo = "From " + originStop + " to " + destStop;
		}
		initInfo();
	}

	private void initInfo() {
		ArrayList<String> stops = new ArrayList<String>();
		String[] departures = new String[] { "A", "B", "C", "D", "E", "F" };
		for (String departure : departures) {
			stops.add(departure);
		}
		orderedStops = stops;

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1; // turn 0-11 to 1-12
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

		String today = dayOfMonth + "/" + month + "/" + year + " ";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		ArrayList<Date> times = new ArrayList<Date>();

		try {
			String departureDate = today + departure;
			Date temp = sdf.parse(departureDate);
			cal.setTime(temp);
			for (int i = 0; i < departures.length; i++) {
				cal.add(Calendar.MINUTE, 5 * i);
				times.add(cal.getTime());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		orderedTimes = times;
	}

	public ArrayList<String> getOrderedStops() {
		return orderedStops;
	}

	public ArrayList<Date> getOrderedTimes() {
		return orderedTimes;
	}

	@Override
	public String getTripInfo() {
		return tripInfo;
	}
}
