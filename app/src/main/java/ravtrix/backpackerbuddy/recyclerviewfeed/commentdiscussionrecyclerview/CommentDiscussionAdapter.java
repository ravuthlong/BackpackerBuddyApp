package ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview;

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
import ravtrix.backpackerbuddy.activities.discussion.discussioncomments.DiscussionComments;
import ravtrix.backpackerbuddy.activities.discussion.editdiscussioncomment.EditDiscussionCommentActivity;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 12/24/16.
 */

public class CommentDiscussionAdapter extends RecyclerView.Adapter<CommentDiscussionAdapter.ViewHolder> {

    private List<CommentModel> commentModels;
    private LayoutInflater inflater;
    private Context context;
    private UserLocalStore userLocalStore;

    public CommentDiscussionAdapter(Context context, List<CommentModel> commentModels,
                                    UserLocalStore userLocalStore) {
        this.commentModels = commentModels;
        this.context = context;
        this.userLocalStore = userLocalStore;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_discussion_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentModel currentItem = commentModels.get(position);

        Picasso.with(context)
                .load(currentItem.getUserpic())
                .fit()
                .centerCrop()
                .into(holder.profileImage);

        holder.tvUsername.setText(currentItem.getUsername());
        holder.tvCountry.setText(currentItem.getCountry());
        holder.tvText.setText(currentItem.getComment());

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                currentItem.getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.tvTime.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvCountry, tvTime, tvText, tvLoveNum;
        private CircleImageView profileImage;
        private RelativeLayout relativeDiscussion;
        private LinearLayout layoutLove, layoutComment;
        private ImageView imageButtonLove, imageButtonMore;

        ViewHolder(View itemView) {
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
            layoutComment.setVisibility(View.GONE);
            layoutLove.setVisibility(View.GONE);
            // Change font for all text view fields
            Helpers.overrideFonts(context, relativeDiscussion);

            imageButtonMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    CommentModel clickedItem = commentModels.get(position);

                    if (clickedItem.getUserID() == userLocalStore.getLoggedInUser().getUserID()) {
                        showDialogOwner(clickedItem.getCommentID(), clickedItem.getDiscussionID(), clickedItem.getComment());
                    } else {
                        showDialogNormal();
                    }
                }
            });


            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    CommentModel clickedItem = commentModels.get(position);

                    if (clickedItem.getUserID() != userLocalStore.getLoggedInUser().getUserID()) {
                        Intent postInfo = new Intent(context, OtherUserProfile.class);
                        postInfo.putExtra("userID", clickedItem.getUserID());
                        context.startActivity(postInfo);
                    }
                }
            });
        }
    }

    public void swap(List<CommentModel> models){
        commentModels.clear();
        commentModels.addAll(models);
        this.notifyDataSetChanged();
    }

    public void clearData() {
        this.commentModels.clear();
    }


    /**
     * Owner pop up dialog includes options to edit and delete their discussion post
     */
    private void showDialogOwner(final int commentID, final int discussionID, final String comment) {
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
                        ((Activity) context).startActivityForResult(intent, 1); // request code 1
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

    private void removeCommentRetrofit(int commentID, int discussionID, final ProgressDialog progressDialog) {
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
                    ((DiscussionComments) context).fetchDiscussionCommentsRefresh();
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
