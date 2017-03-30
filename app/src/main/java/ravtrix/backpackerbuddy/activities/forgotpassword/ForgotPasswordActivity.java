package ravtrix.backpackerbuddy.activities.forgotpassword;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitResetPasswordSingleton;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.token.TokenGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 3/26/17.
 */

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.activity_forgot_password_linearContent) protected LinearLayout linearMain;
    @BindView(R.id.activity_forgot_password_bSend) protected TextView bSend;
    @BindView(R.id.activity_forgot_etEmail) protected EditText etEmail;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private long mLastClickTime = 0;
    private String token;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, linearMain);
        setTitle("Reset Password");

        // randomly generated token to identify validity of email reset
        // only the email receiver has this number. server checks for matching token before allowed reset.
        token = new TokenGenerator().getSecureToken();

        bSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_forgot_password_bSend:
                // Prevents double clicking
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (etEmail.getText().length() <= 0) {
                    Helpers.displayToast(this, "Empty email field.");
                } else {
                    // send token to user table where email is the same as input email
                    // send token and email to reset server side file
                    isEmailTaken();
                }
                break;
        }
    }

    private void isEmailTaken() {

        progressDialog = Helpers.showProgressDialog(this, "Loading...");

        final String email = etEmail.getText().toString().trim();

        Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .isUsernameOrEmailTaken()
                .isUsernameOrEmailTaken("", email);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("emailtaken").getAsInt() == 0) {
                    // put fake message that email has been sent even though email doesn't exist
                    displayDialog();
                    Helpers.hideProgressDialog(progressDialog);
                } else {
                    // insert token in user table
                    insertToken(email, token);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.hideProgressDialog(progressDialog);
                Helpers.displayErrorToast(ForgotPasswordActivity.this);
            }
        });

    }

    private void insertToken(final String email, final String token) {

        Call<JsonObject> retrofit = RetrofitResetPasswordSingleton.getRetrofitResetPassword()
                .updateResetToken()
                .updateResetToken(email, token);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                sendResetLink(email, token);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.hideProgressDialog(progressDialog);
            }
        });
    }

    private void sendResetLink(String email, String token) {

        Call<Void> retrofit = RetrofitResetPasswordSingleton.getRetrofitResetPassword()
                .sendResetEmail()
                .sendResetEmail(email, token);
        retrofit.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Helpers.hideProgressDialog(progressDialog);
                displayDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Helpers.hideProgressDialog(progressDialog);
                Helpers.displayToast(ForgotPasswordActivity.this, "Error sending email.");
            }
        });

    }

    private void displayDialog() {
        Helpers.showAlertDialog(this, "Reset password link sent to email. Check your inbox and spam inbox.");
    }
}
