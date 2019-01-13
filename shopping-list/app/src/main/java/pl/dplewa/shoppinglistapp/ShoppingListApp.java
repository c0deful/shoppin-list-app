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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Collections;

import pl.dplewa.shoppinglistapp.activity.shop.NoopIntentService;
import pl.dplewa.shoppinglistapp.activity.shop.ShopTransitionsIntentService;
import pl.dplewa.shoppinglistapp.data.Shop;
import pl.dplewa.shoppinglistapp.data.ShopDatabase;

/**
 * @author Dominik Plewa
 */
public class ShoppingListApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocationRequest req = LocationRequest.create();
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        req.setInterval(5000);
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(req,
                PendingIntent.getService(this, 0, new Intent(this, NoopIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT));

//        startService(new Intent(this, ShopTransitionsIntentService.class));
    }
}
