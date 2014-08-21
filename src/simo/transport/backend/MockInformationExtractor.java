package simo.transport.backend;

import java.util.ArrayList;

public class MockInformationExtractor implements TransportDAO {

	private ArrayList<String> list = new ArrayList<String>();

	@Override
	public ArrayList<String> getStations() {
		list.clear();
		String[] values = new String[] { "0", "000", "001", "1", "2", "3", "4",
				"5", "6", "7", "8", "9", "A", "B", "C", "D", "Aldnoah",
				"Alberta", "Aberdeen", "Abba", "Bernard", "Belford",
				"Clocktown" };

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
		String[] values = new String[] { "000", "001",
				"002", "003", "100", "200",
				"300", "400", "500", "600", "700",
				"800", "900", "M00", "M50", "N00" };
		
		for (int i = 0; i < values.length; i++) {
			list.add(values[i]);
		}
		
		return list;
	}

	@Override
	public ArrayList<String> getStopsOnRoute(String route, Boolean isRightHand) {
		list.clear();
		String[] values = new String[] { "abc", "bcd",
				"cde", "def", "fgh", "ghi",
				"hij", "ijk", "jkl", "klm", "lmn",
				"mno", "nop", "opq", "pqr" };
		
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
		String[] values = new String[] { "abc", "bcd",
				"cde", "def", "fgh", "ghi",
				"hij", "ijk", "jkl", "klm", "lmn",
				"mno", "nop", "opq", "pqr" };
		
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
	public Timetable getTimetable(String transport) {
		return null;
	}

}
