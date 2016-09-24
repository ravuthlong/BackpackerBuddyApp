package ravtrix.backpackerbuddy.fragments.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.adapter.FeedListAdapterInbox;
import ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.data.FeedItemInbox;

/**
 * Created by Ravinder on 7/29/16.
 */
public class MessagesFragment extends Fragment {
    @BindView(R.id.frag_userinbox_recyclerView) protected RecyclerView recyclerView;
    private FeedListAdapterInbox feedListAdapterInbox;
    private List<FeedItemInbox> feedItemInbox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_userinbox, container, false);
        //RefWatcher refWatcher = UserMainPage.getRefWatcher(getActivity());
        //refWatcher.watch(this);
        ButterKnife.bind(this, v);

        //feedListAdapterInbox = new FeedListAdapterInbox(getActivity());




        return v;
    }
}
