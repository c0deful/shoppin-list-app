package pl.dplewa.shoppinglistapp.activity;

import android.content.Intent;
import android.view.View;

/**
 * @author Dominik Plewa
 */
public class AddProductActivity extends AbstractProductFormActivity {

    @Override
    protected void saveInternal(View view) {
        final String name = nameField.getText().toString();
        final String price = priceField.getText().toString();
        final int count = Integer.parseInt(countField.getText().toString());
        final long rowid = dbOps.insertProduct(name, price, count);
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra("entryId", (int) rowid);
        broadcastIntent.putExtra("name", name);
        broadcastIntent.putExtra("price", price);
        broadcastIntent.putExtra("count", count);
        broadcastIntent.setAction("pl.dplewa.shoppinglistapp.NEW_ENTRY");
        sendBroadcast(broadcastIntent, "pl.dplewa.shoppinglistapp.permissions.NEW_ENTRY_INTENT");
    }
}