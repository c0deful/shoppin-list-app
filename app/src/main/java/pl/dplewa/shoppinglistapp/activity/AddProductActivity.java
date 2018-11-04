package pl.dplewa.shoppinglistapp.activity;

import android.view.View;

/**
 * @author Dominik Plewa
 */
public class AddProductActivity extends AbstractProductFormActivity {

    @Override
    protected void saveInternal(View view) {
        dbOps.insertProduct(nameField.getText().toString(),
                priceField.getText().toString(),
                Integer.parseInt(countField.getText().toString()));
    }
}