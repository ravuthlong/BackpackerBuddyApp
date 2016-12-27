package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddyrecentlyonline.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.Counter;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.fragments.findbuddy.OnFinishedImageLoading;
import ravtrix.backpackerbuddy.models.UserLocationInfo;

/**
 * Created by Ravinder on 12/18/16.
 */

public class CustomGridViewOnline extends BaseAdapter {
    private Context context;
    private List<UserLocationInfo> nearbyUserInfo;
    private View view;
    private Counter counter;
    private OnFinishedImageLoading onFinishedImageLoading;

    public CustomGridViewOnline(Context context, List<UserLocationInfo> nearbyUserInfo, View view,
                          OnFinishedImageLoading onFinishedImageLoading) {
        this.context = context;
        this.nearbyUserInfo = nearbyUserInfo;
        this.view = view;
        this.onFinishedImageLoading = onFinishedImageLoading;
        counter = new Counter(0);
    }

    @Override
    public int getCount() {
        return nearbyUserInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return nearbyUserInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        CircleImageView profileImage;

        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.item_gridview_online, parent, false);
            profileImage = (CircleImageView) gridView.findViewById(R.id.grid_userImageOnline);

            Picasso.with(context)
                    .load(nearbyUserInfo.get(position).getUserpic())
                    .placeholder(R.drawable.ic_placeholder)
                    .fit()
                    .centerCrop()
                    .into(profileImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            counter.addCount();
                            checkPicassoFinished();
                        }

                        @Override
                        public void onError() {
                        }
                    });

            // Clicking on any nearby user brings you to their profile page
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userProfileIntent = new Intent(context, OtherUserProfile.class);
                    userProfileIntent.putExtra("userID", nearbyUserInfo.get(position).getUserID());
                    context.startActivity(userProfileIntent);
                }
            });
        } else {
            gridView = (View) convertView;
        }

        // Display layout only when all images has been loaded
        checkPicassoFinished();
        return gridView;
    }

    private void checkPicassoFinished() {
        if (counter.getCount() == getCount()) {
            onFinishedImageLoading.onFinishedImageLoading();
        }
    }
}
