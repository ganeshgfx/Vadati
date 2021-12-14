package ganesh.gfx.vadati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

public class number_inpt extends AppCompatActivity {

    TextInputLayout myNameSrt,myNoSrt;
    String nameError ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_inpt);

        myNameSrt = (TextInputLayout)findViewById(R.id.MyName);
        myNoSrt = (TextInputLayout)findViewById(R.id.MyNo);

        myNameSrt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!validateName(myNameSrt.getEditText().getText().toString())){
                    myNameSrt.setError(nameError);
                }else{
                    myNameSrt.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        myNoSrt.getEditText().addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        findViewById(R.id.okMyNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_name = myNameSrt.getEditText().getText().toString();
                //Log.d(Constants.TAG, "MY NAME"+str_name);
                String str_no = myNoSrt.getEditText().getText().toString();

                boolean isNumberGood = android.util.Patterns.PHONE.matcher(str_no).matches();

//                TextInputLayout textInputLayout = findViewById(R.id.custom_end_icon);
//                String text = textInputLayout.getEditText().getText();

                if(!validateName(str_name)){
                    myNameSrt.setError(nameError);
                }else{
                    myNameSrt.setError("");
                }

                if(str_no.equals("")){
                    //Toast.makeText(number_inpt.this, "☎️ INPUT NUMBER !", Toast.LENGTH_SHORT).show();
                    myNoSrt.setError("☎️ INPUT NUMBER !");
                }else if(str_no.length() != 10){
                    //Toast.makeText(number_inpt.this, "☎️ INPUT VALID NUMBER !", Toast.LENGTH_SHORT).show();
                    myNoSrt.setError("☎️ INPUT VALID NUMBER !");
                }else if(str_no.length() == 10 && validateName(str_name)){

                    SharedPreferences settings = getApplicationContext().getSharedPreferences("vadati", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("myName",str_name);
                    editor.apply();

                    skipToHome(str_no);
                }
            }
        });
    }
    void skipToHome(String num){
        Intent myIntent = new Intent(number_inpt.this, otp_auth.class);
        myIntent.putExtra("number","+91"+num);
        number_inpt.this.startActivity(myIntent);
        finish();
    }
    boolean validateName(String Name) {

        boolean isValid = false;

        if (Name.length() == 0) {
            nameError = "Field cannot be empty";
            isValid = false;
        }else if(Name.charAt(0)==' '){
            nameError="You cannot use 'SPACE' at beginning";
            isValid = false;
        }else if(!Name.matches("[a-zA-Z0-9 ]+")){
            nameError="Enter only A-Z, a-z, 0-9 character";
            isValid = false;
        }else{
            isValid = true;
            //Toast.makeText(MainActivity.this,"Validation Successful",Toast.LENGTH_LONG).show();
        }
        return isValid;
    }
}