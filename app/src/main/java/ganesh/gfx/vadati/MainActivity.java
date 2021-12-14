package ganesh.gfx.vadati;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import ganesh.gfx.vadati.data.Message;
import ganesh.gfx.vadati.data.Person;
import ganesh.gfx.vadati.data.Room.AppDatabase;
import ganesh.gfx.vadati.service.MsgNotify;
import ganesh.gfx.vadati.ui.main.SectionsPagerAdapter;
import ganesh.gfx.vadati.ui.main.settings;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

public class MainActivity extends AppCompatActivity {
    String TAG = "appgfx";
    FloatingActionButton fab;
    private static TabLayout tabs;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onBoot: Done...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constants.localDb = AppDatabase.getInstance(this.getApplicationContext());
        Constants.context = this;


        ////////////////TEST AREA/////////////////

        //////////////////////////////////////////

        String qrResult = getIntent().getStringExtra("qr_result");
//        if(qrResult.equals(null)){
//            Toast.makeText(this, qrResult, Toast.LENGTH_SHORT).show();
//        }

        fab = findViewById(R.id.fab);

        String conList = getSharedPreferences("vadati", 0).getString("conList", "NULL");
        if(conList==null){
            Constants.conArry = new ArrayList<Person>();
        }else{
            Constants.conArry.clear();
            Person[] cons = Tools.gson.fromJson(conList,Person[].class);
            if(cons != null) {
                for (Person value : cons) {
                    //Log.d(TAG, Tools.gson.toJson(value));
                    Constants.conArry.add(value);
                }
            }
        }
        //Toast.makeText(this,Tools.gson.toJson(Constants.conArry),Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,conList,Toast.LENGTH_SHORT).show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Tools.ServeTost("Login Failed...!",getApplicationContext());
            Intent myIntent = new Intent(MainActivity.this, number_inpt.class);
            MainActivity.this.startActivity(myIntent);
            finish();
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        int[] tabIcons = {
                R.drawable.ic_connect,
                R.drawable.ic_chat
        };
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override public void onPageSelected(int position) {
                if(position == 1){
                    fab.hide();
                    //Log.d(TAG, "onPageSelected: "+position);
                }else{
                    fab.show();
                    //Log.d(TAG, "onPageSelected: "+position);
                }
            }
            @Override public void onPageScrollStateChanged(int state){}
        });

        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //throw new RuntimeException("Test Crash");

                Intent setting = new Intent(MainActivity.this,settings.class);
                MainActivity.this.startActivity(setting);
            }
        });
//        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
//                new IntentIntegrator(getParent()).initiateScan();
//            }
//        });


    //Tools.verCek();
        startService();
    }
    AlertDialog  materialAlertDialogBuilder;

    public void ScanButton(View view){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
        String permission = android.Manifest.permission.CAMERA;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        if(res == PackageManager.PERMISSION_GRANTED){
            //Intent intent = new Intent(this,Scanning.class);
            //startActivity(intent);

            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.scan_options_dialog, viewGroup, false);
            //Material
            materialAlertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle("Show or ask friend to show QR code.")
                    .setView(dialogView)
                    .setNegativeButton("Dismiss", (dialog,which) -> dialog.dismiss())
                    .create();
            materialAlertDialogBuilder.show();
            //
        }else{
            Toast.makeText(this,"Please allow to use camera permission", Toast.LENGTH_LONG).show();
        }
    }
    public void startScanQR(View view){
        materialAlertDialogBuilder.dismiss();
        Intent intent = new Intent(this,Scanning.class);
        startActivity(intent);
    }
    public void startShowQR(View view){
        materialAlertDialogBuilder.dismiss();
        Intent intent = new Intent(this,show_my_qr.class);
        startActivity(intent);
    }

    public static void switchTab(int index){
        tabs.getTabAt(index).select();
    }

    @Override
    public void onResume() {
        super.onResume();
        //stopService();
        startService();
    }

    @Override
    public void onPause() {
        super.onPause();
        //startService();
    }

    public void startService(){
        if(!Constants.isServiceRunning)
        startService(new Intent(getBaseContext(), MsgNotify.class));
    }
    // Method to stop the service
    public void stopService(){
        stopService(new Intent(getBaseContext(), MsgNotify.class));
    }
}