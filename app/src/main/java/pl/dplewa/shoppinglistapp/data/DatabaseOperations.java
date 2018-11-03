package pl.dplewa.shoppinglistapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import pl.dplewa.shoppinglistapp.R;

/**
 * @author Dominik Plewa
 */
public class DatabaseOperations {

    private final SQLiteDatabase db;
    public final String productsTable;
    public final String idColumn;
    public final String nameColumn;
    public final String priceColumn;
    public final String purchasedColumn;

    public DatabaseOperations(Context context) {
        this.db = new DatabaseOpenHelper(context).getWritableDatabase();
        productsTable = context.getString(R.string.db_products_table);
        idColumn = "rowid";
        nameColumn = context.getString(R.string.db_products_name_column);
        priceColumn = context.getString(R.string.db_products_price_column);
        purchasedColumn = context.getString(R.string.db_products_purchased_column);
    }

    public void deleteProduct(@NonNull Integer rowid) {
        if (db.isReadOnly())
            throw new AssertionError("Cannot execute db operation on read-only db");
        db.delete(productsTable, idColumn + " = ?", new String[]{rowid.toString()});
    }

    public void updateProductPurchased(@NonNull Integer rowid, @NonNull Boolean isPurchased) {
        if (db.isReadOnly())
            throw new AssertionError("Cannot execute db operation on read-only db");
        ContentValues productValues = new ContentValues();
        productValues.put(purchasedColumn, isPurchased);
        db.update(productsTable, productValues, idColumn + " = ?" , new String[]{rowid.toString()});
    }
}
