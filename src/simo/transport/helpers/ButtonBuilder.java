package simo.transport.helpers;

import simo.transport.R;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;

public class ButtonBuilder {

	private static final int BORDER_WIDTH = 2;

	public static GradientDrawable getBorderedRectangle(Context ctx,
			int textColor) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setColor(ctx.getResources().getColor(R.color.transparent));
		drawable.setStroke(BORDER_WIDTH, textColor);

		return drawable;
	}
	
	public static GradientDrawable getBlankRectangle(Context ctx) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setColor(ctx.getResources().getColor(R.color.transparent));

		return drawable;
	}

	public static GradientDrawable getHighlightedBorderedRectangle(Context ctx,
			int textColor, int backgroundColor) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);
		if (backgroundColor == ctx.getResources().getColor(R.color.dark_purple)) {
			drawable.setColor(ctx.getResources().getColor(
					R.color.dark_purple_clicked));
		} else {
			drawable.setColor(ctx.getResources()
					.getColor(R.color.black_clicked));
		}
		drawable.setStroke(BORDER_WIDTH, textColor);

		return drawable;
	}

}
