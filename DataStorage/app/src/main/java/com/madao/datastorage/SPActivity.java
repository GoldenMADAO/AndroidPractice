package com.madao.datastorage;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

/* SharedPreference */
public class SPActivity extends Activity {
    public static final String PREFS_NAME = "AppPrefsFile";
    private boolean mSilentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void restore() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean silent = settings.getBoolean("silentMode", false);
        // do something
        mSilentMode = silent;
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("silentMode", mSilentMode);

        editor.apply();
    }
}
