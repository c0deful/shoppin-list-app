package pl.dplewa.shoppinglistapp.activity.shop;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;
import java.util.Map;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.data.Shop;
import pl.dplewa.shoppinglistapp.data.ShopDatabase;

public class ShopMapActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String SELECTED_SHOP_LATITUDE = "selectLat";
    public static final String SELECTED_SHOP_LONGTITUDE = "selectLong";

    private ShopDatabase shopDb;
    private ChildEventListener shopEventListener;
    private GoogleMap mMap;
    private Map<String, Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        shopDb = new ShopDatabase();
        markers = new HashMap<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        shopEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                Shop shop = dataSnapshot.getValue(Shop.class);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(shop.latitude, shop.longtitude))
                        .title(shop.name));
                markers.put(key, marker);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                Shop shop = dataSnapshot.getValue(Shop.class);
                Marker marker = markers.get(key);
                if (marker != null) {
                    marker.setTitle(shop.name);
                    marker.setPosition(new LatLng(shop.latitude, shop.longtitude));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                Marker marker = markers.remove(key);
                if (marker != null) {
                    marker.remove();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // nothing to do
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // nothing to do
            }
        };

        shopDb.getDb().addChildEventListener(shopEventListener);

        Bundle extras = getIntent().getExtras();
        if (extras == null
                || !extras.containsKey(SELECTED_SHOP_LATITUDE)
                || !extras.containsKey(SELECTED_SHOP_LONGTITUDE)) {
            finish();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
                extras.getDouble(SELECTED_SHOP_LATITUDE), extras.getDouble(SELECTED_SHOP_LONGTITUDE))));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shopDb.getDb().removeEventListener(shopEventListener);
    }
}
