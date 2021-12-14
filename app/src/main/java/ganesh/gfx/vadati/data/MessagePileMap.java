package ganesh.gfx.vadati.data;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ganesh.gfx.vadati.data.Room.LocalMessage;
import ganesh.gfx.vadati.data.Room.MyLocalMessage;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

import static ganesh.gfx.vadati.utils.Constants.TAG;

public class MessagePileMap {
    Map<Long,Message> messageMap = new TreeMap<>();
    //SharedPreferences.Editor editor;
    public boolean store(Message msg){
        boolean stored = false;
        //Log.d(TAG, "Thai ili");
        if(!messageMap.containsKey(msg.timeStamp())){
            messageMap.put(msg.timeStamp(),msg);
            stored = true;
            if(!msg.sender.equals(Constants.myFireID)){
                storeLocally(msg);
            }
        }
        return stored;
    }
    public void storeLocally(Message msg){
        //if(!msg.sender.equals(Constants.myFireID)){
        if(!messageMap.containsKey(msg.timeStamp()))
            messageMap.put(msg.timeStamp(),msg);

        LocalMessage lmsg = (LocalMessage)msg.toLocalMessage(Message.THEREDB);
        Constants.localDb.localMessageDao().insertMessage(lmsg);
        //}
    }
    public void storeLocally_ByMe(Message msg){
        //if(!msg.sender.equals(Constants.myFireID)){
        if(!messageMap.containsKey(msg.timeStamp()))
            messageMap.put(msg.timeStamp(),msg);
        MyLocalMessage lmsg = (MyLocalMessage)msg.toLocalMessage(Message.INMYDB);
        Constants.localDb.myLocalMessage().insertMessage(lmsg);
        //}
    }
    public void changeContact(){
        clear();
        readFromLocalDatabase();
    }
    public void readFromLocalDatabase(){
        List<LocalMessage> data = Constants.localDb.localMessageDao().getMsgBySender(Constants.CurrentChat);
        if(data!=null){
            for (LocalMessage Lsms : data){
                Message msg = Tools.gson.fromJson(Lsms.msgJson,Message.class);
                messageMap.put(msg.timeStamp(),msg);
            }
        }
        List<MyLocalMessage> Mdata = Constants.localDb.myLocalMessage().getMsgBySender(Constants.CurrentChat);
        if(Mdata!=null){
            for (MyLocalMessage Lsms : Mdata) {
                Message msg = Tools.gson.fromJson(Lsms.msgJson,Message.class);
                messageMap.put(msg.timeStamp(),msg);
            }
        }
    }
    public ArrayList<Long> getMessageList(){
        ArrayList<Long> keys = new ArrayList<Long>();
        for(Long key:messageMap.keySet()){
            keys.add(key);
        }
        return keys;
    }
    public ArrayList<Long> getMessageListOnlyOne(){
        ArrayList<Long> keys = new ArrayList<Long>();
        long temp = 0;
        for(Long key:messageMap.keySet()){
            temp = key;
        }
        keys.add(temp);
        return keys;
    }
    public Message getMsg(Long Key){
        return messageMap.get(Key);
    }
    public Set<Long> getKeys(){
        return messageMap.keySet();
    }
    public void print(){
        Log.d(TAG, "////////////////////////////");
        for (Long key:messageMap.keySet()){
            if(messageMap.get(key).sender == Constants.myFireID){
                Log.d(TAG, "HIM>"+messageMap.get(key).getTimestampAsFormated()+" : "+messageMap.get(key).msg);
            }
            Log.d(TAG, "ME>"+messageMap.get(key).getTimestampAsFormated()+" : "+messageMap.get(key).msg);
        }
    }
    public void clear() {
        messageMap.clear();
    }
}
