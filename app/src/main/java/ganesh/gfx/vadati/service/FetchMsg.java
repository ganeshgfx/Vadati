package ganesh.gfx.vadati.service;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ganesh.gfx.vadati.R;
import ganesh.gfx.vadati.data.FireMessage;
import ganesh.gfx.vadati.data.Message;
import ganesh.gfx.vadati.data.Person;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

import static ganesh.gfx.vadati.utils.Constants.TAG;
import static ganesh.gfx.vadati.utils.Constants.myFireID;
import static ganesh.gfx.vadati.utils.Constants.database;
import static ganesh.gfx.vadati.utils.Constants.tempMsg;

public class FetchMsg {
    static ValueEventListener myMsgListener;
    static DatabaseReference ref = database.getReference("vadati");

    static void manage(){
        myMsgListener = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    
                    //Log.d(TAG, ds.getKey()+" : "+ds.getValue());
                    String sender = ds.getKey();
                    if(sender.equals(Constants.CurrentChat)){
                        Constants.tempMsg.eraseNotice();
                        continue;
                    }
                    for (DataSnapshot childDs:ds.getChildren()){

                        FireMessage fms = childDs.getValue(FireMessage.class);

                        // FIXME: 21-11-2021 
                        Message message = new Message("Encrypted Data",fms.read,fms.timestamp);

                        //Message message = Tools.gson.fromJson(childDs.getValue(String.class), Message.class);

                        message.sender = sender;

                        //Log.d(TAG, message.sender+" : "+message.msg());
                        //unReadMsgPile.input(message);

                        tempMsg.input(message);
                    }
                }
                //Log.d(TAG, "=============");
                PushNoti();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.d(TAG,"The read failed: " + databaseError.getCode());
            }
        };
    }
    static void PushNoti(){
//        for(String s:unReadMsgPile.senderKeySet()) {
//            String name = "";
//            String sender = "";
//            int id = 0;
//            //
//            ArrayList<String> msgs = new ArrayList<String>();
//            //
//            for(Long l:unReadMsgPile.messageKeySet(s)){
//                Message message = unReadMsgPile.getMsg(s,l);
//                sender = message.sender;
//
//                //
//                msgs.add(message.getTimestampAsFormated()+" : "+message.msg());
//                //
//
//                for (Person p : Constants.conArry) {
//                    if (p.fireID.equals(message.sender)) {
//                        name = p.name;
//                        id = Constants.conArry.indexOf(p);
//                        break;
//                    }
//                }
//            }
//            notice(sender,name,msgs,id);
//        }
        for(String s:tempMsg.senderKeySet()) {
            String name = "";
            String sender = "";
            int id = 0;
            //
            ArrayList<String> msgs = new ArrayList<String>();
            //
            for(Long l:tempMsg.messageKeySet(s)){
                Message message = tempMsg.getUnReadMsg(s,l);
                sender = message.sender;

                //
                msgs.add(message.getTimestampAsFormated()+" : "+message.msg());
                //

                for (Person p : Constants.conArry) {
                    if (p.fireID.equals(message.sender)) {
                        name = p.name;
                        id = Constants.conArry.indexOf(p);
                        break;
                    }
                }
            }
            if(Constants.CurrentChat.equals(sender)) {
                //Log.d(TAG, "PushNoti: Skippd");
                continue;
            }
            notice(sender,name,msgs,id);
        }
    }
    static void notice(String sender,String name,ArrayList<String> msg,int notiId){
        NotificationManagerCompat managerCompat;
        managerCompat = NotificationManagerCompat.from(Constants.context);
        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        Bitmap icon = BitmapFactory.decodeResource(
                Constants.context.getResources(),
                R.drawable.twotone_perm_contact_calendar_black_24dp
        );
        int count = 0;
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.setSummaryText(name);
        for(String s:msg){
            style.addLine(s);
            count++;
        }
        String formMsgs = "";
        if(count>=1){
            formMsgs = msg.get(0)+" + "+(count-1)+" more";
        }else formMsgs = msg.get(0);
        //style.setBigContentTitle(msg.get(0)+" + more"+count--);
        Notification notification = new NotificationCompat.Builder(Constants.context, sender)
                .setLargeIcon(icon)
                .setSmallIcon(R.drawable.ic_untitled)
                //.setContentTitle(name)
                .setContentText(formMsgs)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setStyle(style)
                .build();
        managerCompat.notify(notiId, notification);

    }
    public static void StartMonitor(){
        manage();
        ref.child(myFireID).child("msgs").addValueEventListener(myMsgListener);
    }
    public static void StopMonitor(){
        if(myMsgListener!=null)
            ref.child(myFireID).child("msgs").removeEventListener(myMsgListener);
    }
}