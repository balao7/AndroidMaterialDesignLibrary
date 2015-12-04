package com.android.design.icons;

import java.util.Collection;
import java.util.HashMap;
import android.util.Log;
import com.android.design.icons.typeface.FontAwesome;
import com.android.design.icons.typeface.GoogleMaterialFonts;
import com.android.design.icons.typeface.IIcon;
import com.android.design.icons.typeface.ITypeface;

public final class Icons {
	
	private static final String TAG = "Icons";
	private static HashMap<String, ITypeface> fonts = new HashMap<String, ITypeface>();
	private static Icons icons = null;

	// Add Default fonts into fontList
	static {
		loadFonts();
	}

	private Icons(){}
	
	private static void loadFonts(){
		try {
			final FontAwesome fa = new FontAwesome();
			fonts.put(fa.getMappingPrefix(), fa);
			final GoogleMaterialFonts gm = new GoogleMaterialFonts();
			fonts.put(gm.getMappingPrefix(), gm);
		} catch (Exception ex) {
			Log.e(TAG, "loadFonts:- "+ex.getMessage());
		}
	}
	
	public static Icons getInstance(){
		if(icons == null){
			icons = new Icons();
		}if(fonts.isEmpty()){
			loadFonts();
		}
		return icons;
	}

	public void registerFont(final ITypeface font) {
		if(fonts.isEmpty()){
			loadFonts();
		}
		fonts.put(font.getMappingPrefix(), font);
	}

	public Collection<ITypeface> getRegisteredFonts() {
		return fonts.values();
	}

	public ITypeface findFont(final String key) {
		return fonts.get(key);
	}

	public ITypeface findFont(final IIcon icon) {
		return icon.getTypeface();
	}
	
	public StringBuilder getIcon(final StringBuilder fontText){
		try {
			// check fonts map whether fonts get loaded or not. if not then load default fonts.
			if(fonts == null || fonts.size() == 0){
				loadFonts();
			}
		} catch (IllegalArgumentException ex) {
			Log.e(TAG, "Wrong icon name:- "+ex.getMessage());
		}
		return fontText;
	}
}
