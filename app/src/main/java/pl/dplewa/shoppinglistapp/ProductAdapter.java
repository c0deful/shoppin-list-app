package pl.dplewa.shoppinglistapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products;
    private Context context;

    public ProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder viewHolder, int i) {
        Product product = products.get(i);
        viewHolder.name.setText(product.getName());
        viewHolder.isPurchased.setChecked(product.isPurchased());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private CheckBox isPurchased;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productNameText);
            isPurchased = itemView.findViewById(R.id.productPurchasedCheckBox);
        }
    }
}
