package pl.dplewa.shoppinglistapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.data.DatabaseOperations;

/**
 * @author Dominik Plewa
 */
public class EditProductActivity extends AppCompatActivity {

    public static final String PRODUCT_NAME = "productName";
    public static final String PRODUCT_PRICE = "productPrice";
    public static final String PRODUCT_COUNT = "productCount";
    public static final String PRODUCT_ID = "rowid";

    private DatabaseOperations dbOps;

    private EditText nameField;
    private EditText priceField;
    private EditText countField;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_form);

        Bundle extras = getIntent().getExtras();
        productId = extras.getInt(PRODUCT_ID);
        nameField = findViewById(R.id.productFormName);
        nameField.setText(extras.getString(PRODUCT_NAME));
        priceField = findViewById(R.id.productFormPrice);
        priceField.setText(extras.getString(PRODUCT_PRICE));
        countField = findViewById(R.id.productFormCount);
        countField.setText(extras.getString(PRODUCT_COUNT));
        dbOps = new DatabaseOperations(this);
    }

    public void addProduct(View view) {
        dbOps.updateProduct(productId,
                nameField.getText().toString(),
                priceField.getText().toString(),
                Integer.parseInt(countField.getText().toString()));
        finish();
    }
}
