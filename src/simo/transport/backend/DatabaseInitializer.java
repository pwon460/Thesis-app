package simo.transport.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DatabaseInitializer {

	public DatabaseInitializer() {
		// TODO Auto-generated constructor stub
	}

	private void insertStops (SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			while ((content = reader.readLine()) != null) {
				try {
					String query = "INSERT INTO STOPS VALUES (?, ?, ?, ?, ?, ?)";
					SQLiteStatement stmt = db.compileStatement(query);
					String[] columns = content.split(",");
					if (columns.length == 6) {
						stmt.bindAllArgsAsStrings(columns);
						stmt.executeInsert();
						stmt.clearBindings();
					}
				} catch (Exception e) {
					System.err.println("Inserting initial entry into STOP fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteStops (SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			while ((content = reader.readLine()) != null) {
				try {
					String query = "DELETE FROM STOPS WHERE atcoCode = ?";
					SQLiteStatement stmt = db.compileStatement(query);
					String[] columns = content.split(",");
					if (columns.length == 6) {
						stmt.bindAllArgsAsStrings(new String[] {columns[0]});
						stmt.executeUpdateDelete();
						stmt.clearBindings();
					}
				} catch (Exception e) {
					System.err.println("Deleting an entry from STOP fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insertRoutesOrders (SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			while ((content = reader.readLine()) != null) {
				try {
					String query = "INSERT INTO ROUTES_ORDERS VALUES (?, ?, ?)";
					SQLiteStatement stmt = db.compileStatement(query);
						String[] columns = content.split(",");
					if (columns.length == 3) {
						stmt.bindAllArgsAsStrings(columns);
						stmt.executeInsert();
						stmt.clearBindings();
					}
				} catch (Exception e) {
					System.err.println("Inserting initial entry into ROUTES_ORDERS fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	private void deleteRoutesOrders (SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			while ((content = reader.readLine()) != null) {
				try {
					String query = "DELETE FROM ROUTES_ORDERS WHERE routeId = ? and seq = ? and atcoCode = ?";
					SQLiteStatement stmt = db.compileStatement(query);
					String[] columns = content.split(",");
					if (columns.length == 3) {
						stmt.bindAllArgsAsStrings(columns);
						stmt.executeUpdateDelete();
						stmt.clearBindings();
					}
				} catch (Exception e) {
					System.err.println("Deleting an entry from ROUTES_ORDERS fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertRoutesNames (SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			while ((content = reader.readLine()) != null) {
				try {
					String query = "INSERT INTO ROUTES_NAMES VALUES (?, ?, ?)";
					SQLiteStatement stmt = db.compileStatement(query);
						String[] columns = content.split(",");
					if (columns.length == 3) {
						stmt.bindAllArgsAsStrings(columns);
						stmt.executeInsert();
						stmt.clearBindings();
					}
				} catch (Exception e) {
					System.err.println("Inserting initial entry into ROUTES_NAMES fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void deleteRoutesNames (SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			while ((content = reader.readLine()) != null) {
				try {
					String query = "DELETE FROM ROUTES_NAMES WHERE routeId = ? and lineName = ? and description = ?";
					SQLiteStatement stmt = db.compileStatement(query);
					String[] columns = content.split(",");
					if (columns.length == 3) {
						stmt.bindAllArgsAsStrings(columns);
						stmt.executeUpdateDelete();
						stmt.clearBindings();
					}
				} catch (Exception e) {
					System.err.println("Deleting an entry from ROUTES_NAMES fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertCalendar (SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			while ((content = reader.readLine()) != null) {
				try {
					String query = "INSERT INTO CALENDAR VALUES (?, ?, ?, ?, ?)";
					SQLiteStatement stmt = db.compileStatement(query);
					String[] columns = content.split(",");
					if (columns.length == 5) {
						stmt.bindAllArgsAsStrings(columns);
						stmt.executeInsert();
						stmt.clearBindings();
						stmt.executeInsert();
					}
				} catch (Exception e) {
					System.err.println("Inserting initial entry into CALENDAR fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void deleteCalendar (SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			while ((content = reader.readLine()) != null) {
				try {
					String query = "DELETE FROM CALENDAR WHERE privateCode = ? and dayId = ? " +
							"and routeId = ? and journeyPatternSectionId = ? and departureTime = ?";
					SQLiteStatement stmt = db.compileStatement(query);
					String[] columns = content.split(",");
					if (columns.length == 5) {
						stmt.bindAllArgsAsStrings(columns);
						stmt.executeUpdateDelete();
						stmt.clearBindings();
					}
				} catch (Exception e) {
					System.err.println("Deleting an entry from CALENDAR fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insertTrips(SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			while ((content = reader.readLine()) != null) {
				try {
					String query = "INSERT INTO TRIPS VALUES (?, ?, ?)";
					SQLiteStatement stmt = db.compileStatement(query);
					String[] columns = content.split(",");
					if (columns.length == 3) {
						stmt.bindAllArgsAsStrings(columns);
						stmt.executeInsert();
						stmt.clearBindings();
						stmt.executeInsert();
					}
				} catch (Exception e) {
					System.err.println("Inserting initial entry into TRIPS fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void deleteTrips(SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			while ((content = reader.readLine()) != null) {
				try {
					String query = "DELETE FROM CALENDAR WHERE journeyPatternSectionId = ? and seq = ? and arrival = ?";
					SQLiteStatement stmt = db.compileStatement(query);
					String[] columns = content.split(",");
					if (columns.length == 3) {
						stmt.bindAllArgsAsStrings(columns);
						stmt.executeUpdateDelete();
						stmt.clearBindings();
					}
				} catch (Exception e) {
					System.err.println("Deleting an entry from TRIPS fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insertDaysVariation (SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			while ((content = reader.readLine()) != null) {
				try {
					String query = "INSERT INTO DAYS_VARIATION VALUES (?, ?, ?, ?, ?, ?, ?)";
					SQLiteStatement stmt = db.compileStatement(query);
					String[] columns = content.split(",");
					if (columns.length == 8) {
						stmt.bindAllArgsAsStrings(columns);
						stmt.executeInsert();
						stmt.clearBindings();
						stmt.executeInsert();
					}
				} catch (Exception e) {
					System.err.println("Inserting initial entry into DAYS_VARIATION fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void updateException (SQLiteDatabase db, InputStream stream) {
		BufferedReader reader = null;
		String content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			db.beginTransaction();
			db.execSQL("DROP TABLE IF EXISTS EXCEPTION");
			db.execSQL("CREATE TABLE IF NOT EXISTS EXCEPTION (privateCode INT, dayId INT)");
			while ((content = reader.readLine()) != null) {
				try {
					String query = "INSERT INTO EXCEPTION VALUES (?, ?)";
					SQLiteStatement stmt = db.compileStatement(query);
					String[] columns = content.split(",");
					if (columns.length == 2) {
						stmt.bindAllArgsAsStrings(columns);
						stmt.executeInsert();
						stmt.clearBindings();
					}
				} catch (Exception e) {
					System.err.println("Inserting initial entry into STOP fails.");
					e.printStackTrace();
				}
			}
			db.endTransaction();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initializeDatabase(Context context, String file) {

		SQLiteDatabase bus_db = new MySQLiteHelper(context, MySQLiteHelper.BUS_DATABASE).getWritableDatabase();
		SQLiteDatabase rail_db = new MySQLiteHelper(context, MySQLiteHelper.RAIL_DATABASE).getWritableDatabase();
		SQLiteDatabase ferry_db = new MySQLiteHelper(context, MySQLiteHelper.FERRY_DATABASE).getWritableDatabase();
		SQLiteDatabase tram_db = new MySQLiteHelper(context, MySQLiteHelper.TRAM_DATABASE).getWritableDatabase();

		// Insert stop information to the STOPS
		try {
			// String file = "/Users/cse/Thesis/data/initial/init.zip";
		    ZipFile zipFile = new ZipFile(file);
		    Enumeration<? extends ZipEntry> entries = zipFile.entries();
		    while(entries.hasMoreElements()){
		        ZipEntry entry = entries.nextElement();
		        InputStream stream = zipFile.getInputStream(entry);
		        if (entry.getName().equals("bus_stops.txt")) {
					insertStops(bus_db, stream);
		        } else if (entry.getName().equals("bus_routes_orders.txt")) {
		        	insertRoutesOrders(bus_db, stream);
		        } else if (entry.getName().equals("bus_routes_names.txt")) {
		        	insertRoutesNames(bus_db, stream);
		        } else if (entry.getName().equals("bus_calendar.txt")) {
		        	insertCalendar(bus_db, stream);
		        } else if (entry.getName().equals("bus_trips.txt")) {
		        	insertTrips(bus_db, stream);
		        } else if (entry.getName().equals("rail_stations.txt")) {
		        	insertStops(rail_db, stream);
		        } else if (entry.getName().equals("rail_routes_orders.txt")) {
					insertRoutesOrders(rail_db, stream);
		        } else if (entry.getName().equals("rail_routes_names.txt")) {
		        	insertRoutesNames(rail_db, stream);
		        } else if (entry.getName().equals("rail_calendar.txt")) {
		        	insertCalendar(rail_db, stream);
		        } else if (entry.getName().equals("rail_trips.txt")) {
		        	insertTrips(rail_db, stream);
		        } else if (entry.getName().equals("ferry_wharfs.txt")) {
					insertStops(ferry_db, stream);
		        } else if (entry.getName().equals("ferry_routes_orders.txt")) {
					insertRoutesOrders(ferry_db, stream);
		        } else if (entry.getName().equals("ferry_routes_names.txt")) {
		        	insertRoutesNames(ferry_db, stream);
		        } else if (entry.getName().equals("ferry_calendar.txt")) {
		        	insertCalendar(ferry_db, stream);
		        } else if (entry.getName().equals("ferry_trips.txt")) {
		        	insertTrips(ferry_db, stream);
		        } else if (entry.getName().equals("light_rail_stations.txt")) {
					insertStops(tram_db, stream);
		        } else if (entry.getName().equals("light_rail_routes_orders.txt")) {
					insertRoutesOrders(tram_db, stream);
		        } else if (entry.getName().equals("light_rail_routes_names.txt")) {
		        	insertRoutesNames(tram_db, stream);
		        } else if (entry.getName().equals("light_rail_calendar.txt")) {
		        	insertCalendar(tram_db, stream);
		        } else if (entry.getName().equals("light_rail_trips.txt")) {
		        	insertTrips(tram_db, stream);
		        } else if (entry.getName().equals("days_variation.txt")) {
		        	insertDaysVariation(bus_db, stream);
		        	insertDaysVariation(rail_db, stream);
		        	insertDaysVariation(ferry_db, stream);
		        	insertDaysVariation(tram_db, stream);
		        }
		        stream.close();
		    }
		    zipFile.close();
		    bus_db.close();
		    rail_db.close();
		    ferry_db.close();
		    tram_db.close();
		    // delete zip file after update to save memory
	    	try{
	    		File f = new File(file);
	    		// initial database is always latest version so patch file is not needed.
	    		// However, still need to update a flag file for patch file.
	    		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	    		String timestamp = formatter.format(new Date(f.lastModified()));
		    	FileOutputStream fos = context.openFileOutput("simo.patch." + timestamp, Context.MODE_PRIVATE);
		    	fos.close();
	    		f.delete();
			}catch(Exception e){
	    		e.printStackTrace(); 
	    	}
	    	// create flag file and name it with simo.initialized
	    	FileOutputStream fos = context.openFileOutput("simo.initialized", Context.MODE_PRIVATE);
	    	fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateException(Context context, String file) {
		SQLiteDatabase bus_db = new MySQLiteHelper(context, MySQLiteHelper.BUS_DATABASE).getWritableDatabase();
		SQLiteDatabase rail_db = new MySQLiteHelper(context, MySQLiteHelper.RAIL_DATABASE).getWritableDatabase();
		SQLiteDatabase ferry_db = new MySQLiteHelper(context, MySQLiteHelper.FERRY_DATABASE).getWritableDatabase();
		SQLiteDatabase tram_db = new MySQLiteHelper(context, MySQLiteHelper.TRAM_DATABASE).getWritableDatabase();
		try {
			// String file = "/Users/cse/Thesis/data/initial/init.zip";
		    ZipFile zipFile = new ZipFile(file);
		    Enumeration<? extends ZipEntry> entries = zipFile.entries();
		    while(entries.hasMoreElements()){
		        ZipEntry entry = entries.nextElement();
		        InputStream stream = zipFile.getInputStream(entry);
		        if (entry.getName().equals("bus_exception.txt")) {
					updateException(bus_db, stream);
		        } else if (entry.getName().equals("rail_exception.txt")) {
		        	updateException(rail_db, stream);
		        } else if (entry.getName().equals("ferry_exception.txt")) {
		        	updateException(ferry_db, stream);;
		        } else if (entry.getName().equals("light_rail_exception.txt")) {
		        	updateException(tram_db, stream);
		        }
		        stream.close();
		    }
		    zipFile.close();
		    bus_db.execSQL("VACUUM");
		    rail_db.execSQL("VACUUM");
		    ferry_db.execSQL("VACUUM");
		    tram_db.execSQL("VACUUM");
		    bus_db.close();
		    rail_db.close();
		    ferry_db.close();
		    tram_db.close();
		    // delete zip file after update to save memory
	    	try{
	    		File f = new File(file);
		    	// create flag file and name it with simo.yyyyMMdd
	    		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	    		String timestamp = formatter.format(new Date(f.lastModified()));
		    	FileOutputStream fos = context.openFileOutput("simo." + timestamp, Context.MODE_PRIVATE);
		    	fos.close();
	    		f.delete();
			}catch(Exception e){
	    		e.printStackTrace(); 
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// To decrease size of database, vacuum is required but this will be done in updatedException function.
	// Hence, this function should be called after updatedException function.
	public void updateInitialDatabase (Context context, String file) {

		SQLiteDatabase bus_db = new MySQLiteHelper(context, MySQLiteHelper.BUS_DATABASE).getWritableDatabase();
		SQLiteDatabase rail_db = new MySQLiteHelper(context, MySQLiteHelper.RAIL_DATABASE).getWritableDatabase();
		SQLiteDatabase ferry_db = new MySQLiteHelper(context, MySQLiteHelper.FERRY_DATABASE).getWritableDatabase();
		SQLiteDatabase tram_db = new MySQLiteHelper(context, MySQLiteHelper.TRAM_DATABASE).getWritableDatabase();

		// Insert stop information to the STOPS
		try {
			// String file = "/Users/cse/Thesis/data/initial/init.zip";
		    ZipFile zipFile = new ZipFile(file);
		    Enumeration<? extends ZipEntry> entries = zipFile.entries();
		    while(entries.hasMoreElements()){
		        ZipEntry entry = entries.nextElement();
		        InputStream stream = zipFile.getInputStream(entry);
		        if (entry.getName().equals("d_bus_stops.txt")) {
					deleteStops(bus_db, stream);
		        } else if (entry.getName().equals("d_bus_routes_orders.txt")) {
		        	deleteRoutesOrders(bus_db, stream);
		        } else if (entry.getName().equals("d_bus_routes_names.txt")) {
		        	deleteRoutesNames(bus_db, stream);
		        } else if (entry.getName().equals("d_bus_calendar.txt")) {
		        	deleteCalendar(bus_db, stream);
		        } else if (entry.getName().equals("d_bus_trips.txt")) {
		        	deleteTrips(bus_db, stream);
		        } else if (entry.getName().equals("d_rail_stations.txt")) {
		        	deleteStops(rail_db, stream);
		        } else if (entry.getName().equals("d_rail_routes_orders.txt")) {
					deleteRoutesOrders(rail_db, stream);
		        } else if (entry.getName().equals("d_rail_routes_names.txt")) {
		        	deleteRoutesNames(rail_db, stream);
		        } else if (entry.getName().equals("d_rail_calendar.txt")) {
		        	deleteCalendar(rail_db, stream);
		        } else if (entry.getName().equals("d_rail_trips.txt")) {
		        	deleteTrips(rail_db, stream);
		        } else if (entry.getName().equals("d_ferry_wharfs.txt")) {
					deleteStops(ferry_db, stream);
		        } else if (entry.getName().equals("d_ferry_routes_orders.txt")) {
					deleteRoutesOrders(ferry_db, stream);
		        } else if (entry.getName().equals("d_ferry_routes_names.txt")) {
		        	deleteRoutesNames(ferry_db, stream);
		        } else if (entry.getName().equals("d_ferry_calendar.txt")) {
		        	deleteCalendar(ferry_db, stream);
		        } else if (entry.getName().equals("d_ferry_trips.txt")) {
		        	deleteTrips(ferry_db, stream);
		        } else if (entry.getName().equals("d_light_rail_stations.txt")) {
					deleteStops(tram_db, stream);
		        } else if (entry.getName().equals("d_light_rail_routes_orders.txt")) {
					deleteRoutesOrders(tram_db, stream);
		        } else if (entry.getName().equals("d_light_rail_routes_names.txt")) {
		        	deleteRoutesNames(tram_db, stream);
		        } else if (entry.getName().equals("d_light_rail_calendar.txt")) {
		        	deleteCalendar(tram_db, stream);
		        } else if (entry.getName().equals("d_light_rail_trips.txt")) {
		        	deleteTrips(tram_db, stream);
		        } else if (entry.getName().equals("i_bus_stops.txt")) {
					insertStops(bus_db, stream);
		        } else if (entry.getName().equals("i_bus_routes_orders.txt")) {
		        	insertRoutesOrders(bus_db, stream);
		        } else if (entry.getName().equals("i_bus_routes_names.txt")) {
		        	insertRoutesNames(bus_db, stream);
		        } else if (entry.getName().equals("i_bus_calendar.txt")) {
		        	insertCalendar(bus_db, stream);
		        } else if (entry.getName().equals("i_bus_trips.txt")) {
		        	insertTrips(bus_db, stream);
		        } else if (entry.getName().equals("i_rail_stations.txt")) {
		        	insertStops(rail_db, stream);
		        } else if (entry.getName().equals("i_rail_routes_orders.txt")) {
					insertRoutesOrders(rail_db, stream);
		        } else if (entry.getName().equals("i_rail_routes_names.txt")) {
		        	insertRoutesNames(rail_db, stream);
		        } else if (entry.getName().equals("i_rail_calendar.txt")) {
		        	insertCalendar(rail_db, stream);
		        } else if (entry.getName().equals("i_rail_trips.txt")) {
		        	insertTrips(rail_db, stream);
		        } else if (entry.getName().equals("i_ferry_wharfs.txt")) {
					insertStops(ferry_db, stream);
		        } else if (entry.getName().equals("i_ferry_routes_orders.txt")) {
					insertRoutesOrders(ferry_db, stream);
		        } else if (entry.getName().equals("i_ferry_routes_names.txt")) {
		        	insertRoutesNames(ferry_db, stream);
		        } else if (entry.getName().equals("i_ferry_calendar.txt")) {
		        	insertCalendar(ferry_db, stream);
		        } else if (entry.getName().equals("i_ferry_trips.txt")) {
		        	insertTrips(ferry_db, stream);
		        } else if (entry.getName().equals("i_light_rail_stations.txt")) {
					insertStops(tram_db, stream);
		        } else if (entry.getName().equals("i_light_rail_routes_orders.txt")) {
					insertRoutesOrders(tram_db, stream);
		        } else if (entry.getName().equals("i_light_rail_routes_names.txt")) {
		        	insertRoutesNames(tram_db, stream);
		        } else if (entry.getName().equals("i_light_rail_calendar.txt")) {
		        	insertCalendar(tram_db, stream);
		        } else if (entry.getName().equals("i_light_rail_trips.txt")) {
		        	insertTrips(tram_db, stream);
		        }
		        stream.close();
		    }
		    zipFile.close();
		    bus_db.close();
		    rail_db.close();
		    ferry_db.close();
		    tram_db.close();
		    // delete zip file after update to save memory
	    	try{
	    		File f = new File(file);
		    	// create flag file and name it with simo.patch.yyyyMMdd
	    		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	    		String timestamp = formatter.format(new Date(f.lastModified()));
		    	FileOutputStream fos = context.openFileOutput("simo.patch." + timestamp, Context.MODE_PRIVATE);
		    	fos.close();
	    		f.delete();
			}catch(Exception e){
	    		e.printStackTrace(); 
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
