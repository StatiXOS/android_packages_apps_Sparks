/*
 * Copyright (C) 2018 StatiXOS
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
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.statix.sparks.utils.TelephonyUtils;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.statix.sparks.preferences.CustomSettingsPreferenceFragment;

import java.util.ArrayList;
import java.util.List;

public class System extends CustomSettingsPreferenceFragment implements Indexable{
    private static final String TAG = "System";
    private static final String ADVANCED_REBOOT = "advanced_reboot";
    private static final String SMART_PIXELS = "smart_pixels";
    private static final String INCALL_VIB_OPTIONS = "incall_vib_options";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.system);
        addCustomPreference(findPreference(ADVANCED_REBOOT), SECURE_TWO_STATE, STATE_ON);
        updateSmartPixelsPreference();
         final PreferenceScreen prefSet = getPreferenceScreen();

        PreferenceCategory incallVibCategory = (PreferenceCategory) findPreference(INCALL_VIB_OPTIONS);
        if (!TelephonyUtils.isVoiceCapable(getActivity())) {
			prefSet.removePreference(incallVibCategory);
        }
     }

     private void updateSmartPixelsPreference() {
        PreferenceScreen prefSet = getPreferenceScreen();
        boolean enableSmartPixels = getContext().getResources().
                getBoolean(com.android.internal.R.bool.config_enableSmartPixels);
        Preference smartPixels = findPreference(SMART_PIXELS);
         if (!enableSmartPixels){
            prefSet.removePreference(smartPixels);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.SPARKS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
  
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
       new BaseSearchIndexProvider() {
           @Override
           public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
               final ArrayList<SearchIndexableResource> result = new ArrayList<>();
               final SearchIndexableResource sir = new SearchIndexableResource(context);
               sir.xmlResId = R.xml.system;
               result.add(sir);
               return result;
           }

           @Override
           public List<String> getNonIndexableKeys(Context context) {
              final List<String> keys = super.getNonIndexableKeys(context);
              boolean enableSmartPixels = context.getResources().
                    getBoolean(com.android.internal.R.bool.config_enableSmartPixels);
              if (!enableSmartPixels){
                  keys.add(SMART_PIXELS);
              }
              return keys;
           }
      };
}
