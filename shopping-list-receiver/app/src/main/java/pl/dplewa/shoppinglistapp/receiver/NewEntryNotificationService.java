package pl.dplewa.shoppinglistapp.receiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NewEntryNotificationService extends Service {
    public NewEntryNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
