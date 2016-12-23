package ravtrix.backpackerbuddy.recyclerviewfeed.ausercountryrecyclerview.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.editpost.EditPostActivity;
import ravtrix.backpackerbuddy.fragments.managedestination.ausercountryposts.AUserCountryPostsFragment;
import ravtrix.backpackerbuddy.fragments.userdestinationfrag.countrybytime.CountryRecentFragment;
import ravtrix.backpackerbuddy.helpers.RetrofitUserCountrySingleton;
import ravtrix.backpackerbuddy.recyclerviewfeed.ausercountryrecyclerview.data.FeedItemAUserCountry;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.BackgroundImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ravtrix.backpackerbuddy.R.id.imgbEditPost;

/**
 * Created by Ravinder on 10/8/16.
 */

public class FeedListAdapterAUserPosts extends RecyclerView.Adapter<FeedListAdapterAUserPosts.ViewHolder> {

    private AUserCountryPostsFragment fragment;
    private LayoutInflater inflater;
    private List<FeedItemAUserCountry> feedItemAUserCountries;
    private BackgroundImage backgroundImage;
    private Button bEditPost, bDeletePost;

    public FeedListAdapterAUserPosts(AUserCountryPostsFragment fragment,
                                     List<FeedItemAUserCountry> feedItems) {
        inflater = LayoutInflater.from(fragment.getContext());
        this.fragment = fragment;
        this.feedItemAUserCountries = feedItems;
        backgroundImage = new BackgroundImage();
    }

    @Override
    public FeedListAdapterAUserPosts.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Custom root of recycle view
        View view = inflater.inflate(R.layout.item_countryfeed, parent, false);
        // Hold a structure of a view. See class viewholder, which holds the structure
        return new FeedListAdapterAUserPosts.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedListAdapterAUserPosts.ViewHolder holder, int position) {

        // Initialize fonts
        Typeface countryFont = Typeface.createFromAsset(fragment.getActivity().getAssets(), "Trench.otf");
        Typeface dateFont = Typeface.createFromAsset(fragment.getActivity().getAssets(), "Date.ttf");

        holder.tvCountry.setTypeface(countryFont);
        holder.tvFromDate.setTypeface(dateFont);
        holder.tvToDate.setTypeface(dateFont);
        holder.tvArrow.setTypeface(dateFont);

        final FeedItemAUserCountry currentPos = feedItemAUserCountries.get(position);

        holder.tvCountry.setText(currentPos.getCountry());
        holder.tvFromDate.setText(currentPos.getFromDate());
        holder.tvToDate.setText(currentPos.getToDate());

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        holder.profileImage.setColorFilter(filter);

        // Set image background based on country name. Hash will return the correct background id
        if (backgroundImage.getBackgroundFromHash(currentPos.getCountry()) != 0) {
            holder.backgroundLayout.setBackgroundResource(backgroundImage.getBackgroundFromHash(currentPos.getCountry()));
        }

        // Hide the mail and star buttons and profile image
        holder.imageButtonMail.setVisibility(View.GONE);
        holder.imageButtonStar.setVisibility(View.GONE);
        holder.profileImage.setVisibility(View.GONE);

        holder.imgEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(currentPos.getUserID());
                setPopUpDialogItemListener(currentPos.getId(), currentPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedItemAUserCountries.size();
    }

    // Holder knows and references where the fields are
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCountry, tvFromDate, tvToDate, tvArrow;
        private LinearLayout backgroundLayout;
        private ImageButton imageButtonStar, imageButtonMail, imgEditPost;
        private CircleImageView profileImage;

        ViewHolder(View itemView) {
            super(itemView);
            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
            tvFromDate = (TextView) itemView.findViewById(R.id.tvFromDate);
            tvToDate = (TextView) itemView.findViewById(R.id.tvToDate);
            tvArrow = (TextView) itemView.findViewById(R.id.tvArrow);
            backgroundLayout = (LinearLayout) itemView.findViewById(R.id.backgroundLayout);
            imageButtonStar = (ImageButton) itemView.findViewById(R.id.imageButtonStar);
            imageButtonMail = (ImageButton) itemView.findViewById(R.id.imageButtonMail);
            imgEditPost = (ImageButton) itemView.findViewById(imgbEditPost);
            profileImage = (CircleImageView) itemView.findViewById(R.id.item_countryFeed_profileImage);

        }
    }

    // Displaying the pop up to manage posts
    private void showPopUp(int postUserID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        LayoutInflater inflater = fragment.getActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.pop_up_managepost,
                null);
        final AlertDialog dialog = builder.create();
        assert dialog.getWindow() != null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.BOTTOM;
        window.setAttributes(lp);

        bEditPost = (Button) dialogLayout.findViewById(R.id.bEditPost);
        bDeletePost = (Button) dialogLayout.findViewById(R.id.bDeletePost);

        bDeletePost.setVisibility(View.VISIBLE);
        bEditPost.setVisibility(View.VISIBLE);


        builder.setView(dialogLayout);
        dialog.show();
    }


    // Pop up dialog to manage posts
    private void setPopUpDialogItemListener(final int postID, final FeedItemAUserCountry currentItem) {

        bEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment.getContext(), EditPostActivity.class);
                intent.putExtra("country", currentItem.getCountry());
                intent.putExtra("from", currentItem.getFromDate());
                intent.putExtra("until", currentItem.getToDate());
                fragment.getActivity().startActivity(intent);
            }
        });
        bDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<JsonObject> jsonObjectCall = RetrofitUserCountrySingleton.getRetrofitUserCountry().removePost().removePost(postID);
                jsonObjectCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        android.support.v4.app.FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, new CountryRecentFragment()).commit();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });
    }

}
