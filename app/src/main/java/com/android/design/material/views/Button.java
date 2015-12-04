package com.android.design.material.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.android.design.library.R;
import com.android.design.utils.Util;

public abstract class Button extends CustomView {

	final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

	// Complete in child class
	int minWidth;
	int minHeight;
	int background;
	float rippleSpeed = 12f;
	int rippleSize = 3;
	Integer rippleColor;
	OnClickListener onClickListener;
	boolean clickAfterRipple = true;
	int backgroundColor = Color.parseColor("#1E88E5");
	
	public Button(Context context){
		super(context);
	}
	
	public Button(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context, attrs, 0, 0);
	}
	
	public Button(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr, 0);
	}

	public Button(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		super(context, attrs);
		init(context, attrs, defStyleAttr, defStyleRes);
	}
	
	private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		setDefaultProperties();
		// a.getResourceId(R.styleable.RippleView_rd_style, 0);
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatButton, defStyleAttr, defStyleRes);
		clickAfterRipple = a.getBoolean(R.styleable.FloatButton_animate, true);
		setAttributes(context, attrs, defStyleAttr, defStyleRes);
		beforeBackground = backgroundColor;
		if(rippleColor==null)
		rippleColor = makePressColor();
	}

	protected void setDefaultProperties() {
		// Min size
		setMinimumHeight(Util.convertDpToPx(minHeight, getResources()));
		setMinimumWidth(Util.convertDpToPx(minWidth, getResources()));
		// Background shape
		setBackgroundResource(background);
		setBackgroundColor(backgroundColor);
	}
	

	// Set atributtes of XML to View
	protected abstract void setAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes);

	// ### RIPPLE EFFECT ###

	float x = -1, y = -1;
	float radius = -1;

	public boolean onTouchEvent(MotionEvent event) {
		invalidate();
		if (isEnabled()) {
			isLastTouch = true;
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				radius = getHeight() / rippleSize;
				x = event.getX();
				y = event.getY();
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				radius = getHeight() / rippleSize;
				x = event.getX();
				y = event.getY();
				if (!((event.getX() <= getWidth() && event.getX() >= 0) && (event
						.getY() <= getHeight() && event.getY() >= 0))) {
					isLastTouch = false;
					x = -1;
					y = -1;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if ((event.getX() <= getWidth() && event.getX() >= 0)
						&& (event.getY() <= getHeight() && event.getY() >= 0)) {
					radius++;
					if(!clickAfterRipple && onClickListener != null){
						onClickListener.onClick(this);
					}
				} else {
					isLastTouch = false;
					x = -1;
					y = -1;
				}
			}else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
					isLastTouch = false;
					x = -1;
					y = -1;
			}
		}
		return true;
	}

	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		if (!gainFocus) {
			x = -1;
			y = -1;
		}
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	public Bitmap makeCircle() {
		final Bitmap output = Bitmap.createBitmap(getWidth() - Util.convertDpToPx(6, getResources()), getHeight()
						- Util.convertDpToPx(7, getResources()), Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);
		canvas.drawARGB(0, 0, 0, 0);
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(rippleColor);
		canvas.drawCircle(x, y, radius, paint);
		if (radius > getHeight() / rippleSize)
			radius += rippleSpeed;
		if (radius >= getWidth()) {
			x = -1;
			y = -1;
			radius = getHeight() / rippleSize;
			if (onClickListener != null&& clickAfterRipple)
				onClickListener.onClick(this);
		}
		return output;
	}

	/** Make a dark color to ripple effect **/
	protected int makePressColor() {
		int r = (this.backgroundColor >> 16) & 0xFF;
		int g = (this.backgroundColor >> 8) & 0xFF;
		int b = (this.backgroundColor >> 0) & 0xFF;
		r = (r - 30 < 0) ? 0 : r - 30;
		g = (g - 30 < 0) ? 0 : g - 30;
		b = (b - 30 < 0) ? 0 : b - 30;
		return Color.rgb(r, g, b);
	}

	public void setOnClickListener(OnClickListener l) {
		onClickListener = l;
	}

	// Set color of background
	public void setBackgroundColor(int color) {
		this.backgroundColor = color;
		if (isEnabled())
			beforeBackground = backgroundColor;
		try {
			LayerDrawable layer = (LayerDrawable) getContext().getResources().getDrawable(R.drawable.button_background);
			GradientDrawable shape = (GradientDrawable) layer.findDrawableByLayerId(R.id.shape_bacground);
			shape.setColor(backgroundColor);
			rippleColor = makePressColor();
		} catch (Exception ex) {}
	}
	
	// Set color of background
		public void setBackgroundColor(int color, int drawable) {
			this.backgroundColor = color;
			if (isEnabled())
				beforeBackground = backgroundColor;
			try {
				LayerDrawable layer = (LayerDrawable) getContext().getResources().getDrawable(drawable);
				GradientDrawable shape = (GradientDrawable) layer.findDrawableByLayerId(R.id.shape_bacground);
				shape.setColor(backgroundColor);
				rippleColor = makePressColor();
			} catch (Exception ex) {}
		}

	abstract public TextView getTextView();

	public void setRippleSpeed(float rippleSpeed) {
		this.rippleSpeed = rippleSpeed;
	}

	public float getRippleSpeed() {
		return this.rippleSpeed;
	}
}

