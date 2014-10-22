package simo.transport.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class DisplayedListHandler {

	// stack for storing previous listview states
	private ArrayList<ArrayList<String>> prevListStates = new ArrayList<ArrayList<String>>();
	private int fromIndex;
	private ArrayList<String> fullList; // this one has up and down buttons
										// included in the list
	private int numItemsShown;
	private ArrayList<String> displayedList;
	private boolean prevIsDown;
	private boolean prevIsUp;

	public void setFullList(ArrayList<String> list) {
		fromIndex = 0;
		displayedList = new ArrayList<String>();
		fullList = list;
		prevIsDown = false;
		prevIsUp = false;
		makePartitions();
//		Log.d("debug", "full list = " + fullList);
	}

	private void makePartitions() {

		for (int i = 0; i < fullList.size(); i++) {
			if (i != 0 && i % (numItemsShown - 1) == 0
					&& i < fullList.size() - 1) {
				fullList.add(i, "down");
			}
		}

		for (int i = 0; i < fullList.size(); i++) {
			if (fullList.get(i).equals("down")) {
				fullList.add(i + 1, "up");
			}
		}

	}

	/*
	 * returns the fullList in its original unaltered state
	 */
	public ArrayList<String> getFullList() {
		ArrayList<String> originalList = new ArrayList<String>();

		for (int i = 0; i < fullList.size(); i++) {
			String item = fullList.get(i);
			if (!item.equals("down") && !item.equals("up")) {
				originalList.add(item);
			}
		}

		return originalList;
	}

	public void onDownClicked() {
		displayedList.clear();

		if (prevIsUp) {
			fromIndex += numItemsShown;
			prevIsUp = false;
		}

		// add the actual items to be displayed on the list
		int temp = fromIndex;
		while (temp < fullList.size()) {
			String item = fullList.get(temp);
			displayedList.add(item);
			temp++;
			if (item.equals("down")) {
				break;
			}
		}

		if (fromIndex + numItemsShown < fullList.size()) {
			fromIndex += numItemsShown;
			prevIsDown = true;
		} else {
			prevIsDown = false;
		}

	}

	public void onUpClicked() {
		displayedList.clear();

//		Log.d("debug", "before = " + fromIndex);
		if (prevIsDown) {
			if (fromIndex - numItemsShown > 0) {
				fromIndex -= numItemsShown;
			}
			prevIsDown = false;
		}

		while (fromIndex > 0) {
			fromIndex--;
			String item = fullList.get(fromIndex);
			displayedList.add(item);
			if (item.equals("up")) {
				break;
			}
		}

		Collections.reverse(displayedList);
		prevIsUp = true;
	}

	public void setNumItemsShown(int num) {
		numItemsShown = num;
	}

	public void saveCurrListState() {
		prevListStates.add(fullList);
	}
	
	/*
	 * prev list states only saves the current list state on index button click
	 * for short-term back button usage. once the listview item has been selected,
	 * these 'prev list states' can be wiped and start anew for the next list
	 */
	public void clearPrevListStates() {
		prevListStates.clear();
	}

	public void filterList(String filter) {
		fromIndex = 0;
		displayedList.clear();
		ArrayList<String> tempList = new ArrayList<String>();

		if (filter.contains(">")) {
			String[] parts = filter.split("<");
			String startChar = parts[0];
			// chop off the ">"
			String charRange = parts[1].substring(0, parts[1].length() - 1);
			parts = charRange.split("-");
			String fromChar = parts[0];
			String toChar = parts[1];
			int fromIndex = -1;
			int toIndex = -1;
			
			for (int i = 0; i < fullList.size(); i++) {
				String item = fullList.get(i);
				if (item.startsWith(startChar + fromChar) && fromIndex == -1) {
					fromIndex = i;
				}
				
				if (item.startsWith(startChar + toChar)) {
					toIndex = i;
				}
			}
			
			for (int j = fromIndex; j <= toIndex; j++) {
				String item = fullList.get(j);
				if (item.equals("up") || item.equals("down")) {
					continue;
				}
				tempList.add(item);
			}
			
		} else {
			Log.d("debug", "list size to filter = " + fullList.size());
			for (int i = 0; i < fullList.size(); i++) {
				String item = fullList.get(i);

				if (item.equals("up") || item.equals("down")) {
					continue;
				}
				
				String[] parts = item.split("\\s", 2);
				if (parts[0].matches("^\\d.*")) {
					Pattern pattern = Pattern.compile("^\\d+");
					Matcher matcher = pattern.matcher(parts[0]);
					String temp = "";

					if (matcher.find()) {
						int routeNum = Integer.parseInt(matcher.group());
						if (routeNum < 10) {
							temp = "00" + routeNum;
						} else if (routeNum < 100){
							temp = "0" + routeNum;
						} else {
							temp = "" + routeNum;
						}
					}
					
					if (temp.startsWith(filter)) {
//						Log.d("debug", "adding " + item);
						tempList.add(item);
					}
					
				} else {
					if (item.startsWith(filter)) {
//						Log.d("debug", "adding " + item);
						tempList.add(item);
					}
				}
			}
		}

		fullList = tempList;
		makePartitions();
	}

	private ArrayList<String> getPrevListState() {
		ArrayList<String> prevState = new ArrayList<String>();
		if (prevListStates.size() > 0) {
			prevState = prevListStates.remove(prevListStates.size() - 1);
		}
		return prevState;
	}

	public boolean restorePrevState() {
		boolean hasRestored = true;
		fromIndex = 0;
		displayedList.clear();
		ArrayList<String> temp = fullList;
		fullList = getPrevListState();
		if (temp == fullList) {
			hasRestored = false;
		}
		return hasRestored;
	}

	public ArrayList<String> getDisplayedList() {
		if (displayedList.size() == 0) {
			onDownClicked(); // grab first 'num items shown'
		}
		return displayedList;
	}
}
