package simo.transport.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import simo.transport.R;
import android.content.Context;
import android.util.Log;

/*
 * This is a pretend implementation for testing purposes only
 */
public class MockTransportDAO implements TransportDAO {

	private ArrayList<String> list = new ArrayList<String>();
	private Context ctx;
	private String origin;
	private String dest;
	private String route;
	private String originStop;
	private String destStop;

	public MockTransportDAO(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	public ArrayList<String> getStations() {
		list.clear();
		// String[] values = new String[] { "Aldnoah", "Alberta", "Aberdeen",
		// "Abba", "Bernard", "Belford", "Clocktown", "Mockingbird",
		// "Upton", "Acheron", "North Beach", "Gerogery", "Coolatai",
		// "Wimba", "Leeman", "Lake Cooper", "Atherton", "Roebourne",
		// "Milloo", "Broadwater", "Newhaven", "Towan", "Iama",
		// "Silent Grove", "Saint Kilda", "Yarrock", "Pampas", "Stirling",
		// "Clandulla", "Kalorama", "Bald Hills", "Bulumwaal",
		// "Wallumburrawang", "Yarras" };

		list = loadFile(R.raw.suburb);

		return list;
	}

	private ArrayList<String> loadFile(int resId) {
		InputStream is = ctx.getResources().openRawResource(resId);
		InputStreamReader inputReader = new InputStreamReader(is);
		BufferedReader bufferedReader = new BufferedReader(inputReader);
		ArrayList<String> stuff = new ArrayList<String>();
		String line;

		try {
			while ((line = bufferedReader.readLine()) != null) {
				stuff.add(line);
			}
		} catch (IOException e) {
			Log.d("error", "unable to read file");
		}

		return stuff;
	}

	@Override
	public ArrayList<String> getWharfs() {
		return getStations();
	}

	@Override
	public ArrayList<String> getStops() {
		return getStations();
	}

	@Override
	public ArrayList<String> getRoutes(Boolean isRightHand) {
		list.clear();
		// String[] values = new String[] { "1 Austinmer to Wollongong",
		// "2 Stanwell Park to Wollongong", "3",
		// "10 Tuggerah/Wyong to Wyee", "11",
		// "1A Warriewood to Sydney Olympic Park", "1B", "20", "24", "33",
		// "34\5", "66A Gosford & Copacabana...", "100", "200", "300",
		// "400", "500", "660", "700", "800", "900", "M10", "M20", "M30",
		// "M40", "M50", "N00", "S1", "S2", "S2", "S3", "S3", "S4", "S4",
		// "S5", "S5", "S6", "S6", "S7", "S7", "S8", "S8", "S9", "S9",
		// "S10 Heckenberg to Miller Shops via Busby",
		// "S10 Miller Shops to Heckenberg via Busby", "S11", "S12",
		// "S13", "S14", "S15", "S16", "S17 Spring Farm to Narellan",
		// "S17 Narellan to Spring Farm", "T60", "WPSB" };

		list = loadFile(R.raw.route);

		return list;
	}

	@Override
	public ArrayList<String> getStopsOnRoute(String route) {
		list.clear();
		String[] values = new String[] { "abc", "bcd", "cde", "def", "fgh",
				"ghi", "hij", "ijk", "jkl", "klm", "lmn", "mno", "nop", "opq",
				"pqr" };

		for (int i = 0; i < values.length; i++) {
			list.add(values[i]);
		}

		return list;
	}

	@Override
	public ArrayList<String> getSuburbs() {
		list.clear();
		// String[] values = new String[] { "Abbotsbury", "Abbotsford",
		// "Aberglasslyn", "Balgowlah", "Birchgrove", "Chatswood",
		// "Carlingford", "Dolls Point", "Enfield", "Hornsby", "Killara",
		// "Lane Cove", "North Ryde", "Parramatta", "Zetland" };
		//
		// for (int i = 0; i < values.length; i++) {
		// list.add(values[i]);
		// }

		list = loadFile(R.raw.suburb);

		return list;
	}

	@Override
	public ArrayList<String> getSuburbStops(String suburb) {
		list.clear();
		String[] values = new String[] { "abc", "bcd", "cde", "def", "fgh",
				"ghi", "hij", "ijk", "jkl", "klm", "lmn", "mno", "nop", "opq",
				"pqr" };

		for (int i = 0; i < values.length; i++) {
			list.add(values[i]);
		}

		return list;
	}

	@Override
	public void setTrip(String origin, String destination) {
		this.origin = origin;
		this.dest = destination;
		Log.d("debug", "start at " + origin + ", stop at " + destination);
	}

	@Override
	public void setByStopInfo(String route, String originStop, String destStop) {
		this.route = route;
		this.origin = originStop;
		this.dest = destStop;
		Log.d("debug", "route " + route + ", start at " + originStop
				+ ", stop at " + destStop);
	}

	@Override
	public void setBySuburbInfo(String originSuburb, String destSuburb,
			String originStop, String destStop) {
		this.origin = originSuburb;
		this.dest = destSuburb;
		this.originStop = originStop;
		this.destStop = destStop;
		Log.d("debug", "start at " + originStop + " in " + originSuburb
				+ ", stop at " + destStop + " in " + destSuburb);
	}

	@Override
	public ArrayList<TimetableItem> getTimetable(String transport) {
		Log.d("debug", "mode of transport is " + transport);

		ArrayList<TimetableItem> timetable = new ArrayList<TimetableItem>();

		String[] departures = new String[] { "13:30", "14:00", "15:30",
				"16:00", "17:00", "17:30", "18:00", "19:00", "19:30" };
		String[] arrivals = new String[] { "13:45", "14:15", "15:45", "16:15",
				"17:15", "17:45", "18:15", "19:15", "19:45" };
		String[] descriptions = new String[] { "platform 1", "platform 2",
				"platform 3", "platform 4", "platform 5" };

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1; // turn 0-11 to 1-12
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		String today = dayOfMonth + "/" + month + "/" + year + " ";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		for (int i = 0; i < departures.length; i++) {
			TimetableItem item = new MockTimetableItem();
			try {
				item.setDepartureTime(sdf.parse(today + departures[i]));
				item.setArrivalTime(sdf.parse(today + arrivals[i]));
				item.setDescription(descriptions[i % 5]);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			timetable.add(item);
		}

		// for testing purposes, add one that's 2 minutes from now
		TimetableItem item = new MockTimetableItem();
		DateTime dt = DateTime.now();
		int hours = dt.getHourOfDay();
		int mins = dt.getMinuteOfHour() + 1;
		String temp = hours + ":" + mins;
		mins += 3;
		String temp2 = hours + ":" + mins;
		
		try {
			item.setDepartureTime(sdf.parse(today + temp));
			item.setArrivalTime(sdf.parse(today + temp2));
			item.setDescription("time test");
			timetable.add(item);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return timetable;
	}

	@Override
	public TripInfo getTrip(String transport, String departure) {
		TripInfo trip = new MockTripInfo(transport, departure, origin,
				dest, route, originStop, destStop);
		return trip;
	}

}
