package ravtrix.backpackerbuddy.activities.editinfo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;

public class EditInfoActivity extends AppCompatActivity implements View.OnClickListener, IEditInfoView {

    @BindView(R.id.editTitle) protected TextView editTitle;
    @BindView(R.id.editHint) protected EditText editText;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.relativeEditInfo) protected RelativeLayout relativeLayout;
    private EditInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        Helpers.overrideFonts(this, relativeLayout);
        setEditText();
        setToolbar();
        presenter = new EditInfoPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        TextView tv = new TextView(this);
        tv.setId(R.id.btn_save);
        tv.setText(R.string.saveToolbar);
        tv.setTextColor(Color.WHITE);
        tv.setOnClickListener(this);
        tv.setPadding(5, 0, 20, 0);
        //tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(18);
        menu.add(0, 1, 100, "Save").setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_save:
                if (!presenter.checkEmptyString(editText.getText().toString())) {
                    presenter.editUserInfo(editText.getText().toString().trim(), presenter.getDetailType());
                }
                break;
            default:
        }
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(this);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void setEditText() {
        editText.setFocusable(false);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    @Override
    public void setTitle(String text) {
        editTitle.setText(text);
    }

    @Override
    public void setText(String text) {
        editText.setText(text);

    }

    @Override
    public void setHint(String text) {
        editText.setHint(text);
    }

    private void setToolbar() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    onBackPressed();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
