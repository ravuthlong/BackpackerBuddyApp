package ravtrix.backpackerbuddy.fragments.findbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.models.UserLocationInfo;

/**
 * Created by Ravinder on 9/16/16.
 */
public class CustomGridView extends BaseAdapter {

    private Context context;
    private List<UserLocationInfo> nearbyUserInfo;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private View view;
    private Counter counter;

    public CustomGridView(Context context, List<UserLocationInfo> nearbyUserInfo, View view,
                          FragActivityProgressBarInterface fragActivityProgressBarInterface) {
        this.context = context;
        this.nearbyUserInfo = nearbyUserInfo;
        this.view = view;
        this.fragActivityProgressBarInterface = fragActivityProgressBarInterface;
        counter = new Counter();
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
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View gridView;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.item_gridview, null);
            CircleImageView profileImage = (CircleImageView) gridView.findViewById(R.id.grid_userImage1);
            Picasso.with(context)
                    .load(nearbyUserInfo.get(position).getUserpic())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.drawable.ic_placeholder)
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
            fragActivityProgressBarInterface.setProgressBarInvisible();
            view.setVisibility(View.VISIBLE);
        }
    }

    // Keeps track how many picasso images have been loaded onto grid view
    private class Counter {
        private int count = -1; // Indexing for array start at 0 so first item added should start at 0

        void addCount() {
            count++;
        }

        int getCount() {
            return count;
        }
    }
}
