package simo.transport.helpers;

import java.util.ArrayList;
import java.util.Locale;

import android.util.Log;

public class DisplayedListHandler {

	// stack for storing previous listview states
	private ArrayList<ArrayList<String>> prevListStates = new ArrayList<ArrayList<String>>();
	private int listIndex = 0;
	private ArrayList<String> fullList;
	private int numItemsShown;
	private ArrayList<String> displayedList;

	public void setFullList(ArrayList<String> list) {
		listIndex = 0;
		displayedList = new ArrayList<String>();
		fullList = list;
	}

	public ArrayList<String> getFullList() {
		return fullList;
	}

	public void handleDownSwipe() {
		displayedList.clear();
		for (int i = listIndex; i < listIndex + numItemsShown; i++) {
			if (i == fullList.size()) {
				break;
			}
			displayedList.add(fullList.get(i));
		}
		if (listIndex + numItemsShown < fullList.size()) {
			listIndex += numItemsShown;
		}
	}

	public void handleUpSwipe() {
		displayedList.clear();
		if (listIndex >= numItemsShown) {
			listIndex -= numItemsShown;
		}
		for (int i = listIndex; i < listIndex + numItemsShown; i++) {
			if (i == fullList.size()) {
				break;
			}
			displayedList.add(fullList.get(i));
		}
	}

	public void setNumItemsShown(int num) {
		numItemsShown = num;
	}

	public void saveCurrListState() {
		prevListStates.add(fullList);
	}

	public void filterList(String filter) {
		listIndex = 0;
		displayedList.clear();
		ArrayList<String> tempList = new ArrayList<String>();
		for (int i = 0; i < fullList.size(); i++) {
			String item = fullList.get(i).toUpperCase(Locale.ENGLISH);
			if (item.startsWith(filter)) {
				Log.d("debug", "adding " + item);
				tempList.add(item);
			}
		}

		fullList = tempList;
	}

	private ArrayList<String> getPrevListState() {
		ArrayList<String> prevState = new ArrayList<String>();
		if (prevListStates.size() > 0) {
			prevState = prevListStates.remove(prevListStates.size() - 1);
		}
		return prevState;
	}

	public void restorePrevState() {
		listIndex = 0;
		displayedList.clear();
		fullList = getPrevListState();
	}

	public ArrayList<String> getDisplayedList() {
		if (displayedList.size() == 0) {
			handleDownSwipe(); // grab first 'num items shown'
		}
		return displayedList;
	}
}
