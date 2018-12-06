package pl.dplewa.shoppinglistapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.dplewa.shoppinglistapp.R;

/**
 * @author Dominik Plewa
 */
public class DatabaseOperations {

    final SQLiteDatabase db;
    private final DatabaseReference fbDatabase;
    final String productsTable;
    final String idColumn;
    public final String nameColumn;
    public final String priceColumn;
    public final String countColumn;
    final String purchasedColumn;

    public DatabaseOperations(Context context) {
        this.db = new DatabaseOpenHelper(context).getWritableDatabase();
        productsTable = context.getString(R.string.db_products_table);
        idColumn = "rowid";
        nameColumn = context.getString(R.string.db_products_name_column);
        priceColumn = context.getString(R.string.db_products_price_column);
        countColumn = context.getString(R.string.db_products_count_column);
        purchasedColumn = context.getString(R.string.db_products_purchased_column);
        fbDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void deleteProduct(@NonNull String rowid) {
        if (db.isReadOnly())
            throw new AssertionError("Cannot execute db operation on read-only db");
        db.delete(productsTable, idColumn + " = ?", new String[]{rowid});
    }

    public void updateProductPurchased(@NonNull String rowid, @NonNull Boolean isPurchased) {
        if (db.isReadOnly())
            throw new AssertionError("Cannot execute db operation on read-only db");
        ContentValues productValues = new ContentValues();
        productValues.put(purchasedColumn, isPurchased);
        db.update(productsTable, productValues, idColumn + " = ?", new String[]{rowid});
    }

    public Cursor getProducts(@Nullable String rowid) {
        String selection = null;
        String[] selectionArgs = null;
        if (rowid != null) {
            selection = idColumn + " = ?";
            selectionArgs = new String[]{rowid};
        }
        return db.query(productsTable, new String[]{idColumn, nameColumn, priceColumn, countColumn, purchasedColumn},
                selection, selectionArgs, null, null, null);
    }

    public long insertProduct(@NonNull String name, @NonNull String price, @Nullable Integer count) {
        if (db.isReadOnly())
            throw new AssertionError("Cannot execute db operation on read-only db");
        final ContentValues productValues = new ContentValues(4);
        productValues.put(nameColumn, name);
        productValues.put(priceColumn, price);
        productValues.put(countColumn, count);
        productValues.put(purchasedColumn, false);
        return db.insert(productsTable, null, productValues);
    }

    public String insertProduct(@NonNull String userId, @NonNull Product product) {
        final DatabaseReference childRef = fbDatabase.child("products").child(userId).push();
        childRef.setValue(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Log.i("FB/DPLEWA", "Saved product");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Log.e("FB/DPLEWA", "Failed to save product", e);
                    }
                });
        return childRef.getKey();
    }

    public long updateProduct(@NonNull String productId, @NonNull String name, @NonNull String price, @NonNull Integer count) {
        if (db.isReadOnly())
            throw new AssertionError("Cannot execute db operation on read-only db");
        final ContentValues productValues = new ContentValues(4);
        productValues.put(nameColumn, name);
        productValues.put(priceColumn, price);
        productValues.put(countColumn, count);
        productValues.put(purchasedColumn, false);
        return db.update(productsTable, productValues, idColumn + " = ?", new String[]{productId});
    }
}
