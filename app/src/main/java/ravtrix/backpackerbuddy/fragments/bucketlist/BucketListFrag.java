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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.bucketlist.newbucket.BucketPostActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.adapter.BucketListAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;

public class BucketListFrag extends Fragment implements View.OnClickListener, IBucketListFragView {

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
    private BucketListFragPresenter bucketListFragPresenter;

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
        bucketListFragPresenter = new BucketListFragPresenter(this);
        dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_inbox);

        bucketListFragPresenter.fetchUserBucketList(userLocalStore.getLoggedInUser().getUserID());
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
                                    bucketListFragPresenter.updateBucketVisibilityRetrofit(userLocalStore.getLoggedInUser().getUserID(), 1);
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
                                    bucketListFragPresenter.updateBucketVisibilityRetrofit(userLocalStore.getLoggedInUser().getUserID(), 0);
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

    public void fetchUserBucketListRefresh() {
        bucketListFragPresenter.fetchUserBucketListUpdate(userLocalStore.getLoggedInUser().getUserID(), bucketListAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CODE) {
                bucketListFragPresenter.fetchUserBucketListUpdate(userLocalStore.getLoggedInUser().getUserID(), bucketListAdapter); // refresh from new bucket submission
            }
        }
    }

    private void setTypeface() {
        Helpers.overrideFonts(getContext(), tvNoBucket);
    }

    @Override
    public void setRecyclerViewVisible() {
        bucketRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRecyclerViewInvisible() {
        bucketRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void setProgressbarVisible() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setProgressbarInvisible() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setBucketModels(List<BucketListModel> bucketModels) {
        bucketListModels = bucketModels;
    }

    @Override
    public void setBucketModelsEmpty() {
        bucketListModels = new ArrayList<>();
    }

    @Override
    public void setRecyclerView() {
        bucketListAdapter = new BucketListAdapter(getContext(), BucketListFrag.this, bucketListModels);
        bucketRecyclerView.setAdapter(bucketListAdapter);
        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bucketRecyclerView.addItemDecoration(dividerDecorator);
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(getContext());
    }

    @Override
    public void swapBucketModels(List<BucketListModel> bucketModels) {
        bucketListAdapter.swap(bucketListModels);
    }

    @Override
    public void changeBucketStatLocalstore(int status) {
        userLocalStore.changeBucketStat(status);
    }

    @Override
    public void showTvNoBucket() {
        tvNoBucket.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTvNoBucket() {
        tvNoBucket.setVisibility(View.INVISIBLE);
    }
}