package ravtrix.backpackerbuddy.SlidingDrawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ravtrix.backpackerbuddy.R;

/**
 * Created by Ravinder on 7/29/16.
 */
public class SlidingMenuAdapter extends BaseAdapter {


    private Context context; // Context to access the base view where this is created
    private List<ItemSlideMenu> listItem; // A list of navigation drawer items will be provided

    // Constructor takes in context and an array list of list items in navigation drawer
    public SlidingMenuAdapter(Context context, List<ItemSlideMenu> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout.item_sliding_menu, null);
        TextView text = (TextView) v.findViewById(R.id.title);

        ItemSlideMenu item = listItem.get(position);
        text.setText(item.getTitle());
        text.setTextSize(22);

        return v;
    }
}
