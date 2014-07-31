package simo.transport;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;

public class ButtonBuilder {
	
	public static Drawable makeBorder (Context ctx, int textColor) {
		Drawable border = ctx.getResources().getDrawable(R.drawable.button).mutate();
		border.setColorFilter(textColor, Mode.MULTIPLY);
		return border;
	}
}
