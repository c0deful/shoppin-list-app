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
public class DatabaseOperations {

    private final DatabaseReference fbDatabase;

    public DatabaseOperations() {
        fbDatabase = FirebaseDatabase.getInstance().getReference("products");
    }

    public void deleteProduct(@NonNull String productId) {
        final DatabaseReference productRef = fbDatabase.child(getUserId()).child(productId);
        productRef.removeValue();
    }

    public void updateProductPurchased(@NonNull String productId, @NonNull Boolean isPurchased) {
        final DatabaseReference productRef = fbDatabase.child(getUserId()).child(productId);
        productRef.child("isPurchased").setValue(isPurchased);
    }

    public CompletableFuture<Product> getProduct(@NonNull String productId) {
        final CompletableFuture<Product> result = new CompletableFuture<>();
        fbDatabase.child(getUserId()).child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                result.complete(dataSnapshot.getValue(Product.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("", "Cancelled value read", databaseError.toException());
            }
        });
        return result;
    }

    public String insertProduct(@NonNull Product product) {
        final DatabaseReference productRef = fbDatabase.child(getUserId()).push();
        productRef.setValue(product);
        return productRef.getKey();
    }

    public void updateProduct(@NonNull String productId, @NonNull Product product) {
        final DatabaseReference productRef = fbDatabase.child(getUserId()).child(productId);
        productRef.setValue(product);
    }

    private String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
