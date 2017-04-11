package com.example.ntech.contactsandmemo.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.ntech.contactsandmemo.R;

/**
 * Created by ntech on 25-Feb-17.
 */
public class AppSettings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new Settings()).commit();


    }

    public static class Settings extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_preferences);
        }
    }
}