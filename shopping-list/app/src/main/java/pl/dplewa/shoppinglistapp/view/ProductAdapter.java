package pl.dplewa.shoppinglistapp.view;

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

import java.text.NumberFormat;
import java.util.List;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.activity.EditProductActivity;
import pl.dplewa.shoppinglistapp.data.DatabaseOperations;
import pl.dplewa.shoppinglistapp.data.Product;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * @author Dominik Plewa
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public static final String FONT_SIZE_OPTION = "fontSize";

    private List<Product> products;
    private DatabaseOperations dbOps;
    private Context context;

    public ProductAdapter(Context context, List<Product> products) {
        this.products = products;
        this.context = context;
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

        updateTextSize(viewHolder.name, viewHolder.price, viewHolder.count);

        viewHolder.id = product.getId();
        viewHolder.name.setText(product.getName());
        viewHolder.price.setText(NumberFormat.getCurrencyInstance().format(product.getPrice()));
        viewHolder.count.setText(String.valueOf(product.getCount()));
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
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra(EditProductActivity.PRODUCT_ID, product.getId());
                intent.putExtra(EditProductActivity.PRODUCT_NAME, product.getName());
                intent.putExtra(EditProductActivity.PRODUCT_PRICE, product.getPrice().toPlainString());
                intent.putExtra(EditProductActivity.PRODUCT_COUNT, String.valueOf(product.getCount()));
                startActivity(context, intent, null);
            }
        });
    }

    private void updateTextSize(TextView ... views) {
        for (TextView view : views) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getFontSize());
        }
    }

    private Integer getFontSize() {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(FONT_SIZE_OPTION, 16);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        private int id;
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
