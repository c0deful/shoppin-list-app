package pl.dplewa.shoppinglistapp.activity;

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
import pl.dplewa.shoppinglistapp.view.ProductAdapter;

/**
 * @author Dominik Plewa
 */
public class MainActivity extends ThemedActivity {

    private static final int SETTINGS_REQUEST = 42;

    private RecyclerView shoppingListRecycler;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_list);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddProductActivity.class));
            }
        });

        adapter = new ProductAdapter(this);
        shoppingListRecycler = findViewById(R.id.shoppingListRecycler);
        shoppingListRecycler.setLayoutManager(new LinearLayoutManager(this));
        shoppingListRecycler.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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
            startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
