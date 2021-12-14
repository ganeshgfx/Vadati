package ganesh.gfx.vadati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ganesh.gfx.vadati.utils.Constants;

public class Spash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MobileAds.initialize(this);
        setContentView(R.layout.activity_spash);
        ImageView imageView = (ImageView)findViewById(R.id.image_spash);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(700);
        imageView.startAnimation(animation);
        imageView.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences settings = getSharedPreferences("vadati",0);
                String myNo = settings.getString("myNo", "NULL");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {

                    Constants.myFireID = user.getUid();
                    Constants.myNum = myNo;
                    Constants.myName = settings.getString("myName", "NULL");
                    Intent myIntent = new Intent(Spash.this, MainActivity.class);
                    Spash.this.startActivity(myIntent);
                    finish();
                }else{
                    Intent myIntent = new Intent(Spash.this, number_inpt.class);
                    Spash.this.startActivity(myIntent);
                    finish();
                }
            }
        }, 700);
    }
}