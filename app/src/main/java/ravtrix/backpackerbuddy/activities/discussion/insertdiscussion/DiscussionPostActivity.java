package ravtrix.backpackerbuddy.activities.discussion.insertdiscussion;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuPostBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class DiscussionPostActivity extends OptionMenuPostBaseActivity implements IDiscussionPostView {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.etDiscussion) protected EditText etDiscussion;
    @BindView(R.id.linear_newDiscussion) protected LinearLayout linearNewDis;
    @BindView(R.id.activity_discussion_post_countrySpinner) protected Spinner spinnerCountries;
    private DiscussionPostPresenter discussionPostPresenter;
    private UserLocalStore userLocalStore;
    private long mLastClickTime = 0;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_post);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, linearNewDis);
        this.setTitle("New Discussion");

        setSpinner();
        this.discussionPostPresenter = new DiscussionPostPresenter(this);
        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitPost:

                // Prevents double clicking
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return false;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (spinnerCountries.getSelectedItem().toString().equals("None")) {
                    AlertDialog.Builder dialog = Helpers.showAlertDialogWithTwoOptions(this,
                            getResources().getString(R.string.countryTagMissing),
                            getResources().getString(R.string.continueNoTag), "No");

                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (checkInputOkay()) {
                                discussionPostPresenter.insertDiscussion(getDiscussionHash());
                            }
                        }
                    });
                    dialog.show();
                } else {
                    if (checkInputOkay()) {
                        discussionPostPresenter.insertDiscussion(getDiscussionHash());
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkInputOkay() {
        boolean isOkay = true;

        if (etDiscussion.getText().toString().trim().length() < 10) {
            Helpers.displayToast(DiscussionPostActivity.this, "Post is too short...");
            isOkay =  false;
        } else if (etDiscussion.getText().toString().trim().length() >= 500) {
            Helpers.displayToast(DiscussionPostActivity.this, "Exceeded max character count (500)");
            isOkay = false;
        }
        return isOkay;
    }
    private HashMap<String, String> getDiscussionHash() {

        HashMap<String, String> newDiscussion = new HashMap<>();
        newDiscussion.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        newDiscussion.put("post", etDiscussion.getText().toString().trim());
        newDiscussion.put("time", Long.toString(System.currentTimeMillis()));

        if (spinnerCountries.getSelectedItem().toString().equals("None")) {
            newDiscussion.put("country", ""); // User selects no tag, inserts as blank
        } else {
            newDiscussion.put("country", spinnerCountries.getSelectedItem().toString()); // insert the country tag
        }
        return newDiscussion;
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> countryArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.countriesShort, R.layout.item_spinner);
        // Drop down
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countryArrayAdapter);
    }

    @Override
    public void showProgressDialog() {
        progressDialog = Helpers.showProgressDialog(this, "Posting...");
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) Helpers.hideProgressDialog(progressDialog);
    }

    @Override
    public void startDiscussionPostActivity() {
        startActivity(new Intent(DiscussionPostActivity.this, UserMainPage.class));
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(this);
    }
}
