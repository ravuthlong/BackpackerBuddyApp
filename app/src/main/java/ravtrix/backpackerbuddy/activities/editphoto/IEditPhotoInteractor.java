package ravtrix.backpackerbuddy.activities.editphoto;

/**
 * Created by Ravinder on 9/28/16.
 */

public interface IEditPhotoInteractor {

    void retrofitUploadProfileImg(int userID, String encodedImage,
                                  OnRetrofitEditPhotoListener onRetrofitEditPhotoListener);
}
