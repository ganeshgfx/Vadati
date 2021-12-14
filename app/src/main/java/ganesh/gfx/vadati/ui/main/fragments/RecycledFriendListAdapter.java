package ganesh.gfx.vadati.ui.main.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import ganesh.gfx.vadati.R;
import ganesh.gfx.vadati.data.Person;
import ganesh.gfx.vadati.utils.Constants;

import static java.security.AccessController.getContext;

public class RecycledFriendListAdapter extends RecyclerView.Adapter<RecycledFriendListAdapter.ViewHolder> {


    private static final String TAG = "appgfx";
    public static ArrayList<Person> localDataSet = new ArrayList<Person>();

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button button;
        public View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            // Define click listener for the ViewHolder's View
            button = (Button) view.findViewById(R.id.textView_RV);
        }

        public TextView getButton() {
            return button;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public RecycledFriendListAdapter(ArrayList<Person> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    Context context;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycled_friends_list_item, viewGroup, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        TextView button = viewHolder.getButton();
        button.setText(localDataSet.get(position).number+" : "+localDataSet.get(position).name);
        //button.setIconResource();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
