package ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
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

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.EditPostActivity;
import ravtrix.backpackerbuddy.activities.OtherUserProfile;
import ravtrix.backpackerbuddy.activities.UserMainPage;
import ravtrix.backpackerbuddy.fragments.Activity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserCountrySingleton;
import ravtrix.backpackerbuddy.interfaces.FragActivityResetDrawer;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.BackgroundImage;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.data.FeedItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 2/25/16.
 */
public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    private BackgroundImage backgroundImage;
    private int loggedInUser;
    private FragmentManager fragmentManager;
    private FragActivityResetDrawer fragActivityResetDrawer;
    private UserLocalStore userLocalStore;
    private Button bEditPost, bDeletePost, bReportPost;

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems, int loggedInUser) {
        inflater = LayoutInflater.from(activity.getContext());
        this.activity = activity;
        this.feedItems = feedItems;
        this.loggedInUser = loggedInUser;
        this.fragActivityResetDrawer = (FragActivityResetDrawer) activity.getActivity();
        userLocalStore = new UserLocalStore(activity.getContext());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Custom root of recycle view
        View view = inflater.inflate(R.layout.item_countryfeed, parent, false);
        // Hold a structure of a view. See class viewholder, which holds the structure
        ViewHolder holder = new ViewHolder(view, activity.getContext());
        backgroundImage = new BackgroundImage();

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // Initialize fonts
        Typeface countryFont = Typeface.createFromAsset(activity.getActivity().getAssets(), "Trench.otf");
        Typeface dateFont = Typeface.createFromAsset(activity.getActivity().getAssets(), "Date.ttf");

        holder.tvCountry.setTypeface(countryFont);
        holder.tvFromDate.setTypeface(dateFont);
        holder.tvToDate.setTypeface(dateFont);
        holder.tvArrow.setTypeface(dateFont);

        final FeedItem currentPos = feedItems.get(position);
        holder.tvCountry.setText(currentPos.getCountry());
        holder.tvFromDate.setText(currentPos.getFromDate());
        holder.tvToDate.setText(currentPos.getToDate());

        // Set image background based on country name. Hash will return the correct background id
        if (backgroundImage.getBackgroundFromHash(currentPos.getCountry()) != 0) {
            holder.backgroundLayout.setBackgroundResource(backgroundImage.getBackgroundFromHash(currentPos.getCountry()));
        }

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
                    retrofitRemoveFromFavoriteList(currentPos.getId(), activity.getContext());
                    currentPos.setClicked(0);
                    holder.imageButtonStar.setImageResource(R.drawable.ic_star_border_white_36dp);
                } else {
                    // Insert to favorite list - database/ local model
                    retrofitInsertToFavoriteList(currentPos.getId(), activity.getContext());
                    currentPos.setClicked(1);
                    holder.imageButtonStar.setImageResource(R.drawable.ic_star_border_yellow_36dp);
                }
            }
        });

        holder.imgbEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
                setPopUpDialogItemListener(currentPos.getId(), currentPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    // Holder knows and references where the fields are
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCountry, tvFromDate, tvToDate, tvArrow;
        private LinearLayout backgroundLayout;
        private ImageButton imageButtonStar, imageButtonMail, imgbEditPost;
        private static final int NAVIGATION_ITEM = 4;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
            tvFromDate = (TextView) itemView.findViewById(R.id.tvFromDate);
            tvToDate = (TextView) itemView.findViewById(R.id.tvToDate);
            tvArrow = (TextView) itemView.findViewById(R.id.tvArrow);
            backgroundLayout = (LinearLayout) itemView.findViewById(R.id.backgroundLayout);
            imageButtonStar = (ImageButton) itemView.findViewById(R.id.imageButtonStar);
            imageButtonMail = (ImageButton) itemView.findViewById(R.id.imageButtonMail);
            imgbEditPost = (ImageButton) itemView.findViewById(R.id.imgbEditPost);

            backgroundLayout.setOnClickListener(new View.OnClickListener() {
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

    private void performFragTransaction(final Activity activity, final int navigationItem) {
        // Delay to avoid lag when changing between option in navigation drawer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Reset navigation drawer selected items. Clear all
                fragActivityResetDrawer.onResetDrawer();

                // Perform fragment replacement
                fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        ((UserMainPage) activity.getActivity()).getFragList().get(navigationItem)).commit();
                ((UserMainPage) activity.getActivity()).getDrawerLayout().closeDrawer(GravityCompat.START);
            }
        }, 150);
    }

    // Prepare a HashMap with userID and postID the logged in user want to do database operation to
    private HashMap<String, String> getHashMapWithInfo(int postID) {
        HashMap<String, String> favoriteInfoHashMap = new HashMap<>();
        favoriteInfoHashMap.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        favoriteInfoHashMap.put("postID", Integer.toString(postID));
        return favoriteInfoHashMap;
    }

    // Displaying the pop up to manage posts
    private void showPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
        LayoutInflater inflater = activity.getActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.pop_up,
                null);
        final AlertDialog dialog = builder.create();
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
        bReportPost = (Button) dialogLayout.findViewById(R.id.bReportPost);

        builder.setView(dialogLayout);
        dialog.show();
    }


    // Pop up dialog to manage posts
    private void setPopUpDialogItemListener(final int postID, final FeedItem currentItem) {

        bEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getContext(), EditPostActivity.class);
                intent.putExtra("country", currentItem.getCountry());
                intent.putExtra("from", currentItem.getFromDate());
                intent.putExtra("until", currentItem.getToDate());
                activity.getActivity().startActivity(intent);
            }
        });
        bDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<JsonObject> jsonObjectCall = RetrofitUserCountrySingleton.getRetrofitUserCountry().removePost().removePost(postID);
                jsonObjectCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        activity.getActivity().startActivity(new Intent(activity.getContext(), UserMainPage.class));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });

        bReportPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICKED ON REPORT BUTTON");
            }
        });
    }


    // Retrofit

    // Insert into favorite list in database
    private void retrofitInsertToFavoriteList(int postID, final Context context) {
        HashMap<String, String> favoriteInfoHashMap = getHashMapWithInfo(postID);

        Call<JsonObject> jsonObjectCall = RetrofitUserCountrySingleton
                .getRetrofitUserCountry()
                .insertFavoritePost()
                .insertFavorite(favoriteInfoHashMap);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 1) {
                    // Success message from server
                    Helpers.displayToast(context, "Added to watch list");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    // Remove from favorite list in database
    private void retrofitRemoveFromFavoriteList(int postID, final Context context) {
        HashMap<String, String> favoriteInfoHashMap = getHashMapWithInfo(postID);

        Call<JsonObject> jsonObjectCall = RetrofitUserCountrySingleton
                .getRetrofitUserCountry()
                .removeFavoritePost()
                .removeFavorite(favoriteInfoHashMap);

        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 1) {
                    // Success message from server
                    Helpers.displayToast(context, "Removed from watch list");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
