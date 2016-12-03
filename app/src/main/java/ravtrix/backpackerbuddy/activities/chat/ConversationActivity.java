package ravtrix.backpackerbuddy.activities.chat;

import android.content.Intent;
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
import android.widget.RelativeLayout;
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
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.fcm.model.Message;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserChatSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ravtrix.backpackerbuddy.helpers.RetrofitUserChatSingleton.getRetrofitUserChat;

public class ConversationActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.chat_recyclerView) protected RecyclerView mMessageRecyclerView;
    @BindView(R.id.sendMessageButton) protected ImageView mSendButton;
    @BindView(R.id.textMessage) protected EditText textMessage;
    @BindView(R.id.activity_conversation_spinner) protected ProgressBar progressBar;
    private LinearLayoutManager mLinearLayoutManager;
    private String chatRoomName, chatRoomName2;
    private int chatPosition;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private DatabaseReference mFirebaseDatabaseReference;
    private UserLocalStore userLocalStore;
    private final UserChat userChat = new UserChat();
    private  String otherUserID;
    private ConversationPresentor conversationPresentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);

        setTitle("Conversation");
        toolbar.setTitleTextColor(Color.WHITE);
        progressBar.setVisibility(View.VISIBLE);

        userLocalStore = new UserLocalStore(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        Bundle bundle = getIntent().getExtras();

        otherUserID = "0";
        String myUserID;
        if (bundle != null) {
            otherUserID = bundle.getString("otherUserID"); // ID of the other user in chat
            chatPosition = bundle.getInt("position");
        }

        myUserID = Integer.toString(userLocalStore.getLoggedInUser().getUserID());
        // Create name combo. Only one of these two names exist for the convo between the two users.
        chatRoomName = myUserID + otherUserID;
        chatRoomName2 = otherUserID + myUserID;

        // Create userChat object
        userChat.setUserOne(Integer.parseInt(myUserID));
        userChat.setUserTwo(Integer.parseInt(otherUserID));

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Find which room exists
                if (dataSnapshot.child(chatRoomName).exists()) {
                    setRecyclerView(chatRoomName);
                } if (dataSnapshot.child(chatRoomName2).exists()) {
                    setRecyclerView(chatRoomName2);
                } else  {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textMessage.getText().toString().equals("")) {
                    Helpers.displayToast(ConversationActivity.this, "Empty message");
                } else {
                    sendMessage();

                }

            }
        });
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView1, messageTextView2;
        TextView timeTextView;
        CircleImageView messengerImageView1, messengerImageView2;
        LinearLayout layoutMessage1;
        RelativeLayout layoutMessage2;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView1 = (TextView) itemView.findViewById(R.id.item_message_message);
            timeTextView = (TextView) itemView.findViewById(R.id.item_time);
            messengerImageView1 = (CircleImageView) itemView.findViewById(R.id.item_countryFeed_profileImage);
            layoutMessage1 = (LinearLayout) itemView.findViewById(R.id.layout_message);

            messageTextView2 = (TextView) itemView.findViewById(R.id.item_message_message2);
            messengerImageView2 = (CircleImageView) itemView.findViewById(R.id.item_inboxFeed_profileImage2);
            layoutMessage2 = (RelativeLayout) itemView.findViewById(R.id.layout_message2);
        }
    }

    /*
     * Set recycler view to show appropriate data at the right position
     * The current chatter should have their messages on the right side
     * The receiver should have their messages on the left side
     */
    private void setRecyclerView(String roomName) {

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(roomName)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                if (position + 1 == getItemCount()) {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                if (model.getUserID() == userLocalStore.getLoggedInUser().getUserID()) {
                    viewHolder.layoutMessage2.setVisibility(View.VISIBLE);
                    viewHolder.layoutMessage1.setVisibility(View.GONE);
                    TextView messageTextView2 = viewHolder.messageTextView2;

                    messageTextView2.setText(model.getText());

                    // Converting timestamp into x ago format
                    CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                            model.getTime(),
                            System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                    viewHolder.timeTextView.setText(timeAgo);
                    if (model.getPhotoUrl() == null) {
                        viewHolder.messengerImageView2.setImageDrawable(ContextCompat.getDrawable(ConversationActivity.this,
                                R.drawable.ic_chat_bubble_black_24dp));
                    } else {
                        Picasso.with(ConversationActivity.this)
                                .load(model.getPhotoUrl())
                                .into(viewHolder.messengerImageView2);
                    }

                } else {
                    viewHolder.layoutMessage1.setVisibility(View.VISIBLE);
                    viewHolder.layoutMessage2.setVisibility(View.GONE);
                    TextView messageTextView1 = viewHolder.messageTextView1;

                    messageTextView1.setText(model.getText());
                    // Converting timestamp into x ago format
                    CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                            model.getTime(),
                            System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                    viewHolder.timeTextView.setText(timeAgo);
                    if (model.getPhotoUrl() == null) {
                        viewHolder.messengerImageView1.setImageDrawable(ContextCompat.getDrawable(ConversationActivity.this,
                                R.drawable.ic_chat_bubble_black_24dp));
                    } else {
                        Picasso.with(ConversationActivity.this)
                                .load(model.getPhotoUrl())
                                .into(viewHolder.messengerImageView1);
                    }
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


    /*
     * Send/Save new message to Firebase cloud. Save in the chat room name if room exists.
     * Otherwise, if room doesn't exist between the two users yet, make a new room
     */
    private void sendMessage() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userMessage =  textMessage.getText().toString().trim();
                Long time = System.currentTimeMillis();

                if (dataSnapshot.child(chatRoomName).exists()) {
                    Message message = new
                            Message(userLocalStore.getLoggedInUser().getUserID(), userMessage,
                            "http://backpackerbuddy.net23.net/profile_pic/" +
                                    userLocalStore.getLoggedInUser().getUserID() + ".JPG",
                            time, 0);
                    mFirebaseDatabaseReference.child(chatRoomName)
                            .push().setValue(message);
                    textMessage.setText("");

                    passIntentResult(chatPosition, userMessage, time);

                } else if (dataSnapshot.child(chatRoomName2).exists()) {
                    Message message = new
                            Message(userLocalStore.getLoggedInUser().getUserID(), userMessage,
                            "http://backpackerbuddy.net23.net/profile_pic/" +
                                    userLocalStore.getLoggedInUser().getUserID() + ".JPG",
                            time, 0);
                    mFirebaseDatabaseReference.child(chatRoomName2)
                            .push().setValue(message);
                    textMessage.setText("");

                    passIntentResult(chatPosition, userMessage, time);
                } else {

                    // New chat, so create new chat in FCM and also database
                    Call<JsonObject> jsonObjectCall = getRetrofitUserChat()
                            .insertNewChat()
                            .insertNewChat(userChat.getUserOne(), userChat.getUserTwo());

                    jsonObjectCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            //success
                        }
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("photoUrl",
                            "http://backpackerbuddy.net23.net/profile_pic/" +
                                    userLocalStore.getLoggedInUser().getUserID() + ".JPG");
                    userData.put("text", userMessage);
                    userData.put("time", time);
                    userData.put("userID", userLocalStore.getLoggedInUser().getUserID());
                    userData.put("isOtherUserClicked", 0); // If the other user viewed your message // 0 = false
                    mFirebaseDatabaseReference.child(chatRoomName).push().setValue(userData);
                    textMessage.setText("");
                    setRecyclerView(chatRoomName);

                    passIntentResult(chatPosition, userMessage, time);
                }


                // Notify the other user the message has been sent to them
                notifyOtherUser(userMessage);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /*
     * On back press, pass the new sent message to the MessagesFragment, so it can
     * update the latest message on its list
     */
    private void passIntentResult(int position, String newMessage, long time) {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("newMessage", newMessage);
        intent.putExtra("time", time);
        setResult(2, intent);
    }

    private void notifyOtherUser(String message) {
        // Notify the other user the message has been sent to them
        Call<JsonObject> retrofit = RetrofitUserChatSingleton.getRetrofitUserChat()
                .sendNotification()
                .sendNotification(Integer.parseInt(otherUserID),
                         userLocalStore.getLoggedInUser().getUsername() +
                                ": " + message, userLocalStore.getLoggedInUser().getUserID());
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Notified the other user
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    private class UserChat {
        private int userOne;
        private int userTwo;

        int getUserOne() {
            return userOne;
        }

        void setUserOne(int userOne) {
            this.userOne = userOne;
        }

        int getUserTwo() {
            return userTwo;
        }

        void setUserTwo(int userTwo) {
            this.userTwo = userTwo;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
