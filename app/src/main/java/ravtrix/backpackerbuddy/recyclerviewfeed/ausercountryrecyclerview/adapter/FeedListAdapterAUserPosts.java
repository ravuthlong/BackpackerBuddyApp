package ravtrix.backpackerbuddy.recyclerviewfeed.ausercountryrecyclerview.adapter;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.fragments.ausercountryposts.AUserCountryPostsFragment;
import ravtrix.backpackerbuddy.recyclerviewfeed.ausercountryrecyclerview.data.FeedItemAUserCountry;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.BackgroundImage;

import static ravtrix.backpackerbuddy.R.id.imgbEditPost;

/**
 * Created by Ravinder on 10/8/16.
 */

public class FeedListAdapterAUserPosts extends RecyclerView.Adapter<FeedListAdapterAUserPosts.ViewHolder> {

    private AUserCountryPostsFragment fragment;
    private LayoutInflater inflater;
    private List<FeedItemAUserCountry> feedItemAUserCountries;
    private int loggedInUser;
    private BackgroundImage backgroundImage;

    public FeedListAdapterAUserPosts(AUserCountryPostsFragment fragment,
                                     List<FeedItemAUserCountry> feedItems, int loggedInUser) {
        inflater = LayoutInflater.from(fragment.getContext());
        this.fragment = fragment;
        this.feedItemAUserCountries = feedItems;
        this.loggedInUser = loggedInUser;
        backgroundImage = new BackgroundImage();

    }

    @Override
    public FeedListAdapterAUserPosts.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Custom root of recycle view
        View view = inflater.inflate(R.layout.item_countryfeed, parent, false);
        // Hold a structure of a view. See class viewholder, which holds the structure
        FeedListAdapterAUserPosts.ViewHolder holder = new FeedListAdapterAUserPosts.ViewHolder(view,
                fragment.getContext());
        return holder;
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

        ViewHolder(View itemView, final Context context) {
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

}
