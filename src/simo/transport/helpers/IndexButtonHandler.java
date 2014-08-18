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
	private int originalListSize = 0;

	public void setListToIndex(ArrayList<String> toIndex) {
		Log.d("debug", "list to turn into indices: " + toIndex.toString());
		
		// first list passed in will be the original list
		if (originalListSize == 0) {
			originalListSize = toIndex.size();
		}
		
		if (allIndexBtns == null) {
			allIndexBtns = new ArrayList<String>();
		} else if (filter.length() == 0 && toIndex.size() == originalListSize) {
			// case: back button usage
			Log.d("debug", "asdf");
			allIndexBtns.clear();
		}

		if (filter.length() < 2) {
			startIndex = 0;
			endIndex = startIndex + numBtns - NUM_ARROW_BTNS - 1;
			for (int i = 0; i < toIndex.size(); i++) {
				String item = toIndex.get(i);
				// Log.d("debug", "filter = " + filter);
				if (item.length() <= filter.length()) {
					continue;
				}

				String temp = item.substring(0, filter.length() + 1);
				// Log.d("debug", "temp = " + temp);
				if (!allIndexBtns.contains(temp)) {
					if (filter.length() == 0 || temp.contains(filter)) {
						allIndexBtns.add(temp);
					}
				}
			}
			Collections.sort(allIndexBtns);
		}
	}

	/*
	 * used for grabbing a 12 item 'chunk' of the list to show to the user 14
	 * buttons on the side, 2 are taken by up and down so can only show a
	 * maximum of 12 at a time
	 */
	public ArrayList<String> getIndexBtnSubset() {
		ArrayList<String> subList = new ArrayList<String>();
		// Log.d("debug", "creating sublist");
		// Log.d("debug", "startingIndex = " + startIndex);
		// Log.d("debug", "endingIndex = " + endIndex);
		// Log.d("debug", "list size = " + allIndexBtns.size());
		if (endIndex >= allIndexBtns.size()) {
			// Log.d("debug", "smaller sublist");
			for (int i = startIndex; i < allIndexBtns.size(); i++) {
				// Log.d("debug", "adding to sublist: " + allIndexBtns.get(i));
				subList.add(allIndexBtns.get(i));
			}
		} else {
			// Log.d("debug", "full sized sublist");
			for (int i = startIndex; i <= endIndex; i++) {
				// Log.d("debug", "adding to sublist: " + allIndexBtns.get(i));
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
		String lastChar = s.substring(s.length() - 1);
		filter += lastChar.toUpperCase(Locale.ENGLISH);
		Log.d("debug", "filter after = " + filter);
	}

	public void handleBackBtnClicked() {
		if (filter.length() > 0) {
			// Log.d("debug", "filter before = " + filter);
			filter = filter.substring(0, filter.length() - 1);
			// Log.d("debug", "filter after = " + filter);
		}
	}

	public String getFilter() {
		return filter;
	}

	public void resetFilter() {
		Log.d("debug", "clearing filter");
		filter = "";
	}

	public void setNumBtns(int num) {
		// Log.d("debug", "setting num btns = " + num);
		numBtns = num;
	}
	
}
