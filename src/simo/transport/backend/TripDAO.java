package simo.transport.backend;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

public class TripDAO {

	private Context context;

	public TripDAO(Context context) {
		this.context = context;
	}	
	
	/*
	// TODO: move all constants and put them in one class
	private static final String BUS = "bus";
	private static final String RAIL = "rail";
	private static final String FERRY = "ferry";
	private static final String TRAM = "tram";

	private ArrayList<String> list = new ArrayList<String>();

	private String originId;
	private String destId;
	private String route;
	private String originStop;
	private String destStop;
	private String mode;
	private Location currLocation = null;
	private SQLiteDatabase busDb;
	private SQLiteDatabase railDb;
	private SQLiteDatabase ferryDb;
	private SQLiteDatabase tramDb;
	private MySQLiteHelper busHelper;
	private MySQLiteHelper railHelper;
	private MySQLiteHelper ferryHelper;
	private MySQLiteHelper tramHelper;

	// find which transport db needs to be used.
	private SQLiteDatabase getCurrentDb() {
		Cursor cursor = null;
		SQLiteDatabase curr = null;
		if (this.mode.equals(BUS)) {
			curr = this.busDb;
		} else if (this.mode.equals(RAIL)) {
			curr = this.railDb;
		} else if (this.mode.equals(FERRY)) {
			curr = this.ferryDb;
		} else if (this.mode.equals(TRAM)) {
			curr = this.tramDb;
		}
		return curr;
	}

	// returns list of all train stations
	public ArrayList<String> getStations() {
		if (this.railHelper == null) {
			this.railHelper = new MySQLiteHelper(this.context, MySQLiteHelper.RAIL_DATABASE, MySQLiteHelper.RAIL_DB_VERSION);
			this.railDb = this.railHelper.getReadableDatabase();
		}
		ArrayList<String> stations = new ArrayList<String>();
		String query = "SELECT name from STOPS order by name";
		Cursor cursor = this.railDb.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			stations.add(cursor.getString(0));
		} while (cursor.moveToNext());
		this.mode = BUS;
		return stations;
	}

	// returns list of all ferry wharves
	public ArrayList<String> getWharfs() {
		if (this.ferryHelper == null) {
			this.ferryHelper = new MySQLiteHelper(this.context, MySQLiteHelper.FERRY_DATABASE, MySQLiteHelper.FERRY_DB_VERSION);
			this.ferryDb = this.ferryHelper.getReadableDatabase();
		}
		ArrayList<String> wharfs = new ArrayList<String>();
		String query = "SELECT name from STOPS order by name";
		Cursor cursor = this.ferryDb.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			wharfs.add(cursor.getString(0));
		} while (cursor.moveToNext());
		this.mode = FERRY;
		return wharfs;
	}

	// returns list of all light rail stops
	public ArrayList<String> getStops() {
		if (this.tramHelper == null) {
			this.tramHelper = new MySQLiteHelper(this.context, MySQLiteHelper.TRAM_DATABASE, MySQLiteHelper.TRAM_DB_VERSION);
			this.tramDb = this.tramHelper.getReadableDatabase();
		}
		ArrayList<String> stations = new ArrayList<String>();
		String query = "SELECT name from STOPS order by name";
		Cursor cursor = this.tramDb.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			stations.add(cursor.getString(0));
		} while (cursor.moveToNext());
		this.mode = TRAM;
		return stations;
	}
	
	// returns list of all suburbs that buses visit
	public ArrayList<String> getSuburbs() {
		if (this.busHelper == null) {
			this.busHelper = new MySQLiteHelper(this.context, MySQLiteHelper.BUS_DATABASE, MySQLiteHelper.BUS_DB_VERSION);
			this.busDb = this.busHelper.getReadableDatabase();
		}
		ArrayList<String> suburbs = new ArrayList<String>();
		String query = "SELECT distinct suburb from STOPS order by suburb";
		Cursor cursor = this.busDb.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			suburbs.add(cursor.getString(0));
		} while (cursor.moveToNext());
		this.mode = BUS;
		return suburbs;
	}

	// given a suburb, returns a list of all the stops for that suburb
	// this function should be called after getSuburb.
	// so at that point busDb is created.
	public ArrayList<String> getSuburbStops(String suburb) {
		ArrayList<String> stops = new ArrayList<String>();
		String query = "SELECT name from STOPS where suburb = ? order by name";
		Cursor cursor = this.busDb.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			stops.add(cursor.getString(0));
		} while (cursor.moveToNext());
		return stops;
	}

	//
	// returns list of all bus routes. isRightHand == true means the route
	// number will be placed on the left of the route description if it is
	// false, then the route number will be placed on the right of the route
	// description
	//
	public ArrayList<String> getRoutes(Boolean isRightHand) {
		if (this.busHelper == null) {
			this.busHelper = new MySQLiteHelper(this.context, MySQLiteHelper.BUS_DATABASE, MySQLiteHelper.BUS_DB_VERSION);
			this.busDb = this.busHelper.getReadableDatabase();
		}
		ArrayList<String> digit = new ArrayList<String>();
		ArrayList<String> alphabet = new ArrayList<String>();
		String query = "SELECT distinct lineName, description from DAYS order by cast (lineName as integer)";
		Cursor cursor = this.busDb.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			String lineName = cursor.getString(0);
			if (isRightHand == true) {
				if (Character.isDigit(lineName.charAt(0))) {
					digit.add(cursor.getString(0) + cursor.getString(1));
				} else {
					alphabet.add(cursor.getString(0) + cursor.getString(1));
				}
			} else {
				if (Character.isDigit(lineName.charAt(0))) {
					digit.add(cursor.getString(0) + cursor.getString(1));
				} else {
					alphabet.add(cursor.getString(0) + cursor.getString(1));
				}
			}
		} while (cursor.moveToNext());
		digit.addAll(alphabet);
		this.mode = BUS;
		return digit;
	}

	// Helper method for solution 1 in getStopsOnRoute
	String makePlaceHolders (int length) {
		if (length > 1) {
			StringBuilder holders = new StringBuilder(length * 2 -1);
			holders.append("?");
			for (int i = 1; i < length; i++) {
				holders.append(",?");
			}
			return holders.toString();
		}
		return null;
	}

	// given route, return the stops for that particular route
	// this function should be called after busDb is created in other function.
	public ArrayList<String> getStopsOnRoute(String route) {
		ArrayList<String> stops = new ArrayList<String>();
		// TODO: check which one has better performance
		//		 query including join might have poor performance in low cpu environment
		// solution 1
		//*
		Cursor cursor = db.query(DAYS, new String[] {"privateCode"}, "lineName = ?", new String[] {route}, null, null, null);
		ArrayList<String> codes = new ArrayList<String>();
		if (cursor.moveToFirst()) {
			codes.add(cursor.getString(0));
		} while (cursor.moveToNext());
		String[] pcodes = new String[codes.size()];
		Cursor cursor2 = db.rawQuery("Select distinct stopID from TIMETABLE where privateCode in (" + makePlaceHolders(pcodes.length) + ")", codes.toArray(pcodes));
		ArrayList<String> stopIds = new ArrayList<String>();
		if (cursor2.moveToFirst()) {
			stopIds.add(cursor2.getString(0));
		} while (cursor.moveToNext());
		String[] ids = new String[stopIds.size()];
		Cursor cursor3 = db.rawQuery("Select distinct name from STOPS where atcoCode in (" + makePlaceHolders(ids.length) + ") order by name", codes.toArray(ids));
		if (cursor3.moveToFirst()) {
			stops.add(cursor3.getString(0));
		} while (cursor.moveToNext());
		cursor.close();
		cursor2.close();
		//
		// solution 2
		Cursor cursor = this.busDb.rawQuery("Select distinct s.name from stop as s "
								    + "join timetable as t on s.atcoCode = t.stopId "
								    + "join days as d on t.privateCode = d.privateCode "
								    + "where d.lineName = ?", new String[] {route});
		if (cursor.moveToFirst()) {
			stops.add(cursor.getString(0));
		} while (cursor.moveToNext());
		return stops;
	}
	
	// this function should be called after busDb is created in other function.
	public ArrayList<String> filterSuburb (String origin) {
		ArrayList<String> suburbs = new ArrayList<String>();
		Cursor cursor = this.busDb.rawQuery("Select distinct s.suburb from stops as s "
									+ "join timetable as d on s.atcoCode = d.stopId "
									+ "join timetable as o on d.privateCode = o.privateCode "
									+ "where o.suburb = ? and d.seq > o.seq and"
									+ "order by s.suburb", new String[] {origin});
		if (cursor.moveToFirst()) {
			suburbs.add(cursor.getString(0));
		} while (cursor.moveToNext());
		return suburbs;
	}
		
	
	// maybe better to store possible stops when filterSuburb func is called
	// like HashMap<suburb, stopInfo>
	// then the func below just return the already searched result
	// this function should be  called after busDb is created in other function.
	public ArrayList<String> filterStops (String suburb) {
		ArrayList<String> stops = new ArrayList<String>();
		Cursor cursor = this.busDb.rawQuery("Select distinct s.name from stops as s "
									+ "join timetable as d on s.atcoCode = d.stopId "
									+ "join timetable as o on d.privateCode = o.privateCode "
									+ "where s.suburb = ? and d.seq > o.seq"
									+ "order by s.name", new String[] {suburb});
		if (cursor.moveToFirst()) {
			stops.add(cursor.getString(0));
		} while (cursor.moveToNext());
		return stops;
	}

	// set names and ids of the origin and destination for train, ferry, light rail
	// TODO: take out searching id parts and make in one function
	//		 then remove duplicated code with above function
	public void setTrip(String origin, String destination) {
		this.originStop = origin;
		this.destStop = destination;
		Cursor cursor = null;
		SQLiteDatabase curr = this.getCurrentDb();
		cursor = curr.rawQuery("Select atcoCode, name from stops where name = ? or name = ?", new String[] {origin, destination});
		if (cursor.moveToFirst()) {
			String name = cursor.getString(1);
			if (name.equals(origin)) {
				this.originId = cursor.getString(0);
			} else {
				this.destId = cursor.getString(0);
			}
		} while (cursor.moveToNext());
	}

	// set the stops picked
	public void setByStopInfo(String route, String originStop, String destStop) {
		// TODO: route can be multiple, do we need to store this?
		// 		 take out searching id parts and make in one function
		//		 then remove duplicated code with above function
		this.route = route;
		this.originStop = originStop;
		this.destStop = destStop;
		Cursor cursor = null;
		SQLiteDatabase curr = this.getCurrentDb();
		cursor = curr.rawQuery("Select atcoCode, name from stops where name = ? or name = ?", new String[] {originStop, destStop});
		if (cursor.moveToFirst()) {
			String name = cursor.getString(1);
			if (name.equals(originStop)) {
				this.originId = cursor.getString(0);
			} else {
				this.destId = cursor.getString(0);
			}
		} while (cursor.moveToNext());
	}

	// set the info for the suburb trip
	public void setBySuburbInfo(String originSuburb, String destSuburb,
			String originStop, String destStop) {
		// TODO: do we need to store suburb information?
		// 		 take out searching id parts and make in one function
		//		 then remove duplicated code with above function		
		this.originStop = originStop;
		this.destStop = destStop;
		Cursor cursor = null;
		SQLiteDatabase curr = this.getCurrentDb();
		cursor = curr.rawQuery("Select atcoCode, name from stops where name = ? or name = ?", new String[] {originStop, destStop});
		if (cursor.moveToFirst()) {
			String name = cursor.getString(1);
			if (name.equals(originStop)) {
				this.originId = cursor.getString(0);
			} else {
				this.destId = cursor.getString(0);
			}
		} while (cursor.moveToNext());
	}

	//
	// assumes one of the 3 setters has been correctly called - given a mode of
	// transport, return the timetable for that transport type
	// returned timetable is not ordered by departure time.
	//
	public ArrayList<Path> getTimetable(String transport) {
		ArrayList<Path> timetable = new ArrayList<Path>();
		Cursor cursor = null;
		SQLiteDatabase curr = this.getCurrentDb();
		cursor = curr.rawQuery("Select n.lineName, n.mon, n.tue, n.wed, n.thu, n.fri, n.sat, n.sun, "
									 + "o.privateCode, o.arrivalTime, d.arrivalTime"
									 + "from timetable as o "
									 + "join timetable as d on o.privateCode = d.privateCode "
									 + "join days as n on d.privateCode = n.privateCode "
									 + "where o.stopId = ? and d.stopId = ? and o.seq < d.seq "
									 + "order by d.arrivalTime", new String[] {this.originId, this.destId});
		if (cursor.moveToFirst()) {
			Path item = new Path();
			item.setDescription(cursor.getString(0));
			item.setMon(cursor.getInt(1));
			item.setTue(cursor.getInt(2));
			item.setWed(cursor.getInt(3));
			item.setThu(cursor.getInt(4));
			item.setFri(cursor.getInt(5));
			item.setSat(cursor.getInt(6));
			item.setSun(cursor.getInt(7));
			item.setPrivateCode(cursor.getInt(8));
			item.setArrivalTime(cursor.getInt(9));
			item.setDepartureTime(cursor.getInt(10));
			timetable.add(item);
		} while (cursor.moveToNext());
		cursor.close();
		return timetable;
	}

	// returns the information for the trip that the user selected from the
	// timetable
	// TODO:  The first param transport doesn't need to be returned.
	public TripInfo getTrip(String transport, int privateCode) {
		if (privateCode < 0) {
			System.err.println("Invalid privateCode");
		}
		SQLiteDatabase curr = this.getCurrentDb();
		TripSchedule trip = new TripSchedule();
		trip.setOrigin(this.originStop);
		trip.setDestination(this.destStop);
		String seqRange = "Select seq from TIMETABLE where privateCode = ? and (stopId = ? or stopId = ?)";
		Cursor cursor = curr.rawQuery(seqRange, new String[] {String.valueOf(privateCode), this.originId, this.destId});
		int originSeq = -1;
		int destSeq = -1;
		int temp = -1;
		if (cursor.moveToFirst()) {
			if (originSeq < 0) {
				originSeq = cursor.getInt(0);
			} else {
				temp = cursor.getInt(0);
				if (originSeq > temp) {
					destSeq = originSeq;
					originSeq = temp;
				} else {
					destSeq = temp; 
				}
			}
		} while (cursor.moveToNext());
		cursor.close();
		String query1 = "Select stopId, arrivalTime from TIMETABLE where privateCode = ? seq <= ? and seq >= order by seq";
		String query2 = "Select name, longitude, latitude, number from stops where atcoCode = ?";
		cursor = curr.rawQuery(query1, new String[] {String.valueOf(privateCode)});
		if (cursor.moveToFirst()) {
			Cursor c = curr.rawQuery(query2, new String[] {String.valueOf(cursor.getString(0))});
			if (c.moveToFirst()) {
				// save lat,long vlaues
			}
		} while (cursor.moveToNext());
		return null;
	};
	
	
	// GPS functions
	
	// if within 50 meters of the origin
	public boolean isValidTrip(Location currLocation) {
		float[] results = new float[1];
		Location.distanceBetween(currLocation.getLatitude(), currLocation.getLongitude(), originLatitude, originLongitude, results);
		float distanceInMeters = results[0];
		return distanceInMeters < 50;
	};
	
	// if within 50 meters of the stop provided
	public boolean isAtNextStop(Location location, String nextStop) {
		float[] results = new float[1];
		Location.distanceBetween(location.getLatitude(), location.getLongitude(), nextLatitude, nextLongitude, results);
		float distanceInMeters = results[0];
		return distanceInMeters < 50;
	};
	 */
}
