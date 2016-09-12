package ravtrix.backpackerbuddy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.EditInfoActivity;
import ravtrix.backpackerbuddy.activities.EditPhotoActivity;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.interfaces.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.interfaces.FragActivityUpdateProfilePic;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 8/18/16.
 */
public class UserProfile extends Fragment implements View.OnClickListener {

    @BindView(R.id.profile_image_profile) protected CircleImageView profilePic;
    @BindView(R.id.ll_edit) protected LinearLayout editLayout;
    @BindView(R.id.ll_edit2) protected LinearLayout editLayout2;
    @BindView(R.id.ll_edit3) protected LinearLayout editLayout3;
    @BindView(R.id.ll_edit4) protected LinearLayout editLayout4;
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
    private UserLocalStore userLocalStore;
    private View v;
    private ProgressBar progressBar;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private FragActivityUpdateProfilePic fragActivityUpdateProfilePic;
    private boolean isDetailOneAHint = true;
    private boolean isDetailTwoAHint = true;
    private boolean isDetailThreeAHint = true;
    private boolean isDetailFourAHint = true;
    private boolean refreshProfilePic = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_user_profile, container, false);
        v.setVisibility(View.GONE);
        //RefWatcher refWatcher = UserMainPage.getRefWatcher(getActivity());
        //refWatcher.watch(this);
        fragActivityProgressBarInterface.setProgressBarVisible();
        ButterKnife.bind(this, v);

        editLayout.setOnClickListener(this);
        editLayout2.setOnClickListener(this);
        editLayout3.setOnClickListener(this);
        editLayout4.setOnClickListener(this);
        imgbEditPhoto.setOnClickListener(this);
        userLocalStore = new UserLocalStore(getActivity());

        retrofitFetchProfileInfo();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
        this.fragActivityUpdateProfilePic = (FragActivityUpdateProfilePic) context;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ll_edit:
                setIntentEditInto(title1.getText().toString(), detailOne.getText().toString(), "1");
                break;
            case R.id.ll_edit2:
                setIntentEditInto(title2.getText().toString(), detailTwo.getText().toString(), "2");
                break;
            case R.id.ll_edit3:
                setIntentEditInto(title3.getText().toString(), detailThree.getText().toString(), "3");
                break;
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
        startActivity(intent);
    }

    private void retrofitFetchProfileInfo() {

        Call<JsonObject> jsonObjectCall =
                RetrofitUserInfoSingleton
                        .getRetrofitUserInfo()
                        .getUserDetails()
                        .getUserDetails(userLocalStore.getLoggedInUser().getUserID());
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject responseJSON = response.body();
                // If success
                if (responseJSON.get("success").getAsInt() == 1) {

                    responseJSON.get("firstname").getAsString();
                    responseJSON.get("lastname").getAsString();
                    username.setText(responseJSON.get("username").getAsString());

                    if (!responseJSON.get("detailOne").getAsString().isEmpty()) {
                        detailOne.setText(responseJSON.get("detailOne").getAsString());
                        isDetailOneAHint = false;
                    } else {
                        detailOne.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
                        detailOne.setHint("Write a summary about yourself.");
                    }
                    if (!responseJSON.get("detailTwo").getAsString().isEmpty()) {
                        detailTwo.setText(responseJSON.get("detailTwo").getAsString());
                        isDetailTwoAHint = false;
                    } else {
                        detailTwo.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
                        detailTwo.setHint("List down six backpacking items you must have while backpacking.");
                    }
                    if (!responseJSON.get("detailThree").getAsString().isEmpty()) {
                        detailThree.setText(responseJSON.get("detailThree").getAsString());
                        isDetailThreeAHint = false;
                    } else {
                        detailThree.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
                        detailThree.setText("Tell your potential backpacking buddy about your personality.");
                    }
                    if (!responseJSON.get("detailFour").getAsString().isEmpty()) {
                        detailFour.setText(responseJSON.get("detailFour").getAsString());
                        isDetailFourAHint = false;
                    } else {
                        detailFour.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
                        detailFour.setText("Tell us how you would imagine your backpacking day to go.");
                    }
                }

                if ((userLocalStore.getLoggedInUser().getUserImageURL() == null) ||
                        (userLocalStore.getLoggedInUser().getUserImageURL().equals("0"))) {
                    Picasso.with(getContext()).load("http://i.imgur.com/268p4E0.jpg").noFade().into(profilePic);
                } else {
                    Picasso.with(getContext()).load("http://backpackerbuddy.net23.net/profile_pic/" +
                            userLocalStore.getLoggedInUser().getUserID() + ".JPG").noFade().into(profilePic);
                }
                // call
                fragActivityProgressBarInterface.setProgressBarInvisible();
                v.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
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
        }
    }
}
