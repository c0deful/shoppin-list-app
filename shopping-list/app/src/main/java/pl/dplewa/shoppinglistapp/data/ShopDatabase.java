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
public class ShopDatabase {
    private static final String TAG = ShopDatabase.class.getSimpleName();
    private static final String SHOPS_NODE = "shops";

    private final DatabaseReference fbDatabase;

    public ShopDatabase() {
        fbDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getDb() {
        return fbDatabase.child(getUserId()).child(SHOPS_NODE);
    }

    private DatabaseReference getDb(String id) {
        return fbDatabase.child(getUserId()).child(SHOPS_NODE).child(id);
    }


    public CompletableFuture<Shop> getShop(@NonNull String shopId) {
        final CompletableFuture<Shop> result = new CompletableFuture<>();
        getDb(shopId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                result.complete(dataSnapshot.getValue(Shop.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Cancelled value read", databaseError.toException());
            }
        });
        return result;
    }


    public String insert(@NonNull Shop shop) {
        final DatabaseReference shopRef = getDb().push();
        shopRef.setValue(shop);
        return shopRef.getKey();
    }

    private String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
