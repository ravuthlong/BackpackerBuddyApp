package ravtrix.backpackerbuddy;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Typeface.BOLD;

/**
 * Created by Ravinder on 1/9/17.
 */

public class AppRater {

    private final static String APP_PNAME = "ravtrix.backpackerbuddy";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter. This keeps track the total number of launches.
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least 3 days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }
        editor.apply();
    }

    private static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {

        final Dialog rateDialog = new Dialog(mContext, R.style.FullHeightDialog);
        rateDialog.setContentView(R.layout.dialog_rating);
        rateDialog.setCancelable(true);
        RatingBar ratingBar = (RatingBar) rateDialog.findViewById(R.id.dialog_ratingbar);
        TextView tvRate = (TextView) rateDialog.findViewById(R.id.dialog_tvRate);
        TextView tvLater = (TextView) rateDialog.findViewById(R.id.dialog_tvLater);
        TextView tvNever = (TextView) rateDialog.findViewById(R.id.dialog_tvNever);

        Typeface font = Typeface.createFromAsset(mContext.getAssets(),"Text.ttf");
        tvRate.setTypeface(font, BOLD);
        tvLater.setTypeface(font, BOLD);
        tvNever.setTypeface(font, BOLD);

        tvLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateDialog.dismiss();
            }
        });

        tvNever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                rateDialog.dismiss();
            }
        });

        ratingBar.setRating(0);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {

                // set dontshowagain to true so dialog rate won't show again
                if (rating <= 3) {
                    // Low rating, give user message dialog
                    showMessageDialog(mContext);
                    rateDialog.dismiss();

                    if (editor != null) {
                        editor.putBoolean("dontshowagain", true);
                        editor.commit();
                    }
                } else {
                    // User gave rating 4 or 5, bring them to play store
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                    rateDialog.dismiss();

                    if (editor != null) {
                        editor.putBoolean("dontshowagain", true);
                        editor.commit();
                    }
                }
            }
        });

        //now that the dialog is set up, it's time to show it
        rateDialog.show();
    }

    private static void showMessageDialog(final Context mContext) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

        final EditText edittext = new EditText(mContext);

        edittext.setHint("Suggest us what went wrong and we'll work on it! :)");
        edittext.setPadding(convertPixelsToDp(20, mContext), convertPixelsToDp(20, mContext),
                convertPixelsToDp(20, mContext), convertPixelsToDp(20, mContext));
        edittext.setBackground(ContextCompat.getDrawable(mContext, R.drawable.button));

        alert.setTitle("Your Feedback");
        alert.setView(edittext);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String stringFeedback = edittext.getText().toString();

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ravtixdev@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                i.putExtra(Intent.EXTRA_TEXT, stringFeedback);
                try {
                    mContext.startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    private static int convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        return ((int) (px * scale + 0.5f)); //dp version
    }
}
