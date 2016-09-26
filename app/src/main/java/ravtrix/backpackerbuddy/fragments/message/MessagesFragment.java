package ravtrix.backpackerbuddy.fragments.message;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class MessagesFragment extends Fragment {
    @BindView(R.id.frag_userinbox_recyclerView) protected RecyclerView recyclerView;
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
        //RefWatcher refWatcher = UserMainPage.getRefWatcher(getActivity());
        //refWatcher.watch(this);
        ButterKnife.bind(this, view);
        view.setVisibility(View.INVISIBLE);

        fragActivityProgressBarInterface.setProgressBarVisible();

        userLocalStore = new UserLocalStore(getContext());
        //feedListAdapterInbox = new FeedListAdapterInbox(getActivity());

        Call<List<FeedItemInbox>> retrofitCall = RetrofitUserChatSingleton.getRetrofitUserChat()
                .fetchUserInbox()
                .fetchUserInbox(userLocalStore.getLoggedInUser().getUserID());

        retrofitCall.enqueue(new Callback<List<FeedItemInbox>>() {
            @Override
            public void onResponse(Call<List<FeedItemInbox>> call, Response<List<FeedItemInbox>> response) {
                feedItemInbox = response.body();
                for (int i = 0; i < feedItemInbox.size(); i++) {
                    if (feedItemInbox.get(i).getStatus() == 0) {
                        // You made this chat room. your userID is the first of chat room name;
                        String chatName = userLocalStore.getLoggedInUser().getUserID() +
                                Integer.toString(feedItemInbox.get(i).getUserID());
                        feedItemInbox.get(i).setChatRoom(chatName);
                    } else {
                        // you didn't make this room
                        String chatName = Integer.toString(feedItemInbox.get(i).getUserID()) +
                                Integer.toString(userLocalStore.getLoggedInUser().getUserID());
                        System.out.println("USER ID: " + Integer.toString(feedItemInbox.get(i).getUserID()));
                        feedItemInbox.get(i).setChatRoom(chatName);
                    }
                }

                for (int i = 0; i < feedItemInbox.size(); i++) {
                    System.out.println("CHATNAME: " + feedItemInbox.get(i).getChatRoom());
                }

                feedListAdapterInbox = new FeedListAdapterInbox(getContext(), feedItemInbox,
                        view, fragActivityProgressBarInterface);
                firebaseListener();
            }

            @Override
            public void onFailure(Call<List<FeedItemInbox>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });


        return view;
    }

    private void setRecyclerView(FeedListAdapterInbox feedListAdapterInbox) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapterInbox);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_inbox);
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

                    if (dataSnapshot.child(chatName).exists()) {
                        // access last message
                        long count = dataSnapshot.child(chatName).getChildrenCount(); // Number of children

                        for (int x = 0; x < (int) count; x++) {
                            // Loop to get last child message
                            snapshot = iterator.next();
                        }
                    }
                    if (snapshot != null) {
                        feedItemInbox.get(i).setLatestMessage(snapshot.child("text").getValue().toString());
                        // Converting timestamp into x ago format
                        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                                Long.parseLong(snapshot.child("time").getValue().toString()),
                                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                        feedItemInbox.get(i).setLatestDate((String) timeAgo);
                    }
                    setRecyclerView(feedListAdapterInbox);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
