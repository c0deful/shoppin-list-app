package pl.dplewa.shoppinglistapp.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.data.DatabaseOpenHelper;
import pl.dplewa.shoppinglistapp.data.DatabaseOperations;

/**
 * @author Dominik Plewa
 */
public class EditProductActivity extends AbstractProductFormActivity {

    public static final String PRODUCT_ID = "entryId";

    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        productId = (String) extras.get(PRODUCT_ID);
        if (productId == null) {
            finish();
            return;
        }

        Cursor cursor = dbOps.getProducts(productId);
        if (!cursor.moveToNext()) {
            finish();
            return;
        }

        nameField.setText(cursor.getString(cursor.getColumnIndex(dbOps.nameColumn)));
        priceField.setText(cursor.getString(cursor.getColumnIndex(dbOps.priceColumn)));
        countField.setText(cursor.getString(cursor.getColumnIndex(dbOps.countColumn)));
    }

    @Override
    protected void saveInternal(View view) {
        dbOps.updateProduct(productId,
                nameField.getText().toString(),
                priceField.getText().toString(),
                Integer.parseInt(countField.getText().toString()));
    }
}