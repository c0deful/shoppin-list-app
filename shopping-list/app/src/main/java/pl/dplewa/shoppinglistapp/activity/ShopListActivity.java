package pl.dplewa.shoppinglistapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import pl.dplewa.shoppinglistapp.R;
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

        adapter = new ShopAdapter(this, this);
        shopsRecycler = findViewById(R.id.shopListRecycler);
        shopsRecycler.setLayoutManager(new LinearLayoutManager(this));
        shopsRecycler.setAdapter(adapter);
    }
}
