package pl.dplewa.shoppinglistapp.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Collections;
import java.util.List;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.activity.product.ProductListActivity;
import pl.dplewa.shoppinglistapp.activity.shop.ShopTransitionsIntentService;
import pl.dplewa.shoppinglistapp.data.Shop;
import pl.dplewa.shoppinglistapp.data.ShopDatabase;

/**
 * @author Dominik Plewa
 */
public class AuthenticationActivity extends ThemedActivity {

    private static final int RC_SIGN_IN = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }

    public void startSignIn(View view) {
        final List<AuthUI.IdpConfig> authProviders = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(authProviders)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                new ShopDatabase().getDb()
                        .addChildEventListener(new ShopToGeofenceListener(this));
                // Successfully signed in
                Intent shoppingListIntent = new Intent(this, ProductListActivity.class);
                shoppingListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(shoppingListIntent);
            } else if (response != null) {
                // an error occured
                Toast.makeText(this, "Authentication failed, try again", Toast.LENGTH_LONG).show();
            } else {
                // TODO probably nothing? user pressed back button
            }
        }
    }

    private static final class ShopToGeofenceListener implements ChildEventListener {

        private static final String TAG = ShopToGeofenceListener.class.getSimpleName();

        private GeofencingClient mGeofencingClient;
        private PendingIntent mGeofencePendingIntent;

        private ShopToGeofenceListener(Context context) {
            this.mGeofencingClient = LocationServices.getGeofencingClient(context);
            this.mGeofencePendingIntent = PendingIntent.getService(context, 0,
                    new Intent(context, ShopTransitionsIntentService.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            final String id = dataSnapshot.getKey();
            final Shop shop = dataSnapshot.getValue(Shop.class);
            addGeofence(id, shop);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            final String id = dataSnapshot.getKey();
            final Shop shop = dataSnapshot.getValue(Shop.class);
            mGeofencingClient.removeGeofences(Collections.singletonList(id))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            addGeofence(id, shop);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Failed to remove geofence", e);
                        }
                    });
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            final String id = dataSnapshot.getKey();
            mGeofencingClient.removeGeofences(Collections.singletonList(id));
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            // do nothing
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Shop event listener cancelled", databaseError.toException());
        }


        private void addGeofence(String id, Shop shop) {
            mGeofencingClient.addGeofences(
                    new GeofencingRequest.Builder()
                            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                            .addGeofence(new Geofence.Builder()
                                    .setRequestId(id)
                                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                                    .setCircularRegion(shop.latitude, shop.longtitude, shop.radius.floatValue())
                                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                    .build()).build(),
                    mGeofencePendingIntent)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Failed to add geofence", e);
                        }
                    });
        }
    }
}
