package com.marco.marplex.schoolbook;

import android.os.Bundle;

import com.lb.material_preferences_library.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected int getPreferencesXmlId() {
        return R.xml.settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Impostazioni");
    }
}
