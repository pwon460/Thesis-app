package simo.transport.backend;

import java.util.ArrayList;

public class MockInformationExtractor implements TransportDAO {

	@Override
	public ArrayList<String> getStations() {
		ArrayList<String> mockStations = new ArrayList<String>();

		String[] values = new String[] { "0", "000", "001", "1", "2", "3", "4",
				"5", "6", "7", "8", "9", "A", "B", "C", "D" };

		for (int i = 0; i < values.length; i++) {
			mockStations.add(values[i]);
		}

		return mockStations;
	}

	@Override
	public void setTrainTrip(String start, String stop) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> getWharfs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFerryTrip(String start, String stop) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> getStops() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRailTrip(String start, String stop) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> getRoutes(Boolean isRightHand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getRouteOriginStops(String route,
			Boolean isRightHand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStops(String route, Boolean isRightHand, String origin,
			String destination) {
		// TODO Auto-generated method stub

	}

}