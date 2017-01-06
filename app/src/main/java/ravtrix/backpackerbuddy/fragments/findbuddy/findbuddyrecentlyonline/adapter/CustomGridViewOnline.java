package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddyrecentlyonline.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.otheruserprofile.OtherUserProfile;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocationInfo;

/**
 * Created by Ravinder on 12/18/16.
 */

public class CustomGridViewOnline extends BaseAdapter {
    private Context context;
    private List<UserLocationInfo> nearbyUserInfo;

    public CustomGridViewOnline(Context context, List<UserLocationInfo> nearbyUserInfo) {
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
        TextView country;
        RelativeLayout relativeLayout;

        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.item_gridview_online, parent, false);
        } else {
            gridView = (View) convertView;
        }
        profileImage = (CircleImageView) gridView.findViewById(R.id.grid_userImageOnline);
        country = (TextView) gridView.findViewById(R.id.tvCountry_online);
        relativeLayout = (RelativeLayout) gridView.findViewById(R.id.item_gridLayoutOnline);

        Helpers.overrideFonts(context, relativeLayout);

        String userCountry = nearbyUserInfo.get(position).getCountry();
        if (!userCountry.isEmpty()) {
            country.setText(userCountry);
        } else {
            //empty country
            country.setText("Unknown");
        }
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

        return gridView;
    }
}
