package ravtrix.backpackerbuddy.helpers;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Ravinder on 2/15/17.
 */

public class HelpersDisplay {

    private HelpersDisplay() {}

    public static void makeSnackbar(View parentView, String message) {
        Snackbar snackbar = Snackbar
                .make(parentView, message, Snackbar.LENGTH_SHORT);
        View mView = snackbar.getView();
        TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snackbar.show();
    }

}
