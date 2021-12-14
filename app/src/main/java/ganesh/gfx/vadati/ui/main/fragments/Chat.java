package ganesh.gfx.vadati.ui.main.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ganesh.gfx.vadati.MainActivity;
import ganesh.gfx.vadati.R;
import ganesh.gfx.vadati.data.FireMessage;
import ganesh.gfx.vadati.data.Message;
import ganesh.gfx.vadati.data.Person;
import ganesh.gfx.vadati.data.locker.Chabi;
import ganesh.gfx.vadati.data.locker.Crypt;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

import static ganesh.gfx.vadati.utils.Constants.context;
import static ganesh.gfx.vadati.utils.Constants.myFireID;
import static ganesh.gfx.vadati.utils.Constants.TAG;

import java.security.PublicKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chat extends Fragment {
    private static String TAG = "appgfx";

    private static PublicKey reciverPublicKey;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Chat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chat.
     */
    // TODO: Rename and change types and number of parameters
    public static Chat newInstance(String param1, String param2) {
        Chat fragment = new Chat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    TextView chats;
    TextInputLayout ChalLayout;
    EditText chatBox;
    static View view;
    static RecyclerView recyclerView;
    static RecycledChatAdapter recycledChatAdapter;
    private static void keySetup() {
        try {
            //Chabi chabi = new Chabi(view.getContext(), Constants.CurrentChat);
            reciverPublicKey = Chabi.getReciverPublicKey(view.getContext(), Constants.CurrentChat);
            //Log.d(TAG, "keySetup: Private - "+Tools.encode(chabi.getPrivateChavi().getEncoded()));
           // Log.d(TAG, "keySetup: mPublic - " + Tools.encode(chabi.getPublicChavi().getEncoded()));
            //Log.d(TAG, "keySetup: Public  - " + Tools.encode(chabi.getReciverPublicKey(view.getContext(), Constants.CurrentChat).getEncoded()));
            //reciverPublicKey = Chabi.getReciverPublicKey(view.getContext(),Constants.CurrentChat);
        }catch (Exception e){
            Log.d(TAG, "keySetup: Eroor");
            e.printStackTrace();
        }
    }
    public static void reCycleChat_poulate(){

            keySetup();

        Log.d(TAG, "reCycleChat_poulate: ");

        //recycledChatAdapter = new RecycledChatAdapter(Constants.messagePileMap.getMessageList());
        recycledChatAdapter = new RecycledChatAdapter(Constants.tempMsg.getMessageIndexs());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(recycledChatAdapter);
//        for (long val:Constants.messagePileMap.getMessageList()){
//            recycledChatAdapter.newAddeddata(val);
//        }
        scrol(false);
    }
    public static void reCycleChat_Add_new(long val){
        recycledChatAdapter.newAddeddata(val);
        scrol(true);
    }
    public static void scrol(boolean smooth){
        if(smooth){
            recyclerView.smoothScrollToPosition(recycledChatAdapter.getItemCount()-1);
        }
        else recyclerView.scrollToPosition(recycledChatAdapter.getItemCount()-1);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        ChalLayout = (TextInputLayout)view.findViewById(R.id.msgBox);
        chatBox = (EditText)ChalLayout.getEditText();
        //chats = (TextView)view.findViewById(R.id.textView_chats);
        //sendBtn = (ImageButton)view.findViewById(R.id.SendButton);
        //Constants.chatsList = chats;

        ////////////////////////RECYCLE
        recyclerView = (RecyclerView) view.findViewById(R.id.recycledChats);
        ///////////////////////////////
        
        ChalLayout.setEndIconVisible(false);
        chatBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkText();
            }
            @Override public void afterTextChanged(Editable s){}
        });

        ChalLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = chatBox.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("vadati");
                if(Constants.CurrentChat.equals("")){
                    MainActivity.switchTab(0);
                }else{
                    Message messageObj = new Message(msg);
                    msg = Tools.gson.toJson(messageObj);

                    FireMessage xMsg = new FireMessage();
                    //xMsg.msg = messageObj.msg();

                    /////////////////////////////////////////Cryptic
                    //Chabi key = new Chabi(getContext(),Constants.CurrentChat);

                    //Chabi.storeReciverPublicKey(getContext(),Constants.CurrentChat,key.getPublicChavi());


                    Crypt crypt = new Crypt();
                    try {
                        xMsg.encrypted = Tools.encode(crypt.code(messageObj.msg(),reciverPublicKey));
                        //xMsg.encrypted = Tools.ByteArrToJsonStr(crypt.code(xMsg.msg));
//                        xMsg.encrypted = Tools.encode(crypt.code(xMsg.msg,key.getPublicChavi()));

                        String K = new Chabi(view.getContext(), "UI").getPrivateChavi().getEncoded().toString();
//                        String E = Tools.encode(crypt.code(xMsg.msg,reciverPublicKey));
//                        String D = crypt.deCode(Tools.decode(E));
//
                        Log.d(TAG, "K : "+K);
                        //Log.d(TAG, "P : "+xMsg.encrypted);
//                        Log.d(TAG, "E : "+E);
//                        Log.d(TAG, "D : "+D);

                        //Log.d(TAG, "<SG : "+crypt.deCode(Tools.gson.fromJson(xMsg.encrypted,byte[].class)));
                    }catch (Exception exception) {exception.printStackTrace();}

                    /////////////////////////////////////////Cryptic

                    xMsg.timestamp = messageObj.timeStamp()+"";

                    //Mgs sending here
                   // Log.d(TAG, "SENT AT: "+Constants.CurrentChat);
                    myRef.child(Constants.CurrentChat).child("msgs").child(myFireID)
                   // myRef.child(myFireID).child("msgs").child(Constants.CurrentChat)
                            .push()
                            .setValue(xMsg)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid){
                                    messageObj.sender = myFireID;
                                    ///////new System
                                    Constants.tempMsg.add(messageObj);
                                    Constants.tempMsg.backup(messageObj);
                                    //Log.d(TAG, "onSuccess: SENT MSG");
//                                    reCycleChat_Add_new(
//                                            Constants
//                                            .tempMsg
//                                            .lastSent
//                                    );
                                    reCycleChat_Add_new(Constants.tempMsg.lastSent);
                                    //reCycleChat_Add_new(Constants.messagePileMap.getMessageListOnlyOne().get(0));
                                    //Constants.tempMsg.backup(messageObj);
                                    /////////////////
                                    //Constants.messagePileMap.storeLocally_ByMe(messageObj);

                                    ///reCycleChat_Add_new(Constants.messagePileMap.getMessageListOnlyOne().get(0));
                                }
                            });

//                    myRef.child(myFireID+Constants.CurrentChat)
//                            .child("msgs")
//                            .push
//                            .setValue(msg);
                }
                chatBox.setText("");
            }
        });

        return view;
    }
    void checkText(){
        String text = chatBox.getText().toString();
        ChalLayout.setEndIconVisible(!text.isEmpty());
        //Log.d(TAG, "checkText: "+text.length());
        if (text.length()>101){
            chatBox.setText(text.substring(0,99));
        }
    }
}

/*

<ImageButton
                android:id="@+id/SendButton"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="4dp"
                android:background="?attr/selectableItemBackgroundBorderless"
                android:padding="14dp"
                android:src="@drawable/send_ico"
                android:tint="@color/colorPrimary"

                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toEndOf="@+id/msgBox"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintVertical_bias="1.0" /> */