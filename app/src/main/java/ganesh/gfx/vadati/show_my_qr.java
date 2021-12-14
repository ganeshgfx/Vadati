package ganesh.gfx.vadati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ganesh.gfx.vadati.data.FireStore;
import ganesh.gfx.vadati.data.Person;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

import static ganesh.gfx.vadati.utils.Constants.TAG;
import static ganesh.gfx.vadati.utils.Constants.db;

public class show_my_qr extends AppCompatActivity {

    String TAG = "appgfx";

    @Override
    public void onBackPressed() {
        //Log.d(TAG, "onBackPressed: ENDED");
        removeListenerRegistration();
        super.onBackPressed();
        this.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_qr);

        //TEST AREA
        //addCon();
        readCon();
        //

        ImageView imageView = (ImageView)findViewById(R.id.qr_code);

        Person me = new Person();
        me.name=Constants.myName;
        me.number=Constants.myNum;
        me.fireID = Constants.myFireID;
        Log.d(TAG, ""+Tools.gson.toJson(me));

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(Tools.gson.toJson(me), BarcodeFormat.QR_CODE, 269, 269);
            Bitmap bitmap = Bitmap.createBitmap(269, 269, Bitmap.Config.RGB_565);
            for (int x = 0; x<269; x++){
                for (int y=0; y<269; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ListenConnect();
        //MakeMeConInHisCon();
    }
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("vadati");
    void makeCon(Person fromWeb){
        removeListenerRegistration();

        SharedPreferences settings = getApplicationContext().getSharedPreferences("vadati", 0);
        SharedPreferences.Editor editor = settings.edit();

        Constants.conArry.add(fromWeb);
        //id+Tools.MD5(Constants.myNum)
        editor.putString("conList",Tools.gson.toJson(Constants.conArry));
        editor.apply();

        Intent myIntent = new Intent(show_my_qr.this, MainActivity.class);
        show_my_qr.this.startActivity(myIntent);
        finish();
        //Log.d(TAG, "makeCon: "+id);
    }
    void readCon() {
        // Create a reference to the cities collection
        CollectionReference root = db.collection("makeCon");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("hisId","");
        data1.put("added",false);
        data1.put("name","");
        data1.put("number","");

        root.document(Constants.myFireID)
                .set(data1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        ///Rwiting DATA ////
                        DocumentReference docRef = db.collection("makeCon").document(Constants.myFireID);
                        registration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e);
                                    return;
                                }
                                if (snapshot != null && snapshot.exists()) {
                                    if(Boolean.parseBoolean(snapshot.getData().get("added").toString())){

                                        Log.d(TAG, "onEvent: coppy his data");
                                        Log.d(TAG, "added :"+Boolean.parseBoolean(snapshot.getData().get("added").toString()));
                                        Log.d(TAG, "name :"+snapshot.getData().get("name").toString());
                                        Log.d(TAG, "number :"+snapshot.getData().get("number").toString());
                                        Log.d(TAG, "hisId :"+snapshot.getData().get("hisId").toString());

                                        Person webWala = new Person(
                                                snapshot.getData().get("name").toString(),
                                                Tools.MD5(snapshot.getData().get("number").toString()),
                                                snapshot.getData().get("number").toString(),
                                                snapshot.getData().get("hisId").toString()
                                        );
                                        makeCon(webWala);
                                    }
                                } else {
                                    Log.d(TAG, "Current data: null");
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing MY info to show scanner", e);
                    }
                });;
        /////////////////////////////////
    }
    ListenerRegistration registration;
    void removeListenerRegistration(){
        registration.remove();
    }

}
//qr{Mnumber+Mkey}
//db{HqrKey(Mnum)+Hkey}
/*
{
    user1:{
        cons:[user7,user2,user3]
    }
    user2:{
        cons:[user1,user2,user3]
    }
}
*/