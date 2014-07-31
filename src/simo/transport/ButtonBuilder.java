package simo.transport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

public class ButtonBuilder {

	private static final int BORDER_WIDTH = 2;
	
	public static GradientDrawable getBorderedRectangle (Context ctx, int textColor) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setColor(ctx.getResources().getColor(R.color.transparent));
		drawable.setStroke(BORDER_WIDTH, textColor);
		
		return drawable;
	}
}
