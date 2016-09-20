package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

import android.content.Context;
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
import ravtrix.backpackerbuddy.models.UserLocationInfo;

/**
 * Created by Ravinder on 9/16/16.
 */
public class CustomGridView extends BaseAdapter {

    private Context context;
    private List<UserLocationInfo> profileURL;

    public CustomGridView(Context context, List<UserLocationInfo> userLocationInfo) {
        this.context = context;
        this.profileURL = userLocationInfo;
    }

    @Override
    public int getCount() {
        return profileURL.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.item_gridview, null);
            CircleImageView profileImage = (CircleImageView) gridView.findViewById(R.id.grid_userImage1);
            Picasso.with(context)
                    .load(profileURL.get(position).getUserpic())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(profileImage);
        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}
