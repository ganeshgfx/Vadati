package ganesh.gfx.vadati.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NoticeService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,MsgNotify.class));
    }
}
