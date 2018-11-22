package pl.dplewa.shoppinglistapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import pl.dplewa.shoppinglistapp.R;

/**
 * @author Dominik Plewa
 */
public class ProductLoader extends AsyncTaskLoader<List<Product>> {

    private final DatabaseOperations dbOps;

    public ProductLoader(@NonNull Context context) {
        super(context);
        dbOps = new DatabaseOperations(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Product> loadInBackground() {
        Cursor cursor = dbOps.getProducts(null);
        List<Product> result = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            final int id = cursor.getInt(cursor.getColumnIndex(dbOps.idColumn));
            final String name = cursor.getString(cursor.getColumnIndex(dbOps.nameColumn));
            final String price = cursor.getString(cursor.getColumnIndex(dbOps.priceColumn));
            final int count = cursor.getInt(cursor.getColumnIndex(dbOps.countColumn));
            final boolean isPurchased = cursor.getInt(cursor.getColumnIndex(dbOps.purchasedColumn)) != 0;
            result.add(new Product(id, name, new BigDecimal(price), count, isPurchased));
        }
        return result;
    }
}
