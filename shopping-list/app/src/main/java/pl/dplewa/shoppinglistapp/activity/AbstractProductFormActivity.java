package pl.dplewa.shoppinglistapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.data.ProductDatabase;

/**
 * @author Dominik Plewa
 */
abstract class AbstractProductFormActivity extends ThemedActivity {
    protected ProductDatabase productDb;

    protected EditText nameField;
    protected EditText priceField;
    protected EditText countField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
        nameField = findViewById(R.id.productFormName);
        priceField = findViewById(R.id.productFormPrice);
        countField = findViewById(R.id.productFormCount);
        productDb = new ProductDatabase();
    }

    private boolean validate() {
        boolean valid = true;
        if (nameField.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.productFormNameValidateToast, Toast.LENGTH_LONG).show();
            valid = false;
        } else if (priceField.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.productFormPriceValidateToast, Toast.LENGTH_LONG).show();
            valid = false;
        } else if (countField.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.productFormCountValidateToast, Toast.LENGTH_LONG).show();
            valid = false;
        }
        return valid;
    }

    public void save(View view) {
        if (validate()) {
            saveInternal(view);
            finish();
        }
    }

    protected abstract void saveInternal(View view);
}