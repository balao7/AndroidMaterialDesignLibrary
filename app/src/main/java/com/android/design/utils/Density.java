package com.android.design.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/** Created by Balagovind. **/
public class Density {
  public static int dp2px(Context context, float dp) {
    Resources r = context.getResources();
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    return Math.round(px);
  }
}
