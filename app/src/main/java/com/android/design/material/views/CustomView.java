package com.android.design.material.views;

import com.android.design.utils.DesignConstants;
import com.android.design.utils.Util;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class CustomView extends RelativeLayout{
	
	protected final static String ANDROIDXML = DesignConstants.ANDROIDXML;
	protected final static String MATERIALDESIGNXML = DesignConstants.MATERIALDESIGNXML;
	protected final int disabledBackgroundColor = Color.parseColor("#E2E2E2");
	protected int beforeBackground;
	// Indicate if user touched this view the last time
	protected boolean isLastTouch = false;
	protected boolean animation = false;
	protected Util utils = new Util();
	
	public CustomView(Context context){
		super(context);
	}

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(enabled)
			setBackgroundColor(beforeBackground);
		else
			setBackgroundColor(disabledBackgroundColor);
		invalidate();
	}
	
	protected void onAnimationStart() {
		super.onAnimationStart();
		animation = true;
	}
	
	protected void onAnimationEnd() {
		super.onAnimationEnd();
		animation = false;
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(animation)
			invalidate();
	}
}