package pl.dplewa.shoppinglistapp.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.data.Product;

import static android.view.View.GONE;

/**
 * @author Dominik Plewa
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductAdapter.ViewHolder viewHolder, int i) {
        Product product = products.get(i);
        viewHolder.name.setText(product.getName());
        viewHolder.price.setText(NumberFormat.getCurrencyInstance().format(product.getPrice()));
        viewHolder.isPurchased.setChecked(product.isPurchased());
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                notifyItemChanged(viewHolder.getAdapterPosition());
                notifyItemRangeRemoved(viewHolder.getAdapterPosition(), 1);
                v.setVisibility(GONE);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

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
