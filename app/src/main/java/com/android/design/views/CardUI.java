/*
 * Copyright (C) 2015 The Android Open Source Project
 * Copyright (C) 2015 Balagovind
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.design.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.android.design.library.R;
import com.android.design.utils.Util;

public class CardUI extends View{
	
	private final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";
	private final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
	private int backgroundColor = Color.parseColor("#FFFFFF");
	private int mShadowColor = Color.parseColor("#000000");
	private int mPadding;
    private int mCornerRadius;
    private float mShadowRadius;
    private float mShadowOffsetX;
    private float mShadowOffsetY;
    private float mShadowColorAlpha;
	private static final float SHADOW_RADIUS = 8.0f;
    private static final float SHADOW_OFFSET_X = 8.0f;
    private static final float SHADOW_OFFSET_Y = 4.0f;
    private static final float MIN_SHADOW_COLOR_ALPHA = 0.4f;
    private RectF backgroundRectF;
    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public CardUI(Context context){
		this(context, null);
	}
	
	public CardUI(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CardUI(Context context, AttributeSet attributes, int defStyleAttr) {
		super(context, attributes, defStyleAttr);
		mPadding = attributes.getAttributeResourceValue(ANDROIDXML, "padding", getResources().getDimensionPixelSize(R.dimen.spacing_medium));
		backgroundColor = attributes.getAttributeResourceValue(ANDROIDXML, "background", android.R.color.white);
		mShadowColor = attributes.getAttributeResourceValue(MATERIALDESIGNXML, "shadowColor", android.R.color.black);
		mCornerRadius = attributes.getAttributeResourceValue(MATERIALDESIGNXML, "cornerRadius", getResources().getDimensionPixelSize(R.dimen.default_radius));
		mShadowRadius = attributes.getAttributeFloatValue(MATERIALDESIGNXML, "shadowRadius", SHADOW_RADIUS);
        mShadowOffsetX = attributes.getAttributeFloatValue(MATERIALDESIGNXML, "shadowOffsetX", SHADOW_OFFSET_X);
        mShadowOffsetY = attributes.getAttributeFloatValue(MATERIALDESIGNXML, "shadowOffsetY", SHADOW_OFFSET_Y);
        mShadowColorAlpha = attributes.getAttributeFloatValue(MATERIALDESIGNXML, "shadowColorAlpha", MIN_SHADOW_COLOR_ALPHA);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.FILL);
        mShadowColor = Util.getColorWithAlpha(mShadowColor, mShadowColorAlpha);
        backgroundPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, mShadowColor);
        setWillNotDraw(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
	}
	
	/** Set Background color for view **/
	public void setBackgroundColor(int color) {
		this.backgroundColor = color;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
    }

	/** Set view shadow color **/
    public void setShadowColor(int color) {
        this.mShadowColor = color;
        backgroundPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, mShadowColor);
        invalidate();
    }
    
    private final RectF getRectF() {
        if (backgroundRectF == null) {
            backgroundRectF = new RectF();
            backgroundRectF.left = mPadding;
            backgroundRectF.top = mPadding;
            backgroundRectF.right = getWidth() - mPadding;
            backgroundRectF.bottom = getHeight() - mPadding;
        }
        return backgroundRectF;
    }
    
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        backgroundPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, mShadowColor);
        canvas.drawRoundRect(getRectF(), mCornerRadius, mCornerRadius, backgroundPaint);
        canvas.save();
    }
}
