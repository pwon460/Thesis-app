package simo.transport.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.util.Log;

public class IndexButtonHandler {

	private static final int NUM_ARROW_BTNS = 2;
	private int numBtns = 8;
	private ArrayList<String> allIndexBtns;
	private String filter = "";
	private int startIndex;
	private int endIndex;

	public void setListToIndex(ArrayList<String> toIndex) {
		Log.d("debug", "list to turn into indices: " + toIndex.toString());
		allIndexBtns = new ArrayList<String>();
		if (filter.length() < 2) {
			startIndex = 0;
			endIndex = startIndex + numBtns - NUM_ARROW_BTNS - 1;
			for (int i = 0; i < toIndex.size(); i++) {
				int index = filter.length();
				// Log.d("debug", "index looked at = " + index);
				if (index >= toIndex.get(i).length()) {
					continue;
				}
				// String temp = toIndex.get(i).charAt(index) + "";
				String temp = toIndex.get(i).substring(0, index + 1);
//				Log.d("debug", "temp = " + temp);
				if (!allIndexBtns.contains(temp)) {
					allIndexBtns.add(temp);
				}
			}
			Collections.sort(allIndexBtns);
		}
	}

	// used for grabbing a 12 item 'chunk' of the list to show to the user
	// 14 buttons on the side, 2 are taken by up and down so can only show
	// a maximum of 12 at a time
	public ArrayList<String> getIndexBtnSubset() {
		ArrayList<String> subList = new ArrayList<String>();
		Log.d("debug", "creating sublist");
		Log.d("debug", "startingIndex = " + startIndex);
		Log.d("debug", "endingIndex = " + endIndex);
		Log.d("debug", "list size = " + allIndexBtns.size());
		if (endIndex >= allIndexBtns.size()) {
			Log.d("debug", "smaller sublist");
			for (int i = startIndex; i < allIndexBtns.size(); i++) {
				// Log.d("debug", "adding to sublist: " + allIndexBtns.get(i));
				subList.add(allIndexBtns.get(i));
			}
		} else {
			Log.d("debug", "full sized sublist");
			for (int i = startIndex; i <= endIndex; i++) {
//				Log.d("debug", "adding to sublist: " + allIndexBtns.get(i));
				subList.add(allIndexBtns.get(i));
			}
		}

		return subList;
	}

	public void handleUpClick() {
		if (startIndex >= numBtns - NUM_ARROW_BTNS) {
			endIndex = startIndex - 1;
			startIndex -= numBtns - NUM_ARROW_BTNS;
		}

	}

	public void handleDownClick() {
		if (endIndex + 1 < allIndexBtns.size()) {
			startIndex = endIndex + 1;
			endIndex += numBtns - NUM_ARROW_BTNS;
		}
	}

	public void handleIndexBtnClicked(String s) {
		Log.d("debug", "index button clicked");
		Log.d("debug", "filter before = " + filter);
		filter += s.toUpperCase(Locale.ENGLISH);
		Log.d("debug", "filter after = " + filter);
	}

	public void handleBackBtnClicked() {
		if (filter.length() > 0) {
//			Log.d("debug", "filter before = " + filter);
			filter = filter.substring(0, filter.length() - 1);
//			Log.d("debug", "filter after = " + filter);
		}
	}

	public String getFilter() {
		return filter;
	}

	public void resetFilter() {
		filter = "";
	}

	public void setNumBtns(int num) {
		Log.d("debug", "setting num btns = " + num);
		numBtns = num;
	}
}
