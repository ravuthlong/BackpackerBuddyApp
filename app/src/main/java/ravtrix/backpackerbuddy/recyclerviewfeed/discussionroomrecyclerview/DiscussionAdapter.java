package ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.fragments.discussionroom.DiscussionRoomFragment;

/**
 * Created by Ravinder on 12/21/16.
 */

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.ViewHolder> {

    private List<DiscussionModel> discussionModels;
    private LayoutInflater inflater;
    private DiscussionRoomFragment fragment;

    public DiscussionAdapter(DiscussionRoomFragment discussionRoomFragment, List<DiscussionModel> discussionModels) {
        this.discussionModels = discussionModels;
        this.fragment = discussionRoomFragment;
        inflater = LayoutInflater.from(fragment.getContext());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_discussion_room, parent, false);
        return new DiscussionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiscussionModel currentItem = discussionModels.get(position);

        holder.tvUsername.setText(currentItem.getUsername());
        holder.tvCountry.setText(currentItem.getCountry());
        holder.tvText.setText(currentItem.getText());

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
}
