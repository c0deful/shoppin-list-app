package pl.dplewa.shoppinglistapp.activity;

import android.content.Intent;
import android.view.View;

/**
 * @author Dominik Plewa
 */
public class AddProductActivity extends AbstractProductFormActivity {

    @Override
    protected void saveInternal(View view) {
        long rowid = dbOps.insertProduct(nameField.getText().toString(),
                priceField.getText().toString(),
                Integer.parseInt(countField.getText().toString()));
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra("entryId", rowid);
        broadcastIntent.setAction("pl.dplewa.shoppinglistapp.NEW_ENTRY");
        sendBroadcast(broadcastIntent);
    }
}