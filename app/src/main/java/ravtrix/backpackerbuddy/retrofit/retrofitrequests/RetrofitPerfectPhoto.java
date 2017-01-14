package ravtrix.backpackerbuddy.retrofit.retrofitrequests;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.RetrofitPerfectPhotoInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 1/12/17.
 */

public class RetrofitPerfectPhoto {

    private Retrofit retrofit;

    public RetrofitPerfectPhoto() {
        retrofit = Helpers.retrofitBuilder(Helpers.ServerURL.SERVER_URL);
    }

    public RetrofitPerfectPhotoInterfaces.InsertPerfectPhoto insertPerfectPhoto() {
        return retrofit.create(RetrofitPerfectPhotoInterfaces.InsertPerfectPhoto.class);
    }

    public RetrofitPerfectPhotoInterfaces.GetPerfectPhotos getPerfectPhotos() {
        return retrofit.create(RetrofitPerfectPhotoInterfaces.GetPerfectPhotos.class);
    }

    public RetrofitPerfectPhotoInterfaces.InsertAndUpdateLovePhoto insertAndUpdateLovePhoto() {
        return retrofit.create(RetrofitPerfectPhotoInterfaces.InsertAndUpdateLovePhoto.class);
    }

    public RetrofitPerfectPhotoInterfaces.RemoveAndUpdateLovePhoto removeAndUpdateLovePhoto() {
        return retrofit.create(RetrofitPerfectPhotoInterfaces.RemoveAndUpdateLovePhoto.class);
    }

    public RetrofitPerfectPhotoInterfaces.InsertPhotoComment insertPhotoComment() {
        return retrofit.create(RetrofitPerfectPhotoInterfaces.InsertPhotoComment.class);
    }

    public RetrofitPerfectPhotoInterfaces.IncrementPhotoCommentCount incrementPhotoCommentCount() {
        return retrofit.create(RetrofitPerfectPhotoInterfaces.IncrementPhotoCommentCount.class);
    }

    public RetrofitPerfectPhotoInterfaces.GetPhotoComments getPhotoComments() {
        return retrofit.create(RetrofitPerfectPhotoInterfaces.GetPhotoComments.class);
    }

    public RetrofitPerfectPhotoInterfaces.RemoveCommentAndDecrement removeCommentAndDecrement() {
        return retrofit.create(RetrofitPerfectPhotoInterfaces.RemoveCommentAndDecrement.class);
    }

    public RetrofitPerfectPhotoInterfaces.UpdatePhotoComment updatePhotoComment() {
        return  retrofit.create(RetrofitPerfectPhotoInterfaces.UpdatePhotoComment.class);
    }

    public RetrofitPerfectPhotoInterfaces.DeletePerfectPhoto deletePerfectPhoto() {
        return retrofit.create(RetrofitPerfectPhotoInterfaces.DeletePerfectPhoto.class);
    }

    public RetrofitPerfectPhotoInterfaces.UpdatePhotoPost updatePhotoPost() {
        return retrofit.create(RetrofitPerfectPhotoInterfaces.UpdatePhotoPost.class);
    }
}
