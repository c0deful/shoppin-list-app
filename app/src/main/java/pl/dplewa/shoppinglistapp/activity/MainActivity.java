package pl.dplewa.shoppinglistapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.data.Product;
import pl.dplewa.shoppinglistapp.data.ProductLoader;
import pl.dplewa.shoppinglistapp.view.ProductAdapter;

/**
 * @author Dominik Plewa
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddProductActivity.class));
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        startLoadProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startLoadProducts() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new ProductLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        RecyclerView shoppingListRecycler = findViewById(R.id.shoppingListRecycler);
        shoppingListRecycler.setLayoutManager(new LinearLayoutManager(this));
        List<Product> products = new ArrayList<>(cursor.getCount());
        try {
            while (cursor.moveToNext()) {
                products.add(new Product(cursor.getString(0),
                        new BigDecimal(cursor.getDouble(1)),
                        cursor.getInt(2) != 0));
            }
        } finally {
            cursor.close();
        }
        shoppingListRecycler.setAdapter(new ProductAdapter(products));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // nothing to do here
    }
}
