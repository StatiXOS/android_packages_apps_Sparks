/*
 * Copyright (C) 2018 CarbonROM
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

package com.statix.sparks.fragments.system;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.statix.sparks.preferences.CustomSettingsPreferenceFragment;

import java.util.ArrayList;
import java.util.List;

public class SmartPixels extends CustomSettingsPreferenceFragment
        implements Indexable {

    private static final String TAG = "SmartPixels";
    private static final String SMART_PIXELS_ENABLE = "smart_pixels_enable";
    private static final String SMART_PIXELS_ON_POWER_SAVE = "smart_pixels_on_power_save";

    private SmartPixelsObserver mSmartPixelsObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.smart_pixels);

        addCustomPreference(findPreference(SMART_PIXELS_ENABLE), SYSTEM_TWO_STATE, STATE_OFF);
        addCustomPreference(findPreference(SMART_PIXELS_ON_POWER_SAVE), SYSTEM_TWO_STATE, STATE_OFF);
        mSmartPixelsObserver = new SmartPixelsObserver(new Handler());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSmartPixelsObserver != null) {
            mSmartPixelsObserver.register();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSmartPixelsObserver != null) {
            mSmartPixelsObserver.unregister();
        }
    }

    private class SmartPixelsObserver extends ContentObserver {
        public SmartPixelsObserver(Handler handler) {
            super(handler);
        }

        public void register() {
            getActivity().getContentResolver().registerContentObserver(Settings.System.getUriFor(
                    SMART_PIXELS_ENABLE), false, this, UserHandle.USER_CURRENT);
            getActivity().getContentResolver().registerContentObserver(Settings.System.getUriFor(
                    SMART_PIXELS_ON_POWER_SAVE), false, this, UserHandle.USER_CURRENT);
        }

        public void unregister() {
            getActivity().getContentResolver().unregisterContentObserver(this);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            updateAllCustomPreferences();
        }
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
       new BaseSearchIndexProvider() {
           @Override
           public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
               final ArrayList<SearchIndexableResource> result = new ArrayList<>();
               final SearchIndexableResource sir = new SearchIndexableResource(context);
               sir.xmlResId = R.xml.smart_pixels;
               boolean enableSmartPixels = context.getResources().
                    getBoolean(com.android.internal.R.bool.config_enableSmartPixels);
               if (enableSmartPixels) {
                   result.add(sir);
               }
               return result;
           }

           @Override
           public List<String> getNonIndexableKeys(Context context) {
              final List<String> keys = super.getNonIndexableKeys(context);
              return keys;
           }
      };
}
