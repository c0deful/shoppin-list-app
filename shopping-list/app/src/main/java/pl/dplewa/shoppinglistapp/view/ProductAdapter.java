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
import pl.dplewa.shoppinglistapp.activity.product.EditProductActivity;
import pl.dplewa.shoppinglistapp.data.ProductDatabase;
import pl.dplewa.shoppinglistapp.data.Product;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * @author Dominik Plewa
 */
public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.ViewHolder> {

    public static final String FONT_SIZE_OPTION = "fontSize";

    private ProductDatabase productDb;
    private Context context;

    public ProductAdapter(Context context, LifecycleOwner lifecycleOwner) {
        super(new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(getQuery(), Product.class)
                .setLifecycleOwner(lifecycleOwner)
                .build());
        this.context = context;
        productDb = new ProductDatabase();
    }

    private static Query getQuery() {
        return FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("products");
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product, viewGroup, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position, @NonNull final Product product) {
        updateTextSize(viewHolder.name, viewHolder.price, viewHolder.count);

        viewHolder.id = getRef(position).getKey();
        viewHolder.name.setText(product.name);
        viewHolder.price.setText(NumberFormat.getCurrencyInstance().format(product.price));
        viewHolder.count.setText(String.valueOf(product.count));
        viewHolder.isPurchased.setChecked(product.isPurchased);
        viewHolder.isPurchased.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                productDb.updateProductPurchased(viewHolder.id, isChecked);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                productDb.deleteProduct(viewHolder.id);
                notifyItemRemoved(viewHolder.getAdapterPosition());
                return true;
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra(EditProductActivity.PRODUCT_ID, viewHolder.id);
                startActivity(context, intent, null);
            }
        });
    }

    private void updateTextSize(TextView... views) {
        for (TextView view : views) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getFontSize());
        }
    }

    private Integer getFontSize() {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(FONT_SIZE_OPTION, 16);
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        private String id;
        private final TextView name;
        private final TextView price;
        private final TextView count;
        private final CheckBox isPurchased;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productNameText);
            price = itemView.findViewById(R.id.productPriceText);
            count = itemView.findViewById(R.id.productCountText);
            isPurchased = itemView.findViewById(R.id.productPurchasedCheckBox);
        }
    }
}
