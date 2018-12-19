package pl.dplewa.shoppinglistapp.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.activity.ThemedActivity;
import pl.dplewa.shoppinglistapp.view.ShopAdapter;

/**
 * @author Dominik Plewa
 */
public class ShopListActivity extends ThemedActivity {

    private RecyclerView shopsRecycler;
    private ShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop_list);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddShopActivity.class));
            }
        });

        adapter = new ShopAdapter(this, this);
        shopsRecycler = findViewById(R.id.shopListRecycler);
        shopsRecycler.setLayoutManager(new LinearLayoutManager(this));
        shopsRecycler.setAdapter(adapter);
    }
}
