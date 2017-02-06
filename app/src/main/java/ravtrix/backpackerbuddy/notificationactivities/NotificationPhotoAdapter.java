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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.activities.perfectphoto.commentsperfectphoto.CommentPerfectPhotoActivity;
import ravtrix.backpackerbuddy.activities.perfectphoto.editcommentperfectphoto.EditCommentPerfectPhoto;
import ravtrix.backpackerbuddy.activities.perfectphoto.editphotopostperfect.EditPhotoPostActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitPerfectPhotoSingleton;
import ravtrix.backpackerbuddy.recyclerviewfeed.commentphotorecyclerview.PhotoCommentModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.perfectphotorecyclerview.PerfectPhotoModel;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ravinder on 2/4/17.
 */

public class NotificationPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PhotoCommentModel> photoCommentModelList;
    private PerfectPhotoModel perfectPhotoModel;
    private LayoutInflater inflater;
    private Context context;
    private int userID;
    private boolean firstOfList = true;

    public NotificationPhotoAdapter(Context context, List<PhotoCommentModel> photoCommentModels, PerfectPhotoModel perfectPhotoModel, int userID) {
        this.photoCommentModelList = photoCommentModels;
        this.perfectPhotoModel = perfectPhotoModel;
        this.context = context;
        this.userID = userID;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new NotificationPhotoAdapter.ViewHolder1(inflater.inflate(R.layout.item_perfect_photo, parent, false));
            case 1:
                return new NotificationPhotoAdapter.ViewHolder2(inflater.inflate(R.layout.item_photo_comment, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case 0:
                final ViewHolder1 holder1 = (ViewHolder1) holder;

                Picasso.with(context).load(perfectPhotoModel.getUserpic())
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.default_photo)
                        .into(holder1.profileImage);
                holder1.tvUsername.setText(perfectPhotoModel.getUsername());
                holder1.tvCountry.setText(perfectPhotoModel.getCountry());

                // Converting timestamp into x ago format
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                        perfectPhotoModel.getTime(),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                holder1.tvTime.setText(timeAgo);

                Picasso.with(context).load(perfectPhotoModel.getPath() + "&thumbnail")
                        .into(holder1.imagePerfect, new Callback() {
                            @Override
                            public void onSuccess() {

                                /*
                                if (progressBar.getVisibility() == View.VISIBLE) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                if (recyclerView.getVisibility() == View.INVISIBLE) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                }*/

                                Picasso.with(context).load(perfectPhotoModel.getPath())
                                        .fit()
                                        .centerCrop()
                                        .placeholder(holder1.imagePerfect.getDrawable())
                                        .into(holder1.imagePerfect);
                            }
                            @Override
                            public void onError() {

                            }
                        });

                holder1.tvPost.setText(perfectPhotoModel.getPost());
                holder1.tvLoveNum.setText(String.valueOf(perfectPhotoModel.getLoveNum()));
                holder1.tvCommentNum.setText(String.valueOf(perfectPhotoModel.getCommentNum()));
                break;
            case 1:

                /**
                 * Because the first position 0 is "current user's event post" (unique), we need to start reading
                 * from array list only when position is 1. This is why care 1 reads from position 0.
                 */
                PhotoCommentModel currentItem;
                if (firstOfList) {
                    currentItem = photoCommentModelList.get(0);
                    firstOfList = false;
                } else {
                    currentItem = photoCommentModelList.get(position - 1);
                }

                ViewHolder2 holder2 = (ViewHolder2) holder;

                Picasso.with(context)
                        .load(currentItem.getUserpic())
                        .fit()
                        .centerCrop()
                        .into(holder2.profilePic);
                holder2.tvUsername.setText(currentItem.getUsername());
                holder2.tvCountry.setText(currentItem.getCountry());
                holder2.tvComment.setText(currentItem.getComment());
                // Converting timestamp into x ago format
                CharSequence timeAgo2 = DateUtils.getRelativeTimeSpanString(
                        currentItem.getTime(),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                holder2.tvTime.setText(timeAgo2);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return photoCommentModelList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    private class ViewHolder1 extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvCountry, tvTime, tvLoveNum, tvCommentNum, tvPost;
        private CircleImageView profileImage;
        private RelativeLayout relativePerfect;
        private LinearLayout layoutLove, layoutComment, layoutMore;
        private ImageView imagePerfect, imageButtonLove;

        ViewHolder1(View itemView) {
            super(itemView);

            relativePerfect = (RelativeLayout) itemView.findViewById(R.id.item_perfect);
            profileImage = (CircleImageView) itemView.findViewById(R.id.item_perfect_imgProfile);
            tvCountry = (TextView) itemView.findViewById(R.id.item_perfect_tvCountry);
            tvUsername = (TextView) itemView.findViewById(R.id.item_perfect_tvUsername);
            tvTime = (TextView) itemView.findViewById(R.id.item_perfect_tvTime);
            tvPost = (TextView) itemView.findViewById(R.id.item_perfect_post);
            imagePerfect = (ImageView) itemView.findViewById(R.id.item_perfect_image);
            tvLoveNum = (TextView) itemView.findViewById(R.id.item_perfect_tvLoveNum);
            tvCommentNum = (TextView) itemView.findViewById(R.id.item_perfect_tvCommentNum);
            imageButtonLove = (ImageView) itemView.findViewById(R.id.item_perfect_imageButtonLove);
            layoutLove = (LinearLayout) itemView.findViewById(R.id.item_perfect_layoutLove);
            layoutComment = (LinearLayout) itemView.findViewById(R.id.item_perfect_layoutComment);
            layoutMore = (LinearLayout) itemView.findViewById(R.id.item_perfect_layoutMore);
            Helpers.overrideFonts(context, relativePerfect);

            // On click listener to view a user's profile picture
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (perfectPhotoModel.getUserID() != userID) {
                        Intent postInfo = new Intent(context, OtherUserProfile.class);
                        postInfo.putExtra("userID", perfectPhotoModel.getUserID());
                        context.startActivity(postInfo);
                    }
                }
            });

            layoutMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (perfectPhotoModel.getUserID() == userID) {
                        showDialogOwnerPhoto(perfectPhotoModel.getPerfectPhotoID(), perfectPhotoModel.getPost(),
                                perfectPhotoModel.getPath(), perfectPhotoModel.getDeletePath());
                    } else {
                        showDialogNormal();
                    }
                }
            });
        }
    }

    // comment
    private class ViewHolder2 extends RecyclerView.ViewHolder {

        private RelativeLayout mainRelative;
        private TextView tvUsername, tvCountry, tvTime, tvComment;
        private CircleImageView profilePic;
        private LinearLayout layoutMore;

        ViewHolder2(View itemView) {
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
                    PhotoCommentModel clickedItem = photoCommentModelList.get(position - 1);

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
                    PhotoCommentModel clickedItem = photoCommentModelList.get(position - 1);
                    if (clickedItem.getUserID() == userID) {
                        showDialogOwner(clickedItem.getCommentID(), clickedItem.getPhotoID(), clickedItem.getComment());
                    } else {
                        showDialogNormal();
                    }
                }
            });
        }
    }

    /**
     * Owner pop up dialog includes options to edit and delete their discussion post
     */
    private void showDialogOwnerPhoto(final int photoID, final String post, final String picPath, final String deletePath) {
        CharSequence options[] = new CharSequence[] {"Edit"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch(which) {
                    case 0:
                        // Edit
                        Intent intent = new Intent(context, EditPhotoPostActivity.class);
                        intent.putExtra("photoID", photoID);
                        intent.putExtra("post", post);
                        ((Activity) context).startActivityForResult(intent, 0); // request code 1
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();
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
        retrofit.enqueue(new retrofit2.Callback<JsonObject>() {
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

    void swapComments(List<PhotoCommentModel> models){
        photoCommentModelList.clear();
        photoCommentModelList.addAll(models);
        this.notifyDataSetChanged();
    }

    void swapParentPhoto(PerfectPhotoModel model){
        perfectPhotoModel = model;
        this.notifyDataSetChanged();
    }

}
