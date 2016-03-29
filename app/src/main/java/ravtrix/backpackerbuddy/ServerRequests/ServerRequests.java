package ravtrix.backpackerbuddy.ServerRequests;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import ravtrix.backpackerbuddy.ServerRequests.Callbacks.GetUserCallBack;
import ravtrix.backpackerbuddy.User;

/**
 * Created by Ravinder on 3/28/16.
 */
public class ServerRequests {
    private ProgressDialog progressDialog;
    private Context context;

    public ServerRequests(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please Wait...");
    }
    public void storeUserDataInBackground(User user, GetUserCallBack userCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }
    public void storeExtraUserInfoDataInBackground(int userID, String firstname, String lastname) {
        progressDialog.show();
        new StoreExtraUserInfoDataAsyncTask(userID, firstname, lastname).execute();
    }

    // Signed up will be stored in the database. User type will be returned for UserLocalStore shared preference purpose.
    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, User> {

        private User user;
        private GetUserCallBack userCallBack;

        public StoreUserDataAsyncTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(Void... params) {
            User returnedUser = null;

            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", user.getEmail());
            userInfo.put("username", user.getUsername());
            userInfo.put("password", user.getPassword());

            JSONObject jObject = new JSONObject();

            try {
                HttpRequest req = new HttpRequest("http://backpackerbuddy.net23.net/register.php");
                jObject = req.preparePost().withData(userInfo).sendAndReadJSON();

                if(jObject.getString("user").equals("")) {
                    // No user returned
                    returnedUser = null;
                } else {

                    int userID = jObject.getInt("id");
                    //UserLocalStore.isUserLoggedIn = true;
                    returnedUser = new User(userID, user.getEmail(),
                            user.getUsername());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
            return returnedUser;
        }
        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
        }
    }

    // Signed up will be stored in the database. User type will be returned for UserLocalStore shared preference purpose.
    public class StoreExtraUserInfoDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private String firstname, lastname;
        private int userID;

        public StoreExtraUserInfoDataAsyncTask(int userID, String firstname, String lastname) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.userID = userID;
        }

        @Override
        protected Void doInBackground(Void... params) {

            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("userID", userID);
            userInfo.put("firstname", firstname);
            userInfo.put("lastname", lastname);

            try {
                HttpRequest req = new HttpRequest("http://backpackerbuddy.net23.net/register2.php");
                req.preparePost().withData(userInfo).send();
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }


}
