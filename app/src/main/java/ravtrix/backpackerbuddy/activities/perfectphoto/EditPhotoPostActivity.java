package ravtrix.backpackerbuddy.activities.perfectphoto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;

import static ravtrix.backpackerbuddy.R.string.comment;

public class EditPhotoPostActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_edit_photo_relative) protected RelativeLayout relativeLayout;
    @BindView(R.id.activity_edit_photo_etEditComment) protected EditText etPost;
    private String post = "";
    private int photoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo_post);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Edit Post");

        getBundle();
        etPost.setText(post);
        etPost.setSelection(etPost.getText().length());
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            post = bundle.getString("post");
            photoID = bundle.getInt("photoID");
        }
    }
}
