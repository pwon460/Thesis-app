package simo.transport.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	// Database version 
	// TODO: What happens if each db has a different version?
	public static final int BUS_DB_VERSION = 1;
	public static final int RAIL_DB_VERSION = 1;
	public static final int FERRY_DB_VERSION = 1;
	public static final int TRAM_DB_VERSION = 1;

	// Database name
	public static final String BUS_DATABASE = "b_db";
	public static final String RAIL_DATABASE = "r_db";
    public static final String FERRY_DATABASE = "f_db";
	public static final String TRAM_DATABASE = "l_db";
	private Context context;
	private String dbName;

	// Table names.
	final String STOPS = "STOPS";
	final String ROUTES_ORDERS = "ROUTES_ORDERS";
	final String ROUTES_NAMES = "ROUTES_NAMES";
	final String DAYS_VARIATION = "DAYS_VARIATION";
	final String CALENDAR = "CALENDAR";
	final String EXCEPTION = "EXCEPTION";
	final String TRIPS = "TRIPS";

	// private ArrayList<String> list = new ArrayList<String>();
	//private String originId;
	//private String destId;
	private static String originStop;
	private static String destStop;
	//private String mode;
	private static ArrayList<Integer> stopList;
	private static DetailedInfo detailedInfo;
	
	public MySQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public MySQLiteHelper(Context context, String name, CursorFactory factory,
			              int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	public MySQLiteHelper(Context context, String dbName, int dbVersion) {
		super(context, dbName, null, dbVersion);
		this.context = context;
		this.dbName = dbName;
	}

	public MySQLiteHelper(Context context, String dbName) {
		super(context, dbName, null, 1);
		this.context = context;
		this.dbName = dbName;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create tables
		String query = "CREATE TABLE IF NOT EXISTS ";
		// Create STOPS
		String attributes = " (atcoCode INTEGER PRIMARY KEY,"
							+ "suburb TEXT,"
							+ "longitude INTEGER,"
							+ "latitude INTEGER,"
							+ "name TEXT,"
							+ "number TEXT)"; // Stores stand No, platform No or wharf No.
		db.execSQL(query + this.STOPS + attributes);
		// Create ROUTES_ORDERS
		attributes = " (routeId INT, "
					 + "seq INT, "
				     + "atcoCode INT)";
		db.execSQL(query + this.ROUTES_ORDERS + attributes);
		// Create ROUTES_NAMES
		attributes = " (routeId INT, "
				     + "lineName TEXT, "
				     + "description TEXT)";
		db.execSQL(query + this.ROUTES_NAMES + attributes);
		// Create DAYS_VARIATION
		attributes = " (dayId INT, "
				     + "mon INT, "
				     + "tue INT, "
				     + "wed INT, "
				     + "thu INT, "
				     + "fri INT, "
				     + "sat INT, "
				     + "sun INT)";
		db.execSQL(query + this.DAYS_VARIATION + attributes);
		// Create CALENDAR
		attributes = " (privateCode INT, "
				     + "dayId INT, "
				     + "routeId INT, "
				     + "journeyPatternSectionId INT, "
				     + "departureTime INT)";
					 // + "startDate INT, "
		             // + "endDate INT)";
		db.execSQL(query + this.CALENDAR + attributes);
		// Create EXCEPTION
		attributes = " (privateCode INT, "
				     + "dayId INT)";
		db.execSQL(query + this.EXCEPTION + attributes);
		// Create TRIPS
		attributes = " (journeyPatternSectionId INT, "
				     + "seq INT, "
				     + "arrival INT)";
		db.execSQL(query + this.TRIPS + attributes);

		// query = "CREATE INDEX idx ON";
		// copyDatabase();
	}	
	
	private void copyDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Insert stop information to the STOPS
		String filePath = "";
		String fileName = null;
		if (dbName.equals("bus")) {
			fileName = "stops.txt";
		} else if (dbName.equals("rail")) {
			fileName = "stations.txt";
		} else if (dbName.equals("ferry")) {
			fileName = "wharfs.txt";
		} else if (dbName.equals("tram")) {
			fileName = "t_stations.txt";
		}
		AssetManager manager = this.context.getAssets();
		InputStream input = null;
		try {
			input = manager.open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
		try {
			String line = "";
			db.beginTransaction();
			String query = "INSERT INTO STOPS VALUES (?, ?, ?, ?, ?, ?)";
			SQLiteStatement stmt = db.compileStatement(query);
			while((line = buffer.readLine()) != null) {
				String[] columns = line.split(",");
				if (columns.length == 6) {
					stmt.bindAllArgsAsStrings(columns);
					stmt.executeInsert();
					stmt.clearBindings();
				}
			}
			db.endTransaction();
			input.close();
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Insert route order information to the ROUTES_ORDERS
		if (dbName.equals("bus")) {
			fileName = "bus_routes_orders.txt";
		} else if (dbName.equals("rail")) {
			fileName = "rail_routes_orders.txt";
		} else if (dbName.equals("ferry")) {
			fileName = "ferry_routes_orders.txt";
		} else if (dbName.equals("tram")) {
			fileName = "light_rails_routes_orders.txt";
		}
		try {
			input = manager.open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer = new BufferedReader(new InputStreamReader(input));
		try {
			String line = "";
			db.beginTransaction();
			String query = "INSERT INTO ROUTES_ORDERS VALUES (?, ?, ?)";
			SQLiteStatement stmt = db.compileStatement(query);
			while((line = buffer.readLine()) != null) {
				String[] columns = line.split(",");
				if (columns.length == 3) {
					stmt.bindAllArgsAsStrings(columns);
					stmt.executeInsert();
					stmt.clearBindings();
				}
			}
			db.endTransaction();
			input.close();
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Insert route description to the ROUTES_NAMES
		if (dbName.equals("bus")) {
			fileName = "bus_routes_names.txt";
		} else if (dbName.equals("rail")) {
			fileName = "rail_routes_names.db";
		} else if (dbName.equals("ferry")) {
			fileName = "ferry_routes_names.txt";
		} else if (dbName.equals("tram")) {
			fileName = "light_rail_routes_names.txt";
		}
		try {
			input = manager.open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer = new BufferedReader(new InputStreamReader(input));
		try {
			String line = "";
			db.beginTransaction();
			String query = "INSERT INTO ROUTES_NAMES VALUES (?, ?, ?)";
			SQLiteStatement stmt = db.compileStatement(query);
			while((line = buffer.readLine()) != null) {
				String[] columns = line.split(",");
				if (columns.length == 3) {
					stmt.bindAllArgsAsStrings(columns);
					stmt.executeInsert();
					stmt.clearBindings();
				}
			}
			db.endTransaction();
			input.close();
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Insert days variation to the DAYS_VARIATION
		fileName = "days_variation.txt";
		try {
			input = manager.open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer = new BufferedReader(new InputStreamReader(input));
		try {
			String line = "";
			db.beginTransaction();
			String query = "INSERT INTO DAYS_VARIATION VALUES (?, ?, ?, ?, ?, ?, ?)";
			SQLiteStatement stmt = db.compileStatement(query);
			while((line = buffer.readLine()) != null) {
				String[] columns = line.split(",");
				if (columns.length == 8) {
					stmt.bindAllArgsAsStrings(columns);
					stmt.executeInsert();
					stmt.clearBindings();
				}
			}
			db.endTransaction();
			input.close();
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Insert operating information like to the CALENDER
		if (dbName.equals("bus")) {
			fileName = "bus_calendar.txt";
		} else if (dbName.equals("rail")) {
			fileName = "rail_calendar.txt";
		} else if (dbName.equals("ferry")) {
			fileName = "ferry_calendar.txt";
		} else if (dbName.equals("tram")) {
			fileName = "light_rails_calendar.txt";
		}
		try {
			input = manager.open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer = new BufferedReader(new InputStreamReader(input));
		try {
			String line = "";
			db.beginTransaction();
			String query = "INSERT INTO CALENDAR VALUES (?, ?, ?, ?, ?)";
			SQLiteStatement stmt = db.compileStatement(query);
			while((line = buffer.readLine()) != null) {
				String[] columns = line.split(",");
				if (columns.length == 5) {
					stmt.bindAllArgsAsStrings(columns);
					stmt.executeInsert();
					stmt.clearBindings();
				}
			}
			db.endTransaction();
			input.close();
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Insert exception days to the EXCEPTION
		if (dbName.equals("bus")) {
			fileName = "bus_exception.txt";
		} else if (dbName.equals("rail")) {
			fileName = "rail_exception.txt";
		} else if (dbName.equals("ferry")) {
			fileName = "ferry_exception.txt";
		} else if (dbName.equals("tram")) {
			fileName = "light_rail_exception.txt";
		}
		try {
			input = manager.open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer = new BufferedReader(new InputStreamReader(input));
		try {
			String line = "";
			db.beginTransaction();
			String query = "INSERT INTO EXCEPTION VALUES (?, ?)";
			SQLiteStatement stmt = db.compileStatement(query);
			while((line = buffer.readLine()) != null) {
				String[] columns = line.split(",");
				if (columns.length == 2) {
					stmt.bindAllArgsAsStrings(columns);
					stmt.executeInsert();
					stmt.clearBindings();
				}
			}
			db.endTransaction();
			input.close();
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Insert arrival time of routes to the TRIPS
		if (dbName.equals("bus")) {
			fileName = "bus_trips.txt";
		} else if (dbName.equals("rail")) {
			fileName = "rail_trips.txt";
		} else if (dbName.equals("ferry")) {
			fileName = "ferry_trips.txt";
		} else if (dbName.equals("tram")) {
			fileName = "light_rail_trips.txt";
		}
		try {
			input = manager.open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer = new BufferedReader(new InputStreamReader(input));
		try {
			String line = "";
			db.beginTransaction();
			String query = "INSERT INTO TRIPS VALUES (?, ?, ?)";
			SQLiteStatement stmt = db.compileStatement(query);
			while((line = buffer.readLine()) != null) {
				String[] columns = line.split(",");
				if (columns.length == 3) {
					stmt.bindAllArgsAsStrings(columns);
					stmt.executeInsert();
					stmt.clearBindings();
				}
			}
			db.endTransaction();
			input.close();
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	// Drop old talbes or create new tables.
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String query = "DROP TABLE IF EXISTS ";
		// In most time, stops doesn't need to be updated
		db.execSQL(query + this.STOPS);
		db.execSQL(query + this.ROUTES_ORDERS);
		db.execSQL(query + this.ROUTES_NAMES);
		db.execSQL(query + this.DAYS_VARIATION);
		db.execSQL(query + this.CALENDAR);
		db.execSQL(query + this.EXCEPTION);
		db.execSQL(query + this.TRIPS); 
		db.execSQL("vacuum"); // get rid of fragments
		this.onCreate(db);
	}

	
	/*
	 * CRUD operations (create "add", read "get", update, delete).
	 * However, add and delete operations will not occur in mobile side.
	 */
	
	// TODO: Three functions  below are same.
	//       Hence, get rid of redundancy later.
	// Returns list of all train stations
	public ArrayList<String> getStations() {
		ArrayList<String> stations = new ArrayList<String>();
		String query = "SELECT name from STOPS order by name";
		Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
		if (cursor.moveToFirst()) {
			stations.add(cursor.getString(0));
		} while (cursor.moveToNext());
		//this.mode = RAIL;
		cursor.close();
		return stations;
	}

	// Returns list of all ferry wharves
	public ArrayList<String> getWharfs() {
		ArrayList<String> wharfs = new ArrayList<String>();
		String query = "SELECT name from STOPS order by name";
		Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
		if (cursor.moveToFirst()) {
			wharfs.add(cursor.getString(0));
		} while (cursor.moveToNext());
		//this.mode = FERRY;
		cursor.close();
		return wharfs;
	}

	// Returns list of all light rail stops
	public ArrayList<String> getStops() {
		ArrayList<String> stations = new ArrayList<String>();
		String query = "SELECT name from STOPS order by name";
		Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
		if (cursor.moveToFirst()) {
			stations.add(cursor.getString(0));
		} while (cursor.moveToNext());
		//this.mode = TRAM;
		cursor.close();
		return stations;
	}
	
	// Returns list of all suburbs that buses visit
	public ArrayList<String> getSuburbs() {
		ArrayList<String> suburbs = new ArrayList<String>();
		String query = "SELECT distinct suburb from STOPS order by suburb";
		Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
		if (cursor.moveToFirst()) {
			suburbs.add(cursor.getString(0));
		} while (cursor.moveToNext());
		//this.mode = BUS;
		cursor.close();
		return suburbs;
	}

	// Given a suburb, returns a list of all the stops for that suburb
	// This function should be called after getSuburb.
	public ArrayList<String> getSuburbStops(String suburb) {
		ArrayList<String> stops = new ArrayList<String>();
		String query = "SELECT name from STOPS where suburb = ? order by name";
		Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
		if (cursor.moveToFirst()) {
			stops.add(cursor.getString(0));
		} while (cursor.moveToNext());
		cursor.close();
		return stops;
	}

	/*
	 * Returns list of all bus routes. isRightHand == true means the route
	 * number will be placed on the left of the route description if it is
	 * false, then the route number will be placed on the right of the route
	 * description
	 */
	public ArrayList<String> getRoutes(Boolean isRightHand) {
		ArrayList<String> digit = new ArrayList<String>();
		ArrayList<String> alphabet = new ArrayList<String>();
		String query = "SELECT distinct lineName, description from ROUTES_ORDERS order by cast (lineName as integer)";
		Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
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
		//this.mode = BUS;
		cursor.close();
		return digit;
	}
	
	// Given route, return the stops for that particular route.
	public ArrayList<String> getStopsOnRoute(String route) {
		int i = route.indexOf(" ");
		String lineName = route.substring(0, i);
		String description = route.substring(i+1);
		ArrayList<String> stops = new ArrayList<String>();
		// TODO: check which one has better performance
		//		 query including join might have poor performance in low cpu environment
		Cursor cursor = this.getReadableDatabase().rawQuery("Select distinct s.name from stop as s "
								    + "join routes_orders as o on s.atcoCode = o.atcoCode "
								    + "join routes_names as n on o.routeId = n.routeId "
								    + "where n.lineName = ? and n.description = ?"
								    + "order by s.name", new String[] {lineName, description});
		if (cursor.moveToFirst()) {
			stops.add(cursor.getString(0));
		} while (cursor.moveToNext());
		cursor.close();
		return stops;
	}


	// Helper method to use in statement
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

	public ArrayList<String> filteredSuburb (String origin) {
		ArrayList<String> suburbs = new ArrayList<String>();
		ArrayList<Integer> routeIds = new ArrayList<Integer>();
		ArrayList<Integer> orders = new ArrayList<Integer>();
		ArrayList<Integer> stops = new ArrayList<Integer>();
		ArrayList<String> originIds = new ArrayList<String>();
		Cursor cursor = this.getReadableDatabase().rawQuery("Select atcoCode from stops where name = ?", new String[] {origin});
		if (cursor.moveToFirst()) {
			originIds.add(cursor.getString(0));
		} while (cursor.moveToNext()); // atcoCode can be multiple because of stand, platform and wharf numbers.
		cursor = this.getReadableDatabase().rawQuery("Select o.routeId, o.seq from routes_orders as o "
				+ "join stops as s on o.atcoCode = s.atcoCode "
				+ "where s.atcoCode in (" + makePlaceHolders(originIds.size()) + ")", originIds.toArray(new String[originIds.size()]));
		if (cursor.moveToFirst()) {
			routeIds.add(cursor.getInt(0));
			orders.add(cursor.getInt(1));
		} while (cursor.moveToNext());
		cursor.close();
		for (int i = 0; i < routeIds.size(); i++) {
			cursor = this.getReadableDatabase().rawQuery("Select distinct atcoCode from routes_orders "
					+ "where routeId = ? and seq > ?", new String[] {String.valueOf(routeIds.get(i)), String.valueOf(orders.get(i))});
			if(cursor.moveToFirst()) {
				stops.add(cursor.getInt(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
		String[] args = new String[stops.size()];
		for (int i = 0; i < stops.size(); i++) {
			args[i] = String.valueOf(stops.get(i));
		}
		cursor = this.getReadableDatabase().rawQuery("Select distinct suburb from stops "
				+ "where atcoCode in (" + makePlaceHolders(stops.size()) + ") order by suburb", args);
		if (cursor.moveToFirst()) {
			suburbs.add(cursor.getString(0));
		}
		originStop = origin;
		stopList = stops;
		cursor.close();
		return suburbs;
	}		
	
	// This function should be called after the filteredSuburb.
	public ArrayList<String> filteredStops (String suburb) {
		ArrayList<String> stops = new ArrayList<String>();
		String[] args = new String[stops.size()];
		for (int i = 0; i < stopList.size(); i++) {
			args[i] = String.valueOf(stops.get(i));
		}
		Cursor cursor = this.getReadableDatabase().rawQuery("Select name from stops where suburb = " + suburb +
				        " and atcoCode in (" + makePlaceHolders(args.length) + ") order by name", args);
		if (cursor.moveToFirst()) {
			stops.add(cursor.getString(0));
		}
		cursor.close();
		return stops;
	}
	/*******************************/
	/*********** setters ***********/
	/*******************************/
	
	/******************************************************************************/
	/******************************************************************************/
	/*** these setters will be called before getTimetable or getTrip are called ***/
	/******************************************************************************/
	/******************************************************************************/

	// set names and ids of the origin and destination for train, ferry, light rail
	// TODO: take out searching id parts and make in one function
	//		 then remove duplicated code with above function
	public void setTrip(String origin, String destination) {
		originStop = origin;
		destStop = destination;
		// Problem to store originId and destinatinoId.
		// Ids can be multiple because of stand, platform and wharfs
		/*
		Cursor cursor = this.getReadableDatabase().rawQuery(
						"Select atcoCode, name from stops where name = ? or name = ?",
						new String[] {origin, destination});
		if (cursor.moveToFirst()) {
			String name = cursor.getString(1);
			if (name.equals(origin)) {
				this.originId = cursor.getString(0);
			} else {
				this.destId = cursor.getString(0);
			}
		} while (cursor.moveToNext());
		*/
	}

	// set the stops picked
	public void setByStopInfo(String route, String originName, String destName) {
		// TODO: route can be multiple, do we need to store this?
		// 		 take out searching id parts and make in one function
		//		 then remove duplicated code with above function
		// this.route = route;
		originStop = originName;
		destStop = destName;
		/*
		Cursor cursor = this.getReadableDatabase().rawQuery(
						"Select atcoCode, name from stops where name = ? or name = ?",
						new String[] {originStop, destStop});
		if (cursor.moveToFirst()) {
			String name = cursor.getString(1);
			if (name.equals(originStop)) {
				this.originId = cursor.getString(0);
			} else {
				this.destId = cursor.getString(0);
			}
		} while (cursor.moveToNext());
		*/
	}

	// set the info for the suburb trip
	public void setBySuburbInfo(String originSuburb, String destSuburb,
			String originName, String destName) {
		// TODO: do we need to store suburb information?
		// 		 take out searching id parts and make in one function
		//		 then remove duplicated code with above function		
		originStop = originName;
		destStop = destName;
		/*
		Cursor cursor = this.getReadableDatabase().rawQuery(
						"Select atcoCode, name from stops where name = ? or name = ?",
						new String[] {originStop, destStop});
		if (cursor.moveToFirst()) {
			String name = cursor.getString(1);
			if (name.equals(originStop)) {
				this.originId = cursor.getString(0);
			} else {
				this.destId = cursor.getString(0);
			}
		} while (cursor.moveToNext());
		*/
	}

	/*
	 * assumes one of the 3 setters has been correctly called - given a mode of
	 * transport, return the timetable for that transport type
	 * returned timetable is not ordered by departure time.
	 * TODO: Parameter transport is not required
	 */
	public ArrayList<TimetableItem> getTimetable(String transport) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<TimetableItem> items = new ArrayList<TimetableItem>();
		ArrayList<ReferenceDTO> temp = new ArrayList<ReferenceDTO>();
		ArrayList<Integer> originIds = new ArrayList<Integer>();
		ArrayList<String> originNo = new ArrayList<String>();
		ArrayList<Integer> destIds = new ArrayList<Integer>();
		ArrayList<String> destNo = new ArrayList<String>();
		ArrayList<Integer> routeIds = new ArrayList<Integer>();
		ArrayList<Integer> originSeq = new ArrayList<Integer>();
		// Get stopId/stationId/wharfId of origin
		Cursor cursor = db.rawQuery(
				        "Select atcoCode, number from stops where name = ?",
				        new String[] {originStop});
		if (cursor.moveToFirst()) {
			originIds.add(cursor.getInt(0));
			originNo.add(cursor.getString(1));
		} while (cursor.moveToNext());
		cursor.close();
		// Get stopId/stationId/wharfId of destination
		cursor = db.rawQuery(
		        "Select atcoCode, number from stops where name = ?",
		        new String[] {destStop});
		if (cursor.moveToFirst()) {
			destIds.add(cursor.getInt(0));
			destNo.add(cursor.getString(1));
		} while (cursor.moveToNext());
		cursor.close();
		for (int i = 0; i < originIds.size(); i++) { // origin list loop
			// Get routeId and sequence of the origin
			cursor = db.rawQuery("Select routeId, seq from ROUTES_ORDERS where atcoCode = ?"
			         , new String[] {String.valueOf(originIds.get(i))});
			if (cursor.moveToFirst()) {
				routeIds.add(cursor.getInt(0));
				originSeq.add(cursor.getInt(1));
			} while (cursor.moveToNext()); // this while loop will not occur	
			cursor.close();
			for (int j = 0; j < routeIds.size(); j++) { // route list of the origin loop
				for (int k = 0; k < destIds.size(); k++) { // destination list loop
					cursor = db.rawQuery("Select seq from ROUTES_ORDERS where routeId = ? and seq > ? and atcoCode = ?",
							 new String[] {String.valueOf(routeIds.get(j)), String.valueOf(originSeq.get(j)), String.valueOf(destIds.get(k))});
					if (cursor.moveToFirst()) {
						ReferenceDTO ref = new ReferenceDTO();
						ref.setRouteId(routeIds.get(j));
						ref.setOriginId(originIds.get(i));
						ref.setOrigin(originStop + originNo.get(i));
						ref.setOriginSeq(originSeq.get(j));
						ref.setDestinationId(destIds.get(k));
						ref.setDestination(destStop + destNo.get(k));
						ref.setDestinationSeq(cursor.getInt(0));
						temp.add(ref);
					} while (cursor.moveToNext()); // this while loop will not occur
					cursor.close();
				}
			}
			routeIds = new ArrayList<Integer>();
			originSeq = new ArrayList<Integer>();
			cursor.close();
		}
		ArrayList<CalendarDTO> calendars = new ArrayList<CalendarDTO>();
		for (int i = 0; i < temp.size(); i++) {
			ReferenceDTO curr = temp.get(i);
			int time = -1;
			cursor = db.rawQuery("Select c.privateCode,  c.dayId, c.departureTime, t.arrival "
					 + "from CALENDAR as c join TRIPS as t on c.journeyPatternSectionId = t.journeyPatternSectionId "
					 + "where c.routeId = ? and (t.seq = ? or t.seq = ?) order by t.arrival",
					 new String[] {String.valueOf(curr.getRouteId()),
							       String.valueOf(curr.getOriginSeq()),
							       String.valueOf(curr.getDestinationSeq())});
			if (cursor.moveToFirst()) {
				CalendarDTO c = new CalendarDTO();
				c.setPrivateCode(cursor.getInt(0));
				c.setRouteID(curr.getRouteId());
				c.setDayId(cursor.getInt(1));
				c.setDepartureTime(cursor.getInt(2));
				c.setOriginId(curr.getOriginId());
				c.setDestId(curr.getDestinationId());
				if (time < 0) {
					time = cursor.getInt(3);
				} else {
					if (time > cursor.getInt(3)) {
						c.setOriginArrival(cursor.getInt(3));
						c.setDestArrival(time);
					} else {
						c.setOriginArrival(time);
						c.setDestArrival(cursor.getInt(3));
					}
				}
				calendars.add(c);
			} while (cursor.moveToNext());
		}
		cursor.close();
		for (int i = 0; i < calendars.size(); i++) {
			CalendarDTO dto = calendars.get(i);
			cursor = db.rawQuery("Select dayId from EXCEPTION where privateCode = ?", new String[] {String.valueOf(dto.getPrivateCode())});
			if (cursor.moveToFirst()) {
				dto.setDayId(cursor.getInt(0));
			} while (cursor.moveToNext()); // this loop will not occur
			cursor.close();
			cursor = db.rawQuery("Select mon, tue, wed, thu, fri, sat, sun from DAYS_VARIATION "
					 + "where dayId = ?", new String[] {String.valueOf(dto.getDayId())});

			if (cursor.moveToFirst()) {
				ArrayList<Boolean> days = new ArrayList<Boolean>();
				for (int d = 0; d < 7; d++) {
					days.add(cursor.getInt(d) != 1);
				}
				dto.setDays(days);
				String departAt = String.valueOf(dto.getDepartureTime());
				while (departAt.length() < 6) {
					departAt = "0" + departAt;
				}
				String originT = String.valueOf(dto.getOriginArrival());
				while (originT.length() < 6) {
					originT = "0" + originT;
				}
				String destT = String.valueOf(dto.getDestArrival());
				while (destT.length() < 6) {
					destT = "0" + destT;
				}
				int hour = Integer.valueOf(departAt.substring(0, 2));
				int min = Integer.valueOf(departAt.substring(2, 4));
				int sec = Integer.valueOf(departAt.substring(4, 6));
				SimpleDateFormat input = new SimpleDateFormat("HHmmss");
				Calendar c = Calendar.getInstance();
				// calculate origin departure time
				Date from = null;
				try {
					from = input.parse(originT);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				c.setTime(from);
				c.add(Calendar.HOUR_OF_DAY, hour);
				c.add(Calendar.MINUTE, min);
				c.add(Calendar.SECOND, sec);
				dto.setOriginTime(c.getTime()); // origin arrivalTime
				// calculate destination arrival time
				try {
					from = input.parse(destT);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				c.setTime(from);
				c.add(Calendar.HOUR_OF_DAY, hour);
				c.add(Calendar.MINUTE, min);
				c.add(Calendar.SECOND, sec);
				dto.setDestTime(c.getTime()); // destination arrivalTime						
			} while (cursor.moveToNext()); // this loop will not occur
			cursor.close();
		}
		Collections.sort(calendars, new Comparator<CalendarDTO>() {
			public int compare(CalendarDTO c1, CalendarDTO c2) {
				if (c1.getOriginArrival() >= c2.getDestArrival()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		for (int i = 0; i < calendars.size(); i++) {
			CalendarDTO c = calendars.get(i);
			SimpleInfo item = new SimpleInfo();
			item.setArrivalTime(c.getOriginTime());
			item.setDepartureTime(c.getDestTime());
			item.setPrivateCode(c.getPrivateCode());
			item.setOriginId(c.getOriginId());
			item.setDestId(c.getDestId());
			item.setDays(c.getDays());
			cursor = db.rawQuery("Select lineName from ROUTES_NAMES where routeID = ?",
					 new String[] {String.valueOf(c.getRouteID())});
			if (cursor.moveToFirst()) {
				item.setDescription(cursor.getString(0));
			} while (cursor.moveToNext()); // this loop will not occur
			items.add(item);
		}
		return items;
	}

	// Returns the information for the trip that the user selected from the timetable.
	// TODO:  The first param transport doesn't need to be returned.
	public TripInfo getTrip(String transport, int privateCode, int origin, int dest) {
		if (privateCode < 0) {
			System.err.println("Invalid privateCode");
		}
		SQLiteDatabase db = this.getReadableDatabase();
		String routeId = null;
		String journeyPatternSectionId = null;
		String departureTime = null;
		String originSeq = null;
		String destSeq = null;
		//String temp = null;
		DetailedInfo trip = new DetailedInfo();

		Cursor cursor = db.rawQuery("Select routeId, journeyPatternSectionId, departureTime " +
				                    "from CALENDAR where privateCode = ?", new String[] {String.valueOf(privateCode)});
		if (cursor.moveToFirst()) {
			routeId = cursor.getString(0);
			journeyPatternSectionId = cursor.getString(1);
			departureTime = cursor.getString(2);
			while (departureTime.length() < 6) {
				departureTime = "0" + departureTime;
			}
		} while (cursor.moveToNext()); // this loop will not occur
		cursor.close();
		cursor = db.rawQuery("Select seq from ROUTES_ORDERS where routeId = ? and atoCode in (" +
		         makePlaceHolders(2) + ") order by seq", new String[] {String.valueOf(origin), String.valueOf(dest)});
		cursor.moveToFirst();
		originSeq = cursor.getString(0);
		cursor.moveToNext();
		destSeq = cursor.getString(0);
		cursor.close();
		cursor = db.rawQuery("Select s.longitude, s.latitude, s.name, s.number " +
		         "from stops as s join routes_orders as o on s.atcoCode = o.atcoCode " +
				 "where o.routeId = ? and o.seq >= ? and o.seq <= ? order by seq",
				 new String[] {routeId, originSeq, destSeq});
		ArrayList<String> orderedStops = new ArrayList<String>();
		ArrayList<GPSValues> gpsMap = new ArrayList<GPSValues>();
		//HashMap<String, GPSValues> gpsMap = new HashMap<String, GPSValues>();
		if (cursor.moveToFirst()) {
			GPSValues gps = new GPSValues(cursor.getDouble(1), cursor.getDouble(0));
			String stopName = cursor.getString(2) + cursor.getString(3);
			//gpsMap.put(stopName, gps);
			gpsMap.add(gps);
			orderedStops.add(stopName);
		} while (cursor.moveToNext());
		trip.setPaths(orderedStops);
		trip.setStopInfo(gpsMap);
		cursor.close();
		ArrayList<Date> orderedTimes = new ArrayList<Date>();
		cursor = db.rawQuery("Select arrival from TRIPS where journeyPatternSectionId = ? "
		         + "and seq >= ? and seq <= ? order by seq", new String[] {journeyPatternSectionId, originSeq, destSeq});
		int hour = Integer.valueOf(departureTime.substring(0, 2));
		int min = Integer.valueOf(departureTime.substring(2, 4));
		int sec = Integer.valueOf(departureTime.substring(4, 6));
		SimpleDateFormat input = new SimpleDateFormat("HHmmss");
		if (cursor.moveToFirst()) {
			String arrivedAt = cursor.getString(0);
			while (arrivedAt.length() < 6) {
				arrivedAt = "0" + arrivedAt;
			}	
			Calendar c = Calendar.getInstance();
			// calculate origin departure time
			Date from = null;
			try {
				from = input.parse(arrivedAt);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			c.setTime(from);
			c.add(Calendar.HOUR_OF_DAY, hour);
			c.add(Calendar.MINUTE, min);
			c.add(Calendar.SECOND, sec);
			orderedTimes.add(c.getTime());					
		} while (cursor.moveToNext()); // this loop will not occur
		trip.setOrderedTimes(orderedTimes);
		cursor = db.rawQuery("Select lineName from ROUTES_NAMES where routeId = ?", new String[] {routeId});
		if (cursor.moveToFirst()) {
			trip.setTripDescription("Route " + cursor.getString(0) + ", From " + originStop + " to " + destStop);
		}
		detailedInfo = trip;
		return trip;
	}

	// GPS functions
	
	// if within 50 meters of the origin
	public boolean isValidTrip(Location currLocation) {
		float[] results = new float[1];
		GPSValues gps = detailedInfo.getStopInfo().get(0);
		Location.distanceBetween(currLocation.getLatitude(), currLocation.getLongitude(), gps.getLatitude(), gps.getLongitude(), results);
		float distanceInMeters = results[0];
		return distanceInMeters < 50;
	};
	
	// if within 50 meters of the stop provided
	public boolean isAtNextStop(Location location, String nextStop) {
		float[] results = new float[1];
		ArrayList<String> paths = detailedInfo.getOrderedStops();
		int i = 0;
		for (; i < paths.size(); i++) {
			if (paths.get(i).equalsIgnoreCase(nextStop)) {
				break;
			}
		}
		GPSValues gps = detailedInfo.getStopInfo().get(i);
		Location.distanceBetween(location.getLatitude(), location.getLongitude(), gps.getLatitude(), gps.getLongitude(), results);
		float distanceInMeters = results[0];
		return distanceInMeters < 50;
	};
}
