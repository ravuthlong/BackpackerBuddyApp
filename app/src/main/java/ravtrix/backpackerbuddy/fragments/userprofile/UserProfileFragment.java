package ravtrix.backpackerbuddy.fragments.userprofile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.editinfo.EditInfoActivity;
import ravtrix.backpackerbuddy.activities.editphoto.EditPhotoActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.interfacescom.FragActivityUpdateProfilePic;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ravinder on 8/18/16.
 */
public class UserProfileFragment extends Fragment implements View.OnClickListener, IUserProfileView {

    @BindView(R.id.profile_image_profile) protected CircleImageView profilePic;
    @BindView(R.id.frag_user_profile_etLayout1) protected LinearLayout editLayout;
    @BindView(R.id.frag_user_profile_etLayout2) protected LinearLayout editLayout2;
    @BindView(R.id.frag_user_profile_etLayout3) protected LinearLayout editLayout3;
    @BindView(R.id.frag_user_profile_etLayout4) protected LinearLayout editLayout4;
    @BindView(R.id.ll_edit) protected LinearLayout editLayoutSub1;
    @BindView(R.id.ll_edit2) protected LinearLayout editLayoutSub2;
    @BindView(R.id.ll_edit3) protected LinearLayout editLayoutSub3;
    @BindView(R.id.ll_edit4) protected LinearLayout editLayoutSub4;
    @BindView(R.id.title1) protected TextView title1;
    @BindView(R.id.title2) protected TextView title2;
    @BindView(R.id.title3) protected TextView title3;
    @BindView(R.id.title4) protected TextView title4;
    @BindView(R.id.hint1) protected TextView detailOne;
    @BindView(R.id.hint2) protected TextView detailTwo;
    @BindView(R.id.hint3) protected TextView detailThree;
    @BindView(R.id.hint4) protected TextView detailFour;
    @BindView(R.id.username_profile) protected TextView username;
    @BindView(R.id.imgbEditPhoto) protected ImageButton imgbEditPhoto;
    @BindView(R.id.user_profile_tvLocation) protected TextView tvLocation;
    @BindView(R.id.imgTravel) protected ImageView imgTravel;
    @BindView(R.id.imgNotTravel) protected ImageView imgNotTravel;
    @BindView(R.id.txtTravel) protected TextView txtTravel;
    @BindView(R.id.txtNotTravel) protected TextView txtNotTravel;
    @BindView(R.id.imgTravelStatusEdit) protected ImageView imgTravelStatusEdit;
    @BindView(R.id.relativeFragProfile) protected RelativeLayout relativeLayout;
    private UserLocalStore userLocalStore;
    private View v;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private FragActivityUpdateProfilePic fragActivityUpdateProfilePic;
    private boolean isDetailOneAHint = true;
    private boolean isDetailTwoAHint = true;
    private boolean isDetailThreeAHint = true;
    private boolean isDetailFourAHint = true;
    private UserProfilePresenter presenter;
    private ProgressDialog progressDialog;
    private int travelStatus;
    private boolean refreshPage = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
        this.fragActivityUpdateProfilePic = (FragActivityUpdateProfilePic) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_user_profile, container, false);
        v.setVisibility(View.INVISIBLE);
        fragActivityProgressBarInterface.setProgressBarVisible();
        ButterKnife.bind(this, v);

        userLocalStore = new UserLocalStore(getActivity());
        presenter = new UserProfilePresenter(this);

        setProfilePic();
        Helpers.overrideFonts(getActivity(), relativeLayout);
        setViewListeners();
        checkLocationUpdate();

        // Set user location
        setUserLocation(userLocalStore.getLoggedInUser().getLatitude(),
                userLocalStore.getLoggedInUser().getLongitude());
        presenter.getUserInfo(userLocalStore.getLoggedInUser().getUserID(),
                userLocalStore.getLoggedInUser().getUserImageURL());

        setTravelingStatus();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.frag_user_profile_etLayout1:
            case R.id.ll_edit:
                setIntentEditInto(title1.getText().toString(), detailOne.getText().toString(), "1");
                break;
            case R.id.frag_user_profile_etLayout2:
            case R.id.ll_edit2:
                setIntentEditInto(title2.getText().toString(), detailTwo.getText().toString(), "2");
                break;
            case R.id.frag_user_profile_etLayout3:
            case R.id.ll_edit3:
                setIntentEditInto(title3.getText().toString(), detailThree.getText().toString(), "3");
                break;
            case R.id.frag_user_profile_etLayout4:
            case R.id.ll_edit4:
                setIntentEditInto(title4.getText().toString(), detailFour.getText().toString(), "4");
                break;
            case R.id.imgbEditPhoto:
                startActivityForResult(new Intent(getActivity(), EditPhotoActivity.class), 1);
                break;
            case R.id.imgTravelStatusEdit:
                AlertDialog.Builder alertDialog = Helpers.showAlertDialogWithTwoOptions(getActivity(), "Travel Status",
                        "Change your traveling status?", "No");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog = Helpers.showProgressDialog(getContext(), "Updating...");

                        final HashMap<String, String> userInfo = new HashMap<>();
                        userInfo.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));

                        Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                                .updateTravelingStatus()
                                .updateTravelStatus(userInfo);

                        retrofit.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (userLocalStore.getLoggedInUser().getTraveling() == 0) {
                                    // User is now traveling
                                    imgNotTravel.setVisibility(View.GONE);
                                    txtNotTravel.setVisibility(View.GONE);
                                    imgTravel.setVisibility(View.VISIBLE);
                                    txtTravel.setVisibility(View.VISIBLE);
                                } else {
                                    // User no longer travels
                                    imgTravel.setVisibility(View.GONE);
                                    txtTravel.setVisibility(View.GONE);
                                    imgNotTravel.setVisibility(View.VISIBLE);
                                    txtNotTravel.setVisibility(View.VISIBLE);
                                }
                                // Change local store value for travel status
                                int newStatus = userLocalStore.getLoggedInUser().getTraveling() == 1 ? 0 : 1;
                                userLocalStore.changeTravelStat(newStatus);
                                Helpers.hideProgressDialog(progressDialog);
                            }
                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Helpers.displayToast(getContext(), "Error");
                            }
                        });
                    }
                });
                alertDialog.show();
                break;
            default:
        }
    }

    private void setUserLocation(double latitude, double longitude) {
        try {
            tvLocation.setText(Helpers.cityGeocoder(getContext(), latitude, longitude));
        } catch (IOException e) {
            // When the device failed to retrieve city and country information using Geocoder,
            // run google location API directly
            RetrieveCityCountryTask retrieveFeedTask = new RetrieveCityCountryTask(userLocalStore.getLoggedInUser().getLatitude().toString(),
                    userLocalStore.getLoggedInUser().getLongitude().toString());
            retrieveFeedTask.execute();
        }
    }

    // Pass title and hint to edit info activity based on edit selection type
    private void setIntentEditInto(String title, String detail, String detailType) {
        Intent intent = new Intent(getActivity(), EditInfoActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("detail", detail);
        intent.putExtra("detailType", detailType); // Type of detail to know which column in the database to insert
        boolean isAHint = true;

        // Pass isHint extra because default description should be displayed as hint in the EditInfoActivity
        // Non-default (user edited before) should be displayed in EditText as text not hint
        switch (detailType) {
            case "1":
                isAHint = isDetailOneAHint;
                break;
            case "2":
                isAHint = isDetailTwoAHint;
                break;
            case "3":
                isAHint = isDetailThreeAHint;
                break;
            case "4":
                isAHint = isDetailFourAHint;
                break;
            default:
        }
        intent.putExtra("isHint", isAHint);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data.getBooleanExtra("refresh", true)) { // only refresh if new photo set
            Picasso.with(getContext())
                    .load(userLocalStore.getLoggedInUser().getUserImageURL())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit()
                    .centerCrop()
                    .into(profilePic);
            v.setVisibility(View.VISIBLE);

            // Also notify navigation drawer to change its profile picture by invoking the main activity
            fragActivityUpdateProfilePic.onUpdateProfilePic();
        } else if (requestCode == 3) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            UserProfileFragment fragment = new UserProfileFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
            fragment.refreshPage = true;
        }
    }

    private void setViewListeners() {
        editLayout.setOnClickListener(this);
        editLayout2.setOnClickListener(this);
        editLayout3.setOnClickListener(this);
        editLayout4.setOnClickListener(this);
        imgbEditPhoto.setOnClickListener(this);
        editLayoutSub1.setOnClickListener(this);
        editLayoutSub2.setOnClickListener(this);
        editLayoutSub3.setOnClickListener(this);
        editLayoutSub4.setOnClickListener(this);
        imgbEditPhoto.setOnClickListener(this);
        imgTravelStatusEdit.setOnClickListener(this);
    }

    /**
     *   Check if location update is needed. If needed, update local store and server
     */
    private void checkLocationUpdate() {
        long currentTime = System.currentTimeMillis();

        // If it's been 5 minute since last location update, do the update
        if (Helpers.timeDifInMinutes(currentTime,
                userLocalStore.getLoggedInUser().getTime()) > 5) {
            Helpers.updateLocationAndTime(getContext(), userLocalStore, currentTime);
        }
    }

    private void setTravelingStatus() {
        travelStatus = userLocalStore.getLoggedInUser().getTraveling();
        if (travelStatus == 0) { // not traveling
            imgNotTravel.setVisibility(View.VISIBLE);
            txtNotTravel.setVisibility(View.VISIBLE);
        } else {
            imgTravel.setVisibility(View.VISIBLE);
            txtTravel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUsername(String username) {
        this.username.setText(username);
    }
    @Override
    public void setDetailOneText(String text) {
        detailOne.setText(text);
        setViewVisible();
        hideProgressBar();
    }
    @Override
    public void setDetailOneHint(String hint) {
        detailOne.setHint(hint);
        setViewVisible();
        hideProgressBar();
    }
    @Override
    public void setDetailTwoText(String text) {
        detailTwo.setText(text);
    }
    @Override
    public void setDetailTwoHint(String hint) {
        detailTwo.setHint(hint);
    }
    @Override
    public void setDetailThreeText(String text) {
        detailThree.setText(text);
    }
    @Override
    public void setDetailThreeHint(String hint) {
        detailThree.setHint(hint);
    }
    @Override
    public void setDetailFourText(String text) {
        detailFour.setText(text);
    }
    @Override
    public void setDetailFourHint(String hint) {
        detailFour.setHint(hint);
    }

    public void setProfilePic() {
        // Refresh imageview without cache after edit information
        Picasso.with(getContext())
                .load(userLocalStore.getLoggedInUser().getUserImageURL())
                .fit()
                .centerCrop()
                .into(profilePic);
    }
    @Override
    public void hideProgressBar() {
        fragActivityProgressBarInterface.setProgressBarInvisible();
    }
    @Override
    public void setViewVisible() {
        v.setVisibility(View.VISIBLE);
    }
    @Override
    public void setDetailOneColor() {
        detailOne.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
    }
    @Override
    public void setDetailTwoColor() {
        detailTwo.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
    }
    @Override
    public void setDetailThreeColor() {
        detailThree.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
    }
    @Override
    public void setDetailFourColor() {
        detailFour.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
    }
    @Override
    public void isDetailOneAHint(boolean hint) {
        isDetailOneAHint = hint;
    }
    @Override
    public void isDetailTwoAHint(boolean hint) {
        isDetailTwoAHint = hint;
    }
    @Override
    public void isDetailThreeAHint(boolean hint) {
        isDetailThreeAHint = hint;
    }
    @Override
    public void isDetailFourAHint(boolean hint) {
        isDetailFourAHint = hint;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Retrieve city and country location information from google location API
     */
    private class RetrieveCityCountryTask extends AsyncTask<Void, Void, String> {
        String latitude;
        String longitude;

        RetrieveCityCountryTask(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return (Helpers.getLocationInfo(latitude, longitude));
        }

        @Override
        protected void onPostExecute(String s) {
            tvLocation.setText(s);
            setViewVisible();
            hideProgressBar();
        }
    }
}
