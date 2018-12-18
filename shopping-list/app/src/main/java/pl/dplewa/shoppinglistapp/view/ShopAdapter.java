package pl.dplewa.shoppinglistapp.view;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.NumberFormat;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.activity.EditProductActivity;
import pl.dplewa.shoppinglistapp.data.Product;
import pl.dplewa.shoppinglistapp.data.ProductDatabase;
import pl.dplewa.shoppinglistapp.data.Shop;
import pl.dplewa.shoppinglistapp.data.ShopDatabase;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * @author Dominik Plewa
 */
public class ShopAdapter extends FirebaseRecyclerAdapter<Shop, ShopAdapter.ViewHolder> {

    private ShopDatabase shopDb;
    private Context context;

    public ShopAdapter(Context context, LifecycleOwner lifecycleOwner) {
        super(new FirebaseRecyclerOptions.Builder<Shop>()
                .setQuery(getQuery(), Shop.class)
                .setLifecycleOwner(lifecycleOwner)
                .build());
        this.context = context;
        this.shopDb = new ShopDatabase();
    }

    private static Query getQuery() {
        return FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("shops");
    }

    @NonNull
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shop, viewGroup, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position, @NonNull final Shop shop) {
        viewHolder.id = getRef(position).getKey();
        viewHolder.name.setText(shop.name);
        viewHolder.description.setText(shop.description);
        viewHolder.latitude.setText(shop.latitude.toString());
        viewHolder.longtitude.setText(shop.longtitude.toString());
        viewHolder.radius.setText(shop.radius.toString());
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        private String id;
        private final TextView name;
        private final TextView description;
        private final TextView latitude;
        private final TextView longtitude;
        private final TextView radius;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shopNameText);
            description = itemView.findViewById(R.id.shopDescriptionText);
            latitude = itemView.findViewById(R.id.shopLatitudeText);
            longtitude = itemView.findViewById(R.id.shopLongtitudeText);
            radius = itemView.findViewById(R.id.shopRadiusText);
        }
    }
}
