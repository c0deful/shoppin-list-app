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

    private static final String SHOPS_NODE = "shops";

    private final DatabaseReference fbDatabase;

    public ShopDatabase() {
        fbDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private DatabaseReference getDb() {
        return fbDatabase.child(getUserId()).child(SHOPS_NODE);
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
