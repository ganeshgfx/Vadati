package ganesh.gfx.vadati.utils;

import android.content.Context;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

import ganesh.gfx.vadati.data.MessagePileMap;
import ganesh.gfx.vadati.data.Person;
import ganesh.gfx.vadati.data.Room.AppDatabase;
import ganesh.gfx.vadati.data.UnReadMsgPile;
import ganesh.gfx.vadati.data.TempMsg;

public class Constants {
    public static String TAG = "appgfx";
    public static String myNum = "";
    public static String myName = "";
    public static String myFireID = "";
    public static ArrayList<Person> conArry = new ArrayList<Person>();
    public static String CurrentChat = "";
    public static Person chatID = null;
    public static TextView chatsList;
    public static boolean isServiceRunning = false;
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static Context context;

    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final  DatabaseReference myRef = database.getReference("vadati");

    public static AppDatabase localDb;

    public static MessagePileMap messagePileMap = new MessagePileMap();
    //public static UnReadMsgPile unReadMsgPile = new UnReadMsgPile();
    public static TempMsg tempMsg = new TempMsg();
}