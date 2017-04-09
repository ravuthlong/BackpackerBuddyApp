package ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.editpost.EditPostActivity;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.fragments.userdestinationfrag.CountryTabFragment;
import ravtrix.backpackerbuddy.fragments.userdestinationfrag.countrybytime.CountryRecentFragment;
import ravtrix.backpackerbuddy.fragments.userprofile.UserProfileFragment;
import ravtrix.backpackerbuddy.helpers.RetrofitUserCountrySingleton;
import ravtrix.backpackerbuddy.interfacescom.FragActivityResetDrawer;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.BackgroundImage;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.data.FeedItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ravtrix.backpackerbuddy.R.id.imgbEditPost;

/**
 * Created by Ravinder on 2/25/16.
 */
public class FeedListAdapterMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private CountryRecentFragment activity;
    private List<FeedItem> feedItems;
    private BackgroundImage backgroundImage;
    private final int loggedInUser;
    private FragmentManager fragmentManager;
    private FragActivityResetDrawer fragActivityResetDrawer;
    private Button bEditPost, bDeletePost;

    private OnLoadMoreListener onLoadMoreListener;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    private AlertDialog dialog;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public FeedListAdapterMain(CountryRecentFragment activity, int loggedInUser,
                               OnLoadMoreListener onLoadMoreListener) {

        this.activity = activity;
        this.loggedInUser = loggedInUser;
        this.fragActivityResetDrawer = (FragActivityResetDrawer) activity.getActivity();
        this.onLoadMoreListener = onLoadMoreListener;
        backgroundImage = new BackgroundImage();
        this.feedItems = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            return new ViewHolder(LayoutInflater.from(activity.getContext()).inflate(R.layout.item_countryfeed, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(activity.getContext()).inflate(R.layout.item_progress, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            final FeedItem currentPos = feedItems.get(position);

            initVisibilityItems(holder, currentPos);
            initTypeface(holder);
            initTextInfo(holder, currentPos);
            initProfileImage(holder, currentPos);
            initBackgroundImage(holder, currentPos);
            initEditPostListener(holder, currentPos);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;

        if (feedItems.get(position) != null) {
            viewType = VIEW_ITEM;
        } else if (feedItems.get(position) == null && feedItems.size() >= 10) {
            viewType = VIEW_PROG;
        }

        return viewType;
        //return feedItems.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pBar;
        ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.pBar);
        }
    }


    // Holder knows and references where the fields are
    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCountry, tvFromDate, tvToDate, tvArrow;
        private LinearLayout backgroundLayout;
        private ImageButton imgEditPost;
        private CircleImageView profileImage;
        private static final int NAVIGATION_ITEM = 4;

        ViewHolder(View itemView) {
            super(itemView);
            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
            tvFromDate = (TextView) itemView.findViewById(R.id.tvFromDate);
            tvToDate = (TextView) itemView.findViewById(R.id.tvToDate);
            tvArrow = (TextView) itemView.findViewById(R.id.tvArrow);
            backgroundLayout = (LinearLayout) itemView.findViewById(R.id.backgroundLayout);
            imgEditPost = (ImageButton) itemView.findViewById(imgbEditPost);
            profileImage = (CircleImageView) itemView.findViewById(R.id.item_countryFeed_profileImage);

            Typeface dateFont = Typeface.createFromAsset(activity.getActivity().getAssets(), "Text.ttf");
            tvFromDate.setTypeface(dateFont);
            tvToDate.setTypeface(dateFont);
            tvArrow.setTypeface(dateFont);

            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    FeedItem clickedItem = feedItems.get(position);

                    if (clickedItem.getUserID() == loggedInUser) {
                        performFragTransaction(activity, NAVIGATION_ITEM);
                    } else {
                        Intent postInfo = new Intent(activity.getActivity(), OtherUserProfile.class);
                        postInfo.putExtra("postID", clickedItem.getId());
                        postInfo.putExtra("userID", clickedItem.getUserID());
                        activity.startActivity(postInfo);
                    }
                }
            });
        }
    }

    public void addAll(List<FeedItem> feedItems){
        this.feedItems.clear();
        this.feedItems.addAll(feedItems);
        notifyDataSetChanged();
    }

    // Add 10 more items to feed list
    public void addItemMore(List<FeedItem> feedTen){
        int currentSize = feedItems.size();
        this.feedItems.addAll(feedTen);
        notifyItemRangeChanged(currentSize, feedItems.size());
    }


    public void resetAll() {
        this.visibleThreshold = 1;
        this.feedItems.clear();
        this.onLoadMoreListener = null;
        this.firstVisibleItem = 0;
        this.visibleItemCount = 0;
        this.totalItemCount = 0;
    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading = isMoreLoading;
    }


    /* Controlling of adding and removing spinner for loading more items in recycler view
     * feedItem adds null so viewholder can inflate the spinner
     */
    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    feedItems.add(null);
                    notifyItemInserted(feedItems.size() - 1);
                }
            });
        } else {
            feedItems.remove(feedItems.size() - 1);
            notifyItemRemoved(feedItems.size());
        }
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public void setRecyclerView(RecyclerView mView){

        if (feedItems.size() >= 10) {
            mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = mLinearLayoutManager.getItemCount();
                    firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (!isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) { // Load more items
                            onLoadMoreListener.onLoadMore();
                        }
                        isMoreLoading = true;
                    }
                }
            });
        }
    }

    private void performFragTransaction(final CountryRecentFragment activity, final int navigationItem) {
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

    // Prepare a HashMap with userID and postID the logged in user want to do database operation to
    private static HashMap<String, String> getHashMapWithInfo(int postID, final int loggedInUser) {
        HashMap<String, String> favoriteInfoHashMap = new HashMap<>();
        favoriteInfoHashMap.put("userID", Integer.toString(loggedInUser));
        favoriteInfoHashMap.put("postID", Integer.toString(postID));
        return favoriteInfoHashMap;
    }

    // Displaying the pop up to manage posts
    private void showPopUp(int postUserID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
        LayoutInflater inflater = activity.getActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.pop_up_managepost,
                null);
        dialog = builder.create();
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

        if (loggedInUser == postUserID) {
            bDeletePost.setVisibility(View.VISIBLE);
            bEditPost.setVisibility(View.VISIBLE);
        }

        builder.setView(dialogLayout);
        dialog.show();
    }


    // Pop up dialog to manage posts
    private void setPopUpDialogItemListener(final int postID, final FeedItem currentItem) {

        bEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                Intent intent = new Intent(activity.getContext(), EditPostActivity.class);
                intent.putExtra("country", currentItem.getCountry());
                intent.putExtra("from", currentItem.getFromDate());
                intent.putExtra("until", currentItem.getToDate());
                intent.putExtra("postID", currentItem.getId());
                intent.putExtra("returnActivity", 0); // 0 = back to main activity posts
                activity.startActivityForResult(intent, 1);
            }
        });
        bDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                Call<JsonObject> jsonObjectCall = RetrofitUserCountrySingleton.getRetrofitUserCountry().removePost().removePost(postID);
                jsonObjectCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        // Go to Country Recent fragment and refresh after deletion
                        CountryTabFragment parentTab = (CountryTabFragment) activity.getParentFragment();
                        parentTab.refreshAUserCountryTab();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void initVisibilityItems(RecyclerView.ViewHolder holder, FeedItem currentPos) {
        if (loggedInUser == currentPos.getUserID()) {
            ((ViewHolder) holder).imgEditPost.setVisibility(View.VISIBLE);
        } else {
            ((ViewHolder) holder).imgEditPost.setVisibility(View.GONE);
        }
    }

    private void initTypeface(RecyclerView.ViewHolder holder) {
        // Initialize fonts
        Typeface countryFont = Typeface.createFromAsset(activity.getActivity().getAssets(), "Monu.otf");
        ((ViewHolder) holder).tvCountry.setTypeface(countryFont);
    }

    private void initTextInfo(RecyclerView.ViewHolder holder, FeedItem currentPos) {
        // Prevent long country names from getting cut from the screen. Adjust text size.
        if (currentPos.getCountry() != null && currentPos.getCountry().length() >= 15) {
            ((ViewHolder) holder).tvCountry.setTextSize(30);
        } else {
            ((ViewHolder) holder).tvCountry.setTextSize(45);
        }
        ((ViewHolder) holder).tvCountry.setText(currentPos.getCountry());
        ((ViewHolder) holder).tvFromDate.setText(getDateFromInReadFormat(currentPos));
        ((ViewHolder) holder).tvToDate.setText(getDateToInReadFormat(currentPos));
    }


    private String getDateToInReadFormat(FeedItem currentPos) {

        String[] dateSplitsTo = currentPos.getToDate().split("/");
        String monthToDisplayFrom = "";

        switch (dateSplitsTo[0]) {
            case "01":
                monthToDisplayFrom = "Jan";
                break;
            case "02":
                monthToDisplayFrom = "Feb";
                break;
            case "03":
                monthToDisplayFrom = "March";
                break;
            case "04":
                monthToDisplayFrom = "April";
                break;
            case "05":
                monthToDisplayFrom = "May";
                break;
            case "06":
                monthToDisplayFrom = "June";
                break;
            case "07":
                monthToDisplayFrom = "July";
                break;
            case "08":
                monthToDisplayFrom = "Aug";
                break;
            case "09":
                monthToDisplayFrom = "Sept";
                break;
            case "10":
                monthToDisplayFrom = "Oct";
                break;
            case "11":
                monthToDisplayFrom = "Nov";
                break;
            case "12":
                monthToDisplayFrom = "Dec";
                break;
            default:
        }
        return monthToDisplayFrom + "/" + dateSplitsTo[1] + "/" + dateSplitsTo[2];
    }

    private String getDateFromInReadFormat(FeedItem currentPos) {
        String[] dateSplitsFrom = currentPos.getFromDate().split("/");
        String monthToDisplayFrom = "";

        switch (dateSplitsFrom[0]) {
            case "01":
                monthToDisplayFrom = "Jan";
                break;
            case "02":
                monthToDisplayFrom = "Feb";
                break;
            case "03":
                monthToDisplayFrom = "March";
                break;
            case "04":
                monthToDisplayFrom = "April";
                break;
            case "05":
                monthToDisplayFrom = "May";
                break;
            case "06":
                monthToDisplayFrom = "June";
                break;
            case "07":
                monthToDisplayFrom = "July";
                break;
            case "08":
                monthToDisplayFrom = "Aug";
                break;
            case "09":
                monthToDisplayFrom = "Sept";
                break;
            case "10":
                monthToDisplayFrom = "Oct";
                break;
            case "11":
                monthToDisplayFrom = "Nov";
                break;
            case "12":
                monthToDisplayFrom = "Dec";
                break;
            default:
        }
        return monthToDisplayFrom + "/" + dateSplitsFrom[1] + "/" + dateSplitsFrom[2];
    }

    private void initProfileImage(RecyclerView.ViewHolder holder, FeedItem currentPos) {
        Picasso.with(activity.getContext()).load(currentPos.getUserpic())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.default_photo)
                .into(((ViewHolder) holder).profileImage);
    }

    private void initBackgroundImage(RecyclerView.ViewHolder holder, FeedItem currentPos) {
        // Set image background based on country name. Hash will return the correct background id
        if (backgroundImage.getBackgroundFromHash(currentPos.getCountry()) != 0) {
            ((ViewHolder) holder).backgroundLayout
                    .setBackgroundResource(backgroundImage.getBackgroundFromHash(currentPos.getCountry()));
        }
    }

    private void initEditPostListener(final RecyclerView.ViewHolder holder, final FeedItem currentPos) {
        ((ViewHolder) holder).imgEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(currentPos.getUserID());
                setPopUpDialogItemListener(currentPos.getId(), currentPos);
            }
        });
    }
}
