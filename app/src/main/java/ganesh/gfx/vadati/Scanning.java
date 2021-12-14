package ganesh.gfx.vadati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ganesh.gfx.vadati.data.Person;
import ganesh.gfx.vadati.ui.main.settings;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

import static ganesh.gfx.vadati.data.FireStore.MakeMeConInHisCon;
import static ganesh.gfx.vadati.utils.Constants.db;

public class Scanning extends AppCompatActivity {
    String TAG = "appgfx";
    Context context;
    private CodeScanner mCodeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Test Area
        //scanCon();
        //MakeMeConInHisCon("ths is thmp ed");
        //

        context = getApplicationContext();
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                Person scannedPerson = Tools.gson.fromJson(result.getText(),Person.class);

                SharedPreferences settings = getApplicationContext().getSharedPreferences("vadati", 0);
                SharedPreferences.Editor editor = settings.edit();

                scannedPerson.id = Constants.myFireID+scannedPerson.fireID;
                Log.d(TAG, "onDecoded: "+result.getText());

                boolean found = false;
                for (Person value: Constants.conArry) {
                    if(value.number.equals(scannedPerson.number)){
                       found = true;
                        break;
                    }
                }
                //MakeMeConInHisCon(scannedPerson.fireID);
                if(found){
                    Log.d(TAG, "Dublicate found");
                    goBackToHome();
                }else {

                    Constants.conArry.add(scannedPerson);
                    //Constants.conArry.clear(); //FIXME: 05/12/2020
                    //
                    editor.putString("conList", Tools.gson.toJson(Constants.conArry));
                    editor.apply();

                    MakeMeConInHisCon(scannedPerson.fireID);
                    //#OLD CODE Rdb
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    DatabaseReference myRef = database.getReference("vadati");
//                    myRef.child(scannedPerson.fireID).child("Scanner").child("ScannerID").setValue(Constants.myFireID);

                    goBackToHome();
                }

            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    void goBackToHome(){
        Intent back = new Intent(Scanning.this, MainActivity.class);
        Scanning.this.startActivity(back);
        finish();
    }
}