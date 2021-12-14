package ganesh.gfx.vadati.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Maps;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.protobuf.Internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

import static ganesh.gfx.vadati.utils.Constants.db;
import static ganesh.gfx.vadati.utils.Constants.TAG;

public class FireStore {
    static DocumentReference updtConList = db.collection("vadati")
            .document("cons");
    static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    public static void MakeMeCon(String ConID){
//        DocumentReference updtConList = db.collection("vadati").document("cons");
//
//        Map<String,Object> hisInfo = new HashMap<>();
//        hisInfo.put("added",false);
//        hisInfo.put("hisFireID",ConID);
//
//        Map<String, Object> meCon = new HashMap<>();
//        meCon.put(""+user.getUid(), FieldValue.arrayUnion(hisInfo));
//
//        updtConList.update(meCon).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d(Constants.TAG, "DocumentSnapshot successfully written!");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(Constants.TAG, "Error writing document", e);
//            }
//        });
//    }
    public static void MakeMeConInHisCon(String personFireID){
//        DocumentReference updtConList = db.collection("vadati").document("cons");
//
//        Map<String,Object> hisInfo = new HashMap<>();
//        hisInfo.put("fireID",Constants.myFireID);
//        hisInfo.put("id",Tools.MD5(Constants.myNum));
//        hisInfo.put("added",true);
//        hisInfo.put("name",Constants.myName);
//        hisInfo.put("number",Constants.myNum);
//        Map<String, Object> meCon = new HashMap<>();
//        meCon.put(""+personFireID,hisInfo);

        CollectionReference root = db.collection("makeCon");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("hisId",Constants.myFireID);
        data1.put("added",true);
        data1.put("name",Constants.myName);
        data1.put("number",Constants.myNum);

        root.document(personFireID).set(data1);


//        updtConList.set(meCon,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                //Log.d(Constants.TAG, "DocumentSnapshot successfully written!");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                //Log.w(Constants.TAG, "Error writing document", e);
//            }
//        });
    }
}
