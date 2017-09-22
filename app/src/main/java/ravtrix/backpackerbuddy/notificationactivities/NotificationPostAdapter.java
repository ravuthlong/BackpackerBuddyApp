package ravtrix.backpackerbuddy.notificationactivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import ravtrix.backpackerbuddy.activities.discussion.editdiscussion.EditDiscussionActivity;
import ravtrix.backpackerbuddy.activities.discussion.editdiscussioncomment.EditDiscussionCommentActivity;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview.CommentModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data.DiscussionModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 2/4/17.
 */

public class NotificationPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CommentModel> commentModels;
    private DiscussionModel discussionModel;
    private LayoutInflater inflater;
    private Context context;
    private UserLocalStore userLocalStore;
    private boolean firstOfList = true;

    public NotificationPostAdapter(Context context, List<CommentModel> commentModels, DiscussionModel discussionModel,
                                    UserLocalStore userLocalStore) {
        this.commentModels = commentModels;
        this.discussionModel = discussionModel;
        this.context = context;
        this.userLocalStore = userLocalStore;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                return new NotificationPostAdapter.ViewHolder1(inflater.inflate(R.layout.item_discussion_room, parent, false));
            case 1:
                return new NotificationPostAdapter.ViewHolder2(inflater.inflate(R.layout.item_discussion_room, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()) {

            case 0:
                ViewHolder1 holder1 = (ViewHolder1) holder;

                Picasso.with(context).load(discussionModel.getUserpic())
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.default_photo)
                        .into(holder1.profileImage);


                // Converting timestamp into x ago format
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                        discussionModel.getTime(),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                holder1.tvTime.setText(timeAgo);
                holder1.tvUsername.setText(discussionModel.getUsername());
                holder1.tvCountry.setText(discussionModel.getCountry());
                holder1.tvText.setText(discussionModel.getPost());
                holder1.tvLoveNum.setText(Integer.toString(discussionModel.getLoveNum()));
                holder1.tvCommentNum.setText(Integer.toString(discussionModel.getCommentNum()));
                break;

            case 1:
                /**
                 * Because the first position 0 is "current user's event post" (unique), we need to start reading
                 * from array list only when position is 1. This is why care 1 reads from position 0.
                 */
                CommentModel currentItem;
                if (firstOfList) {
                    currentItem = commentModels.get(0);
                    firstOfList = false;
                } else {
                    currentItem = commentModels.get(position - 1);
                }


                ViewHolder2 holder2 = (ViewHolder2) holder;

                Picasso.with(context)
                        .load(currentItem.getUserpic())
                        .fit()
                        .centerCrop()
                        .into(holder2.profileImage);

                holder2.tvUsername.setText(currentItem.getUsername());
                holder2.tvCountry.setText(currentItem.getCountry());
                holder2.tvText.setText(currentItem.getComment());

                // Converting timestamp into x ago format
                CharSequence timeAgo2 = DateUtils.getRelativeTimeSpanString(
                        currentItem.getTime(),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                holder2.tvTime.setText(timeAgo2);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return commentModels.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0; // first item unique
        } else {
            return 1;
        }
    }

    // Holder knows and references where the fields are
    private class ViewHolder1 extends RecyclerView.ViewHolder {

        private TextView tvUsername, tvCountry, tvTime, tvText, tvLoveNum, tvCommentNum;
        private CircleImageView profileImage;
        private RelativeLayout relativeDiscussion;
        private LinearLayout layoutLove, layoutComment, relativeMore;
        private ImageView imageButtonLove;

        ViewHolder1(View itemView) {
            super(itemView);
            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry_discussion);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername_discussion);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime_discussion);
            tvText = (TextView) itemView.findViewById(R.id.tvText_discussion);
            profileImage = (CircleImageView) itemView.findViewById(R.id.profileimage_discussion);
            relativeDiscussion = (RelativeLayout) itemView.findViewById(R.id.relative_discussion);
            relativeMore = (LinearLayout) itemView.findViewById(R.id.item_discussion_layoutMore);
            tvLoveNum = (TextView) itemView.findViewById(R.id.tvLoveNum);
            tvCommentNum = (TextView) itemView.findViewById(R.id.tvCommentNum);
            imageButtonLove = (ImageView) itemView.findViewById(R.id.imageButtonLove);
            layoutLove = (LinearLayout) itemView.findViewById(R.id.layoutLove);
            layoutComment = (LinearLayout) itemView.findViewById(R.id.layoutComment);

            // Change font for all text view fields
            Helpers.overrideFonts(context, relativeDiscussion);

            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (discussionModel.getUserID() != userLocalStore.getLoggedInUser().getUserID()) {
                        Intent postInfo = new Intent(context, OtherUserProfile.class);
                        postInfo.putExtra("userID", discussionModel.getUserID());
                        context.startActivity(postInfo);
                    }
                }
            });

            relativeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (discussionModel.getUserID() == userLocalStore.getLoggedInUser().getUserID()) {
                        showDialogOwnerParent(discussionModel.getDiscussionID(), discussionModel.getPost(), 1);
                    } else {
                        showDialogNormal();
                    }
                }
            });
        }
    }

    private class ViewHolder2 extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvCountry, tvTime, tvText, tvLoveNum, tvViewLoves;
        private CircleImageView profileImage;
        private RelativeLayout relativeDiscussion;
        private LinearLayout layoutLove, layoutComment;
        private ImageView imageButtonLove, imageButtonMore;

        ViewHolder2(View itemView) {
            super(itemView);
            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry_discussion);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername_discussion);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime_discussion);
            tvText = (TextView) itemView.findViewById(R.id.tvText_discussion);
            profileImage = (CircleImageView) itemView.findViewById(R.id.profileimage_discussion);
            relativeDiscussion = (RelativeLayout) itemView.findViewById(R.id.relative_discussion);
            tvLoveNum = (TextView) itemView.findViewById(R.id.tvLoveNum);
            imageButtonLove = (ImageView) itemView.findViewById(R.id.imageButtonLove);
            imageButtonMore = (ImageView) itemView.findViewById(R.id.item_discussion_imageButtonMore);
            layoutLove = (LinearLayout) itemView.findViewById(R.id.layoutLove);
            layoutComment = (LinearLayout) itemView.findViewById(R.id.layoutComment);
            tvViewLoves = (TextView) itemView.findViewById(R.id.item_discussion_tvViewLoves);

            layoutComment.setVisibility(View.GONE);
            layoutLove.setVisibility(View.GONE);
            tvViewLoves.setVisibility(View.GONE);
            // Change font for all text view fields
            Helpers.overrideFonts(context, relativeDiscussion);

            imageButtonMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    CommentModel clickedItem = commentModels.get(position - 1);

                    if (clickedItem.getUserID() == userLocalStore.getLoggedInUser().getUserID()) {
                        showDialogOwner(clickedItem.getCommentID(), clickedItem.getDiscussionID(), clickedItem.getComment(), 2);
                    } else {
                        showDialogNormal();
                    }
                }
            });


            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    CommentModel clickedItem = commentModels.get(position - 1);

                    if (clickedItem.getUserID() != userLocalStore.getLoggedInUser().getUserID()) {
                        Intent postInfo = new Intent(context, OtherUserProfile.class);
                        postInfo.putExtra("userID", clickedItem.getUserID());
                        context.startActivity(postInfo);
                    }
                }
            });
        }
    }

    void swapComments(List<CommentModel> models){
        commentModels.clear();
        commentModels.addAll(models);
        this.notifyDataSetChanged();
    }

    void swapParentDiscussion(DiscussionModel model){
        discussionModel = model;
        this.notifyDataSetChanged();
    }


    /**
     * Owner pop up dialog includes options to edit and delete their discussion post
     */
    private void showDialogOwner(final int commentID, final int discussionID, final String comment, final int requestCode) {
        CharSequence options[] = new CharSequence[] {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch(which) {
                    case 0:
                        Intent intent = new Intent(context, EditDiscussionCommentActivity.class);
                        intent.putExtra("commentID", commentID);
                        intent.putExtra("comment", comment);
                        ((Activity) context).startActivityForResult(intent, requestCode); // request code
                        break;
                    case 1:
                        final ProgressDialog progressDialog = Helpers.showProgressDialog(context, "Deleting...");
                        removeCommentRetrofit(commentID, discussionID, progressDialog);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Report", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }


    private void showDialogOwnerParent(final int discussionID, final String post, final int requestCode) {
        CharSequence options[] = new CharSequence[] {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch(which) {
                    case 0:
                        Intent intent = new Intent(context, EditDiscussionActivity.class);
                        intent.putExtra("discussionID", discussionID);
                        intent.putExtra("post", post);
                        ((Activity) context).startActivityForResult(intent, requestCode);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();
    }

    private void removeCommentRetrofit(int commentID, final int discussionID, final ProgressDialog progressDialog) {
        HashMap<String, String> commentInfo = new HashMap<>();
        commentInfo.put("commentID", Integer.toString(commentID));
        commentInfo.put("discussionID", Integer.toString(discussionID));

        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .removeCommentAndDecrement()
                .removeCommentAndDecrement(commentInfo);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Helpers.hideProgressDialog(progressDialog);
                if (response.body().get("status").getAsInt() == 0) {
                    Helpers.displayToast(context, "Error");
                } else {
                    // refresh the comment page
                    ((NotificationPostActivity) context).fetchDiscussionCommentsRefresh(userLocalStore.getLoggedInUser().getUserID(), discussionID);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.hideProgressDialog(progressDialog);
                Helpers.displayToast(context, "Error");
            }
        });
    }
}
