package ravtrix.backpackerbuddy.recyclerviewfeed.perfectphotorecyclerview;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.activities.perfectphoto.commentsperfectphoto.CommentPerfectPhotoActivity;
import ravtrix.backpackerbuddy.activities.perfectphoto.editphotopostperfect.EditPhotoPostActivity;
import ravtrix.backpackerbuddy.fragments.perfectphoto.PerfectPhotoFragment;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitPerfectPhotoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/12/17.
 */

public class PerfectPhotoAdapter extends RecyclerView.Adapter<PerfectPhotoAdapter.ViewHolder> {

    private List<PerfectPhotoModel> perfectPhotoModels;
    private Context context;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private UserLocalStore userLocalStore;
    private Fragment fragment;

    public PerfectPhotoAdapter(Context context, Fragment fragment,
                               List<PerfectPhotoModel> photoModels, ProgressBar progressBar,
                               RecyclerView recyclerView, UserLocalStore userLocalStore) {
        this.context = context;
        this.perfectPhotoModels = photoModels;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
        this.userLocalStore = userLocalStore;
        this.fragment = fragment;
        if (context != null) inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_perfect_photo, parent, false);
        return new PerfectPhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PerfectPhotoModel currentItem = perfectPhotoModels.get(position);

        Picasso.with(context).load(currentItem.getUserpic())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.default_photo)
                .into(holder.profileImage);
        holder.tvUsername.setText(currentItem.getUsername());
        holder.tvCountry.setText(currentItem.getCountry());

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                currentItem.getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.tvTime.setText(timeAgo);

        Picasso.with(context).load(currentItem.getPath() + "&thumbnail")
                .into(holder.imagePerfect, new Callback() {
                    @Override
                    public void onSuccess() {

                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                        if (recyclerView.getVisibility() == View.INVISIBLE) {
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        Picasso.with(context).load(currentItem.getPath())
                                .fit()
                                .centerCrop()
                                .placeholder(holder.imagePerfect.getDrawable())
                                .into(holder.imagePerfect);
                    }
                    @Override
                    public void onError() {

                    }
                });

        holder.tvPost.setText(currentItem.getPost());
        holder.tvLoveNum.setText(String.valueOf(currentItem.getLoveNum()));
        holder.tvCommentNum.setText(String.valueOf(currentItem.getCommentNum()));

        // Set love status
        if (userLocalStore.getLoggedInUser().getUserID() == 0) {
            holder.imageButtonLove.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        } else if (currentItem.getIsClicked() == 1) {
            holder.imageButtonLove.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp);
        } else {
            holder.imageButtonLove.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        }

        // Set listener for love button
        if (userLocalStore.getLoggedInUser().getUserID() != 0) {
            holder.layoutLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (currentItem.getIsClicked() == 0) {
                        // If the user clicks love, add to their love list and increment total love
                        insertAndUpdateLoveRetrofit(userLocalStore.getLoggedInUser().getUserID(), currentItem.getPerfectPhotoID());
                        currentItem.setIsClicked(1);
                        currentItem.setLoveNum(currentItem.getLoveNum() + 1);
                        holder.imageButtonLove.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp);
                    } else {
                        // If the user clicks love, remove from their love list and decrement total love
                        removeAndUpdateLoveRetrofit(userLocalStore.getLoggedInUser().getUserID(), currentItem.getPerfectPhotoID());
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
        return perfectPhotoModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvCountry, tvTime, tvLoveNum, tvCommentNum, tvPost;
        private CircleImageView profileImage;
        private RelativeLayout relativePerfect;
        private LinearLayout layoutLove, layoutComment, layoutMore;
        private ImageView imagePerfect, imageButtonLove;

        ViewHolder(View itemView) {
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
                    int position = getAdapterPosition();
                    PerfectPhotoModel clickedItem = perfectPhotoModels.get(position);

                    if (clickedItem.getUserID() != userLocalStore.getLoggedInUser().getUserID()) {
                        Intent postInfo = new Intent(context, OtherUserProfile.class);
                        postInfo.putExtra("userID", clickedItem.getUserID());
                        context.startActivity(postInfo);
                    }
                }
            });

            // User clicks on comment button
            layoutComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    PerfectPhotoModel clickedItem = perfectPhotoModels.get(position);

                    Intent intent = new Intent(context, CommentPerfectPhotoActivity.class);
                    intent.putExtra("photoID", clickedItem.getPerfectPhotoID());
                    intent.putExtra("ownerID", clickedItem.getUserID());
                    context.startActivity(intent);
                }
            });

            layoutMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    PerfectPhotoModel clickedItem = perfectPhotoModels.get(position);
                    if (clickedItem.getUserID() == userLocalStore.getLoggedInUser().getUserID()) {
                        showDialogOwner(clickedItem.getPerfectPhotoID(), clickedItem.getPost(), clickedItem.getPath(),
                                clickedItem.getDeletePath());
                    } else {
                        showDialogNormal();
                    }
                }
            });
        }
    }

    /**
     * Refresh the adapter/recycler view with new data
     * @param photoModels           the new data
     */
    public void swap(List<PerfectPhotoModel> photoModels) {
        this.perfectPhotoModels.clear();
        this.perfectPhotoModels.addAll(photoModels);
        this.notifyDataSetChanged();
    }

    private void insertAndUpdateLoveRetrofit(int userID, int photoID) {

        HashMap<String, String> photoInfo = new HashMap<>();
        photoInfo.put("userID", Integer.toString(userID));
        photoInfo.put("photoID", Integer.toString(photoID));

        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .insertAndUpdateLovePhoto()
                .insertAndUpdateLovePhoto(photoInfo);
        retrofit.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayErrorToast(context);
            }
        });
    }

    private void removeAndUpdateLoveRetrofit(int userID, int photoID) {

        HashMap<String, String> photoInfo = new HashMap<>();
        photoInfo.put("userID", Integer.toString(userID));
        photoInfo.put("photoID", Integer.toString(photoID));

        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .removeAndUpdateLovePhoto()
                .removeAndUpdateLovePhoto(photoInfo);
        retrofit.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayErrorToast(context);
            }
        });

    }

    /**
     * Owner pop up dialog includes options to edit and delete their discussion post
     */
    private void showDialogOwner(final int photoID, final String post, final String picPath, final String deletePath) {
        CharSequence options[] = new CharSequence[] {"Edit", "Delete"};

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
                        fragment.startActivityForResult(intent, 1); // request code 1
                        break;
                    case 1:
                        // Delete
                        final ProgressDialog progressDialog = Helpers.showProgressDialog(context, "Deleting...");
                        removePerfectPhoto(photoID, picPath, deletePath, progressDialog);
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

    private void removePerfectPhoto(int photoID, String picPath, String deletePath, final ProgressDialog progressDialog) {
        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .deletePerfectPhoto()
                .deletePerfectPhoto(photoID, picPath, deletePath);
        retrofit.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Helpers.hideProgressDialog(progressDialog);

                if (response.body().get("status").getAsInt() == 0) {
                    Helpers.displayToast(context, "Error");
                } else {
                    // refresh the comment page
                    if (fragment instanceof PerfectPhotoFragment) {
                        PerfectPhotoFragment perfectPhotoFragment = (PerfectPhotoFragment) fragment;
                        perfectPhotoFragment.fetchPerfectPhotosRefresh();
                    }
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
