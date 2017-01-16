package ravtrix.backpackerbuddy.recyclerviewfeed.commentphotorecyclerview;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.activities.perfectphoto.CommentPerfectPhotoActivity;
import ravtrix.backpackerbuddy.activities.perfectphoto.EditCommentPerfectPhoto;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitPerfectPhotoSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/13/17.
 */

public class PhotoCommentAdapter extends RecyclerView.Adapter<PhotoCommentAdapter.ViewHolder> {

    private List<PhotoCommentModel> photoCommentModelList;
    private LayoutInflater inflater;
    private Context context;
    private int userID;

    public PhotoCommentAdapter(Context context, List<PhotoCommentModel> photoCommentModels, int userID) {
        this.photoCommentModelList = photoCommentModels;
        this.context = context;
        this.userID = userID;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_photo_comment, parent, false);
        return new PhotoCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        PhotoCommentModel currentItem = photoCommentModelList.get(position);

        Picasso.with(context)
                .load(currentItem.getUserpic())
                .fit()
                .centerCrop()
                .into(holder.profilePic);
        holder.tvUsername.setText(currentItem.getUsername());
        holder.tvCountry.setText(currentItem.getCountry());
        holder.tvComment.setText(currentItem.getComment());
        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                currentItem.getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.tvTime.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return photoCommentModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mainRelative;
        private TextView tvUsername, tvCountry, tvTime, tvComment;
        private CircleImageView profilePic;
        private LinearLayout layoutMore;

        ViewHolder(View itemView) {
            super(itemView);

            mainRelative = (RelativeLayout) itemView.findViewById(R.id.item_photo_comment_relative);
            tvUsername = (TextView) itemView.findViewById(R.id.item_photo_comment_tvUsername);
            tvCountry = (TextView) itemView.findViewById(R.id.item_photo_comment_tvCountry);
            tvTime = (TextView) itemView.findViewById(R.id.item_photo_comment_tvTime);
            tvComment = (TextView) itemView.findViewById(R.id.item_photo_comment_tvText);
            profilePic = (CircleImageView) itemView.findViewById(R.id.item_photo_comment_profileimage);
            layoutMore = (LinearLayout) itemView.findViewById(R.id.item_photo_comment_layoutMore);
            Helpers.overrideFonts(context, mainRelative);

            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    PhotoCommentModel clickedItem = photoCommentModelList.get(position);

                    if (clickedItem.getUserID() != userID) {
                        Intent postInfo = new Intent(context, OtherUserProfile.class);
                        postInfo.putExtra("userID", clickedItem.getUserID());
                        context.startActivity(postInfo);
                    }
                }
            });

            layoutMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    PhotoCommentModel clickedItem = photoCommentModelList.get(position);
                    if (clickedItem.getUserID() == userID) {
                        showDialogOwner(clickedItem.getCommentID(), clickedItem.getPhotoID(), clickedItem.getComment());
                    } else {
                        showDialogNormal();
                    }
                }
            });

        }
    }

    public void swap(List<PhotoCommentModel> photoCommentModels) {
        this.photoCommentModelList.clear();
        this.photoCommentModelList.addAll(photoCommentModels);
        notifyDataSetChanged();
    }

    /**
     * Owner pop up dialog includes options to edit and delete their discussion post
     */
    private void showDialogOwner(final int commentID, final int photoID, final String comment) {
        CharSequence options[] = new CharSequence[] {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch(which) {
                    case 0:
                        // Edit
                        Intent intent = new Intent(context, EditCommentPerfectPhoto.class);
                        intent.putExtra("commentID", commentID);
                        intent.putExtra("comment", comment);
                        ((Activity) context).startActivityForResult(intent, 1); // request code 1
                        break;
                    case 1:
                        // Delete
                        final ProgressDialog progressDialog = Helpers.showProgressDialog(context, "Deleting...");
                        removePhotoComment(commentID, photoID, progressDialog);
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

    private void removePhotoComment(final int commentID, int photoID, final ProgressDialog progressDialog) {
        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .removeCommentAndDecrement()
                .removeCommentAndDecrement(commentID, photoID);
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Helpers.hideProgressDialog(progressDialog);

                if (response.body().get("status").getAsInt() == 0) {
                    Helpers.displayToast(context, "Error");
                } else {
                    // refresh the comment page
                    ((CommentPerfectPhotoActivity) context).fetchPhotoCommentsRefresh();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.hideProgressDialog(progressDialog);
                Helpers.displayErrorToast(context);
            }
        });
    }

}
