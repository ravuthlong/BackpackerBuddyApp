package ravtrix.backpackerbuddy.fragments.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.RetrofitUserChatSingleton;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.decorator.DividerDecoration;
import ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.adapter.FeedListAdapterInbox;
import ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.data.FeedItemInbox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 7/29/16.
 */
public class MessagesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.frag_userinbox_recyclerView) protected RecyclerView recyclerView;
    @BindView(R.id.frag_userInbox_swipeRefresh) protected SwipeRefreshLayout swipeRefreshLayout;
    private FeedListAdapterInbox feedListAdapterInbox;
    private List<FeedItemInbox> feedItemInbox;
    private UserLocalStore userLocalStore;
    private DatabaseReference mFirebaseDatabaseReference;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private View view;
    private RecyclerView.ItemDecoration dividerDecorator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
        dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_inbox);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.frag_userinbox, container, false);
        //RefWatcher refWatcher = UserMainPage.getRefWatcher(getActivity());
        //refWatcher.watch(this);
        ButterKnife.bind(this, view);
        view.setVisibility(View.INVISIBLE);

        fragActivityProgressBarInterface.setProgressBarVisible();
        userLocalStore = new UserLocalStore(getContext());
        swipeRefreshLayout.setOnRefreshListener(this);

        fetchUserInboxChat();
        return view;
    }

    @Override
    public void onRefresh() {
        fetchUserInboxChat();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void fetchUserInboxChat() {
        Call<List<FeedItemInbox>> retrofitCall = RetrofitUserChatSingleton.getRetrofitUserChat()
                .fetchUserInbox()
                .fetchUserInbox(userLocalStore.getLoggedInUser().getUserID());

        retrofitCall.enqueue(new Callback<List<FeedItemInbox>>() {
            @Override
            public void onResponse(Call<List<FeedItemInbox>> call, Response<List<FeedItemInbox>> response) {

                feedItemInbox = response.body(); // Retrofit returns back the user IDs you're chatting with
                // and whether or not you are the creator of that chat

                for (int i = 0; i < feedItemInbox.size(); i++) {
                    if (feedItemInbox.get(i).getStatus() == 0) {
                        // You made this chat room. your userID is the first of chat room name (Firebase chat name)
                        String chatName = userLocalStore.getLoggedInUser().getUserID() +
                                Integer.toString(feedItemInbox.get(i).getUserID());
                        feedItemInbox.get(i).setChatRoom(chatName);
                    } else {
                        // you didn't make this room. your userID is the second of chat room name (Firebase chat name)
                        String chatName = Integer.toString(feedItemInbox.get(i).getUserID()) +
                                Integer.toString(userLocalStore.getLoggedInUser().getUserID());
                        feedItemInbox.get(i).setChatRoom(chatName);
                    }
                }

                firebaseListener(); // Call listener to fill inbox recyclerview data
                feedListAdapterInbox = new FeedListAdapterInbox(MessagesFragment.this, getContext(), feedItemInbox,
                        view, fragActivityProgressBarInterface);

            }
            @Override
            public void onFailure(Call<List<FeedItemInbox>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void setRecyclerView(FeedListAdapterInbox feedListAdapterInbox) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapterInbox);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(dividerDecorator);
    }

    private void firebaseListener() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot snapshot = null;

                for (int i = 0; i < feedItemInbox.size(); i++) {
                    String chatName = feedItemInbox.get(i).getChatRoom();
                    Iterator<DataSnapshot> iterator = dataSnapshot.child(chatName).getChildren().iterator();

                    // Iterate to the last child of the chatroom to get latest snapshot for latest message
                    if (dataSnapshot.child(chatName).exists()) {
                        // access last message
                        long count = dataSnapshot.child(chatName).getChildrenCount(); // Number of children

                        for (int x = 0; x < (int) count; x++) {
                            // Loop to get last child message
                            snapshot = iterator.next();
                        }
                    }

                    /*
                     * Set the components of each inbox message by getting its child from the database through key vale pair
                     */
                    if (snapshot != null) {
                        String time = snapshot.child("time").getValue().toString();
                        feedItemInbox.get(i).setLatestMessage(snapshot.child("text").getValue().toString());

                        // Converting timestamp into x ago format
                        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                                Long.parseLong(time),
                                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                        feedItemInbox.get(i).setLatestDate((String) timeAgo);
                        feedItemInbox.get(i).setTimeMilli(Long.parseLong(time));

                        if (snapshot.child("isOtherUserClicked").exists()) {
                            feedItemInbox.get(i).setIsOtherUserClicked((Integer.parseInt(snapshot.child("isOtherUserClicked").getValue().toString())));
                        } else {
                            feedItemInbox.get(i).setIsOtherUserClicked(1);
                        }
                        if (snapshot.child("userID").exists()) {
                            feedItemInbox.get(i).setLastMessageUserID(Integer.parseInt(snapshot.child("userID").getValue().toString()));
                        }
                    }
                    feedItemInbox.get(i).setSnapshot(snapshot);
                }
                // Sort chat rooms in order based on time
                Collections.sort(feedItemInbox);
                setRecyclerView(feedListAdapterInbox);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            int postChangePosition = data.getIntExtra("position", 0);
            String newMessage = data.getStringExtra("newMessage");
            long time = data.getLongExtra("time", 0);

            // Update latest message at the position the user sent a new message
            feedListAdapterInbox.getViewHolder().updateMessage(postChangePosition, newMessage);

            // Update the timestamp at the position the user sent a new message
            feedListAdapterInbox.getViewHolder().updateTime(postChangePosition, time);
        }
    }
}
