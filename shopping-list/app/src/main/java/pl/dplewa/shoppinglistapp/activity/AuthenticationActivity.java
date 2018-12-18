package pl.dplewa.shoppinglistapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.util.Collections;
import java.util.List;

import pl.dplewa.shoppinglistapp.R;

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
}
