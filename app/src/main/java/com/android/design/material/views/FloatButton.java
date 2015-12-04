package com.android.design.material.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.design.library.R;
import com.android.design.utils.Util;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class FloatButton extends Button{
	
	private int sizeIcon = 24;
	private int sizeRadius = 28;
	private ImageView icon; // Icon of float button
	private Drawable drawableIcon;
	private boolean isShow = false;
	private float showPosition;
	private float hidePosition;
	
	public FloatButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundResource(R.drawable.float_button_background);
		sizeRadius = 28;
		setDefaultProperties();
		icon = new ImageView(context);
		icon.setAdjustViewBounds(true);
		icon.setScaleType(ScaleType.CENTER_CROP);
		if(drawableIcon != null) {
			icon.setImageDrawable(drawableIcon);
		}
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Util.convertDpToPx(sizeIcon, getResources()),
				Util.convertDpToPx(sizeIcon, getResources()));
		params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		icon.setLayoutParams(params);
		addView(icon);		
		
	}
	
	protected void setDefaultProperties(){
		rippleSpeed = Util.convertDpToPx(2, getResources());
		rippleSize = Util.convertDpToPx(5, getResources());
		setMinimumWidth(Util.convertDpToPx(sizeRadius*2, getResources()));
		setMinimumHeight(Util.convertDpToPx(sizeRadius*2, getResources()));
		super.background = R.drawable.float_button_background;
//		super.setDefaultProperties();
	}
	
	
	// Set atributtes of XML to View
	protected void setAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		//Set background Color (Color by resource).
		final TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.FloatButton, defStyleAttr, defStyleRes);
		int backgroundColor = attrs.getAttributeResourceValue(ANDROIDXML,"background",-1);
		if(backgroundColor != -1){
			// Color by hexadecimal
			setBackgroundColor(backgroundColor, R.drawable.button_background);
			this.backgroundColor = backgroundColor;
		}
		// Set Ripple Color (Color by resource).
		int rippleColor = attrs.getAttributeResourceValue(MATERIALDESIGNXML, "rippleColor", -1);
		if (rippleColor != -1) {
			setRippleColor(getResources().getColor(rippleColor));
		} else {
			// Color by hexadecimal
			int background = attrs.getAttributeIntValue(MATERIALDESIGNXML, "rippleColor", -1);
			if (background != -1){
				setRippleColor(background);
			}else{
				setRippleColor(Util.getColorWithAlpha(this.backgroundColor, 0.4f));
			}
		}
		// Icon of button
		int iconResource = typeArray.getResourceId(R.styleable.FloatButton_iconDrawable, -1);
		if(iconResource != -1){
			drawableIcon = getResources().getDrawable(iconResource);
		}
		final boolean animate = attrs.getAttributeBooleanValue(MATERIALDESIGNXML,"animate", false);
		post(new Runnable() {
			public void run() {
				showPosition = ViewHelper.getY(FloatButton.this) - Util.convertDpToPx(0, getResources());
				hidePosition = ViewHelper.getY(FloatButton.this) + getHeight() * 3;
				if (animate) {
					ViewHelper.setY(FloatButton.this, hidePosition);
					show();
				}
			}
		});
	}
		

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (x != -1) {
			final Rect src = new Rect(0, 0, getWidth(), getHeight());
			final Rect dst = new Rect(Util.convertDpToPx(1, getResources()), Util.convertDpToPx(2, getResources()), 
					getWidth()-Util.convertDpToPx(1, getResources()), getHeight()-Util.convertDpToPx(2, getResources()));
			canvas.drawBitmap(cropCircle(makeCircle()), src, dst, null);
			invalidate();
		}
	}
	
	public ImageView getIcon() {
		return icon;
	}

	public void setIcon(ImageView icon) {
		this.icon = icon;
	}

	public Drawable getDrawableIcon() {
		return drawableIcon;
	}

	@SuppressWarnings("deprecation")
	public void setDrawableIcon(Drawable drawableIcon) {
		this.drawableIcon = drawableIcon;
		try {
			icon.setBackground(drawableIcon);
		} catch (NoSuchMethodError e) {
			icon.setBackgroundDrawable(drawableIcon);
		}
	}

	public Bitmap cropCircle(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth()/2, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    return output;
	}

	public TextView getTextView() {
		return null;
	}
	
	public void setRippleColor(int rippleColor) {
		this.rippleColor = rippleColor;
	}
	
	public void show(){
		final ObjectAnimator animator = ObjectAnimator.ofFloat(FloatButton.this, "y", showPosition);
		animator.setInterpolator(new BounceInterpolator());
		animator.setDuration(1500);
		animator.start();
		isShow = true;
	}
	
	public void hide(){
		final ObjectAnimator animator = ObjectAnimator.ofFloat(FloatButton.this, "y", hidePosition);
		animator.setInterpolator(new BounceInterpolator());
		animator.setDuration(1500);
		animator.start();
		isShow = false;
	}
	
	public boolean isShow(){
		return isShow;
	}
}
