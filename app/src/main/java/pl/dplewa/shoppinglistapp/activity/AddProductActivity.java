package pl.dplewa.shoppinglistapp.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.data.DatabaseOpenHelper;

/**
 * @author Dominik Plewa
 */
public class AddProductActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    private EditText nameField;
    private EditText priceField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
        nameField = findViewById(R.id.productFormName);
        priceField = findViewById(R.id.productFormPrice);
        db = new DatabaseOpenHelper(this).getWritableDatabase();
    }

    public void addProduct(View view) {
        final String productsTable = getString(R.string.db_products_table);
        final String nameColumn = getString(R.string.db_products_name_column);
        final String priceColumn = getString(R.string.db_products_price_column);
        final String purchasedColumn = getString(R.string.db_products_purchased_column);
        final ContentValues productValues = new ContentValues(3);
        productValues.put(nameColumn, nameField.getText().toString());
        productValues.put(priceColumn, priceField.getText().toString());
        productValues.put(purchasedColumn, 0);
        db.insert(productsTable, null, productValues);
        finish();
    }
}
