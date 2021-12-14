package ganesh.gfx.vadati.ui.main.fragments;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import ganesh.gfx.vadati.R;
import ganesh.gfx.vadati.data.Message;
import ganesh.gfx.vadati.utils.Constants;
import ganesh.gfx.vadati.utils.Tools;

import static ganesh.gfx.vadati.utils.Constants.TAG;

public class RecycledChatAdapter extends RecyclerView.Adapter<RecycledChatAdapter.ViewHolder>{
    public static ArrayList<Long> messages = new ArrayList<Long>();
    Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycled_chat_list_item, viewGroup, false);
        context = view.getContext();
        return new RecycledChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView chat = holder.getChat();
        //Message mms = Constants.messagePileMap.getMsg(messages.get(position));
        Message mms = Constants.tempMsg.getMessage(messages.get(position));
        //Log.d(TAG, Tools.gson.toJson(mms));
        //Log.d(TAG, "onBindViewHolder: "+Constants.tempMsg.getMessage(messages.get(position)).msg());
        chat.setText(mms.msg());
        if(mms.sender.equals(Constants.myFireID)){
            //chat.setGravity(Gravity.LEFT);
            //Log.d(TAG, Constants.CurrentChat);
            //chat.setGravity(Gravity.RIGHT);
            chat.setBackgroundResource(R.drawable.rounded_corner);
            holder.layout.setGravity(Gravity.RIGHT);
        }else{
            //Log.d(TAG, Constants.myFireID);
            holder.layout.setGravity(Gravity.LEFT);
            chat.setBackgroundResource(R.drawable.leftbbl);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView chat;
        private final LinearLayout layout;
        public ViewHolder(View view) {
            super(view);
            chat = (TextView)view.findViewById(R.id.chipRV);
            layout = (LinearLayout)view.findViewById(R.id.ChatLayid);
        }
        public TextView getChat(){
            return chat;
        }
        public LinearLayout getLayout(){return layout;}
    }
    public RecycledChatAdapter(ArrayList<Long> dataSet) {
        messages = dataSet;
    }
    public void newAddeddata(Long newValue){
        messages.add(newValue);
        //Log.d(Constants.TAG,"newAddeddata: "+newValue);
        notifyDataSetChanged();
    }
}
