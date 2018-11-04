package pl.dplewa.shoppinglistapp.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import pl.dplewa.shoppinglistapp.R;

/**
 * @author Dominik Plewa
 */
public class SettingsActivity extends ThemedActivity {
    private String selectedTheme;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        selectedTheme = sharedPreferences.getString(THEME_OPTION, THEME_LIGHT);
        if (THEME_LIGHT.equals(selectedTheme)) {
            ((RadioButton) findViewById(R.id.lightThemeRadioButton)).setChecked(true);
        } else if (THEME_DARK.equals(selectedTheme)) {
            ((RadioButton) findViewById(R.id.darkThemeRadioButton)).setChecked(true);
        }
        RadioGroup themeRadioGroup = findViewById(R.id.themeRadioGroup);
        themeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lightThemeRadioButton) {
                    selectedTheme = THEME_LIGHT;
                } else if (checkedId == R.id.darkThemeRadioButton) {
                    selectedTheme = THEME_DARK;
                }
            }
        });
    }
    public void saveSettings(View view) {
        String originalTheme = sharedPreferences.getString(THEME_OPTION, THEME_LIGHT);
        sharedPreferences.edit()
                .putString(THEME_OPTION, selectedTheme)
                .apply();
        setResult(selectedTheme.equals(originalTheme) ? Activity.RESULT_CANCELED : Activity.RESULT_OK);
        finish();
    }
}
