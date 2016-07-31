package ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.BackgroundImage;
import ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.data.FeedItem;
import ravtrix.backpackerbuddy.ServerRequests.ServerRequests;
import ravtrix.backpackerbuddy.UserLocalStore;

/**
 * Created by Ravinder on 2/25/16.
 */
public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();;
    ServerRequests serverRequests;
    private UserLocalStore userLocalStore;
    private BackgroundImage backgroundImage;

    public FeedListAdapter(Context context, List<FeedItem> feedItems) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.feedItems = feedItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Custom root of recycle view
        View view = inflater.inflate(R.layout.item_countryfeed, parent, false);
        // Hold a structure of a view. See class viewholder, which holds the structure
        ViewHolder holder = new ViewHolder(view);
        userLocalStore = new UserLocalStore(mContext);
        backgroundImage = new BackgroundImage();

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        serverRequests = new ServerRequests(mContext);

        // Initialize fonts
        Typeface killFillFont = Typeface.createFromAsset(mContext.getAssets(), "Menufont.ttf");
        holder.tvCountry.setTypeface(killFillFont);

        final FeedItem currentPos = feedItems.get(position);
        holder.tvCountry.setText(currentPos.getCountry());
        holder.tvFromDate.setText(currentPos.getFromDate());
        holder.tvToDate.setText(currentPos.getToDate());

        // Set image background based on country name. Hash will return the correct background id
        holder.backgroundLayout.setBackgroundResource(backgroundImage.getBackgroundFromHash(currentPos.getCountry()));
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    // Holder knows and references where the fields are
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCountry, tvFromDate, tvToDate;
        private  LinearLayout backgroundLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
            tvFromDate = (TextView) itemView.findViewById(R.id.tvFromDate);
            tvToDate = (TextView) itemView.findViewById(R.id.tvToDate);
            backgroundLayout = (LinearLayout) itemView.findViewById(R.id.backgroundLayout);
        }
    }



}
