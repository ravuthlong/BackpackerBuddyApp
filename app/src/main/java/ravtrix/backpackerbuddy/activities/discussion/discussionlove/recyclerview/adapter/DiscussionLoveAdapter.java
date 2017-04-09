package ravtrix.backpackerbuddy.activities.discussion.discussionlove.recyclerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.discussion.discussionlove.recyclerview.model.DiscussionLove;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 4/8/17.
 */

public class DiscussionLoveAdapter extends RecyclerView.Adapter<DiscussionLoveAdapter.ViewHolder> {

    private List<DiscussionLove> discussionLoveList;
    private UserLocalStore userLocalStore;
    private Context context;

    public DiscussionLoveAdapter(Context context, List<DiscussionLove> discussionLoves, UserLocalStore userLocalStore) {
        this.context  = context;
        this.discussionLoveList = discussionLoves;
        this.userLocalStore = userLocalStore;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_discussion_love, parent, false);
        return new DiscussionLoveAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DiscussionLove currentItem = discussionLoveList.get(position);

        if (!currentItem.getProfileImage().isEmpty()) {
            Picasso.with(context)
                    .load(currentItem.getProfileImage())
                    .fit()
                    .centerCrop()
                    .into(holder.profileImage);
        }

        holder.username.setText(currentItem.getUsername());
        holder.country.setText(currentItem.getCountry());
    }

    @Override
    public int getItemCount() {
        return discussionLoveList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mainItemRelative;
        private CircleImageView profileImage;
        private TextView username, country;

        public ViewHolder(View itemView) {
            super(itemView);

            mainItemRelative = (RelativeLayout) itemView.findViewById(R.id.item_discussion_love_relativeMain);
            profileImage = (CircleImageView) itemView.findViewById(R.id.item_discussion_love_profileImage);
            username = (TextView) itemView.findViewById(R.id.item_discussion_love_tvUsername);
            country = (TextView) itemView.findViewById(R.id.item_discussion_love_tvCountry);

            Helpers.overrideFonts(context, mainItemRelative);

            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    DiscussionLove clickedItem = discussionLoveList.get(position);

                    if (clickedItem.getUserID() != userLocalStore.getLoggedInUser().getUserID()) {
                        Intent postInfo = new Intent(context, OtherUserProfile.class);
                        postInfo.putExtra("userID", clickedItem.getUserID());
                        context.startActivity(postInfo);
                    }
                }
            });
        }
    }
}
