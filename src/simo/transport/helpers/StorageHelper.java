package simo.transport.helpers;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class StorageHelper {

	private static final String TDX_FILE_FORMAT = "simo\\.\\d{8}.zip";
	private Context ctx;

	public StorageHelper(Context ctx) {
		this.ctx = ctx;
	}

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

//		Log.d("debug", "directory name = " + directory.getName());
		File[] filesInDir = directory.listFiles();
		for (File f : filesInDir) {
			Log.d("debug", "file looked at = " + f.getName());
			if (f.getName().matches(TDX_FILE_FORMAT)) {
				data = f;
			}
		}

		return data;
	}

//	public File getSIMOFolder() {
//		File folder;
//
//		if (Environment.getExternalStorageState() != null
//				&& isExternalStorageUsable()) { // has sd card and is usable
//			folder = new File(Environment.getExternalStorageDirectory()
//					+ "/SIMO");
//			if (!folder.exists()) {
//				folder.mkdir();
//			}
//		} else { // doesn't have sd card or unable to use external storage
//			folder = new File(Environment.getDataDirectory() + "/SIMO");
//			if (!folder.exists()) {
//				folder.mkdir();
//			}
//		}
//		return folder;
//	}

	public void removeOldFiles(File downloadDirectory, File newFile) {

		File[] files = downloadDirectory.listFiles();
		for (File f : files) {
			if (f.getName().matches(TDX_FILE_FORMAT)
					&& !f.getName().equals(newFile.getName())) {
				f.delete();
			}
		}
	}
	
	public boolean fileExists(String fname){
	    File file = ctx.getFileStreamPath(fname);
	    return file.exists();
	}
	
	public File patchFileExists() {
		File f = this.ctx.getFilesDir();
		if (f.exists() && f.isDirectory()){
		    final Pattern p = Pattern.compile("simo\\.patch\\.\\d{8}\\.zip");
		    File[] flists = f.listFiles(new FileFilter() {
		        @Override
		        public boolean accept(File file) {
		            return p.matcher(file.getName()).matches();

		        }
		    });
		    if (flists.length == 1) {
		    	return flists[0];
		    } else {
		    	System.err.println("multiple patch file records exists");
		    }
		}
		return null;
	}

}
