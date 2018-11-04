package pl.dplewa.shoppinglistapp.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

/**
 * @author Dominik Plewa
 */
abstract class ThemedActivity extends AppCompatActivity {

    protected static final String THEME_OPTION = "theme";
    protected static final String THEME_DARK = "dark";
    protected static final String THEME_LIGHT = "light";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String theme = PreferenceManager.getDefaultSharedPreferences(this).getString(THEME_OPTION, THEME_LIGHT);
        if (THEME_LIGHT.equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (THEME_DARK.equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
}
