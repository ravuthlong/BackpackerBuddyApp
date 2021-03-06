package ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.chat.ConversationActivity;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.fragments.message.MessagesFragment;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.data.FeedItemInbox;

/**
 * Created by Ravinder on 9/24/16.
 */

public class FeedListAdapterInbox extends RecyclerView.Adapter<FeedListAdapterInbox.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<FeedItemInbox> feedItemInbox;
    private UserLocalStore userLocalStore;
    private MessagesFragment messagesFragment;
    private FeedListAdapterInbox.ViewHolder viewHolder;
    private ArrayList<View> itemViews;
    private int i = 0;

    public FeedListAdapterInbox(MessagesFragment messagesFragment, Context context, List<FeedItemInbox> feedItemInbox) {
        if (context != null) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.feedItemInbox = feedItemInbox;
            this.messagesFragment = messagesFragment;
            this.userLocalStore = new UserLocalStore(context);
            this.itemViews = new ArrayList<>();
        }
    }

    @Override
    public FeedListAdapterInbox.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_inboxfeed, parent, false);
        viewHolder =  new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Typeface fontType = Typeface.createFromAsset(context.getAssets(), "Text.ttf");

        FeedItemInbox currentItem = feedItemInbox.get(position);
        Picasso.with(context).load(currentItem.getUserpic())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.default_photo)
                .into(holder.profileImage);

        holder.username.setTypeface(fontType);
        holder.date.setTypeface(fontType);
        holder.latestMessage.setTypeface(fontType);

        holder.username.setText(currentItem.getUsername());
        holder.date.setText(currentItem.getLatestDate());
        holder.latestMessage.setText(currentItem.getLatestMessage());

        if ((userLocalStore.getLoggedInUser().getUserID() != currentItem.getLastMessageUserID()) &&
                (currentItem.getIsOtherUserClicked() == 0)) {
                holder.latestMessage.setTypeface(null, Typeface.BOLD);
        }
    }


    @Override
    public int getItemCount() {
        return feedItemInbox.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView username, latestMessage, date;
        private RelativeLayout relativeLayout;

        ViewHolder(final View itemView) {
            super(itemView);

            // Keep an array list of views for each view for easy modification
            itemViews.add(i, itemView);
            i++;

            profileImage = (CircleImageView) itemView.findViewById(R.id.item_countryFeed_profileImage);
            username = (TextView) itemView.findViewById(R.id.item_inboxFeed_username);
            latestMessage = (TextView) itemView.findViewById(R.id.item_inboxFeed_lastMessage);
            date = (TextView) itemView.findViewById(R.id.item_inboxFeed_time);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_inbox_relative);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    FeedItemInbox clickedItem = feedItemInbox.get(position);

                    // Set the message to clicked already by the user in Firebase Cloud
                    if ((userLocalStore.getLoggedInUser().getUserID() != clickedItem.getLastMessageUserID()) &&
                            (clickedItem.getIsOtherUserClicked() == 0)) {
                        clickedItem.getSnapshot().child("isOtherUserClicked").getRef().setValue(1);
                        clickedItem.setIsOtherUserClicked(1);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                latestMessage.setTypeface(null, Typeface.NORMAL);
                            }
                        }, 1000); // 1 second delay
                    }

                    Intent intent = new Intent(context, ConversationActivity.class);
                    intent.putExtra("otherUserID", Integer.toString(clickedItem.getUserID()));
                    intent.putExtra("chatRoom", clickedItem.getChatRoom());
                    intent.putExtra("otherUserImage", clickedItem.getUserpic());
                    intent.putExtra("position", position);
                    messagesFragment.startActivityForResult(intent, 2);
                }
            });

            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    FeedItemInbox clickedItem = feedItemInbox.get(position);

                    if (clickedItem.getUserID() != userLocalStore.getLoggedInUser().getUserID()) {
                        Intent postInfo = new Intent(context, OtherUserProfile.class);
                        postInfo.putExtra("userID", clickedItem.getUserID());
                        context.startActivity(postInfo);
                    }
                }
            });
        }

        // Update latest message in inbox fragment after user sent a new message
        public void updateMessage(int position, String message) {

            FeedItemInbox currentItem = feedItemInbox.get(position);
            currentItem.setLatestMessage(message);

            TextView lastMessage = (TextView) itemViews.get(position).findViewById(R.id.item_inboxFeed_lastMessage);
            lastMessage.setText(message);
        }

        // Update latest time in inbox fragment after user sent a new message
        public void updateTime(int position, Long timestamp) {

            FeedItemInbox currentItem = feedItemInbox.get(position);
            // Converting timestamp into x ago format
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(String.valueOf(timestamp)),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            currentItem.setLatestDate(timeAgo.toString());

            TextView latestTime = (TextView) itemViews.get(position).findViewById(R.id.item_inboxFeed_time);
            latestTime.setText(timeAgo);
        }
    }

    public FeedListAdapterInbox.ViewHolder getViewHolder() {
        return this.viewHolder;
    }

}
