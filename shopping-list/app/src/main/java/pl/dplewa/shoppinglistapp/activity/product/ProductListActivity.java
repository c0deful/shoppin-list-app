package pl.dplewa.shoppinglistapp.activity.product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.activity.SettingsActivity;
import pl.dplewa.shoppinglistapp.activity.shop.ShopListActivity;
import pl.dplewa.shoppinglistapp.activity.ThemedActivity;
import pl.dplewa.shoppinglistapp.view.ProductAdapter;

/**
 * @author Dominik Plewa
 */
public class ProductListActivity extends ThemedActivity {

    private static final int SETTINGS_REQUEST = 42;

    private RecyclerView productListRecycler;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddProductActivity.class));
            }
        });

        adapter = new ProductAdapter(this, this);
        productListRecycler = findViewById(R.id.productListRecycler);
        productListRecycler.setLayoutManager(new LinearLayoutManager(this));
        productListRecycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_REQUEST);
                return true;
            case R.id.action_shops:
                startActivity(new Intent(this, ShopListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != SETTINGS_REQUEST) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (resultCode == Activity.RESULT_OK) {
            recreate();
        }
    }
}
