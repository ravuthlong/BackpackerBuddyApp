package ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
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
import ravtrix.backpackerbuddy.fragments.discussionroom.DiscussionRoomFragment;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;

/**
 * Created by Ravinder on 12/21/16.
 */

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.ViewHolder> {

    private List<DiscussionModel> discussionModels;
    private LayoutInflater inflater;
    private DiscussionRoomFragment fragment;
    private Counter counter;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private View view;

    public DiscussionAdapter(DiscussionRoomFragment discussionRoomFragment, List<DiscussionModel> discussionModels,
                             View view, FragActivityProgressBarInterface fragActivityProgressBarInterface) {
        this.discussionModels = discussionModels;
        this.fragment = discussionRoomFragment;
        this.view = view;
        this.fragActivityProgressBarInterface = fragActivityProgressBarInterface;
        inflater = LayoutInflater.from(fragment.getContext());
        counter = new Counter();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_discussion_room, parent, false);
        return new DiscussionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiscussionModel currentItem = discussionModels.get(position);

        Picasso.with(fragment.getContext()).load("http://backpackerbuddy.net23.net/profile_pic/" +
                currentItem.getUserID() + ".JPG")
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit()
                .centerCrop()
                .into(holder.profileImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        counter.addCount();
                        // Display layout only when all images has been loaded
                        checkPicassoFinished();
                    }
                    @Override
                    public void onError() {}
                });

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                currentItem.getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        holder.tvTime.setText(timeAgo);
        holder.tvUsername.setText(currentItem.getUsername());
        holder.tvCountry.setText(currentItem.getCountry());
        holder.tvText.setText(currentItem.getPost());

    }

    @Override
    public int getItemCount() {
        return discussionModels.size();
    }

    // Holder knows and references where the fields are
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername, tvCountry, tvTime, tvText;
        private CircleImageView profileImage;

        ViewHolder(View itemView) {
            super(itemView);
            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry_discussion);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername_discussion);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime_discussion);
            tvText = (TextView) itemView.findViewById(R.id.tvText_discussion);
            profileImage = (CircleImageView) itemView.findViewById(R.id.profileimage_discussion);

        }
    }

    private void checkPicassoFinished() {
        if (counter.getCount() == getItemCount()) {
            fragActivityProgressBarInterface.setProgressBarInvisible();
            view.setVisibility(View.VISIBLE);
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
