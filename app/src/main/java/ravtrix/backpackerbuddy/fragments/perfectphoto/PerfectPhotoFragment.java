package ravtrix.backpackerbuddy.fragments.perfectphoto;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.perfectphoto.postperfectphoto.PostPerfectPhotoActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitPerfectPhotoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.perfectphotorecyclerview.PerfectPhotoAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.perfectphotorecyclerview.PerfectPhotoModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getCacheDir;
import static com.facebook.GraphRequest.TAG;

/**
 * Created by Ravinder on 1/11/17.
 */

public class PerfectPhotoFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.frag_perfect_circleview) protected CircleImageView bSubmitPhoto;
    @BindView(R.id.frag_perfect_recyclerView) protected RecyclerView recyclerView;
    @BindView(R.id.frag_perfect_progressBar) protected ProgressBar progressBar;
    @BindView(R.id.frag_perfect_swipe) protected SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private static final int REQUEST_REFRESH = 1;
    private static final int RESULT_REFRESH = 1;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    private AlertDialog mAlertDialog;
    private UserLocalStore userLocalStore;
    private List<PerfectPhotoModel> perfectPhotoModels;
    private PerfectPhotoAdapter perfectPhotoAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.frag_perfect_photo, container, false);
        ButterKnife.bind(this, view);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        bSubmitPhoto.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        userLocalStore = new UserLocalStore(getContext());
        Helpers.checkLocationUpdate(getActivity(), userLocalStore);

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_inbox);
        recyclerView.addItemDecoration(dividerDecorator);

        fetchPerfectPhotos();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_perfect_circleview:
                if (userLocalStore.getLoggedInUser().getUserID() != 0) {
                    pickFromGallery();
                } else {
                    Helpers.displayToast(getContext(), "Become a member to post...");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        fetchPerfectPhotosRefresh();
        // stop refreshing layout is it is running
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void fetchPerfectPhotos() {

        Call<List<PerfectPhotoModel>> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .getPerfectPhotos()
                .getPerfectPhotos(userLocalStore.getLoggedInUser().getUserID());
        retrofit.enqueue(new Callback<List<PerfectPhotoModel>>() {
            @Override
            public void onResponse(Call<List<PerfectPhotoModel>> call, Response<List<PerfectPhotoModel>> response) {
                if (response.body().get(0).getSuccess() == 1) {
                    perfectPhotoModels = response.body();
                } else {
                    perfectPhotoModels = new ArrayList<>(); //empty
                    progressBar.setVisibility(View.GONE);

                    // Empty recycler view
                }
                perfectPhotoAdapter = new PerfectPhotoAdapter(getContext(), PerfectPhotoFragment.this,
                        perfectPhotoModels, progressBar, recyclerView, userLocalStore);
                recyclerView.setAdapter(perfectPhotoAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            @Override
            public void onFailure(Call<List<PerfectPhotoModel>> call, Throwable t) {
                Helpers.displayErrorToast(getContext());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void fetchPerfectPhotosRefresh() {

        Call<List<PerfectPhotoModel>> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .getPerfectPhotos()
                .getPerfectPhotos(userLocalStore.getLoggedInUser().getUserID());
        retrofit.enqueue(new Callback<List<PerfectPhotoModel>>() {
            @Override
            public void onResponse(Call<List<PerfectPhotoModel>> call, Response<List<PerfectPhotoModel>> response) {
                if (response.body().get(0).getSuccess() == 1) {
                    perfectPhotoModels = response.body();
                } else {
                    perfectPhotoModels = new ArrayList<>(); //empty
                    // Empty recycler view
                }
                perfectPhotoAdapter.swap(perfectPhotoModels);
            }
            @Override
            public void onFailure(Call<List<PerfectPhotoModel>> call, Throwable t) {
                Helpers.displayErrorToast(getContext());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    Toast.makeText(getActivity(), R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (requestCode == REQUEST_REFRESH && resultCode == RESULT_REFRESH) {
            fetchPerfectPhotosRefresh();
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    public void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            PostPerfectPhotoActivity.startWithUri(getContext(), this, resultUri);
        } else {
            Toast.makeText(getActivity(), R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    public void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(getActivity(), cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }
    private void startCropActivity(Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
        destinationFileName += ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)))
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500);
        uCrop.start(getActivity(), this);
    }

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void showAlertDialog(String title, String message,
                                   DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   String positiveText,
                                   DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }
}
