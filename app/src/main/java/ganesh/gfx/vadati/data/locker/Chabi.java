package ganesh.gfx.vadati.data.locker;

import static ganesh.gfx.vadati.utils.Constants.TAG;
import static ganesh.gfx.vadati.utils.Constants.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

public class Chabi {

    private static Context context;
    private String algorithm = "RSA";
    private int keysize = 1024;
    private PrivateKey privateChavi;
    private PublicKey publicChavi;

    private static PublicKey reciverPublicKey;

//    public Chabi(){
//        try {
//            KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
//            generator.initialize(keysize);
//            KeyPair pair = generator.generateKeyPair();
//            privateChavi = pair.getPrivate();
//            publicChavi = pair.getPublic();
//        } catch (Exception ignored) { }
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Chabi(Context context, String forUser){

        Log.d(TAG, "Chabi: ");

        this.context = context;

        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            String alias = forUser;
            int nBefore = keyStore.size();

            boolean newKey = false;

            // Create the keys if necessary
            if (!keyStore.containsAlias(alias)) {

                newKey = true;

                Log.d(TAG, "Chabi: NOT EXITS");
                
                Calendar notBefore = Calendar.getInstance();
                Calendar notAfter = Calendar.getInstance();
                //notAfter.add(Calendar.YEAR, 10);
                notAfter.add(Calendar.YEAR, 1);


                // *** Replaced deprecated KeyPairGeneratorSpec with KeyPairGenerator
                KeyPairGenerator spec = KeyPairGenerator.getInstance(
                        // *** Specified algorithm here
                        // *** Specified: Purpose of key here
                        KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
                spec.initialize(new KeyGenParameterSpec.Builder(
                        alias, KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1) //  RSA/ECB/PKCS1Padding
                        .setKeySize(keysize)
                        // *** Replaced: setStartDate
                        .setKeyValidityStart(notBefore.getTime())
                        // *** Replaced: setEndDate
                        .setKeyValidityEnd(notAfter.getTime())
                        // *** Replaced: setSubject
                        .setCertificateSubject(new X500Principal("CN=test"))
                        // *** Replaced: setSerialNumber
                        .setCertificateSerialNumber(BigInteger.ONE)
                        .build());
                KeyPair keyPair = spec.generateKeyPair();
                //Log.i(TAG, keyPair.toString());
            }

            int nAfter = keyStore.size();
            //Log.v(TAG, "Before = " + nBefore + " After = " + nAfter);

            // Retrieve the keys
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);

            privateChavi = privateKeyEntry.getPrivateKey();
            publicChavi = privateKeyEntry.getCertificate().getPublicKey();

            Log.d(TAG, "Chabi: "+Tools.encode(privateKeyEntry.getPrivateKey().getEncoded()));

            if(newKey){
                Log.d(TAG, "Chabi: NEW");
                Map<String, Object> key = new HashMap<>();
                key.put("key",Tools.encode(publicChavi.getEncoded()));
                db
                        .collection("keys")
                        .document(Constants.myFireID)
                        .collection("keyChain")
                        .document(Constants.CurrentChat)
                        .set(key)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // FIXME: 21-11-2021
                        fetchReciverPublicKey();
                        Log.d(TAG, "onSuccess: KEY STORED");
                    }
                });

            }else {
                Log.d(TAG, "OLD key: ");
                reciverPublicKey = getReciverPublicKey(context,forUser);
            }
        } catch (Exception e) {
            e.printStackTrace();}
        }

    public PrivateKey getPrivateChavi() {
        return privateChavi;
    }

    public PublicKey getPublicChavi() {
        return publicChavi;
    }

    public static PublicKey getKey(String key){
        try{
            byte[] byteKey = Base64.decode(key.getBytes(), Base64.URL_SAFE);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePublic(X509publicKey);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
    private static String localKeyStore = "ReciverPublicKeys";
    public static void storeReciverPublicKey(Context context,String Reciver,PublicKey Key){
        SharedPreferences settings = context.getSharedPreferences(localKeyStore, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Reciver,Tools.encode(Key.getEncoded()));
        editor.apply();

        //Log.d(TAG, "getReciverPublicKey: "+getReciverPublicKey(context,Reciver));
    }
    public static PublicKey getReciverPublicKey(Context context,String Reciver){
        String pkStr = context.getSharedPreferences(localKeyStore,0).getString(Reciver,"NULL");
        PublicKey pk = getKey(pkStr);

        if(pkStr.equals("NULL")){
            fetchReciverPublicKey();
        }

        Log.d(TAG, "getReciverPublicKey: "+pkStr);

        return pk;
    }
    public static void fetchReciverPublicKey(){

        String pkStr = context.getSharedPreferences(localKeyStore,0).getString(Constants.CurrentChat,"NULL");

        if(!pkStr.equals("NULL")){
            Log.d(TAG, "fetchReciverPublicKey: Found It - "+pkStr);
            return;
        }

        db
                .collection("keys")
                .document(Constants.CurrentChat)
                //.document(Constants.myFireID)
                .collection("keyChain")
                .document(Constants.myFireID)
                //.document(Constants.CurrentChat)
                .get()
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();

                        //Log.d(TAG, "GET K: "+doc.get("key",String.class));

                        if(doc.exists()){
                           Log.d(TAG, "GET reciver Public Ker: "+doc.get("key",String.class));
                            reciverPublicKey = getKey(doc.get("key",String.class));
                            storeReciverPublicKey(context,Constants.CurrentChat,reciverPublicKey);

                        }else{
                            Log.d(TAG, "Key not found");
                            //fetchReciverPublicKey();
                        }
                    }
                });
    }
}
