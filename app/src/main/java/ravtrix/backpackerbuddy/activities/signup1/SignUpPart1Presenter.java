package ravtrix.backpackerbuddy.activities.signup1;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 9/27/16.
 */

class SignUpPart1Presenter implements ISignUpPart1Presenter {

    private ISignUpPart1View iSignUpPart1View;
    private SignUpPart1Interactor signUpPart1Interactor;
    private UserLocalStore userLocalStore;

    SignUpPart1Presenter(ISignUpPart1View iSignUpPart1View) {
        this.iSignUpPart1View = iSignUpPart1View;

        userLocalStore = new UserLocalStore((Activity) iSignUpPart1View);
        signUpPart1Interactor = new SignUpPart1Interactor();
    }

    @Override
    public void retrofitStoreUser(final HashMap<String, String> userInfo) {

        signUpPart1Interactor.signUserUpRetrofit(userInfo, new OnRetrofitSignUp1SuccessFail() {
            @Override
            public void onSuccess(JsonObject jsonObject) {

                iSignUpPart1View.hideProgressDialog();
                if (userInfo.get("country").isEmpty()) { //empty because IOException thrown
                    // Update country info after the user signs up
                    iSignUpPart1View.updateCountry(userInfo.get("username"));
                }
                // Set new user local store
                long currentTime = System.currentTimeMillis();
                LoggedInUser user = new LoggedInUser();
                user.setUsername(userInfo.get("username"));
                user.setEmail(userInfo.get("email"));
                user.setUserID(jsonObject.get("id").getAsInt());
                user.setUserImageURL(jsonObject.get("userpic").getAsString());
                user.setLatitude(Double.valueOf(userInfo.get("latitude")));
                user.setLongitude(Double.valueOf(userInfo.get("longitude")));
                user.setTime(currentTime);
                user.setIsFacebook(0); // Not facebook user
                userLocalStore.storeUserData(user);
                iSignUpPart1View.startUserMainPage();
            }

            @Override
            public void onFailure() {
                iSignUpPart1View.hideProgressDialog();
                iSignUpPart1View.showAlertDialogError();
            }
        });
    }

    @Override
    public void updateCountry(String username, String country) {

    }

    @Override
    public void inputValidation(final boolean isPhotoUploaded, final String username, final String password, final String email,
                                final String etConfirmPassword) {

        String errorFields = validate(isPhotoUploaded, username, password, email, etConfirmPassword);

        if (errorFields.length() > 0) {
            iSignUpPart1View.hideProgressDialog();
            Toast toast= Toast.makeText((Activity) iSignUpPart1View,
                    errorFields, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {

            signUpPart1Interactor.isUsernameTaken(username, email, new OnRetrofitSignUp1() {

                @Override
                public void onUsernameAndEmailNotTaken() {
                    // Email and username not taken. User can proceed.
                    iSignUpPart1View.startSignUpPart2Activity(email, username, password);
                }

                @Override
                public void onUsernameTaken() {
                    // Username already taken
                    iSignUpPart1View.displayUsernameTakenDialog();
                }

                @Override
                public void onEmailTaken() {
                    // Email already taken
                    iSignUpPart1View.displayEmailTakenDialog();
                }

                @Override
                public void onUsernameAndEmailTaken() {
                    // Both email and username taken
                    iSignUpPart1View.displayUsernameAndEmailTakenDialog();
                }

                @Override
                public void onError() {
                    // Retrofit error
                    iSignUpPart1View.displayErrorToast();
                }
            });
        }
    }


    private String validate(boolean isPhotoUploaded, String username, String password, String email, String etConfirmPassword) {
        String errorFields = "";

        if (!isPhotoUploaded) {
            errorFields += "Missing Profile Picture";
            return errorFields;
        }
        if (email.matches("")) {
            errorFields += "Missing Email";
            return errorFields;
        }
        if (!Helpers.isEmailValid(email)) {
            errorFields += "Email is in invalid format (Ex. xxxx@xxx.xxx)";
            return errorFields;
        }
        if (username.matches("")) {
            errorFields += "Missing Username";
            return errorFields;
        }
        if (password.matches("")) {
            errorFields += "Missing Password";
            return errorFields;
        } else if (!passwordLength(password)) {
            errorFields += "Password must be at least 6 characters long";
            return errorFields;
        }
        if(!password.equals(etConfirmPassword)) {
            errorFields += "Password must match";
            return errorFields;
        }

        return errorFields;
    }

    // Validate password length, must be 6 or more
    private static boolean passwordLength(String password) {
        boolean isValid = true;
        final int passwordLengthMin = 6;

        if (password.length() < passwordLengthMin) {
            isValid = false;
        }
        return isValid;
    }

}
