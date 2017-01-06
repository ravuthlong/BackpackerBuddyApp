package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.models.UserLocationInfo;

/**
 * Created by Ravinder on 9/16/16.
 */
public class CustomGridView extends BaseAdapter {

    private Context context;
    private List<UserLocationInfo> nearbyUserInfo;

    public CustomGridView(Context context, List<UserLocationInfo> nearbyUserInfo) {
        this.context = context;
        this.nearbyUserInfo = nearbyUserInfo;
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
            gridView = inflater.inflate(R.layout.item_gridview_nearby, parent, false);
        } else {
            gridView = (View) convertView;
        }

        profileImage = (CircleImageView) gridView.findViewById(R.id.grid_userImage1);

        Picasso.with(context)
                .load(nearbyUserInfo.get(position).getUserpic())
                .placeholder(R.drawable.default_photo)
                .fit()
                .centerCrop()
                .into(profileImage);

        // Clicking on any nearby user brings you to their profile page
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userProfileIntent = new Intent(context, OtherUserProfile.class);
                userProfileIntent.putExtra("userID", nearbyUserInfo.get(position).getUserID());
                context.startActivity(userProfileIntent);
            }
        });

        // Display layout only when all images has been loaded
        return gridView;
    }
}
