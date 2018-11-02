package pl.dplewa.shoppinglistapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import pl.dplewa.shoppinglistapp.R;

/**
 * @author Dominik Plewa
 */
public class ProductLoader extends CursorLoader {

    private final SQLiteDatabase db;
    private final String productTable;

    public ProductLoader(@NonNull Context context) {
        super(context);
        db = new DatabaseOpenHelper(context).getReadableDatabase();
        productTable = context.getString(R.string.db_products_table);
    }

    @Override
    public Cursor loadInBackground() {
        return db.query(productTable, new String[] { "ROWID", "*" }, null, null, null, null, null);
    }
}
