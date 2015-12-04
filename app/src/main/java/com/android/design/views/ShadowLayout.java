package com.android.design.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
 
/**
 * This custom layout paints a drop shadow behind all children. The size and opacity
 * of the drop shadow is determined by a "depth" factor that can be set and animated.
 */
public class ShadowLayout extends RelativeLayout {
 
	private static final int SHADOW_RADIUS = 8;
    private static final int SHADOW_COLOR = Color.parseColor("#000000");
    private static final Paint SHADOW_PAINT = new Paint();

    static {
        SHADOW_PAINT.setShadowLayer(SHADOW_RADIUS / 2.0f, 0.0f, 0.0f, SHADOW_COLOR);
        SHADOW_PAINT.setColor(0x000000);
        SHADOW_PAINT.setStyle(Paint.Style.FILL);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onDraw(Canvas canvas) {
        final int containerWidth = getMeasuredWidth();
        final int containerHeight = getMeasuredHeight();

        canvas.drawRect(
                SHADOW_RADIUS / 2.0f,
                SHADOW_RADIUS / 2.0f,
                containerWidth - SHADOW_RADIUS / 2.0f,
                containerHeight - SHADOW_RADIUS / 2.0f,
                SHADOW_PAINT);
    }
}

