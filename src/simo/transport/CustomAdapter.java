package simo.transport;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class CustomAdapter extends ArrayAdapter<String> {

	private Context context;
	private ArrayList<String> values;
	private int resource;
	private int textColor;
	private int textSettings;

	public CustomAdapter(Context context, int resource, ArrayList<String> values) {
		super(context, resource, values);
		this.resource = resource;
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		// reuse views
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(resource, parent, false);
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
		tv.setText(s);
		tv.setTextColor(textColor);
		tv.setGravity(Gravity.CENTER);
		setTextSettings(tv);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			tv.setBackground(ButtonBuilder.getBorderedRectangle(context,
					textColor));
		} else {
			tv.setBackgroundDrawable(ButtonBuilder.getBorderedRectangle(
					context, textColor));
		}

		return rowView;
	}
	
	private void setTextSettings(TextView tv) {
		tv.setGravity(Gravity.CENTER);
		if (textSettings == 2) {
			tv.setTextAppearance(context, R.style.MediumText);
		} else if (textSettings == 3) {
			tv.setTextAppearance(context, R.style.LargeText);
		} else {
			tv.setTextAppearance(context, R.style.SmallText);
		}
	}

	public void setTextColor(int color) {
		this.textColor = color;
	}

	public void setTextSettings(int textSettings) {
		this.textSettings = textSettings;
	}

}
