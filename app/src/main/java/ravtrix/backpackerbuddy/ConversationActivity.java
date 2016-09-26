package ravtrix.backpackerbuddy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.fcm.model.Message;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserChatSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.chat_recyclerView) protected RecyclerView mMessageRecyclerView;
    @BindView(R.id.sendMessageButton) protected ImageView mSendButton;
    @BindView(R.id.textMessage) protected EditText textMessage;
    @BindView(R.id.activity_conversation_spinner) protected ProgressBar progressBar;
    private LinearLayoutManager mLinearLayoutManager;
    private String chatRoomName, chatRoomName2;
    private Bundle bundle;

    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private DatabaseReference mFirebaseDatabaseReference;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);

        toolbar.setTitle("Conversation");
        toolbar.setTitleTextColor(Color.WHITE);

        progressBar.setVisibility(View.VISIBLE);

        userLocalStore = new UserLocalStore(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        bundle = getIntent().getExtras();

        String otherUserID = "0";
        String myUserID = "0";
        if (bundle != null) {
            otherUserID = bundle.getString("otherUserID");
        }

        myUserID = Integer.toString(userLocalStore.getLoggedInUser().getUserID());
        chatRoomName = myUserID + otherUserID;
        chatRoomName2 = otherUserID + myUserID;

        // Create userChat object
        final UserChat userChat = new UserChat();
        userChat.setUserOne(Integer.parseInt(myUserID));
        userChat.setUserTwo(Integer.parseInt(otherUserID));

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(chatRoomName).exists()) {
                    System.out.println("CHILD EXISTS");
                    setRecyclerView(chatRoomName);
                } if (dataSnapshot.child(chatRoomName2).exists()) {
                    setRecyclerView(chatRoomName2);
                } else  {
                    progressBar.setVisibility(View.INVISIBLE);
                    System.out.println("CHILD DOESN'T EXISTS");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(chatRoomName).exists()) {
                            System.out.println("CHILD EXISTS");
                            Message message = new
                                    Message(userLocalStore.getLoggedInUser().getUserID(), textMessage.getText().toString(),
                                    "http://backpackerbuddy.net23.net/profile_pic/" +
                                            userLocalStore.getLoggedInUser().getUserID() + ".JPG",
                                    System.currentTimeMillis());
                            mFirebaseDatabaseReference.child(chatRoomName)
                                    .push().setValue(message);
                            textMessage.setText("");
                        } else if (dataSnapshot.child(chatRoomName2).exists()) {
                            Message message = new
                                    Message(userLocalStore.getLoggedInUser().getUserID(), textMessage.getText().toString(),
                                    "http://backpackerbuddy.net23.net/profile_pic/" +
                                            userLocalStore.getLoggedInUser().getUserID() + ".JPG",
                                    System.currentTimeMillis());
                            mFirebaseDatabaseReference.child(chatRoomName2)
                                    .push().setValue(message);
                            textMessage.setText("");
                        } else {

                            // New chat, so create new chat in FCM and also database
                            Call<JsonObject> jsonObjectCall = RetrofitUserChatSingleton.getRetrofitUserChat()
                                    .insertNewChat()
                                    .insertNewChat(userChat.getUserOne(), userChat.getUserTwo());

                            jsonObjectCall.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    //sucess
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });


                            Map<String, Object> userData = new HashMap<>();
                            userData.put("photoUrl",
                                    "http://backpackerbuddy.net23.net/profile_pic/" +
                                            userLocalStore.getLoggedInUser().getUserID() + ".JPG");
                            userData.put("text", textMessage.getText().toString());
                            userData.put("time", System.currentTimeMillis());
                            userData.put("userID", userLocalStore.getLoggedInUser().getUserID());
                            mFirebaseDatabaseReference.child(chatRoomName).push().setValue(userData);
                            textMessage.setText("");
                            setRecyclerView(chatRoomName);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timeTextView;
        CircleImageView messengerImageView;
        LinearLayout layoutMessage;


        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.item_message_message);
            timeTextView = (TextView) itemView.findViewById(R.id.item_time);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.item_inboxFeed_profileImage);
            layoutMessage = (LinearLayout) itemView.findViewById(R.id.layout_message);
        }
    }

    private void setRecyclerView(String roomName) {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(roomName)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                System.out.println("!!!!POSITION :  " + position);
                System.out.println("!!!!COUNT :  " + getItemCount());

                if (position + 1 == getItemCount()) {
                    progressBar.setVisibility(View.INVISIBLE);
                }


                if (model.getUserID() == userLocalStore.getLoggedInUser().getUserID()) {
                    // It's the current user's chat so display it on the right side

                    TextView messageTextView = viewHolder.messageTextView;
                    //messageTextView.setGravity(Gravity.LEFT);
                    CircleImageView profileImage = viewHolder.messengerImageView;
                    viewHolder.layoutMessage.removeAllViews();

                    viewHolder.layoutMessage.addView(messageTextView);
                    viewHolder.layoutMessage.addView(profileImage);

                }

                viewHolder.messageTextView.setText(model.getText());

                // Converting timestamp into x ago format
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                        model.getTime(),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                viewHolder.timeTextView.setText(timeAgo);
                if (model.getPhotoUrl() == null) {
                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(ConversationActivity.this,
                            R.drawable.ic_chat_bubble_black_24dp));
                } else {
                    Picasso.with(ConversationActivity.this)
                            .load(model.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private class UserChat {
        private int userOne;
        private int userTwo;

        public int getUserOne() {
            return userOne;
        }

        public void setUserOne(int userOne) {
            this.userOne = userOne;
        }

        public int getUserTwo() {
            return userTwo;
        }

        public void setUserTwo(int userTwo) {
            this.userTwo = userTwo;
        }
    }
}
