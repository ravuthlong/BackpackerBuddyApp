package ravtrix.backpackerbuddy.recyclerviewfeed.auserfavrecyclerview.adapter;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.chat.ConversationActivity;
import ravtrix.backpackerbuddy.fragments.managedestination.auserfavoriteposts.AUserFavPostsFragment;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.BackgroundImage;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.adapter.FeedListAdapterMain;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.data.FeedItem;

import static ravtrix.backpackerbuddy.R.id.imgbEditPost;

/**
 * Created by Ravinder on 12/13/16.
 */

public class FeedListAdapterUserFav extends RecyclerView.Adapter<FeedListAdapterUserFav.ViewHolder> {

    private List<FeedItem> feedItemAUserFav;
    private LayoutInflater inflater;
    private AUserFavPostsFragment fragment;
    private int loggedInUser;
    private BackgroundImage backgroundImage;

    public FeedListAdapterUserFav(AUserFavPostsFragment fragment, List<FeedItem> feedItemAUserFav, int loggedInUser) {
        this.feedItemAUserFav = feedItemAUserFav;
        this.fragment = fragment;
        this.inflater = LayoutInflater.from(fragment.getContext());
        this.loggedInUser = loggedInUser;
        backgroundImage = new BackgroundImage();
    }

    @Override
    public FeedListAdapterUserFav.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_countryfeed, parent, false);
        // Hold a structure of a view. See class viewholder, which holds the structure
        return new FeedListAdapterUserFav.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        // Initialize fonts
        Typeface countryFont = Typeface.createFromAsset(fragment.getActivity().getAssets(), "Trench.otf");
        Typeface dateFont = Typeface.createFromAsset(fragment.getActivity().getAssets(), "Date.ttf");

        holder.tvCountry.setTypeface(countryFont);
        holder.tvFromDate.setTypeface(dateFont);
        holder.tvToDate.setTypeface(dateFont);
        holder.tvArrow.setVisibility(View.GONE);

        final FeedItem currentPos = feedItemAUserFav.get(position);

        holder.tvCountry.setText(currentPos.getCountry());
        holder.tvFromDate.setText(currentPos.getFromDate());
        holder.tvToDate.setText(currentPos.getToDate());

        // Set image background based on country name. Hash will return the correct background id
        if (backgroundImage.getBackgroundFromHash(currentPos.getCountry()) != 0) {
            holder.backgroundLayout.setBackgroundResource(backgroundImage.getBackgroundFromHash(currentPos.getCountry()));
        }

        Picasso.with(fragment.getContext()).load("http://backpackerbuddy.net23.net/profile_pic/" +
                currentPos.getUserID() + ".JPG")
                .resize(400, 400)
                .centerCrop()
                .placeholder(R.drawable.default_photo)
                .into(holder.profileImage);

        if (currentPos.getClicked() == 1) {
            holder.imageButtonStar.setImageResource(R.drawable.ic_star_border_yellow_36dp);
            currentPos.isFavorite();
        } else {
            holder.imageButtonStar.setImageResource(R.drawable.ic_star_border_white_36dp);
        }

        holder.imageButtonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentPos.getClicked() == 1) {
                    // Cancel from favorite list - database/local model
                    FeedListAdapterMain.retrofitRemoveFromFavoriteList(currentPos.getId(), fragment.getContext(), loggedInUser);
                    currentPos.setClicked(0);
                    holder.imageButtonStar.setImageResource(R.drawable.ic_star_border_white_36dp);
                } else {
                    // Insert to favorite list - database/ local model
                    FeedListAdapterMain.retrofitInsertToFavoriteList(currentPos.getId(), fragment.getContext(), loggedInUser);
                    currentPos.setClicked(1);
                    holder.imageButtonStar.setImageResource(R.drawable.ic_star_border_yellow_36dp);
                }
            }
        });

        holder.imageButtonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loggedInUser == currentPos.getUserID()) {
                    Helpers.displayToast(fragment.getContext(), "Can't message yourself...");
                } else {
                    Intent convoIntent = new Intent(fragment.getContext(), ConversationActivity.class);
                    convoIntent.putExtra("otherUserID", Integer.toString(currentPos.getUserID()));
                    fragment.startActivity(convoIntent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return feedItemAUserFav.size();
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
}
