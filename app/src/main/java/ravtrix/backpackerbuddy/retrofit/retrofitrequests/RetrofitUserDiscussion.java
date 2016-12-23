package ravtrix.backpackerbuddy.retrofit.retrofitrequests;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.RetrofitUserDiscussionInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 12/22/16.
 */

public class RetrofitUserDiscussion {
    private Retrofit retrofit;

    public RetrofitUserDiscussion() {
        retrofit = Helpers.retrofitBuilder(Helpers.ServerURL.SERVER_URL);
    }

    public RetrofitUserDiscussionInterfaces.InsertDiscussion insertDiscussion() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.InsertDiscussion.class);
    }

    public RetrofitUserDiscussionInterfaces.GetDiscussions getDiscussions() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.GetDiscussions.class);
    }
}
