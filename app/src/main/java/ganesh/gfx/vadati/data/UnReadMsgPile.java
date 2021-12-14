package ganesh.gfx.vadati.data;

import android.util.Log;
import static ganesh.gfx.vadati.utils.Constants.TAG;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class UnReadMsgPile {
    Map<String,Map<Long,Message>> messageMap = new TreeMap<>();
    public void input(Message message){
        if(messageMap.get(message.sender)==null) {
            //Log.d(TAG, "input: NEW");
            Map<Long,Message> m = new HashMap<>();
            m.put(message.timeStamp(),message);
            messageMap.put(message.sender,m);
        }else{
            if(messageMap.get(message.sender).get(message.timeStamp())==null){
                messageMap.get(message.sender).put(message.timeStamp(),message);
            }
        }
    }
    public Message getMsg(String sender,Long time){
        return messageMap.get(sender).get(time);
    }
    public Set<String> senderKeySet(){
        return messageMap.keySet();
    }
    public Set<Long> messageKeySet(String sender){
        return messageMap.get(sender).keySet();
    }

    public void print(){
        for (String sender:messageMap.keySet()) {
            for (Long time:messageMap.get(sender).keySet()) {
                Log.d(TAG, "print: "+messageMap.get(sender).get(time).msg());
            }
        }
    }
}
