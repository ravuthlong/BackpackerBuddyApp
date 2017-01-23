package ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.adapter;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.bucketlist.editbucket.EditBucketActivity;
import ravtrix.backpackerbuddy.fragments.bucketlist.BucketListFrag;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserBucketListSingleton;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/1/17.
 */

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder> {

    private Context context;
    private List<BucketListModel> bucketListModels;
    private LayoutInflater inflater;
    private BucketListFrag bucketListFrag;

    public BucketListAdapter(Context context, BucketListFrag bucketListFrag, List<BucketListModel> bucketListModels) {
        this.context = context;
        this.bucketListModels = bucketListModels;
        this.bucketListFrag = bucketListFrag;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bucket_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BucketListModel currentItem = bucketListModels.get(position);
        holder.bucketPost.setText(currentItem.getPost());

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                currentItem.getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.tvTime.setText(timeAgo);

        // Check if the user has completed this bucket list item. Set image accordingly.
        if (currentItem.getStatus() == 1) {
            holder.imgStatus.setImageResource(R.drawable.ic_done_white_24dp);
        } else {
            holder.imgStatus.setImageResource(R.drawable.ic_done_incomplete_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return bucketListModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout bucketRelative;
        private TextView bucketPost, tvTime;
        private CircleImageView imgStatus;

        ViewHolder(View itemView) {
            super(itemView);

            bucketRelative = (RelativeLayout) itemView.findViewById(R.id.item_relative);
            bucketPost = (TextView) itemView.findViewById(R.id.item_tv_bucket);
            tvTime = (TextView) itemView.findViewById(R.id.item_tvBucket_time);
            imgStatus = (CircleImageView) itemView.findViewById(R.id.frag_bucket_circleImage);

            Helpers.overrideFonts(context, bucketRelative);


            // If it is null, a null argument was passed, which mean it was used in the OtherUserBucketListActivity
            // so it doesn't need listeners.
            if (bucketListFrag != null) {
                bucketRelative.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        int position = getAdapterPosition();
                        BucketListModel clickedItem = bucketListModels.get(position);

                        showManageDialog(clickedItem.getBucketID(), clickedItem.getPost());
                        return true;
                    }
                });
            }

            if (bucketListFrag != null) {
                imgStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        BucketListModel clickedItem = bucketListModels.get(position);

                        if (clickedItem.getStatus() == 0) {
                            // status not complete
                            showStatusDialogBucketStatus("Goal has been completed?", clickedItem.getBucketID(),
                                    clickedItem.getStatus());
                        } else {
                            // status complete
                            showStatusDialogBucketStatus("Goal not yet completed?", clickedItem.getBucketID(),
                                    clickedItem.getStatus());
                        }
                    }
                });
            }
        }
    }

    /**
     *  Swap the adapter's Bucket List models with new models for refreshing
     * @param models            the new models to be used
     */
    public void swap(List<BucketListModel> models){
        bucketListModels.clear();
        bucketListModels.addAll(models);
        this.notifyDataSetChanged();
    }

    /**
     * Show status dialog to change the status of bucket list item completion
     * @param message           the message to be displayed on alert dialog
     * @param bucketID          the unique if of the bucket list item
     * @param status            the new status to be changed to
     */
    private void showStatusDialogBucketStatus(String message, final int bucketID, final int status) {
        AlertDialog.Builder builder = Helpers.showAlertDialogWithTwoOptions((Activity) context, null, message, "No");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (status == 0) {
                    // Was 0, now bucket complete so change to 1
                    retrofitUpdateBucketStatus(bucketID, 1);
                } else {
                    // Was 1, now bucket changed back to incomplete so change to 0
                    retrofitUpdateBucketStatus(bucketID, 0);
                }
            }
        });
        builder.show();
    }

    /**
     * Dialog with two options to manage the bucket list item (Delete and Edit)
     * @param bucketID      the unique id of the bucket list item
     * @param post          the new post to edit to
     */
    private void showManageDialog(final int bucketID, final String post) {
        CharSequence options[] = new CharSequence[] {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i) {
                    case 0:
                        Intent intent = new Intent(context, EditBucketActivity.class);
                        intent.putExtra("bucketID", bucketID);
                        intent.putExtra("post", post);
                        bucketListFrag.startActivityForResult(intent, 1);
                        break;
                    case 1:
                        Call<JsonObject> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                                .deleteBucket()
                                .deleteBucket(bucketID);

                        retrofit.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.body().get("status").getAsInt() == 1) {
                                    bucketListFrag.fetchUserBucketListRefresh();
                                } else {
                                    Helpers.displayToast(context, "Error");
                                }
                            }
                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Helpers.displayToast(context, "Error");
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
     * Change the status of the bucket list item
     * @param bucketID      the unique id of the bucket list item
     * @param status        the new status to change to (0 or 1)
     *                      0 is incomplete, 1 is complete
     */
    private void retrofitUpdateBucketStatus(int bucketID, int status) {

        final ProgressDialog progressDialog = Helpers.showProgressDialog(context, "Updating...");

        Call<JsonObject> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .updateBucketStatus()
                .updateBucketStatus(bucketID, status);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Helpers.hideProgressDialog(progressDialog);
                if (response.body().get("status").getAsInt() == 1) {
                    bucketListFrag.fetchUserBucketListRefresh();
                } else {
                    Helpers.displayToast(context, "Error");
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