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

    public RetrofitUserDiscussionInterfaces.DeleteDiscussion deleteDiscussion() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.DeleteDiscussion.class);
    }

    public RetrofitUserDiscussionInterfaces.InsertAndUpdateLove insertAndUpdateLove() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.InsertAndUpdateLove.class);
    }


    public RetrofitUserDiscussionInterfaces.RemoveAndUpdateLove removeAndUpdateLove() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.RemoveAndUpdateLove.class);
    }

    public RetrofitUserDiscussionInterfaces.GetDiscussions getDiscussions() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.GetDiscussions.class);
    }

    public RetrofitUserDiscussionInterfaces.GetAUserDiscussions getAUserDiscussions() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.GetAUserDiscussions.class);
    }

    public RetrofitUserDiscussionInterfaces.GetDiscussionComments getDiscussionComments() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.GetDiscussionComments.class);
    }

    public RetrofitUserDiscussionInterfaces.IncrementCommentCount incrementCommentCount() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.IncrementCommentCount.class);
    }

    public RetrofitUserDiscussionInterfaces.RemoveCommentAndDecrement removeCommentAndDecrement() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.RemoveCommentAndDecrement.class);
    }

    public RetrofitUserDiscussionInterfaces.InsertComment insertComment() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.InsertComment.class);
    }

    public RetrofitUserDiscussionInterfaces.UpdateComment updateComment() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.UpdateComment.class);
    }

    public RetrofitUserDiscussionInterfaces.UpdateDiscussion updateDiscussion() {
        return retrofit.create(RetrofitUserDiscussionInterfaces.UpdateDiscussion.class);
    }
}
