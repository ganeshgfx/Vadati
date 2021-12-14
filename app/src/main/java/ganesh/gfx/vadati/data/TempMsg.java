package ganesh.gfx.vadati.data;

import static ganesh.gfx.vadati.utils.Constants.TAG;
import static ganesh.gfx.vadati.utils.Constants.tempMsg;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ganesh.gfx.vadati.data.Room.LocalMessage;
import ganesh.gfx.vadati.data.Room.MyLocalMessage;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

public class TempMsg {
    String TAG = "appgfx";
    static Map<Long, Message> message = new HashMap<Long, Message>();

    public void load() {
        List<LocalMessage> data = Constants.localDb.localMessageDao().getMsgBySender(Constants.CurrentChat);
        if (data != null) {
            for (LocalMessage Lsms : data) {
                Message msg = Tools.gson.fromJson(Lsms.msgJson, Message.class);
                msg.sender = Constants.myFireID;
//                String by = "==>";
//                if(msg.sender== Constants.myFireID)
//                    by = "<==";
//                Log.d(TAG, by+" " + msg.msg());
                //Log.d(TAG,Tools.gson.toJson(msg));
                add(msg);
            }
        }
        List<MyLocalMessage> Mdata = Constants.localDb.myLocalMessage().getMsgBySender(Constants.CurrentChat);
        if (Mdata != null) {
            for (MyLocalMessage Lsms : Mdata) {
                Message msg = Tools.gson.fromJson(Lsms.msgJson, Message.class);
                msg.sender = Constants.CurrentChat;
                //Log.d(TAG,Tools.gson.toJson(msg));
//                String by = "==>";
//                if(msg.sender== Constants.myFireID)
//                    by = "<==";
//                Log.d(TAG, by+" " + msg.msg());
                add(msg);
            }
        }
    }
    public Long lastSent;
    public void backup(Message msg){
        //Log.d(TAG, "Backup"+Tools.gson.toJson(msg));
        //Log.d(TAG, "MY: "+Constants.myFireID);
        //Log.d(TAG, "HIM : "+Constants.CurrentChat);
        if(msg.sender.equals(Constants.myFireID)) {
            //Log.d(TAG, "New MSG : ME");
            lastSent= msg.timeStamp();

//            LocalMessage lmsg = (LocalMessage) msg.toLocalMessage(Message.INMYDB);
//            Constants.localDb.localMessageDao().insertMessage(lmsg);
            Constants.localDb.localMessageDao().insertMessage(msg.toRoomMessageSender());


           // Log.d(TAG, "backup: ME"+Tools.gson.toJson(msg));
        }
        if(msg.sender.equals(Constants.CurrentChat)){
            //Log.d(TAG, "New MSG : BE");
//            MyLocalMessage lmsg = (MyLocalMessage)msg.toLocalMessage(Message.THEREDB);
//            Constants.localDb.myLocalMessage().insertMessage(lmsg);
            Constants.localDb.myLocalMessage().insertMessage(msg.toRoomMessageMy());

            //Log.d(TAG, "backup: BE"+Tools.gson.toJson(msg));
        }
//        if(msg.sender == Constants.myFireID){
//            LocalMessage lmsg = (LocalMessage)msg.toLocalMessage(Message.INMYDB);
//            Constants.localDb.localMessageDao().insertMessage(lmsg);
//        }
//        else{
//            MyLocalMessage lmsg = (MyLocalMessage)msg.toLocalMessage(Message.THEREDB);
//            Constants.localDb.myLocalMessage().insertMessage(lmsg);
//        }
    }

    public void clear() {
        message.clear();
    }
    public void add(Message msg) {
        message.put(msg.timeStamp(), msg);
    }

    public void sort() {
    }

    public ArrayList<Long> getMessageIndexs() {

        //sorting
        List<Map.Entry<Long,Message>>list = new ArrayList<>(message.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Long, Message>>() {
            @Override
            public int compare(Map.Entry<Long, Message> longMessageEntry, Map.Entry<Long, Message> t1) {
                Long A = Long.parseLong(longMessageEntry.getValue().timestamp);
                Long B = Long.parseLong(t1.getValue().timestamp);
                return A.compareTo(B);
            }
        });
        //

        ArrayList<Long> keys = new ArrayList<Long>();
//        for (Long key : message.keySet()) {
//            keys.add(key);
//        }
        for (Map.Entry<Long,Message> msg: list) {
            keys.add(msg.getKey());
        }
        return keys;
    }

    public Message getMessage(Long key){
        return message.get(key);
    }

    public void print() {
        Log.d(TAG, "\n//////////////////////////////////////////////////////////////////////////////");

        for (Long time : getMessageIndexs()) {
            Message m = getMessage(time);
            String by = "==> ";
            if (m.sender == Constants.myFireID)
                by = "<== ";
           Log.d(TAG, by + m.getTimestampAsFormated() + " [" + m.msg() + "]");
            //Log.d(TAG, by + m.getTimestampAsFormated());
        }
    }
    public boolean hitTest(Message sms){
        return message.containsKey(sms.timeStamp());
    }

    ////////////////////////////////////////////
//    void merge(int arr[], int l, int m, int r) {
//        // Find sizes of two subarrays to be merged
//        int n1 = m - l + 1;
//        int n2 = r - m;
//
//        /* Create temp arrays */
//        int L[] = new int[n1];
//        int R[] = new int[n2];
//
//        /*Copy data to temp arrays*/
//        for (int i = 0; i < n1; ++i)
//            L[i] = arr[l + i];
//        for (int j = 0; j < n2; ++j)
//            R[j] = arr[m + 1 + j];
//
//        /* Merge the temp arrays */
//
//        // Initial indexes of first and second subarrays
//        int i = 0, j = 0;
//
//        // Initial index of merged subarray array
//        int k = l;
//        while (i < n1 && j < n2) {
//            if (L[i] <= R[j]) {
//                arr[k] = L[i];
//                i++;
//            } else {
//                arr[k] = R[j];
//                j++;
//            }
//            k++;
//        }
//
//        /* Copy remaining elements of L[] if any */
//        while (i < n1) {
//            arr[k] = L[i];
//            i++;
//            k++;
//        }
//
//        /* Copy remaining elements of R[] if any */
//        while (j < n2) {
//            arr[k] = R[j];
//            j++;
//            k++;
//        }
//    }
//
//    // Main function that sorts arr[l..r] using
//    // merge()
//    void sort(int arr[], int l, int r) {
//        if (l < r) {
//            // Find the middle point
//            int m = l + (r - l) / 2;
//
//            // Sort first and second halves
//            sort(arr, l, m);
//            sort(arr, m + 1, r);
//
//            // Merge the sorted halves
//            merge(arr, l, m, r);
//        }
//    }
    ///////////////////////Notice and Unread//////////////////////////
    Map<String,Map<Long,Message>> unReads = new TreeMap<>();
    public void input(Message message){
        if(unReads.get(message.sender)==null) {
            //Log.d(TAG, "input: NEW");
            Map<Long,Message> m = new HashMap<>();
            m.put(message.timeStamp(),message);
            unReads.put(message.sender,m);
        }else{
            if(unReads.get(message.sender).get(message.timeStamp())==null){
                unReads.get(message.sender).put(message.timeStamp(),message);
            }
        }
    }
    public void eraseNotice(){
        //Log.d(TAG, "///////////////////");
        for (String sender:unReads.keySet()) {
            for (Long time:unReads.get(sender).keySet()) {
                Message sms = unReads.get(sender).get(time);
                if(sms.sender.equals(Constants.CurrentChat)) {
                    //Log.d(TAG, "print: " + sms.msg());
                    sms.read = true;
                }
            }
        }
    }
    public Message getUnReadMsg(String sender, Long time){
        return unReads.get(sender).get(time);
    }
    public Set<String> senderKeySet(){
        return unReads.keySet();
    }
    public Set<Long> messageKeySet(String sender){
        return unReads.get(sender).keySet();
    }
}