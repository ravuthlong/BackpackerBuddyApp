package ravtrix.backpackerbuddy.fragments.userprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.editphoto.EditPhotoActivity;
import ravtrix.backpackerbuddy.activities.editinfo.EditInfoActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.interfacescom.FragActivityUpdateProfilePic;
import ravtrix.backpackerbuddy.models.UserLocalStore;

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
    private UserLocalStore userLocalStore;
    private View v;
    private ProgressBar progressBar;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private FragActivityUpdateProfilePic fragActivityUpdateProfilePic;
    private boolean isDetailOneAHint = true;
    private boolean isDetailTwoAHint = true;
    private boolean isDetailThreeAHint = true;
    private boolean isDetailFourAHint = true;
    private UserProfilePresenter presenter;

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
        v.setVisibility(View.GONE);
        //RefWatcher refWatcher = UserMainPage.getRefWatcher(getActivity());
        //refWatcher.watch(this);
        fragActivityProgressBarInterface.setProgressBarVisible();
        ButterKnife.bind(this, v);

        userLocalStore = new UserLocalStore(getActivity());
        presenter = new UserProfilePresenter(this);

        setViewListeners();
        checkLocationUpdate();

        // Set user location
        setUserLocation(userLocalStore.getLoggedInUser().getLatitude(),
                userLocalStore.getLoggedInUser().getLongitude());

        presenter.getUserInfo(userLocalStore.getLoggedInUser().getUserID(),
                userLocalStore.getLoggedInUser().getUserImageURL());
        return v;
    }

    private void setUserLocation(double latitude, double longitude) {
        tvLocation.setText(Helpers.cityGeocoder(getContext(), latitude, longitude));
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
            default:
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
        if (requestCode == 1) {
            Picasso.with(getContext())
                    .load("http://backpackerbuddy.net23.net/profile_pic/" +
                            userLocalStore.getLoggedInUser().getUserID() + ".JPG")
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(profilePic);
            v.setVisibility(View.VISIBLE);

            // Also notify navigation drawer to change its profile picture by invoking the main activity
            fragActivityUpdateProfilePic.onUpdateProfilePic();
        } else if (requestCode == 3) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,
                    new UserProfileFragment()).commit();
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
    }

    /*
     * Check if location update is needed. If needed, update local store and server
     */
    private void checkLocationUpdate() {
        long currentTime = System.currentTimeMillis();

        // If it's been 5 minute since last location update, do the update
        if (Helpers.timeDifInMinutes(currentTime,
                userLocalStore.getLoggedInUser().getTime()) > 5) {
            Helpers.updateLocationAndTime(getContext(), userLocalStore, currentTime);
        }
    }

    @Override
    public void setUsername(String username) {
        this.username.setText(username);
    }
    @Override
    public void setDetailOneText(String text) {
        detailOne.setText(text);
    }
    @Override
    public void setDetailOneHint(String hint) {
        detailOne.setHint(hint);
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
    @Override
    public void setProfilePic(String pic) {
        Picasso.with(getContext()).load(pic).noFade().into(profilePic, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                hideProgressBar();
                setViewVisible();
            }

            @Override
            public void onError() {

            }
        });
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
        presenter.onDestroy();
    }
}
