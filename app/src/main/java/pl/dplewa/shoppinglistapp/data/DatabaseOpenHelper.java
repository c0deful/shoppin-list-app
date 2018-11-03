package pl.dplewa.shoppinglistapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.dplewa.shoppinglistapp.R;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private Context context;

    public DatabaseOpenHelper(Context context) {
        super(context, context.getString(R.string.db_name), null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String productsTable = context.getString(R.string.db_products_table);
        final String nameColumn = context.getString(R.string.db_products_name_column);
        final String priceColumn = context.getString(R.string.db_products_price_column);
        final String purchasedColumn = context.getString(R.string.db_products_purchased_column);
        db.execSQL("CREATE TABLE " + productsTable + " ("
                + nameColumn + " TEXT NOT NULL, "
                + priceColumn + " TEXT NOT NULL, "
                + purchasedColumn + " INTEGER NOT NULL CHECK(" + purchasedColumn + " IN (0,1)));");
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO implement actual migrations if the schema ever changes
        final String productsTable = context.getString(R.string.db_products_table);
        db.delete(productsTable, null, null);
    }
}

