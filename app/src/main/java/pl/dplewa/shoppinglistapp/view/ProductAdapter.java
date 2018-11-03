package pl.dplewa.shoppinglistapp.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.data.DatabaseOpenHelper;
import pl.dplewa.shoppinglistapp.data.DatabaseOperations;
import pl.dplewa.shoppinglistapp.data.Product;

import static android.view.View.GONE;

/**
 * @author Dominik Plewa
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products;
    private DatabaseOperations dbOps;

    public ProductAdapter(Context context, List<Product> products) {
        this.products = products;
        dbOps = new DatabaseOperations(context);
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductAdapter.ViewHolder viewHolder, int i) {
        final Product product = products.get(i);
        viewHolder.id = product.getId();
        viewHolder.name.setText(product.getName());
        viewHolder.price.setText(NumberFormat.getCurrencyInstance().format(product.getPrice()));
        viewHolder.isPurchased.setChecked(product.isPurchased());
        viewHolder.isPurchased.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dbOps.updateProductPurchased(viewHolder.id, isChecked);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dbOps.deleteProduct(viewHolder.id);
                products.remove(viewHolder.getAdapterPosition());
                notifyItemRemoved(viewHolder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        private int id;
        private final TextView name;
        private final TextView price;
        private final CheckBox isPurchased;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productNameText);
            price = itemView.findViewById(R.id.productPriceText);
            isPurchased = itemView.findViewById(R.id.productPurchasedCheckBox);
        }
    }
}
