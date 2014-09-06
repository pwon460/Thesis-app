package simo.transport.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import simo.transport.R;
import android.content.Context;
import android.util.Log;

public class MockInformationExtractor implements TransportDAO {

	private ArrayList<String> list = new ArrayList<String>();
	private Context ctx;
	
	public MockInformationExtractor(Context ctx) {
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
	public void setTrainTrip(String start, String stop) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> getWharfs() {
		return getStations();
	}

	@Override
	public void setFerryTrip(String start, String stop) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> getStops() {
		return getStations();
	}

	@Override
	public void setRailTrip(String start, String stop) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> getRoutes(Boolean isRightHand) {
		list.clear();
//		String[] values = new String[] { "1 Austinmer to Wollongong",
//				"2 Stanwell Park to Wollongong", "3",
//				"10 Tuggerah/Wyong to Wyee", "11",
//				"1A Warriewood to Sydney Olympic Park", "1B", "20", "24", "33",
//				"34\5", "66A Gosford & Copacabana...", "100", "200", "300",
//				"400", "500", "660", "700", "800", "900", "M10", "M20", "M30",
//				"M40", "M50", "N00", "S1", "S2", "S2", "S3", "S3", "S4", "S4",
//				"S5", "S5", "S6", "S6", "S7", "S7", "S8", "S8", "S9", "S9",
//				"S10 Heckenberg to Miller Shops via Busby",
//				"S10 Miller Shops to Heckenberg via Busby", "S11", "S12",
//				"S13", "S14", "S15", "S16", "S17 Spring Farm to Narellan",
//				"S17 Narellan to Spring Farm", "T60", "WPSB" };

		list = loadFile(R.raw.route);
		
		return list;
	}

	@Override
	public ArrayList<String> getStopsOnRoute(String route, Boolean isRightHand) {
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
	public void setStops(String route, Boolean isRightHand, String origin,
			String destination) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> getSuburbs() {
		list.clear();
		String[] values = new String[] { "Abbotsbury", "Abbotsford",
				"Aberglasslyn", "Balgowlah", "Birchgrove", "Chatswood",
				"Carlingford", "Dolls Point", "Enfield", "Hornsby", "Killara",
				"Lane Cove", "North Ryde", "Parramatta", "Zetland" };

		for (int i = 0; i < values.length; i++) {
			list.add(values[i]);
		}

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
	public void setSuburbInfo(String originSuburb, String destSuburb,
			String originStop, String destStop) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<TimetableItem> getTimetable(String transport) {
		ArrayList<TimetableItem> timetable = new ArrayList<TimetableItem>();

		String[] departures = new String[] { "1:30", "2:00", "2:30", "3:00",
				"4:00", "8:00", "9:20", "10:00", "11:27" };
		String[] arrivals = new String[] { "1:35", "2:05", "2:35", "3:05",
				"4:05", "8:45", "10:00", "10:23", "11:36" };
		String[] descriptions = new String[] { "platform 1", "platform 2",
				"platform 3", "platform 4", "platform 5" };

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
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

		return timetable;
	}

}
