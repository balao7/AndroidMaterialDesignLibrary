package com.android.design.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.android.design.drawable.RippleDrawable;
import com.android.design.drawable.ToolbarRippleDrawable;
import com.android.design.library.R;

public class RippleManager implements View.OnClickListener, Runnable{
	
	private View.OnClickListener mClickListener;
	private View mView;
	
	public RippleManager(){}
	
	public void onCreate(final View view, final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes){
		if(view.isInEditMode()){
			return;
		}
		mView = view;
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RippleView, defStyleAttr, defStyleRes);
        final int rippleStyle = a.getResourceId(R.styleable.RippleView_rd_style, 0);
		RippleDrawable drawable = null;
		if(rippleStyle != 0){
			drawable = new RippleDrawable.Builder(context, mView, rippleStyle).build();
		}else{
			final boolean rippleEnable = a.getBoolean(R.styleable.RippleView_rd_enable, false);
			if(rippleEnable){
				drawable = new RippleDrawable.Builder(context, mView, attrs, defStyleAttr, defStyleRes).build();
			}
		}
		a.recycle();
		if(drawable != null)
			Util.setBackground(mView, drawable);
	}
		
	public void setOnClickListener(View.OnClickListener l) {
		mClickListener = l;
	}

	public boolean onTouchEvent(MotionEvent event){
		final Drawable background = mView.getBackground();
        return background instanceof RippleDrawable && ((RippleDrawable) background).onTouch(mView, event);
    }
	
	public void onClick(View v) {
		final Drawable background = mView.getBackground();
		long delay = 0;
		if(background instanceof RippleDrawable){
			delay = ((RippleDrawable)background).getClickDelayTime();
		}
		else if(background instanceof ToolbarRippleDrawable){
			delay = ((ToolbarRippleDrawable) background).getClickDelayTime();
		}
		if(delay > 0 && mView.getHandler() != null){
			mView.getHandler().postDelayed(this, delay);
		}
		else{
			run();
		}
	}
		
    public void run() {
    	if(mClickListener != null){
    		mClickListener.onClick(mView);
    	}
    }

    /** Cancel the ripple effect of this view and all of it's children **/
	public static void cancelRipple(View v){
		final Drawable background = v.getBackground();
		if(background instanceof RippleDrawable){
			((RippleDrawable)background).cancel();
		}
		else if(background instanceof ToolbarRippleDrawable){
			((ToolbarRippleDrawable)background).cancel();
		}
		if(v instanceof ViewGroup){
			final ViewGroup vg = (ViewGroup) v;
			for(int i = 0, count = vg.getChildCount(); i < count; i++){
				RippleManager.cancelRipple(vg.getChildAt(i));
			}
		}
	}
}
