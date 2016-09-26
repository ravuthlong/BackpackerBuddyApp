package ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.ConversationActivity;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.data.FeedItemInbox;

/**
 * Created by Ravinder on 9/24/16.
 */

public class FeedListAdapterInbox extends RecyclerView.Adapter<FeedListAdapterInbox.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<FeedItemInbox> feedItemInbox;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private Counter counter;
    private View view;

    public FeedListAdapterInbox(Context context, List<FeedItemInbox> feedItemInbox, View view,
                                FragActivityProgressBarInterface fragActivityProgressBarInterface) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.feedItemInbox = feedItemInbox;
        this.fragActivityProgressBarInterface = fragActivityProgressBarInterface;
        this.view = view;
        counter = new Counter();
    }

    @Override
    public FeedListAdapterInbox.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_inboxfeed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FeedItemInbox currentItem = feedItemInbox.get(position);

        Picasso.with(context).load("http://backpackerbuddy.net23.net/profile_pic/" +
                currentItem.getUserID() + ".JPG")
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.profileImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        counter.addCount();
                        // Display layout only when all images has been loaded
                        checkPicassoFinished();
                    }

                    @Override
                    public void onError() {

                    }
                });
        holder.username.setText(currentItem.getUsername());
        holder.date.setText(currentItem.getLatestDate());
        holder.latestMessage.setText(currentItem.getLatestMessage());

        // Display layout only when all images has been loaded
        checkPicassoFinished();
    }

    private void checkPicassoFinished() {
        if (counter.getCount() == getItemCount()) {
            fragActivityProgressBarInterface.setProgressBarInvisible();
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return feedItemInbox.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView username, latestMessage, date;
        private RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);

            profileImage = (CircleImageView) itemView.findViewById(R.id.item_inboxFeed_profileImage);
            username = (TextView) itemView.findViewById(R.id.item_inboxFeed_username);
            latestMessage = (TextView) itemView.findViewById(R.id.item_inboxFeed_lastMessage);
            date = (TextView) itemView.findViewById(R.id.item_inboxFeed_time);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_inboxFeed_relativeLayout);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    FeedItemInbox clickedItem = feedItemInbox.get(position);

                    Intent intent = new Intent(context, ConversationActivity.class);
                    intent.putExtra("otherUserID", Integer.toString(clickedItem.getUserID()));
                    context.startActivity(intent);
                }
            });
        }
    }

    // Keeps track how many picasso images have been loaded onto grid view
    private class Counter {
        private int count = 0;

        void addCount() {
            count++;
        }

        int getCount() {
            return count;
        }
    }
}
