package simo.transport.helpers;

import simo.transport.R;
import simo.transport.R.color;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

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

	public static GradientDrawable getHighlightedRectangle(Context ctx,
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

	public static Drawable getColoredArrow(View button, int textColor) {
		Drawable drawable = button.getBackground();
		return drawable;
	}
}
