package pl.dplewa.shoppinglistapp;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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

import pl.dplewa.shoppinglistapp.activity.shop.ShopTransitionsIntentService;
import pl.dplewa.shoppinglistapp.data.Shop;
import pl.dplewa.shoppinglistapp.data.ShopDatabase;

/**
 * @author Dominik Plewa
 */
public class ShoppingListApp extends Application {

    private static final String TAG = ShoppingListApp.class.getSimpleName();

    private ShopDatabase mShopDb;
    private GeofencingClient mGeofencingClient;
    private PendingIntent mGeofencePendingIntent;
    private ChildEventListener mShopListener;

    @Override
    public void onCreate() {
        super.onCreate();

        this.mShopDb = new ShopDatabase();
        this.mGeofencingClient = LocationServices.getGeofencingClient(this);
        this.mShopListener = new ShopToGeofenceListener();
        this.mGeofencePendingIntent = PendingIntent.getService(this, 0,
                new Intent(this, ShopTransitionsIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        startService(new Intent(this, ShopTransitionsIntentService.class));

        mShopDb.getDb().addChildEventListener(mShopListener);
    }

    private class ShopToGeofenceListener implements ChildEventListener {

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
