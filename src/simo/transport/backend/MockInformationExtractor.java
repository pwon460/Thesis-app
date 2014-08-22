package simo.transport.backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MockInformationExtractor extends TransportDAO {

	private static final long serialVersionUID = 1L;
	private ArrayList<String> list = new ArrayList<String>();

	@Override
	public ArrayList<String> getStations() {
		list.clear();
		String[] values = new String[] { "0", "000", "001", "1", "2", "3", "4",
				"5", "6", "7", "8", "9", "A", "B", "C", "D", "Aldnoah",
				"Alberta", "Aberdeen", "Abba", "Bernard", "Belford",
				"Clocktown", "Mockingbird", "Upton" };

		for (int i = 0; i < values.length; i++) {
			list.add(values[i]);
		}

		return list;
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
		String[] values = new String[] { "000", "001", "002", "003", "100",
				"200", "300", "400", "500", "600", "700", "800", "900", "M00",
				"M50", "N00" };

		for (int i = 0; i < values.length; i++) {
			list.add(values[i]);
		}

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
				"4:00" };
		String[] arrivals = new String[] { "1:35", "2:05", "2:35", "3:05",
				"4:05" };
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
				item.setDescription(descriptions[i]);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			timetable.add(item);
		}

		return timetable;
	}

}
