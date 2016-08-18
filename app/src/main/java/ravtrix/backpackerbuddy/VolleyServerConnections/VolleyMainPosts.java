package ravtrix.backpackerbuddy.VolleyServerConnections;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.adapter.FeedListAdapter;
import ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.app.AppController;
import ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.data.FeedItem;
import ravtrix.backpackerbuddy.Models.UserLocalStore;

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

    public void getUserPostsNotLoggedIn(FeedListAdapter feedListAdapter, List<FeedItem> feedItems) {
        new FetchUserPostsVolley(feedListAdapter, feedItems).getJsonLive();
    }

    public class FetchUserPostsVolley {
        private FeedListAdapter feedListAdapter;
        private List<FeedItem> feedItems;
        private ProgressBar spinner;

        public FetchUserPostsVolley(FeedListAdapter feedListAdapter, List<FeedItem> feedItems) {
            this.feedListAdapter = feedListAdapter;
            this.feedItems = feedItems;
            this.spinner = spinner;
        }

        // Populate FeedItem Array with JSONArray from PHP
        public void fetchUserPostsVolley(JSONObject response) {
            try {
                JSONArray feedArray = response.getJSONArray("feed");
                for (int i = 0; i < feedArray.length(); i++) {

                    JSONObject feedObj = (JSONObject) feedArray.get(i);

                    FeedItem item = new FeedItem();
                    String name;

                    item.setUserID(feedObj.getInt("userID"));
                    name = feedObj.getString("firstname") + " " + feedObj.getString("lastname");
                    item.setId(feedObj.getInt("travelID"));
                    item.setName(name);
                    item.setUsername(feedObj.getString("username"));
                    item.setCountry(feedObj.getString("country"));
                    item.setFromDate(feedObj.getString("from"));
                    item.setToDate(feedObj.getString("until"));
                    feedItems.add(item);
                }
                // notify data changes to list adapter
                feedListAdapter.notifyDataSetChanged();
                //spinner.setVisibility(View.GONE);

            } catch (JSONException e) {
                //spinner.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }

        // Pull JSON directly from the PHP JSON result
        public void getJsonLive() {
            UserLocalStore.visitCounter++;
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    "http://backpackerbuddy.net23.net/fetchActivities.php", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    if (response != null) {
                        fetchUserPostsVolley(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }


}
