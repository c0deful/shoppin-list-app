package pl.dplewa.shoppinglistapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.function.BiConsumer;

import pl.dplewa.shoppinglistapp.data.Product;

/**
 * @author Dominik Plewa
 */
public class EditProductActivity extends AbstractProductFormActivity {

    public static final String PRODUCT_ID = "entryId";

    private String productId;
    private boolean isPurchased;

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

        productDb.getProduct(productId)
                .whenComplete(new BiConsumer<Product, Throwable>() {
                    @Override
                    public void accept(Product product, Throwable throwable) {
                        if (throwable != null) {
                            Log.e("", "Error while getting product", throwable);
                            finish();
                        } else {
                            nameField.setText(product.name);
                            priceField.setText(product.price.toString());
                            countField.setText(product.count.toString());
                            isPurchased = product.isPurchased;
                        }
                    }
                });
    }

    @Override
    protected void saveInternal(View view) {
        productDb.updateProduct(productId, new Product(nameField.getText().toString(),
                        Double.parseDouble(priceField.getText().toString()),
                        Long.parseLong(countField.getText().toString()),
                        isPurchased));
    }
}