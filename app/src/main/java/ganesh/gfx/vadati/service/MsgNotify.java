package ganesh.gfx.vadati.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import static ganesh.gfx.vadati.service.FetchMsg.StartMonitor;
import static ganesh.gfx.vadati.service.FetchMsg.StopMonitor;
import static ganesh.gfx.vadati.utils.Constants.isServiceRunning;

public class MsgNotify extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        //StartMonitor();
        isServiceRunning = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // Let it continue running until it is stopped.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
       // StartMonitor();
        isServiceRunning = true;
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //StopMonitor();
        isServiceRunning = false;
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

        Intent intent = new Intent("ganesh.gfx.vadati");
        intent.putExtra("yourvalue", "torestore");
        sendBroadcast(intent);
    }

}
