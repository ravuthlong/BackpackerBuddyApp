package ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import ravtrix.backpackerbuddy.activities.DiscussionComments;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.fragments.userprofile.UserProfileFragment;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.interfacescom.FragActivityResetDrawer;
import ravtrix.backpackerbuddy.models.UserLocalStore;
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
    private Counter counter;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private View view;
    private UserLocalStore userLocalStore;
    private FragmentManager fragmentManager;
    private FragActivityResetDrawer fragActivityResetDrawer;
    private SwipeRefreshLayout refreshLayout;
    private int sizeToCompare; //for when view can be visible

    public DiscussionAdapter(Fragment fragment, List<DiscussionModel> discussionModels,
                             View view, FragActivityProgressBarInterface fragActivityProgressBarInterface,
                             UserLocalStore userLocalStore, SwipeRefreshLayout refreshLayout) {
        this.discussionModels = discussionModels;
        this.fragment = fragment;
        this.view = view;
        this.fragActivityProgressBarInterface = fragActivityProgressBarInterface;
        this.fragActivityResetDrawer = (FragActivityResetDrawer) fragment.getActivity();
        this.userLocalStore = userLocalStore;
        this.refreshLayout = refreshLayout;
        inflater = LayoutInflater.from(fragment.getContext());
        counter = new Counter(1);

        switch(discussionModels.size()) {
            case 0:
                sizeToCompare = 0;
                break;
            case 1:
                sizeToCompare = 1;
                break;
            case 2:
                sizeToCompare = 2;
                break;
            case 3:
                sizeToCompare = 3;
                break;
            default:
                sizeToCompare = 3;
                break;
        }
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
        holder.tvLoveNum.setText(Integer.toString(currentItem.getLoveNum()));
        holder.tvCommentNum.setText(Integer.toString(currentItem.getCommentNum()));

        if (currentItem.getIsClicked() == 1) {
            holder.imageButtonLove.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp);
        } else {
            holder.imageButtonLove.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        }

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

                    if (clickedItem.getUserID() == userLocalStore.getLoggedInUser().getUserID()) {
                        performFragTransaction(fragment);
                    } else {
                        Intent postInfo = new Intent(fragment.getActivity(), OtherUserProfile.class);
                        postInfo.putExtra("postID", clickedItem.getPost());
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
                        showDialogOwner();
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
    private void showDialogOwner() {
        CharSequence options[] = new CharSequence[] {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch(which) {
                    case 0:
                        Toast.makeText(fragment.getActivity(), "Edit", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(fragment.getActivity(), "Delete", Toast.LENGTH_SHORT).show();
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

    private void checkPicassoFinished() {

        if (counter.getCount() >= sizeToCompare) {
            // stop progress bar
            fragActivityProgressBarInterface.setProgressBarInvisible();
            view.setVisibility(View.VISIBLE);

            // stop refreshing layout is it is running
            if (refreshLayout != null && refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
                System.out.println("STOP REFRESH");
            }
        }
    }

    private void performFragTransaction(final Fragment activity) {
        // Delay to avoid lag when changing between option in navigation drawer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Reset navigation drawer selected items. Clear all
                fragActivityResetDrawer.onResetDrawer();

                // Perform fragment replacement
                fragmentManager = activity.getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new UserProfileFragment()).commit();
                ((UserMainPage) activity.getActivity()).getDrawerLayout().closeDrawer(GravityCompat.START);
            }
        }, 150);
    }

    // Keeps track how many picasso images have been loaded onto grid view
    private class Counter {
        private int count = 0;

        Counter(int count) {
            this.count = count;
        }
        void addCount() {
            count++;
        }

        int getCount() {
            return count;
        }
    }
}
