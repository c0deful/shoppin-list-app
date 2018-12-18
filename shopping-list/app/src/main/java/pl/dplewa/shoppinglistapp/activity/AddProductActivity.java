package pl.dplewa.shoppinglistapp.activity;

import android.content.Intent;
import android.view.View;

import pl.dplewa.shoppinglistapp.data.Product;

/**
 * @author Dominik Plewa
 */
public class AddProductActivity extends AbstractProductFormActivity {

    @Override
    protected void saveInternal(View view) {
        final String name = nameField.getText().toString();
        final Double price = Double.parseDouble(priceField.getText().toString());
        final Long count = Long.parseLong(countField.getText().toString());
        final Product product = new Product(name, price, count, Boolean.FALSE);
        final String productId = productDb.insert(product);
        broadcastNewProduct(productId, product);
    }

    private void broadcastNewProduct(String productId, Product product) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra("entryId", productId);
        broadcastIntent.putExtra("name", product.name);
        broadcastIntent.putExtra("price", product.price);
        broadcastIntent.putExtra("count", product.count);
        broadcastIntent.setAction("pl.dplewa.shoppinglistapp.NEW_ENTRY");
        sendBroadcast(broadcastIntent, "pl.dplewa.shoppinglistapp.permissions.NEW_ENTRY_INTENT");
    }
}