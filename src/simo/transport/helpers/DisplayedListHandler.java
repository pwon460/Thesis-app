package simo.transport.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

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
		makePartitions();
		Log.d("debug", "full list = " + fullList);
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

	public ArrayList<String> getOriginalList() {
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
		}

		prevIsDown = true;
	}

	public void onUpClicked() {
		displayedList.clear();

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

	public void filterList(String filter) {
		fromIndex = 0;
		displayedList.clear();
		ArrayList<String> tempList = new ArrayList<String>();
		for (int i = 0; i < fullList.size(); i++) {
			String item = fullList.get(i);
			if (item.toUpperCase(Locale.ENGLISH).startsWith(filter)) {
				Log.d("debug", "adding " + item);
				tempList.add(item);
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

	public void restorePrevState() {
		fromIndex = 0;
		displayedList.clear();
		fullList = getPrevListState();
	}

	public ArrayList<String> getDisplayedList() {
		if (displayedList.size() == 0) {
			onDownClicked(); // grab first 'num items shown'
		}
		return displayedList;
	}
}
