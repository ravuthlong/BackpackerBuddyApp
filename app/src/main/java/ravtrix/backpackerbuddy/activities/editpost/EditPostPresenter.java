package ravtrix.backpackerbuddy.activities.editpost;

/**
 * Created by Ravinder on 9/14/16.
 */
public class EditPostPresenter {

    private IEditPostView editPostView;
    private String detailType;

    public EditPostPresenter(IEditPostView editView) {
        this.editPostView = editView;
    }
}
