package com.android.design.utils;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;
import java.util.concurrent.atomic.AtomicInteger;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import com.android.design.library.R;

public class Util {
	
	public static final long FRAME_DURATION = 1000 / 60;
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	private static TypedValue value;
	
	public Util(){}
	
	public static int convertDpToPx(final Context context, final float dp) {
        return (int) applyDimension(COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

	/** Convert Dp to Pixel **/
	public static int convertDpToPx(final float dp, final Resources resources) {
		final float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
		return (int) px;
	}
	
	public static boolean isEnabled(final int[] stateSet) {
        for (int state : stateSet){
            if (state == android.R.attr.state_enabled){
                return true;
            }
        }
        return false;
    }

	public static int getRelativeTop(final View myView) {
		if (myView.getId() == android.R.id.content){
			return myView.getTop();
		}
		else{
			return myView.getTop() + getRelativeTop((View) myView.getParent());
		}
	}

	public static int getRelativeLeft(final View myView) {
		if (myView.getId() == android.R.id.content){
			return myView.getLeft();
		}
		else{
			return myView.getLeft() + getRelativeLeft((View) myView.getParent());
		}
	}
	
	public static int generateViewId() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (;;) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue))
                    return result;                
            }
        } 
        else{
            return android.view.View.generateViewId();
        }
    }
    
    public static boolean hasState(final int[] states, final int state){
		if(states == null){
			return false;
		}
        for (int state1 : states){
            if (state1 == state){
                return true;
            }
    	}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static void setBackground(final View view, final Drawable drawable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
	}
	
	public static boolean isLight(final int color) {
	    return Math.sqrt(
	        Color.red(color) * Color.red(color) * .241 +
	            Color.green(color) * Color.green(color) * .691 +
	            Color.blue(color) * Color.blue(color) * .068) > 130;
	  }

	  public static int getBaseColor(final int color) {
	    if (isLight(color)) {
	      return Color.BLACK;
	    }
	    return Color.WHITE;
	  }

	private static int getMiddleValue(final int prev, final int next, final float factor){
		return Math.round(prev + (next - prev) * factor);
	}
	
	public static int getMiddleColor(final int prevColor, final int curColor, final float factor){		
		if(prevColor == curColor)
			return curColor;
		if(factor == 0f)
			return prevColor;
		else if(factor == 1f)
			return curColor;
		final int a = getMiddleValue(Color.alpha(prevColor), Color.alpha(curColor), factor);
		final int r = getMiddleValue(Color.red(prevColor), Color.red(curColor), factor);
		final int g = getMiddleValue(Color.green(prevColor), Color.green(curColor), factor);
		final int b = getMiddleValue(Color.blue(prevColor), Color.blue(curColor), factor);
		return Color.argb(a, r, g, b);
	}
	
	public static int getColor(final int baseColor, final float alphaPercent){				
		final int alpha = Math.round(Color.alpha(baseColor) * alphaPercent);
		return (baseColor & 0x00FFFFFF) | (alpha << 24);
	}
	
	public static int getColorWithAlpha(final int color, final float ratio) {
		int newColor = 0;
		final int alpha = Math.round(Color.alpha(color) * ratio);
		final int r = Color.red(color);
		final int g = Color.green(color);
		final int b = Color.blue(color);
		newColor = Color.argb(alpha, r, g, b);
		return newColor;
	}
	
	public static int getDarkColor(final int color) {
        final float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }
	
	private static int getColor(final Context context, final int id, final int defaultValue){
		if(value == null)
			value = new TypedValue();
		try{
			final Theme theme = context.getTheme();		
			if(theme != null && theme.resolveAttribute(id, value, true)){
                if (value.type >= TypedValue.TYPE_FIRST_INT && value.type <= TypedValue.TYPE_LAST_INT)
                    return value.data;
                else if (value.type == TypedValue.TYPE_STRING)
                    return context.getResources().getColor(value.resourceId);
            }
		}
		catch(Exception ex){}
		return defaultValue;
	}
	
	public static int windowBackground(final Context context, final int defaultValue){
		return getColor(context, android.R.attr.windowBackground, defaultValue);
	}

    public static int textColorPrimary(final Context context, final int defaultValue){
        return getColor(context, android.R.attr.textColorPrimary, defaultValue);
    }

    public static int textColorSecondary(final Context context, final int defaultValue){
        return getColor(context, android.R.attr.textColorSecondary, defaultValue);
    }
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static int colorPrimary(final Context context, final int defaultValue){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			return getColor(context, android.R.attr.colorPrimary, defaultValue);
		return getColor(context, R.attr.colorPrimary, defaultValue);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static int colorPrimaryDark(Context context, int defaultValue){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			return getColor(context, android.R.attr.colorPrimaryDark, defaultValue);
		return getColor(context, R.attr.colorPrimaryDark, defaultValue);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static int colorAccent(final Context context, final int defaultValue){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			return getColor(context, android.R.attr.colorAccent, defaultValue);
		return getColor(context, R.attr.colorAccent, defaultValue);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static int colorControlNormal(final Context context, final int defaultValue){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			return getColor(context, android.R.attr.colorControlNormal, defaultValue);
		return getColor(context, R.attr.colorControlNormal, defaultValue);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static int colorControlActivated(Context context, int defaultValue){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			return getColor(context, android.R.attr.colorControlActivated, defaultValue);
		return getColor(context, R.attr.colorControlActivated, defaultValue);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static int colorControlHighlight(final Context context, final int defaultValue){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			return getColor(context, android.R.attr.colorControlHighlight, defaultValue);
		return getColor(context, R.attr.colorControlHighlight, defaultValue);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static int colorButtonNormal(final Context context, final int defaultValue){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			return getColor(context, android.R.attr.colorButtonNormal, defaultValue);
		return getColor(context, R.attr.colorButtonNormal, defaultValue);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static int colorSwitchThumbNormal(final Context context, final int defaultValue){		
		return getColor(context, R.attr.colorSwitchThumbNormal, defaultValue);
	}

    public static int getType(final TypedArray array, final int index){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return array.getType(index);
        else{
        	final TypedValue value = array.peekValue(index);
            return value == null ? TypedValue.TYPE_NULL : value.type;
        }
    }

    public static CharSequence getString(final TypedArray array, final int index, final CharSequence defaultValue){
    	final String result = array.getString(index);
        return result == null ? defaultValue : result;
    }

    public static CharSequence getString(final TintTypedArray array, final int index, final CharSequence defaultValue){
        final String result = array.getString(index);
        return result == null ? defaultValue : result;
    }
    
    /** Return a float value within the range. This is just a wrapper for Math.min() and Math.max().
     * This may be useful if you feel it confusing ("Which is min and which is max?"). **/
    public static float getFloat(final float value, final float minValue, final float maxValue) {
        return Math.min(maxValue, Math.max(minValue, value));
    }
    
    /** Create a color integer value with specified alpha. This may be useful to change alpha value of background color. **/
    public static int getColorAlpha(final float alpha, final int baseColor) {
        final int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        final int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
    
    /** Add an OnGlobalLayoutListener for the view. This is just a convenience method for using 
     * This also handles removing listener when onGlobalLayout is called. The target view to add global layout listener
     * runnable runnable to be executed after the view is laid out. **/
    
    public static void addOnGlobalLayoutListener(final View view, final Runnable runnable) {
        final ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                runnable.run();
            }
        });
    }
    
    /** Mix two colors. {@code toColor} will be {@code toAlpha/1} percent, and {@code fromColor} will be 
     * {@code (1-toAlpha)/1} percent. fromColor first color to be mixed toColor, second color to be mixed toAlpha
     * alpha value of toColor, 0.0f to 1.0f. mixed color value in ARGB. Alpha is fixed value (255). **/
    public static int mixColors(final int fromColor, final int toColor, final float toAlpha) {
        final float[] fromCmyk = Util.cmykFromRgb(fromColor);
        final float[] toCmyk = Util.cmykFromRgb(toColor);
        final float[] result = new float[4];
        for (int i = 0; i < 4; i++) {
            result[i] = Math.min(1, fromCmyk[i] * (1 - toAlpha) + toCmyk[i] * toAlpha);
        }
        return 0xff000000 + (0x00ffffff & Util.rgbFromCmyk(result));
    }
    
    /** Convert RGB color to CMYK color. rgbColor target color CMYK array **/
    public static float[] cmykFromRgb(final int rgbColor) {
        final int red = (0xff0000 & rgbColor) >> 16;
        final int green = (0xff00 & rgbColor) >> 8;
        final int blue = (0xff & rgbColor);
        final float black = Math.min(1.0f - red / 255.0f, Math.min(1.0f - green / 255.0f, 1.0f - blue / 255.0f));
        float cyan = 1.0f;
        float magenta = 1.0f;
        float yellow = 1.0f;
        if (black != 1.0f) {
            // black 1.0 causes zero divide
            cyan = (1.0f - (red / 255.0f) - black) / (1.0f - black);
            magenta = (1.0f - (green / 255.0f) - black) / (1.0f - black);
            yellow = (1.0f - (blue / 255.0f) - black) / (1.0f - black);
        }
        return new float[]{cyan, magenta, yellow, black};
    }
    
    /** Convert CYMK color to RGB color. This method doesn't check f cmyk is not null or have 4 elements in array.
     * Cmyk target CYMK color. Each value should be between 0.0f to 1.0f, and should be set in this order: cyan, 
     * magenta, yellow, black. ARGB color. Alpha is fixed value (255). **/
    public static int rgbFromCmyk(final float[] cmyk) {
        final float cyan = cmyk[0];
        final float magenta = cmyk[1];
        final float yellow = cmyk[2];
        final float black = cmyk[3];
        final int red = (int) ((1.0f - Math.min(1.0f, cyan * (1.0f - black) + black)) * 255);
        final int green = (int) ((1.0f - Math.min(1.0f, magenta * (1.0f - black) + black)) * 255);
        final int blue = (int) ((1.0f - Math.min(1.0f, yellow * (1.0f - black) + black)) * 255);
        return ((0xff & red) << 16) + ((0xff & green) << 8) + (0xff & blue);
    }
}
