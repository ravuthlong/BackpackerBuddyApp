package ravtrix.backpackerbuddy.VolleyServerConnections;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Ravinder on 7/31/16.
 */
public class VolleyMainPosts {

    private ProgressDialog progressDialog;
    private Context context;

    public VolleyMainPosts(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
    }


}
