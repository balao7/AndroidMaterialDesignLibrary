/*
 * Copyright 2015 Balagovind
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.design.icons.typeface;

import java.util.Collection;
import java.util.HashMap;
import android.content.Context;
import android.graphics.Typeface;

public interface ITypeface {
	public IIcon getIcon(String key);
    public HashMap<String, Character> getCharacters();
    public String getMappingPrefix();
    public String getFontName();
    public String getVersion();
    public int getIconCount();
    public Collection<String> getIcons();
    public String getAuthor();
    public String getUrl();
    public String getDescription();
    public String getLicense();
    public String getLicenseUrl();
    public Typeface getTypeface(Context ctx);
}
