package simo.transport.helpers;

import java.io.File;

import android.os.Environment;

public class StorageHelper {

	public File getDownloadDirectory() {
		File directory;

		if (Environment.getExternalStorageState() != null
				&& isExternalStorageUsable()) { // has sd card and is usable
			directory = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			if (!directory.exists()) {
				directory.mkdir();
			}
		} else { // doesn't have sd card or unable to use external storage
			directory = new File(Environment.getDataDirectory() + "/Download/");
			if (!directory.exists()) {
				directory.mkdir();
			}
		}

		return directory;
	}

	/*
	 * 'usable' means can both read and write
	 */
	private boolean isExternalStorageUsable() {
		boolean isUsable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			isUsable = true;
		}

		return isUsable;
	}
	
	public File getTDXData(File directory) {
		File data = null;
		
		File[] filesInDir = directory.listFiles();
		for (File f: filesInDir) {
			if (f.getName().matches("\\d{4}-\\d{2}-\\d{2}")) {
				data = f;
			}
		}
		
		return data;
	}
	
}
