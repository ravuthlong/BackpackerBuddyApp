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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.fcm.FirebaseMessagingService;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserChatSingleton;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
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
    @BindView(R.id.tvNoInfo_FragInbox) protected TextView noInbox;
    private FeedListAdapterInbox feedListAdapterInbox;
    private List<FeedItemInbox> feedItemInbox;
    private UserLocalStore userLocalStore;
    private DatabaseReference mFirebaseDatabaseReference;
    private FragActivityProgressBarInterface fragActivityProgressBarInterface;
    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragActivityProgressBarInterface = (FragActivityProgressBarInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.frag_userinbox, container, false);
        FirebaseMessagingService.cancelNotification(getActivity(), 0);

        ButterKnife.bind(this, view);
        view.setVisibility(View.INVISIBLE);

        Helpers.overrideFonts(getContext(), noInbox);

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_inbox);
        recyclerView.addItemDecoration(dividerDecorator);

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

    private void setRecyclerView(FeedListAdapterInbox feedListAdapterInbox) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapterInbox);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * Fetch all the chats user is in from the database and their latest message,
     * latest time from Firebase cloud
     */
    private void fetchUserInboxChat() {
        Call<List<FeedItemInbox>> retrofitCall = RetrofitUserChatSingleton.getRetrofitUserChat()
                .fetchUserInbox()
                .fetchUserInbox(userLocalStore.getLoggedInUser().getUserID());

        retrofitCall.enqueue(new Callback<List<FeedItemInbox>>() {
            @Override
            public void onResponse(Call<List<FeedItemInbox>> call, Response<List<FeedItemInbox>> response) {

                feedItemInbox = response.body(); // Retrofit returns back the user IDs you're chatting with
                                                    // and whether or not you are the creator of that chat

                if (feedItemInbox.get(0).getSuccess() == 0) {
                    fragActivityProgressBarInterface.setProgressBarInvisible();
                    view.setVisibility(View.VISIBLE);
                    noInbox.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                } else {

                    String chatName;

                    /**
                     * Setting up chat room names for each chat feed items, before accessing the FCM
                     */
                    for (int i = 0; i < feedItemInbox.size(); i++) {
                        // Status 0 means you made this chat, status 1 means you didn't make this chat
                        if (feedItemInbox.get(i).getStatus() == 0) {
                            // You made this chat room. your userID is the first of chat room name (Firebase chat name)
                             chatName = userLocalStore.getLoggedInUser().getUserID() +
                                    Integer.toString(feedItemInbox.get(i).getUserID());
                            feedItemInbox.get(i).setChatRoom(chatName);
                        } else {
                            // you didn't make this room. your userID is the second of chat room name (Firebase chat name)
                            chatName = Integer.toString(feedItemInbox.get(i).getUserID()) +
                                    Integer.toString(userLocalStore.getLoggedInUser().getUserID());
                            feedItemInbox.get(i).setChatRoom(chatName);
                        }
                    }

                    firebaseListener(); // Call listener to fill inbox recyclerview data
                }

            }
            @Override
            public void onFailure(Call<List<FeedItemInbox>> call, Throwable t) {
                fragActivityProgressBarInterface.setProgressBarInvisible();
                Helpers.displayErrorToast(getContext());
            }
        });
    }

    private void firebaseListener() {
        final PassValue counter = new PassValue(0);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (int i = 0; i < feedItemInbox.size(); i++) {

                    final PassValue passValue = new PassValue(i);
                    String chatName = feedItemInbox.get(i).getChatRoom();
                    //Iterator<DataSnapshot> iterator = dataSnapshot.child(chatName).getChildren().iterator();

                    // Iterate to the last child of the chatroom to get latest snapshot for latest message
                    if (dataSnapshot.child(chatName).exists()) {

                        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference().child(chatName);
                        mFirebaseDatabaseReference.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                counter.increment();

                                if (dataSnapshot.exists()) {
                                    DataSnapshot snapshot;
                                    // access last message
                                    snapshot = dataSnapshot.getChildren().iterator().next();

                                    /*
                                     * Set the components of each inbox message by getting its child from the database through key vale pair
                                     */
                                    if (snapshot != null) {
                                        String time = snapshot.child("time").getValue().toString();
                                        String latestMessage = snapshot.child("text").getValue().toString();

                                        if (countLines(latestMessage) > 4) {
                                            latestMessage = getFirstFourLines(latestMessage);
                                        }

                                        feedItemInbox.get(passValue.getI()).setLatestMessage(latestMessage);

                                        // Converting timestamp into x ago format
                                        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                                                Long.parseLong(time),
                                                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                                        feedItemInbox.get(passValue.getI()).setLatestDate((String) timeAgo);
                                        feedItemInbox.get(passValue.getI()).setTimeMilli(Long.parseLong(time));

                                        if (snapshot.child("isOtherUserClicked").exists()) {
                                            feedItemInbox.get(passValue.getI()).setIsOtherUserClicked((Integer.parseInt(snapshot.child("isOtherUserClicked").getValue().toString())));
                                        } else {
                                            feedItemInbox.get(passValue.getI()).setIsOtherUserClicked(1);
                                        }
                                        if (snapshot.child("userID").exists()) {
                                            feedItemInbox.get(passValue.getI()).setLastMessageUserID(Integer.parseInt(snapshot.child("userID").getValue().toString()));
                                        }
                                    }
                                    feedItemInbox.get(passValue.getI()).setSnapshot(snapshot);
                                }

                                // completion
                                if (counter.getI() == feedItemInbox.size()) {
                                    // Sort chat rooms in order based on time
                                    Collections.sort(feedItemInbox);
                                    feedListAdapterInbox = new FeedListAdapterInbox(MessagesFragment.this, getContext(), feedItemInbox,
                                            view, fragActivityProgressBarInterface);
                                    setRecyclerView(feedListAdapterInbox);

                                    fragActivityProgressBarInterface.setProgressBarInvisible();
                                    view.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
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

            if (countLines(newMessage) > 4) {
                newMessage = getFirstFourLines(newMessage);
            }

            // Update latest message at the position the user sent a new message
            feedListAdapterInbox.getViewHolder().updateMessage(postChangePosition, newMessage);

            // Update the timestamp at the position the user sent a new message
            feedListAdapterInbox.getViewHolder().updateTime(postChangePosition, time);
        }
    }

    private static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }

    private static String getFirstFourLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines[0] + "\n" + lines[1] + "\n" + lines[2] + "\n" + lines[3] + "...";
    }

    private class PassValue {
        int i;

        PassValue(int i) {
            this.i = i;
        }
        void setI(int i) {
            this.i = i;
        }
        int getI() {
            return i;
        }
        void increment() { i++; }
    }
}
