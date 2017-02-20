package ravtrix.backpackerbuddy.activities.discussion.editdiscussion;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;

public class EditDiscussionActivity extends OptionMenuSaveBaseActivity implements IEditDiscussionView {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.linearEditDiscussion) protected LinearLayout linearEdit;
    @BindView(R.id.etEditDiscussion) protected EditText etEditDiscussion;
    @BindView(R.id.activity_edit_discussion_countrySpinner) protected Spinner spinnerCountry;
    private EditDiscussionPresenter editDiscussionPresenter;
    private int discussionID;
    private String post;
    private String countryTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_discussion);
        ButterKnife.bind(this);
        setTitle("Edit Post");
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, linearEdit);

        editDiscussionPresenter = new EditDiscussionPresenter(this);

        getBundle();
        setSpinner();

        Helpers.overrideFonts(this, etEditDiscussion);
        etEditDiscussion.setText(post);
        etEditDiscussion.setSelection(etEditDiscussion.getText().length());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:

                if (etEditDiscussion.getText().toString().trim().length() < 10) {
                    Helpers.displayToast(this, "Post is too short...");
                } else {
                    editDiscussionPresenter.editDiscussion(discussionID, etEditDiscussion.getText().toString().trim(),
                            spinnerCountry.getSelectedItem().toString());
                    Helpers.hideKeyboard(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            discussionID = bundle.getInt("discussionID");
            post = bundle.getString("post");
            countryTag = bundle.getString("countryTag");
        }
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> countryArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.countriesShort, R.layout.item_spinner);
        // Drop down
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryArrayAdapter);

        for (int i = 0; i < countryArrayAdapter.getCount(); i++) {
            if (countryArrayAdapter.getItem(i).equals(countryTag)) {
                spinnerCountry.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void setResultCode(int code) {
        setResult(1);
    }

    @Override
    public void finished() {
        finish();
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(this);
    }
}

