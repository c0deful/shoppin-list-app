package pl.dplewa.shoppinglistapp.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * @author Dominik Plewa
 */
public class ShoppingListContentProvider extends ContentProvider {

    private static final String AUTHORITY = "pl.dplewa.shoppinglistapp.data";
    private static final String BASE_PATH = "products";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/products";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/product";

    private static final int NO_ID = 11;
    private static final int WITH_ID = 79;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NO_ID);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", WITH_ID);
    }

    private DatabaseOperations dbOps;

    @Override
    public boolean onCreate() {
        dbOps = new DatabaseOperations(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(dbOps.productsTable);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case NO_ID:
                break;
            case WITH_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(dbOps.idColumn + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }


        Cursor cursor = queryBuilder.query(dbOps.db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case NO_ID:
                Long rowid = dbOps.db.insert(dbOps.productsTable, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return (rowid != -1) ? Uri.parse(BASE_PATH + "/" + rowid) : null;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case NO_ID:
                rowsDeleted = dbOps.db.delete(dbOps.productsTable, selection,
                        selectionArgs);
                break;
            case WITH_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = dbOps.db.delete(dbOps.productsTable,dbOps.idColumn + "=" + id, null);
                } else {
                    rowsDeleted = dbOps.db.delete(dbOps.productsTable,
                            dbOps.idColumn + "=" + id + " AND " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsUpdated = 0;
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case NO_ID:
                rowsUpdated = dbOps.db.update(dbOps.productsTable, values, selection,
                        selectionArgs);
                break;
            case WITH_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = dbOps.db.update(dbOps.productsTable, values,
                            dbOps.idColumn + "=" + id, null);
                } else {
                    rowsUpdated = dbOps.db.update(dbOps.productsTable, values,
                            dbOps.idColumn + "=" + id + " AND " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
