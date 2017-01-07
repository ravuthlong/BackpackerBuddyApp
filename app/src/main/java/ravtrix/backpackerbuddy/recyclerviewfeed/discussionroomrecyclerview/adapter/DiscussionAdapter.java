package ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.discussion.DiscussionComments;
import ravtrix.backpackerbuddy.activities.discussion.EditDiscussionActivity;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.fragments.discussionroom.DiscussionRoomFragment;
import ravtrix.backpackerbuddy.fragments.managedestination.auserdiscussionposts.AUserDisPostsFragment;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data.DiscussionModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 12/21/16.
 */

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.ViewHolder> {

    private List<DiscussionModel> discussionModels;
    private LayoutInflater inflater;
    private Fragment fragment;
    private UserLocalStore userLocalStore;

    public DiscussionAdapter(Fragment fragment, List<DiscussionModel> discussionModels,
                             UserLocalStore userLocalStore) {
        this.discussionModels = discussionModels;
        this.fragment = fragment;
        this.userLocalStore = userLocalStore;
        inflater = LayoutInflater.from(fragment.getContext());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_discussion_room, parent, false);
        return new DiscussionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DiscussionModel currentItem = discussionModels.get(position);
        Picasso.with(fragment.getContext()).load(currentItem.getUserpic())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.default_photo)
                .into(holder.profileImage);

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                currentItem.getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        holder.tvTime.setText(timeAgo);

        holder.tvUsername.setText(currentItem.getUsername());
        holder.tvCountry.setText(currentItem.getCountry());
        holder.tvText.setText(currentItem.getPost().replace("\\", "")); // remove extra /
        holder.tvLoveNum.setText(Integer.toString(currentItem.getLoveNum()));
        holder.tvCommentNum.setText(Integer.toString(currentItem.getCommentNum()));

        if (userLocalStore.getLoggedInUser().getUserID() == 0) {
            holder.imageButtonLove.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        } else if (currentItem.getIsClicked() == 1) {
            holder.imageButtonLove.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp);
        } else {
            holder.imageButtonLove.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        }

        if (userLocalStore.getLoggedInUser().getUserID() != 0) {
            holder.layoutLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (currentItem.getIsClicked() == 0) {
                        // If the user clicks love, add to their love list and increment total love
                        insertAndUpdateLoveRetrofit(currentItem.getDiscussionID());
                        currentItem.setIsClicked(1);
                        currentItem.setLoveNum(currentItem.getLoveNum() + 1);
                        holder.imageButtonLove.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp);
                    } else {
                        // If the user clicks love, remove from their love list and decrement total love
                        removeAndUpdateLoveRetrofit(currentItem.getDiscussionID());
                        currentItem.setIsClicked(0);
                        holder.imageButtonLove.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                        currentItem.setLoveNum(currentItem.getLoveNum() - 1);
                    }
                    holder.tvLoveNum.setText(Integer.toString(currentItem.getLoveNum()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return discussionModels.size();
    }

    // Holder knows and references where the fields are
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername, tvCountry, tvTime, tvText, tvLoveNum, tvCommentNum;
        private CircleImageView profileImage;
        private RelativeLayout relativeDiscussion;
        private LinearLayout layoutLove, layoutComment;
        private ImageView imageButtonLove;

        ViewHolder(View itemView) {
            super(itemView);
            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry_discussion);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername_discussion);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime_discussion);
            tvText = (TextView) itemView.findViewById(R.id.tvText_discussion);
            profileImage = (CircleImageView) itemView.findViewById(R.id.profileimage_discussion);
            relativeDiscussion = (RelativeLayout) itemView.findViewById(R.id.relative_discussion);
            tvLoveNum = (TextView) itemView.findViewById(R.id.tvLoveNum);
            tvCommentNum = (TextView) itemView.findViewById(R.id.tvCommentNum);
            imageButtonLove = (ImageView) itemView.findViewById(R.id.imageButtonLove);
            layoutLove = (LinearLayout) itemView.findViewById(R.id.layoutLove);
            layoutComment = (LinearLayout) itemView.findViewById(R.id.layoutComment);

            // Change font for all text view fields
            Helpers.overrideFonts(fragment.getContext(), relativeDiscussion);

            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    DiscussionModel clickedItem = discussionModels.get(position);

                    if (clickedItem.getUserID() != userLocalStore.getLoggedInUser().getUserID()) {
                        Intent postInfo = new Intent(fragment.getActivity(), OtherUserProfile.class);
                        postInfo.putExtra("userID", clickedItem.getUserID());
                        fragment.startActivity(postInfo);
                    }
                }
            });

            relativeDiscussion.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    int position = getAdapterPosition();
                    DiscussionModel clickedItem = discussionModels.get(position);

                    if (clickedItem.getUserID() == userLocalStore.getLoggedInUser().getUserID()) {
                        showDialogOwner(clickedItem.getDiscussionID(), clickedItem.getPost());
                    } else {
                        showDialogNormal();
                    }
                    return true;
                }
            });

            layoutComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    DiscussionModel clickedItem = discussionModels.get(position);

                    Intent intent = new Intent(fragment.getActivity(), DiscussionComments.class);
                    intent.putExtra("discussionID", clickedItem.getDiscussionID());
                    intent.putExtra("ownerID", clickedItem.getUserID());
                    fragment.getActivity().startActivity(intent);
                }
            });

        }
    }

    /**
     * Update the database by inserting into love list and increment love count
     * @param discussionID      the discussion post id
     */
    private void insertAndUpdateLoveRetrofit(int discussionID) {

        HashMap<String, String> discussionInfoHash = new HashMap<>();
        discussionInfoHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        discussionInfoHash.put("discussionID", Integer.toString(discussionID));

        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .insertAndUpdateLove().insertAndUpdateLove(discussionInfoHash);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 0) {
                    Toast.makeText(fragment.getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(fragment.getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Update the database by removing from love list and decrement love count
     * @param discussionID      the discussion post id
     */
    private void removeAndUpdateLoveRetrofit(int discussionID) {

        HashMap<String, String> discussionInfoHash = new HashMap<>();
        discussionInfoHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        discussionInfoHash.put("discussionID", Integer.toString(discussionID));

        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .removeAndUpdateLove().removeAndUpdateLove(discussionInfoHash);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 0) {
                    Toast.makeText(fragment.getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(fragment.getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Owner pop up dialog includes options to edit and delete their discussion post
     */
    private void showDialogOwner(final int discussionID, final String post) {
        CharSequence options[] = new CharSequence[] {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch(which) {
                    case 0:
                        Intent intent = new Intent(fragment.getContext(), EditDiscussionActivity.class);
                        intent.putExtra("discussionID", discussionID);
                        intent.putExtra("post", post);
                        fragment.startActivityForResult(intent, 1);
                        break;
                    case 1:
                        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                                .deleteDiscussion()
                                .deleteDiscussion(discussionID);

                        retrofit.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                                if (response.body().get("status").getAsInt() == 1) {

                                    if (fragment instanceof DiscussionRoomFragment) {
                                        DiscussionRoomFragment discussionRoomFragment = (DiscussionRoomFragment) fragment;
                                        discussionRoomFragment.refresh();
                                    } else if (fragment instanceof AUserDisPostsFragment) {
                                        AUserDisPostsFragment aUserDisPostsFragment = (AUserDisPostsFragment) fragment;
                                        aUserDisPostsFragment.refresh();
                                    }
                                } else {
                                    Helpers.displayToast(fragment.getActivity(), "Error");
                                }
                            }
                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Helpers.displayToast(fragment.getActivity(), "Error");
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Option pop up dialog only includes option to report a post
     */
    private void showDialogNormal() {
        CharSequence options[] = new CharSequence[] {"Report"};

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(fragment.getActivity(), "Report", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    public void swap(List<DiscussionModel> models){
        discussionModels.clear();
        discussionModels.addAll(models);
        this.notifyDataSetChanged();
    }
}
