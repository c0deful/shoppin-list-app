package pl.dplewa.shoppinglistapp.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.Arrays;

import pl.dplewa.shoppinglistapp.R;

import static pl.dplewa.shoppinglistapp.view.ProductAdapter.FONT_SIZE_OPTION;

/**
 * @author Dominik Plewa
 */
public class SettingsActivity extends ThemedActivity {

    private static final Integer[] fontSizes = new Integer[]{12, 16, 20};
    private String selectedTheme;
    private Spinner fontSizeSpinner;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // initialize theme radio group
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

        // initialize font size spinner
        Integer selectedFontSize = sharedPreferences.getInt(FONT_SIZE_OPTION, 16);
        fontSizeSpinner = findViewById(R.id.settingsFontSizeSpinner);
        SpinnerAdapter adapter = new ArrayAdapter<>(this, R.layout.font_size_spinner_element,
                R.id.fontSizeSpinnerElement, fontSizes);
        fontSizeSpinner.setAdapter(adapter);
        fontSizeSpinner.setSelection(Arrays.asList(fontSizes).indexOf(selectedFontSize));
    }

    public void saveSettings(View view) {
        Integer selectedFontSize = fontSizes[fontSizeSpinner.getSelectedItemPosition()];
        Integer originalFontSize = sharedPreferences.getInt(FONT_SIZE_OPTION, 16);
        String originalTheme = sharedPreferences.getString(THEME_OPTION, THEME_LIGHT);
        sharedPreferences.edit()
                .putString(THEME_OPTION, selectedTheme)
                .putInt(FONT_SIZE_OPTION, selectedFontSize)
                .apply();
        setResult(selectedTheme.equals(originalTheme) && selectedFontSize.equals(originalFontSize)
                ? Activity.RESULT_CANCELED : Activity.RESULT_OK);
        finish();
    }
}
