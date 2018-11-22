package pl.dplewa.shoppinglistapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Dominik Plewa
 */
public class ShoppingListNewEntryReceiver extends BroadcastReceiver {

    private static final int MISSING_ENTRY_ID = -42;
    private static final String ENTRY_ID_EXTRA = "entryId";

    @Override
    public void onReceive(Context context, Intent intent) {
        NewEntryNotificationService service = ((NewEntryNotificationService.Binder) peekService(
                context, new Intent(context, NewEntryNotificationService.class))).getService();
        int entryId = intent.getIntExtra(ENTRY_ID_EXTRA, MISSING_ENTRY_ID);
        if (entryId != MISSING_ENTRY_ID) {
            service.notifyAboutNewEntry(entryId, intent.getStringExtra("name"),
                    intent.getStringExtra("price"));
        }
    }
}
