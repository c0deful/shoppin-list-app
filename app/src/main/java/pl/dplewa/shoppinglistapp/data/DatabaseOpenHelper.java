package pl.dplewa.shoppinglistapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import pl.dplewa.shoppinglistapp.R;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    final String productsTable;
    final String nameColumn;
    final String priceColumn;
    final String countColumn;
    final String purchasedColumn;

    DatabaseOpenHelper(@NonNull Context context) {
        super(context, context.getString(R.string.db_name), null, DATABASE_VERSION);
        productsTable = context.getString(R.string.db_products_table);
        nameColumn = context.getString(R.string.db_products_name_column);
        priceColumn = context.getString(R.string.db_products_price_column);
        countColumn = context.getString(R.string.db_products_count_column);
        purchasedColumn = context.getString(R.string.db_products_purchased_column);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + productsTable + " ("
                + nameColumn + " TEXT NOT NULL, "
                + priceColumn + " TEXT NOT NULL, "
                + countColumn + " INTEGER NOT NULL CHECK(" + countColumn + " > 0) DEFAULT 1, "
                + purchasedColumn + " INTEGER NOT NULL CHECK(" + purchasedColumn + " IN (0,1)));");
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + productsTable
                + " ADD " + countColumn +  " INTEGER NOT NULL CHECK(" + countColumn + " > 0) DEFAULT 1;");
        }
    }
}

