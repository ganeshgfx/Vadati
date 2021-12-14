package ganesh.gfx.vadati.ui.main.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static ganesh.gfx.vadati.utils.Constants.myFireID;
import static ganesh.gfx.vadati.utils.Constants.chatID;

import java.security.PrivateKey;
import java.security.PublicKey;

import ganesh.gfx.vadati.MainActivity;
import ganesh.gfx.vadati.R;
import ganesh.gfx.vadati.data.FireMessage;
import ganesh.gfx.vadati.data.Message;
import ganesh.gfx.vadati.data.Person;
import ganesh.gfx.vadati.data.locker.Chabi;
import ganesh.gfx.vadati.data.locker.Crypt;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Connect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Connect extends Fragment {
    String TAG = "appgfx";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Connect() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Connect.
     */
    // TODO: Rename and change types and number of parameters
    public static Connect newInstance(String param1, String param2) {
        Connect fragment = new Connect();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(old_chat != null) {
            //Log.d(TAG, "onResume: ");
            attachValueEventListener();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(old_chat != null){
            //Log.d(TAG, "onPause: ");
            removeValueEventListener();
        }
    }
    //FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("vadati");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_connect, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycledFriends);
        //Log.d(TAG, "onCreateView: "+Constants.conArry.size());
        RecycledFriendListAdapter adapter = new RecycledFriendListAdapter(Constants.conArry);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(createItemClickListener(recyclerView));
        return view;
    }

    //final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = Constants.database.getReference("vadati");
    String old_chat;
    ValueEventListener myMsgListener;
    void attachValueEventListener(){
        //Log.d(TAG, "attachValueEventListener: (ME)"+myFireID+"->(HIM)"+chatID.fireID);
        ref.child(myFireID).child("msgs").child(chatID.fireID).addValueEventListener(myMsgListener);
        //ref.child(chatID.fireID+myFireID).child("msgs").addValueEventListener(hisMsgListener);
    }
    void removeValueEventListener(){
        //Log.d(TAG, "removeValueEventListener: removed");
        ref.child(myFireID).child("msgs").child(chatID.fireID).child(chatID.fireID).removeEventListener(myMsgListener);
        //ref.child(old_chat+myFireID).child("msgs").removeEventListener(hisMsgListener);
    }
    private Crypt decoder;
    public RecyclerItemClickListener createItemClickListener(final RecyclerView recyclerView) {
        return new RecyclerItemClickListener (getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view instanceof Button){

                    chatID = Constants.conArry.get(position);
                    Constants.CurrentChat = chatID.fireID;
                    //Constants.chatsList.setText("Loading...");
                    if(old_chat != chatID.fireID){

                        ///////New System
                        Constants.tempMsg.clear();
                        Constants.tempMsg.load();
                        //Constants.tempMsg.print();
                        ////////////////

                        //E
                        decoder = new Crypt(new Chabi(getContext(), Constants.CurrentChat));
                        //

                        Constants.messagePileMap.changeContact();
                        Chat.reCycleChat_poulate();
                    }

                    //Log.d(TAG, "ME : "+ myFireID);
                    //Log.d(TAG, "ME : "+ Constants.CurrentChat);

                    if(old_chat == null){
                        //Log.d(TAG, "onItemClick: NonINPT");
                    }else{
                        //Log.d(TAG, "onItemClick: "+old_chat);
                        removeValueEventListener();
                    }
                    old_chat = chatID.fireID;
//                    hisMsgListener = new ValueEventListener(){
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot){
//                            //Log.d(TAG,"by ME");
//                            storeMassage(dataSnapshot,Constants.myFireID);
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError){
//                            //Log.d(TAG,"The read failed: " + databaseError.getCode());
//                        }
//                    };
                    myMsgListener = new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            //Log.d(TAG, "onDataChange: new data"+dataSnapshot.toString());
                            storeMassage(dataSnapshot,Constants.CurrentChat);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG,"The read failed: " + databaseError.getCode());
                        }
                    };
                    attachValueEventListener();
                    MainActivity.switchTab(1);
                    //WriteMgs();

                } else {
                    // ... tapped on the item container (row), so do something different
                }
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }
    void storeMassage(DataSnapshot dataSnapshot,String sender){
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            //Log.d(TAG, "dataSnapshot: "+dataSnapshot+"\ndataSnapshot.getChildren() "+dataSnapshot.getChildren());


            //String value = ds.getValue(String.class);
            //Message message = Tools.gson.fromJson(value,Message.class);

            FireMessage fms = ds.getValue(FireMessage.class);

            ////E
            String dec = "Unable To Decrypt the Message ✉️";
            try {
                dec = decoder.deCode(Tools.decode(fms.encrypted));
                Log.d(TAG, "DecryptedMassage: "+dec);
            }catch(Exception e){
                e.printStackTrace();
            }
            /////

            Message message = new Message(dec,fms.read,fms.timestamp);

            message.sender = sender;



            //boolean stored = Constants.messagePileMap.store(message);
            //Log.d(TAG, "NEW MESSAGE : "+message.msg());
            ///////////NEW SYSTEM
            if(!Constants.tempMsg.hitTest(message)) {
                //Log.d(TAG, "NEW MESSAGE : "+message.msg());
                Constants.tempMsg.add(message);
                Constants.tempMsg.backup(message);
                Chat.reCycleChat_Add_new(message.timeStamp());
            }
            /////////////////////

            //Log.d(TAG,"storeMassage: "+stored);
            //if(stored)
                //Chat.reCycleChat_Add_new(Constants.messagePileMap.getMessageListOnlyOne().get(0));
            //Log.d(TAG, "DELETING - "+Constants.CurrentChat+myFireID+"/"+ds.getKey());
            if(!sender.equals(myFireID)){

                //Log.d(TAG, "DELETING - "+myFireID+"/"+chatID.fireID+"/"+"msgs/"+ds.getKey());
                //FIXME: 24/03/2021 DELETE ONLY ONCE chek in map
                Constants.myRef.child(myFireID).child("msgs").child(chatID.fireID).child(ds.getKey()).removeValue();

            }
        }
        //WriteMgs();
    }
}