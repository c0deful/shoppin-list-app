package pl.dplewa.shoppinglistapp.activity.shop;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.activity.ThemedActivity;
import pl.dplewa.shoppinglistapp.data.Shop;
import pl.dplewa.shoppinglistapp.data.ShopDatabase;

/**
 * @author Dominik Plewa
 */
public class AddShopActivity extends ThemedActivity {

    private static final int PERMISSION_REQUEST_CODE = 11123;

    private ShopDatabase shopDb;
    private FusedLocationProviderClient locationProviderClient;

    private EditText nameField;
    private EditText descriptionField;
    private EditText radiusField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_form);
        nameField = findViewById(R.id.shopFormName);
        descriptionField = findViewById(R.id.shopFormDescription);
        radiusField = findViewById(R.id.shopFormRadius);
        shopDb = new ShopDatabase();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private boolean validate() {
        boolean valid = true;
        if (nameField.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.shopFormNameValidateToast, Toast.LENGTH_LONG).show();
            valid = false;
        } else if (descriptionField.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.shopFormDescriptionValidateToast, Toast.LENGTH_LONG).show();
            valid = false;
        } else if (radiusField.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.shopFormRadiusValidateToast, Toast.LENGTH_LONG).show();
            valid = false;
        }
        return valid;
    }

    public void save(final View view) {
        if (!validate()) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, PERMISSION_REQUEST_CODE);
            return;
        }

        locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    Toast.makeText(AddShopActivity.this, R.string.shopFormEnableGps, Toast.LENGTH_SHORT).show();
                    // reload location provider in case location service was restarted
                    locationProviderClient = LocationServices.getFusedLocationProviderClient(AddShopActivity.this);
                } else {
                    saveInternal(location);
                    finish();
                }
            }
        });
    }

    private void saveInternal(@NonNull Location location) {
        try {
            Shop shop = new Shop();
            shop.name = nameField.getText().toString();
            shop.description = descriptionField.getText().toString();
            shop.radius = Double.parseDouble(radiusField.getText().toString());
            shop.longtitude = location.getLongitude();
            shop.latitude = location.getLatitude();
            shopDb.insert(shop);
        } catch (SecurityException e) {
            throw new IllegalStateException("Tried getting location without checking permission first", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        boolean locationPermit = false;
        for (int i = 0; i < permissions.length; i++) {
            locationPermit = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            if (locationPermit) {
                break;
            }
        }

        if (locationPermit) {
            save(findViewById(android.R.id.content));
        } else {
            Toast.makeText(this, R.string.shopFormLocationPermissionRequired, Toast.LENGTH_LONG).show();
        }
    }
}