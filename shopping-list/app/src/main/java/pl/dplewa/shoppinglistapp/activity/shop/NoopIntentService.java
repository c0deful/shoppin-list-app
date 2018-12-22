package pl.dplewa.shoppinglistapp.activity.shop;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * @author Dominik Plewa
 */
public class NoopIntentService extends IntentService {

    public NoopIntentService() {
        super(NoopIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // noop
    }
}
