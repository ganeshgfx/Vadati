package ganesh.gfx.vadati.data;

import android.icu.text.DateFormat;
import android.util.Log;

import ganesh.gfx.vadati.data.Room.LocalMessage;
import ganesh.gfx.vadati.data.Room.MyLocalMessage;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

public class Message implements Comparable<Message>{
    String rtID;
    String msg;
    public String sender;
    String timestamp;
    boolean read;
    public Message(){}
    public static final byte INMYDB = 0,THEREDB = 1;
    public Message(String msg) {
        this.msg = msg;
        this.timestamp = ""+System.currentTimeMillis();
    }
    public Message(String msg,String sender,String timestamp) {
        this.msg = msg;
        this.sender = sender;
        this.timestamp = timestamp;
        //this.timestamp = (long)genRandom(1,999);//TESTING
    }
    public Message(String msg,boolean read,String timestamp) {
        this.msg = msg;
        this.read = read;
        this.timestamp = timestamp;
    }
    public String getTimestampAsFormated(){
        //return DateFormat.getTimeInstance(DateFormat.SHORT).format(timeStamp());
        return DateFormat.getDateTimeInstance().format(timeStamp());

        ///////////eg
//        DateFormat.getDateInstance().format(new Date(0));
//        DateFormat.getDateTimeInstance().format(new Date(0));
//        DateFormat.getTimeInstance().format(new Date(0));
    }
    public long timeStamp(){
        return Long.parseLong(this.timestamp);
    }
    public String msg(){return msg;}
    @Override
    public int compareTo(Message o){
        Long A = Long.parseLong(this.timestamp);
        Long B = Long.parseLong(o.timestamp);
        return A.compareTo(B);//(0-9)
    }
    public Object toLocalMessage(byte ToDB){
        if(ToDB == THEREDB){
            Log.d("appgfx", "toLocalMessage: ");
            LocalMessage tolMsg = new LocalMessage();
            tolMsg.sender = sender;
            tolMsg.msgJson = Tools.gson.toJson(new Message(this.msg, this.sender, this.timestamp));
            return tolMsg;
        }
        if(ToDB == INMYDB){
            Log.d("appgfx", "toLocalMessage: ");
            MyLocalMessage tolMsg = new MyLocalMessage();
            tolMsg.sender = Constants.CurrentChat;
            tolMsg.msgJson = Tools.gson.toJson(new Message(this.msg, this.sender, this.timestamp));
            return tolMsg;
        }
        return null;
    }
    public LocalMessage toRoomMessageSender(){
            LocalMessage tolMsg = new LocalMessage();
            //tolMsg.sender = sender;
            tolMsg.sender = Constants.CurrentChat;
            tolMsg.msgJson = Tools.gson.toJson(new Message(this.msg, this.sender, this.timestamp));
            return tolMsg;
    }
    public MyLocalMessage toRoomMessageMy(){
            MyLocalMessage tolMsg = new MyLocalMessage();
            tolMsg.sender = Constants.CurrentChat;
            tolMsg.msgJson = Tools.gson.toJson(new Message(this.msg, this.sender, this.timestamp));
            return tolMsg;
    }
}
