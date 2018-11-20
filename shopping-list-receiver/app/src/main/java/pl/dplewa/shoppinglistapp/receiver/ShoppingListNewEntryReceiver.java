package pl.dplewa.shoppinglistapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Dominik Plewa
 */
public class ShoppingListNewEntryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NewEntryNotificationService service = ((NewEntryNotificationService.Binder) peekService(
                context, new Intent(context, NewEntryNotificationService.class))).getService();
        service.notifyAboutNewEntry(intent.getLongExtra("entryId", -1L));
    }
}
