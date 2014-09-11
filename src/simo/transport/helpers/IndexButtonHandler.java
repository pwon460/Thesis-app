package simo.transport.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class IndexButtonHandler {

	private static final int NUM_ARROW_BTNS = 2;
	private int numIndexBtns = 8;
	private ArrayList<String> allIndexBtns;
	private String filter = "";
	private int startIndex;
	private int endIndex;
	private boolean canGoUp;
	private boolean canGoDown;
	private String firstItem;
	private String lastItem;

	public void setListToIndex(ArrayList<String> toIndex, int numItemsShown) {
		// Log.d("debug", "list to turn into indices: " + toIndex.toString());

		if (allIndexBtns == null) {
			allIndexBtns = new ArrayList<String>();
		} else {
			allIndexBtns.clear();
		}

		startIndex = 0;
		endIndex = startIndex + numIndexBtns - NUM_ARROW_BTNS - 1;

		if (filter.length() < 2 && toIndex.size() > numItemsShown) {
			if (toIndex.get(0).matches("\\D+")) {
				// Log.d("debug", "handling non route");
				handleNonRoute(toIndex, numItemsShown);
			} else {
				// Log.d("debug", "handling route");
				handleRoute(toIndex, numItemsShown);
			}
		}

		Collections.sort(allIndexBtns);

		if (allIndexBtns != null && allIndexBtns.size() > 1) {
			firstItem = allIndexBtns.get(0);
			lastItem = allIndexBtns.get(allIndexBtns.size() - 1);
		}

	}

	private void handleNonRoute(ArrayList<String> toIndex, int numItemsShown) {
		if (filter.length() == 0) {
			for (int i = 0; i < toIndex.size(); i++) {
				String temp = Character.toString(toIndex.get(i).charAt(0));
				if (!allIndexBtns.contains(temp)) {
					allIndexBtns.add(temp);
				}
			}
		} else { // filter length == 1
			int numItemsOnTwoPgs = numItemsShown * 2 - 2; // -2 to account for
															// up and down arrow
			String anchor = toIndex.get(0);
			StringBuilder sb;
			for (int i = numItemsOnTwoPgs; i < toIndex.size() - 1; i += numItemsOnTwoPgs) {
				// Log.d("debug", "initial i = " + i);
				sb = new StringBuilder();
				String currItem = toIndex.get(i);
				String prevItem = toIndex.get(i - 1);
				String nextItem = toIndex.get(i + 1);

				int j = i - 1;
				boolean handled = true;
				while (currItem.charAt(1) == prevItem.charAt(1)
						&& currItem.charAt(1) == nextItem.charAt(1) && j >= 0) {
					currItem = prevItem;
					prevItem = toIndex.get(j--);
					if (j == i - numItemsOnTwoPgs) {
						handled = false;
						break;
					}
				}

				// Log.d("debug", "go back method: index j = " + j);

				if (!handled) {
					currItem = toIndex.get(i);
					j = i + 1;
					while (currItem.charAt(1) == nextItem.charAt(1)
							&& j < toIndex.size()) {
						currItem = nextItem;
						nextItem = toIndex.get(j++);
					}

					// Log.d("debug", "go fwd method: index j = " + j);
				}

				// index button will be in form of eg. 'Ab-Ac'
				char rootChar = anchor.charAt(0);
				char fromChar = anchor.charAt(1);
				char toChar;
				sb.append(rootChar).append(fromChar);

				if (currItem.charAt(1) == nextItem.charAt(1)) {
					toChar = prevItem.charAt(1);
					anchor = currItem;
				} else {
					toChar = currItem.charAt(1);
					anchor = nextItem;
				}

				// if 'Ab-Ab' then don't need to repeat 'Ab'
				if (fromChar != toChar) {
					sb.append(" to ").append(rootChar).append(toChar);
				}
				allIndexBtns.add(sb.toString());

				i = j;
				// Log.d("debug", "setting i to j, i = " + i);
			}

			sb = new StringBuilder();
			char rootChar = anchor.charAt(0);
			char fromChar = anchor.charAt(1);
			char toChar = toIndex.get(toIndex.size() - 1).charAt(1);
			sb.append(rootChar).append(fromChar);
			if (fromChar != toChar) {
				sb.append(" to ").append(rootChar).append(toChar);
			}
			// Log.d("debug", "index added = " + sb.toString());
			allIndexBtns.add(sb.toString());

		}
	}

	private void handleRoute(ArrayList<String> toIndex, int numItemsShown) {
		if (filter.length() == 0) {
			for (int i = 0; i < toIndex.size(); i++) {
				String[] items = toIndex.get(i).split("\\s", 2);
				Pattern pattern = Pattern.compile("^\\d+");
				Matcher matcher = pattern.matcher(items[0]);
				String temp = "";

				if (matcher.find()) {
					int routeNum = Integer.parseInt(matcher.group());
					if (routeNum < 100) {
						temp = "000";
					} else {
						temp = matcher.group().charAt(0) + "00";
					}
				} else {
					temp = items[0].charAt(0) + "00";
				}

				if (!allIndexBtns.contains(temp)) {
					allIndexBtns.add(temp);
				}

			}
		} else { // filter length == 1
			// if stuff to show is more than 2 pages, then index it
			// the random '2' comes from 1 down button on first page and 1 up
			// button on next page
			if (toIndex.size() >= numItemsShown * 2 - 2) {
				for (int i = 0; i < toIndex.size(); i++) {
					String[] items = toIndex.get(i).split(" ", 2);
					Pattern pattern = Pattern.compile("^\\d+");
					Matcher matcher = pattern.matcher(items[0]);
					String temp = "";

					if (matcher.find()) {
						int routeNum = Integer.parseInt(matcher.group());
						if (routeNum < 100) {
							temp = Integer.toString(routeNum / 10);
						} else if (routeNum >= 100 & routeNum < 1000) {
							temp = Integer.toString(routeNum / 10) + "0";
						} else if (routeNum >= 1000) {
							temp = Integer.toString(routeNum / 100) + "0";
						}
					} else {
						temp = items[0].substring(0, 2) + "0";
					}

					if (!allIndexBtns.contains(temp)) {
						allIndexBtns.add(temp);
					}
				}
			}

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

		if (subList.contains(firstItem) && subList.contains(lastItem)) {
			canGoDown = false;
			canGoUp = false;
		} else if (subList.contains(firstItem)) {
			canGoDown = true;
			canGoUp = false;
		} else if (subList.contains(lastItem)) {
			canGoDown = false;
			canGoUp = true;
		} else {
			canGoDown = true;
			canGoUp = true;
		}

		return subList;
	}

	public void handleUpClick() {
		if (startIndex >= numIndexBtns - NUM_ARROW_BTNS) {
			endIndex = startIndex - 1;
			startIndex -= numIndexBtns - NUM_ARROW_BTNS;
		}

	}

	public void handleDownClick() {
		if (endIndex + 1 < allIndexBtns.size()) {
			startIndex = endIndex + 1;
			endIndex += numIndexBtns - NUM_ARROW_BTNS;
		}
	}

	public void handleIndexBtnClicked(String s) {
		// Log.d("debug", "index button clicked");
		// Log.d("debug", "filter before = " + filter);
		if (filter.length() == 0) {
			filter += s.charAt(0);
		} else {
			if (s.matches("[A-Z][a-z]-[A-Z][a-z]")) {
				filter += "<" + s.charAt(1) + "-" + s.charAt(4) + ">";
			} else {
				filter += s.charAt(filter.length());
			}
		}
		// Log.d("debug", "filter after = " + filter);
	}

	public void handleBackBtnClicked() {
		if (filter.length() > 0) {
			// Log.d("debug", "filter before = " + filter);
			if (filter.substring(filter.length() - 1).equals(">")) {
				String[] parts = filter.split("<");
				filter = parts[0];
			} else {
				filter = filter.substring(0, filter.length() - 1);
			}
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
		numIndexBtns = num;
	}

	public boolean canGoDown() {
		return canGoDown;
	}

	public boolean canGoUp() {
		return canGoUp;
	}

}
