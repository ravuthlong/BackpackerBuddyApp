package ravtrix.backpackerbuddy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.EditInfoActivity;
import ravtrix.backpackerbuddy.activities.EditPhotoActivity;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.interfaces.FragActivityProgressBarInterface;
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

        Picasso.with(getActivity()).load("http://i.imgur.com/268p4E0.jpg").noFade().into(profilePic);
        userLocalStore = new UserLocalStore(getActivity());
        retrofitFetchProfileInfo();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
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
                startActivity(new Intent(getActivity(), EditPhotoActivity.class));
                break;
            default:
        }
    }

    // Pass title and hint to edit info activity based on edit selection type
    private void setIntentEditInto(String title, String hint, String detailType) {
        Intent intent = new Intent(getActivity(), EditInfoActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("hint", hint);
        intent.putExtra("detailType", detailType); // Type of detail to know which column in the database to insert
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
                    }
                    if (!responseJSON.get("detailTwo").getAsString().isEmpty()) {
                        detailTwo.setText(responseJSON.get("detailTwo").getAsString());
                    }
                    if (!responseJSON.get("detailThree").getAsString().isEmpty()) {
                        detailThree.setText(responseJSON.get("detailThree").getAsString());
                    }
                    if (!responseJSON.get("detailFour").getAsString().isEmpty()) {
                        detailFour.setText(responseJSON.get("detailFour").getAsString());
                    }
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
}
