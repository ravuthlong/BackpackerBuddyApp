package ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.data.FeedItemInbox;

/**
 * Created by Ravinder on 9/24/16.
 */

public class FeedListAdapterInbox extends RecyclerView.Adapter<FeedListAdapterInbox.ViewHolder> {

    private LayoutInflater inflator;
    private Context context;
    private List<FeedItemInbox> feedItemInbox;

    public FeedListAdapterInbox(Context context, List<FeedItemInbox> feedItemInbox) {
        this.context = context;
        this.feedItemInbox = feedItemInbox;
    }

    @Override
    public FeedListAdapterInbox.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.item_inboxFeed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FeedItemInbox currentItem = feedItemInbox.get(position);

        Picasso.with(context).load("http://backpackerbuddy.net23.net/profile_pic/" +
                currentItem.getUserID() + ".JPG")
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.profileImage);
        holder.username.setText(currentItem.getUsername());
        holder.date.setText(currentItem.getLatestDate());
        holder.latestMessage.setText(currentItem.getLatestMessage());
    }

    @Override
    public int getItemCount() {
        return feedItemInbox.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView username, latestMessage, date;

        ViewHolder(View itemView) {
            super(itemView);

            profileImage = (CircleImageView) itemView.findViewById(R.id.item_inboxFeed_profileImage);
            username = (TextView) itemView.findViewById(R.id.item_inboxFeed_username);
            latestMessage = (TextView) itemView.findViewById(R.id.item_inboxFeed_lastMessage);
            date = (TextView) itemView.findViewById(R.id.item_inboxFeed_time);

        }
    }
}
