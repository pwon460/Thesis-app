package simo.transport.helpers;

import java.util.ArrayList;

import simo.transport.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class CustomAdapter extends ArrayAdapter<String> {

	private static final int PADDING = 10;
	private int numItemsShown;
	private Context context;
	private ArrayList<String> values;
	private int resource;
	private int textColor;
	private int background;
	private boolean isInverted;
	private int itemHeight;

	public CustomAdapter(Context context, int resource,
			ArrayList<String> values, int numItemsShown) {
		super(context, resource, values);
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
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = (TextView) inflater.inflate(resource, parent, false);
			// override the size of the item in the listview
			itemHeight = (parent.getMeasuredHeight() - PADDING) / numItemsShown;
			rowView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, itemHeight));

			// Log.d("debug", "list view item height = " + itemHeight);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.setTextView((TextView) rowView
					.findViewById(R.id.customTextView));
			rowView.setTag(viewHolder);
		}

		// put stuff into the view
		ViewHolder holder = (ViewHolder) rowView.getTag();
		String s = values.get(position);
		TextView tv = holder.getTextView();
		tv.setTextColor(textColor);
		tv.setTextAppearance(context, R.style.VerySmallText);
		if (s.equals("up")) {
			tv.setText("^");
			tv.setGravity(Gravity.CENTER);
			tv.setContentDescription("Scroll up list");
		} else if (s.equals("down")) {
			tv.setText("v");
			tv.setGravity(Gravity.CENTER);
			tv.setContentDescription("Scroll down list");
		} else {
			tv.setText(s);
			tv.setPadding(parent.getMeasuredWidth()/15, 0, parent.getMeasuredWidth()/15, 0);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setContentDescription(s);
			if (isInverted) {
				setTextViewBackground(tv, ButtonBuilder.getBorderedRectangle(
						context, textColor, background));
			} else {
				setTextViewBackground(tv,
						ButtonBuilder.getBorderedRectangle(context, textColor));
			}
		}

		return rowView;
	}

	private void setTextViewBackground(TextView tv, Drawable drawable) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			tv.setBackground(drawable);
		} else {
			tv.setBackgroundDrawable(drawable);
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
