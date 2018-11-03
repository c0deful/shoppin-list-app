package pl.dplewa.shoppinglistapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pl.dplewa.shoppinglistapp.R;

/**
 * @author Dominik Plewa
 */
public class DatabaseOperations {

    private final SQLiteDatabase db;
    final String productsTable;
    final String idColumn;
    final String nameColumn;
    final String priceColumn;
    final String countColumn;
    final String purchasedColumn;

    public DatabaseOperations(Context context) {
        this.db = new DatabaseOpenHelper(context).getWritableDatabase();
        productsTable = context.getString(R.string.db_products_table);
        idColumn = "rowid";
        nameColumn = context.getString(R.string.db_products_name_column);
        priceColumn = context.getString(R.string.db_products_price_column);
        countColumn = context.getString(R.string.db_products_count_column);
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
        db.update(productsTable, productValues, idColumn + " = ?", new String[]{rowid.toString()});
    }

    Cursor getAllProducts() {
        return db.query(productsTable, new String[]{idColumn, nameColumn, priceColumn, countColumn, purchasedColumn},
                null, null, null, null, null);
    }

    public void insertProduct(@NonNull String name, @NonNull String price, @Nullable Integer count) {
        if (db.isReadOnly())
            throw new AssertionError("Cannot execute db operation on read-only db");
        final ContentValues productValues = new ContentValues(4);
        productValues.put(nameColumn, name);
        productValues.put(priceColumn, price);
        productValues.put(countColumn, count);
        productValues.put(purchasedColumn, false);
        db.insert(productsTable, null, productValues);
    }

    public void updateProduct(@NonNull Integer productId, @NonNull String name, @NonNull String price, @NonNull Integer count) {
        if (db.isReadOnly())
            throw new AssertionError("Cannot execute db operation on read-only db");
        final ContentValues productValues = new ContentValues(4);
        productValues.put(nameColumn, name);
        productValues.put(priceColumn, price);
        productValues.put(countColumn, count);
        productValues.put(purchasedColumn, false);
        db.update(productsTable, productValues, idColumn + " = ?", new String[]{String.valueOf(productId)});
    }
}
