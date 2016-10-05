package ravtrix.backpackerbuddy.activities.signup2;

/**
 * Created by Ravinder on 9/28/16.
 */

class SignUpPart2Presenter implements ISignUpPart2Presenter {

    private ISignUpPart2View iSignUpPart2View;

    SignUpPart2Presenter(ISignUpPart2View iSignUpPart2View) {
        this.iSignUpPart2View = iSignUpPart2View;
    }

    @Override
    public void inputValidation(String firstname, String lastname) {
        String errorFields = "";

        if (lastname.matches("")) {
            errorFields += "Missing Last Name \n";
        } else if (!isAlpha(lastname)) {
            errorFields += "Last Name must only contain letters\n";
        }
        if (firstname.matches("")) {
            errorFields += "Missing First Name \n";
        } else if (!isAlpha(firstname)) {
            errorFields += "First Name must only contain letters\n";
        }
        if (errorFields.length() > 0) {
            iSignUpPart2View.displayInputErrorToast(errorFields);
        } else {
            iSignUpPart2View.startSignUpPart3(firstname, lastname);
        }
    }

    // Validate string must only contain letters
    private boolean isAlpha(String text) {
        char[] chars = text.toCharArray();
        boolean isValid = true;

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                isValid = false;
            }
        }
        return isValid;
    }
}
