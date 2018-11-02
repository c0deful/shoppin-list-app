package pl.dplewa.shoppinglistapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private static final String OPTION_1 = "option1";
    private EditText editText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        editText = findViewById(R.id.editText);
        editText.setText(sharedPreferences.getString(OPTION_1, ""));
    }
    public void saveSettings(View view) {
        // TODO
        String value = editText.getText().toString();
        sharedPreferences.edit().putString(OPTION_1, value).apply();
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }
}
