package ravtrix.backpackerbuddy.activities.signup1;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import ravtrix.backpackerbuddy.helpers.Helpers;

/**
 * Created by Ravinder on 9/27/16.
 */

class SignUpPart1Presenter implements ISignUpPart1Presenter {

    private ISignUpPart1View iSignUpPart1View;
    private SignUpPart1Interactor signUpPart1Interactor;

    SignUpPart1Presenter(ISignUpPart1View iSignUpPart1View) {
        this.iSignUpPart1View = iSignUpPart1View;
        signUpPart1Interactor = new SignUpPart1Interactor();
    }

    @Override
    public void inputValidation(final String username, final String password, final String email,
                                final String etConfirmPassword) {

        String errorFields = validate(username, password, email, etConfirmPassword);

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


    private String validate(String username, String password, String email, String etConfirmPassword) {
        String errorFields = "";

        if (username.matches("")) {
            errorFields += "Missing Username \n";
        }
        if (password.matches("")) {
            errorFields += "Missing Password \n";
        } else if (!passwordLength(password)) {
            errorFields += "Password must be at least 6 characters long\n";
        }
        if (email.matches("")) {
            errorFields += "Missing Email \n";
        } else if (!Helpers.isEmailValid(email)) {
            errorFields += "Email is in invalid format (Ex. xxxx@xxx.xxx)\n";
        }
        if(!password.equals(etConfirmPassword)) {
            errorFields += "Password must match \n";
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
