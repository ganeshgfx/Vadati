package ganesh.gfx.vadati;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import java.util.ArrayList;

import ganesh.gfx.vadati.data.Person;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

public class AppGfx extends Application {
    public static final String CHANEL_1_ID = "Messages";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            String conList = getSharedPreferences("vadati", 0).getString("conList", "NULL");
            if(conList!=null){
                Person[] cons = Tools.gson.fromJson(conList,Person[].class);
                if(cons != null) {
                    for (Person value : cons) {
                        NotificationChannel channel1 = new NotificationChannel(
                                value.fireID,
                                value.name,
                                NotificationManager.IMPORTANCE_HIGH
                        );
                        channel1.setDescription("Messages by "+value.name);
                        NotificationManager manager = getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(channel1);
                    }
                }
            }
        }
    }
}
