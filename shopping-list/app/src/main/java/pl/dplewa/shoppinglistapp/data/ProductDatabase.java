package pl.dplewa.shoppinglistapp.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CompletableFuture;

/**
 * @author Dominik Plewa
 */
public class ProductDatabase {

    private static final String TAG = "PDB";
    private static final String PRODUCTS_NODE = "products";

    private final DatabaseReference fbDatabase;

    public ProductDatabase() {
        fbDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private DatabaseReference getDb() {
        return fbDatabase.child(getUserId()).child(PRODUCTS_NODE);
    }

    private DatabaseReference getDb(String id) {
        return fbDatabase.child(getUserId()).child(PRODUCTS_NODE).child(id);
    }


    public void deleteProduct(@NonNull String productId) {
        final DatabaseReference productRef = getDb(productId);
        productRef.removeValue();
    }

    public void updateProductPurchased(@NonNull String productId, @NonNull Boolean isPurchased) {
        final DatabaseReference productRef = getDb(productId);
        productRef.child("isPurchased").setValue(isPurchased);
    }

    public CompletableFuture<Product> getProduct(@NonNull String productId) {
        final CompletableFuture<Product> result = new CompletableFuture<>();
        getDb(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                result.complete(dataSnapshot.getValue(Product.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Cancelled value read", databaseError.toException());
            }
        });
        return result;
    }

    public String insert(@NonNull Product product) {
        final DatabaseReference productRef = getDb().push();
        productRef.setValue(product);
        return productRef.getKey();
    }

    public void updateProduct(@NonNull String productId, @NonNull Product product) {
        final DatabaseReference productRef = getDb(productId);
        productRef.setValue(product);
    }

    private String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
