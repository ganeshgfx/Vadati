package ganesh.gfx.vadati.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ganesh.gfx.vadati.R;
import ganesh.gfx.vadati.data.Message;
import ganesh.gfx.vadati.data.Room.AppDatabase;
import ganesh.gfx.vadati.data.Room.LocalMessage;
import ganesh.gfx.vadati.data.Room.MyLocalMessage;
import ganesh.gfx.vadati.data.locker.Chabi;
import ganesh.gfx.vadati.data.locker.Crypt;
import ganesh.gfx.vadati.number_inpt;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

import static ganesh.gfx.vadati.AppGfx.CHANEL_1_ID;
import static ganesh.gfx.vadati.utils.Constants.TAG;
import static ganesh.gfx.vadati.utils.Constants.db;

import javax.crypto.SealedObject;

/*

*/


public class settings extends AppCompatActivity {
    TextView my_info;
    Button linkInsta;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        my_info = findViewById(R.id.my_info);
        my_info.setText(Constants.myName);

        ////////////////TEST AREA/////////////////

        ///Enci test
//        Crypt crypt = new Crypt(new Chabi());
//        String msg = "1234567890 ";
//        try {
//            byte[] e = crypt.code(msg);
//            Log.d(TAG, "MSG = "+msg);
//            Log.d(TAG, "E : "+new String(e,"UTF8"));
//            for (byte b:e) {Log.d(TAG, "Byte : "+b); }
//            Log.d(TAG, "D   = "+crypt.deCode(e));
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
        ///

        //Share PK
//
//        Map<String, Object> key = new HashMap<>();
//        key.put("key",Tools.encode((Chabi.getReciverPublicKey(getApplicationContext(),Constants.CurrentChat)).getEncoded()));
//        Log.d(TAG, "Local K: "+Tools.encode((Chabi.getReciverPublicKey(getApplicationContext(),Constants.CurrentChat)).getEncoded()));
//        db
//                .collection("keys")
//                .document(Constants.myFireID)
//                .collection("keyChain")
//                .document(Constants.CurrentChat)
//                .set(key);
//
//        db
//                .collection("keys")
//                .document(Constants.myFireID)
//                .collection("keyChain")
//                .document(Constants.CurrentChat)
//                .get()
//                .addOnCompleteListener(task ->{
//                    if(task.isSuccessful()){
//                        DocumentSnapshot doc = task.getResult();
//
//                        //Log.d(TAG, "GET K: "+doc.get("key",String.class));
//
//                        if(doc.exists()){
//                            //Log.d(TAG, "GET K: "+doc.get("key",String.class));
//
//                            PublicKey pk = Chabi.getKey(doc.get("key",String.class));
//                            //Log.d(TAG, "Network K: "+Tools.encode(pk.getEncoded()));
//
//
//                        }else{
//                            Log.d(TAG, "Key not found");
//                        }
//                    }
//                });

        //Constants.unReadMsgPile.print();

//        for (int i=0;i<5;i++){
//            NotificationManagerCompat managerCompat;
//            managerCompat = NotificationManagerCompat.from(this);
//            Notification notification = new NotificationCompat.Builder(this,CHANEL_1_ID)
//                    .setSmallIcon(R.drawable.ic_untitled)
//                    .setContentTitle("TTL"+Tools.genRandom(0,999))
//                    .setContentText("YOU GOT MAIL"+Tools.genRandom(0,999))
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                    .build();
//            managerCompat.notify(i,notification);
//        }
        //////////////////////////////////////////

        findViewById(R.id.loadAD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        linkInsta = findViewById(R.id.instaView);
        //linkInsta.setMovementMethod(LinkMovementMethod.getInstance());
        linkInsta.setOnClickListener(v->{
            Uri uri = Uri.parse("http://www.instagram.com/ganesh.gfx"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        //signOut();

       // loadAd();
    }
    private void loadAd(){

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
    private void signOut(){
        findViewById(R.id.settings_signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(settings.this, "Please Wait...", Toast.LENGTH_SHORT).show();
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                SharedPreferences settings = getApplicationContext().getSharedPreferences("vadati", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("myNo");
                editor.clear();
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(settings.this, number_inpt.class);
                settings.this.startActivity(myIntent);
                finish();
            }
        });
    }
}