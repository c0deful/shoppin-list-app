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
public class AddProductActivity extends AppCompatActivity {

    private DatabaseOperations dbOps;

    private EditText nameField;
    private EditText priceField;
    private EditText countField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
        nameField = findViewById(R.id.productFormName);
        priceField = findViewById(R.id.productFormPrice);
        countField = findViewById(R.id.productFormCount);
        dbOps = new DatabaseOperations(this);
    }

    public void addProduct(View view) {
        dbOps.insertProduct(nameField.getText().toString(),
                priceField.getText().toString(),
                Integer.parseInt(countField.getText().toString()));
        finish();
    }
}
