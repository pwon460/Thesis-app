package simo.transport.helpers;

import java.util.ArrayList;

import simo.transport.R;
import simo.transport.templates.BasicListenerActivity;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class CustomAdapter extends ArrayAdapter<String> {

	private static final int PADDING = 10;
	private static final int NO_PADDING = 0;
	private static final int PADDING_ADJUSTMENT = 15;
	private int numItemsShown;
	private Context context;
	private ArrayList<String> values;
	private int resource;
	private int textColor;
	private int background;
	private boolean isInverted;
	private int itemHeight;
	private BasicListenerActivity activity;

	public CustomAdapter(Context context, int resource,
			ArrayList<String> values, int numItemsShown) {
		super(context, resource, values);
		this.activity = (BasicListenerActivity) context;
		this.resource = resource;
		this.context = context;
		this.values = values;
		this.isInverted = false;
		this.numItemsShown = numItemsShown;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView rowView = (TextView) convertView;
		// reuse views as per Holder pattern
		if (rowView == null) {
			rowView = initRowView(parent);
		}

		// put stuff into the view
		ViewHolder holder = (ViewHolder) rowView.getTag();
		String s = values.get(position);
		populateRowView(parent, holder, s);

		return rowView;
	}

	private TextView initRowView(ViewGroup parent) {
		TextView rowView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = (TextView) inflater.inflate(resource, parent, false);
		// override the size of the item in the listview
		itemHeight = (parent.getMeasuredHeight() - PADDING) / numItemsShown;
		rowView.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, itemHeight));
		rowView.setOnHoverListener(activity);

		// Log.d("debug", "list view item height = " + itemHeight);
		// configure view holder
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.setTextView((TextView) rowView
				.findViewById(R.id.customTextView));
		rowView.setTag(viewHolder);
		return rowView;
	}

	private void populateRowView(ViewGroup parent, ViewHolder holder, String s) {
		TextView tv = holder.getTextView();

		tv.setTextColor(textColor);
		tv.setTextAppearance(context, R.style.VerySmallText);

		if (s.equals("up")) {
			tv.setText("^");
			tv.setGravity(Gravity.CENTER);
			tv.setContentDescription("List scroll up");
		} else if (s.equals("down")) {
			tv.setText("v");
			tv.setGravity(Gravity.CENTER);
			tv.setContentDescription("List scroll down");
		} else {
			tv.setText(s);
			tv.setPadding(parent.getMeasuredWidth() / PADDING_ADJUSTMENT,
					NO_PADDING, parent.getMeasuredWidth() / PADDING_ADJUSTMENT,
					NO_PADDING);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setContentDescription(s);
		}
		
		setTextViewBackground(tv);

	}

	private void setTextViewBackground(TextView tv) {
		GradientDrawable drawable;
		StateListDrawable states = new StateListDrawable();

		/*
		 * set highlighted states
		 */
		if (isInverted) {
			drawable = ButtonBuilder.getHighlightedBorderedRectangle(context,
					background, textColor);
		} else {
			drawable = ButtonBuilder.getHighlightedBorderedRectangle(context,
					textColor, background);
		}

		states.addState(new int[] { android.R.attr.state_pressed }, drawable);
		states.addState(new int[] { android.R.attr.state_selected }, drawable);

		// set non highlighted state
		if (isInverted) {
			drawable = ButtonBuilder.getBorderedRectangle(context, textColor,
					background);
		} else {
			drawable = ButtonBuilder.getBorderedRectangle(context, textColor);
		}

		states.addState(new int[] { -android.R.attr.state_selected }, drawable);

		if (activity.getAPIVersion() >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			tv.setBackground(states);
		} else {
			tv.setBackgroundDrawable(states);
		}
	}

	public void setTextColor(int color) {
		this.textColor = color;
	}

	public void setInverseMode(boolean isInverted) {
		this.isInverted = isInverted;
	}

	public void setBackgroundColor(int color) {
		this.background = color;
	}

	public int getItemHeight() {
		return itemHeight;
	}

	public int getNumItemsShown() {
		return numItemsShown;
	}
	
}
