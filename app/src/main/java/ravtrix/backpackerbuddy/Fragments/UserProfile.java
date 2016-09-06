package ravtrix.backpackerbuddy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.EditInfoActivity;

/**
 * Created by Ravinder on 8/18/16.
 */
public class UserProfile  extends Fragment implements View.OnClickListener {

    @BindView(R.id.profile_image_profile) protected CircleImageView profilePic;
    @BindView(R.id.ll_edit) protected LinearLayout editLayout;
    @BindView(R.id.ll_edit2) protected LinearLayout editLayout2;
    @BindView(R.id.ll_edit3) protected LinearLayout editLayout3;
    @BindView(R.id.ll_edit4) protected LinearLayout editLayout4;
    @BindView(R.id.title1) protected TextView title1;
    @BindView(R.id.title2) protected TextView title2;
    @BindView(R.id.title3) protected TextView title3;
    @BindView(R.id.title4) protected TextView title4;
    @BindView(R.id.hint1) protected TextView hint1;
    @BindView(R.id.hint2) protected TextView hint2;
    @BindView(R.id.hint3) protected TextView hint3;
    @BindView(R.id.hint4) protected TextView hint4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_user_profile, container, false);

        ButterKnife.bind(this, v);

        editLayout.setOnClickListener(this);
        editLayout2.setOnClickListener(this);
        editLayout3.setOnClickListener(this);
        editLayout4.setOnClickListener(this);

        Picasso.with(getActivity()).load("http://i.imgur.com/268p4E0.jpg").noFade().into(profilePic);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ll_edit:
                setIntentEditInto(title1.getText().toString(), hint1.getText().toString(), "1");
                break;
            case R.id.ll_edit2:
                setIntentEditInto(title2.getText().toString(), hint2.getText().toString(), "2");
                break;
            case R.id.ll_edit3:
                setIntentEditInto(title3.getText().toString(), hint3.getText().toString(), "3");
                break;
            case R.id.ll_edit4:
                setIntentEditInto(title4.getText().toString(), hint4.getText().toString(), "4");
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
}
