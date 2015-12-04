package com.android.design.material.widgets;

import android.content.Context;
import android.util.AttributeSet;
import com.android.design.drawable.RadioButtonDrawable;

public class RadioButton extends CompoundButton {

    public RadioButton(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

	public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr, 0);
	}

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, defStyleRes);
    }
	
	private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		applyStyle(context, attrs, defStyleAttr, defStyleRes);
    }

    public void applyStyle(int resId){
        applyStyle(getContext(), null, 0, resId);
    }

    private void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        final RadioButtonDrawable drawable = new RadioButtonDrawable.Builder(context, attrs, defStyleAttr, defStyleRes).build();
        drawable.setInEditMode(isInEditMode());
        drawable.setAnimEnable(false);
        setButtonDrawable(drawable);
        drawable.setAnimEnable(true);
    }
	
    public void toggle() {
        // we override to prevent toggle when the radio is already checked (as opposed to check boxes widgets)
        if (!isChecked()) {
            super.toggle();
        }
    }

    /** Change the checked state of this button immediately without showing animation. Checked The checked state. **/
    public void setCheckedImmediately(boolean checked){
        if(mButtonDrawable instanceof RadioButtonDrawable){
            final RadioButtonDrawable drawable = (RadioButtonDrawable) mButtonDrawable;
            drawable.setAnimEnable(false);
            setChecked(checked);
            drawable.setAnimEnable(true);
        }
        else{
            setChecked(checked);
        }
    }
}
