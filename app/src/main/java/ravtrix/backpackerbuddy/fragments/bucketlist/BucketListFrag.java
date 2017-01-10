package ravtrix.backpackerbuddy.fragments.bucketlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.BucketComparator;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.bucketlist.BucketPostActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserBucketListSingleton;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.adapter.BucketListAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BucketListFrag extends Fragment implements View.OnClickListener {

    @BindView(R.id.frag_bucket_RecyclerView) protected RecyclerView bucketRecyclerView;
    @BindView(R.id.frag_bucket_noBucket) protected TextView tvNoBucket;
    @BindView(R.id.frag_bucket_progressBar) protected ProgressBar progressBar;
    @BindView(R.id.frag_bucket_txAddBucket) protected TextView tvAddBucket;
    private View view;
    private BucketListAdapter bucketListAdapter;
    private List<BucketListModel> bucketListModels;
    private RecyclerView.ItemDecoration dividerDecorator;
    private UserLocalStore userLocalStore;
    private static int REQUEST_CODE = 1;
    private static int RESULT_CODE = 1; // After new submission, edit
    private boolean cancelState;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.frag_bucket_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        Helpers.overrideFonts(getContext(), tvAddBucket);
        progressBar.setVisibility(View.VISIBLE);

        setTypeface();
        tvAddBucket.setOnClickListener(this);
        userLocalStore = new UserLocalStore(getContext());
        dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_inbox);

        fetchUserBucketList();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (userLocalStore.getLoggedInUser().getUserID() != 0) {
            MenuInflater menuInflater = getActivity().getMenuInflater();
            menuInflater.inflate(R.menu.toolbar_switch, menu);

            final MenuItem menuItemPrivatePublic = menu.findItem(R.id.tvPrivatePublic);
            MenuItem menuItemSwitch = menu.findItem(R.id.myswitch);
            View view = MenuItemCompat.getActionView(menuItemSwitch);
            final SwitchCompat switchButton = (SwitchCompat) view.findViewById(R.id.switchButton);

            if (userLocalStore.getLoggedInUser().getBucketStatus() == 1) { // Public
                switchButton.setChecked(true);
                menuItemPrivatePublic.setTitle("Public");
            } else {
                // Private
                switchButton.setChecked(false);
            }

            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (checked) {

                        if (!cancelState) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                            dialogBuilder.setTitle("Change visibility");
                            dialogBuilder.setMessage("Are you sure you want to make it public? Other users will be able to see your bucket list.");
                            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    cancelState = true;
                                    switchButton.setChecked(false);
                                    dialogInterface.dismiss();
                                }
                            });
                            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    menuItemPrivatePublic.setTitle("Public");
                                    updateBucketVisibility(userLocalStore.getLoggedInUser().getUserID(), 1);
                                }
                            });
                            dialogBuilder.show();
                        }
                    } else {

                        if (!cancelState) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                            dialogBuilder.setTitle("Change visibility");
                            dialogBuilder.setMessage("Are you sure you want to make it private? Other users will not be able to see your bucket list.");
                            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    cancelState = true;
                                    switchButton.setChecked(true);
                                    dialogInterface.dismiss();
                                }
                            });
                            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    menuItemPrivatePublic.setTitle("Private");
                                    updateBucketVisibility(userLocalStore.getLoggedInUser().getUserID(), 0);
                                }
                            });
                            dialogBuilder.show();
                        }
                    }
                    cancelState = false;
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.frag_bucket_txAddBucket:
                if (userLocalStore.getLoggedInUser().getUserID() != 0) {
                    startActivityForResult(new Intent(getContext(), BucketPostActivity.class), 1);
                } else  {
                    Helpers.displayToast(getContext(), "Become a member to add to bucket list");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (requestCode == RESULT_CODE) {
                fetchUserBucketListRefresh(); // refresh from new bucket submission
            }
        }
    }

    private void fetchUserBucketList() {

        Call<List<BucketListModel>> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .fetchUserBucketList()
                .fetchUserBucketList(userLocalStore.getLoggedInUser().getUserID());

        retrofit.enqueue(new Callback<List<BucketListModel>>() {
            @Override
            public void onResponse(Call<List<BucketListModel>> call, Response<List<BucketListModel>> response) {

                progressBar.setVisibility(View.GONE);
                if (response.body().get(0).getSuccess() == 0) {
                    // Empty no list
                    bucketRecyclerView.setVisibility(View.GONE);
                    tvNoBucket.setVisibility(View.VISIBLE);
                } else {
                    bucketListModels = response.body();
                    Collections.sort(bucketListModels, new BucketComparator());

                    bucketListAdapter = new BucketListAdapter(getContext(), BucketListFrag.this, bucketListModels);
                    bucketRecyclerView.setAdapter(bucketListAdapter);
                    bucketRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    bucketRecyclerView.addItemDecoration(dividerDecorator);
                }
            }

            @Override
            public void onFailure(Call<List<BucketListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Helpers.displayErrorToast(getContext());
            }
        });
    }

    public void fetchUserBucketListRefresh() {

        Call<List<BucketListModel>> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .fetchUserBucketList()
                .fetchUserBucketList(userLocalStore.getLoggedInUser().getUserID());

        retrofit.enqueue(new Callback<List<BucketListModel>>() {
            @Override
            public void onResponse(Call<List<BucketListModel>> call, Response<List<BucketListModel>> response) {

                if (response.body().get(0).getSuccess() == 0) {
                    // Empty no list
                    bucketRecyclerView.setVisibility(View.GONE);
                    tvNoBucket.setVisibility(View.VISIBLE);
                } else {
                    bucketListModels = response.body();
                    Collections.sort(bucketListModels, new BucketComparator());

                    bucketListAdapter.swap(bucketListModels);
                }
            }
            @Override
            public void onFailure(Call<List<BucketListModel>> call, Throwable t) {
                Helpers.displayErrorToast(getContext());
            }
        });
    }

    private void updateBucketVisibility(int userID, final int status) {

        Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .updateBucketVisibility()
                .updateBucketVisibility(userID, status);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 0) {
                    Helpers.displayToast(getContext(), "Error");
                } else {
                    userLocalStore.changeBucketStat(status);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(getContext(), "Error");
            }
        });
    }

    private void setTypeface() {
        Helpers.overrideFonts(getContext(), tvNoBucket);
    }

}