package ravtrix.backpackerbuddy.activities.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import ravtrix.backpackerbuddy.activities.login.LogInActivity;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.fcm.model.Message;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ravtrix.backpackerbuddy.helpers.RetrofitUserChatSingleton.getRetrofitUserChat;

public class ConversationActivity extends AppCompatActivity implements IConversationView {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.chat_recyclerView) protected RecyclerView mMessageRecyclerView;
    @BindView(R.id.sendMessageButton) protected Button mSendButton;
    @BindView(R.id.textMessage) protected EditText textMessage;
    @BindView(R.id.activity_conversation_spinner) protected ProgressBar progressBar;
    @BindView(R.id.activity_conversation_loading_spinner) protected ProgressBar progressBarSendLoad;

    private LinearLayoutManager mLinearLayoutManager;
    private String chatRoomName, chatRoomName2, chatRoomNameNoPlus, chatRoomName2NoPlus, realChatName = "";
    private int chatPosition;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private DatabaseReference mFirebaseDatabaseReference;
    private UserLocalStore userLocalStore;
    private final UserChat userChat = new UserChat();
    private  String otherUserID, otherUserImage;
    private ConversationPresentor conversationPresentor;
    private int backPressExit = 1;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, textMessage);
        setTitle("Conversation");

        toolbar.setTitleTextColor(Color.WHITE);

        userLocalStore = new UserLocalStore(this);

        if (userLocalStore.getLoggedInUser().getUserID() != 0) {

            progressBar.setVisibility(View.VISIBLE);
            conversationPresentor = new ConversationPresentor(this);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mLinearLayoutManager.setStackFromEnd(true);

            getChatRoomInformation();
            setChatRoom();
        } else {
            Helpers.displayToast(this, "Please log in to chat");
            startActivity(new Intent(this, LogInActivity.class));
        }
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView1, messageTextView2;
        TextView timeTextView;
        CircleImageView messengerImageView1, messengerImageView2;
        RelativeLayout layoutMessage1, layoutMessage2;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView1 = (TextView) itemView.findViewById(R.id.item_message_message);
            timeTextView = (TextView) itemView.findViewById(R.id.item_time);
            messengerImageView1 = (CircleImageView) itemView.findViewById(R.id.item_countryFeed_profileImage);
            layoutMessage1 = (RelativeLayout) itemView.findViewById(R.id.layout_message);

            messageTextView2 = (TextView) itemView.findViewById(R.id.item_message_message2);
            messengerImageView2 = (CircleImageView) itemView.findViewById(R.id.item_inboxFeed_profileImage2);
            layoutMessage2 = (RelativeLayout) itemView.findViewById(R.id.layout_message2);
        }
    }

    /**
     * Set recycler view to show appropriate data at the right position
     * The current chatter should have their messages on the right side
     * The receiver should have their messages on the left side
     * @param roomName          the room number to access in FCM
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
                    /*
                    messageTextView2.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            AlertDialog.Builder builder = Helpers.showAlertDialogWithTwoOptions(ConversationActivity.this,
                                    "Delete Message?", getResources().getString(R.string.deleteMessage), "Cancel");
                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //delete
                                }
                            });
                            builder.show();
                            return true;
                        }
                    });*/
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
                                .load(userLocalStore.getLoggedInUser().getUserImageURL())
                                .fit()
                                .centerCrop()
                                .into(viewHolder.messengerImageView2);
                    }

                } else {
                    viewHolder.layoutMessage1.setVisibility(View.VISIBLE);
                    viewHolder.layoutMessage2.setVisibility(View.GONE);
                    TextView messageTextView1 = viewHolder.messageTextView1;
                    /*
                    messageTextView1.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            AlertDialog.Builder builder = showAlertDialogWithTwoOptions(ConversationActivity.this,
                                    "Delete Message?", getResources().getString(R.string.deleteMessage), "Cancel");
                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //delete
                                }
                            });
                            builder.show();
                            return true;
                        }
                    });*/
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
                        if (!otherUserImage.isEmpty()) {
                            Picasso.with(ConversationActivity.this)
                                    .load(otherUserImage)
                                    .fit()
                                    .centerCrop()
                                    .into(viewHolder.messengerImageView1);
                        }
                    }
                }
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


    /**
     * We know the room exists because user clicks through their inbox fragment
     */
    private void sendMessageThoughInbox() {

        String userMessage =  textMessage.getText().toString().trim();
        Long time = System.currentTimeMillis();
        sendMessageFirebase(realChatName, userMessage, time);

        // Notify the other user the message has been sent to them
        conversationPresentor.notifyOtherUser(userMessage, otherUserID, userLocalStore.getLoggedInUser().getUsername(),
                userLocalStore.getLoggedInUser().getUserID());
    }

    /**
     * This is the case where the user is trying to chat through another user's profile page
     * Send/Save new message to Firebase cloud. Save in the chat room name if room exists.
     * Otherwise, if room doesn't exist between the two users yet, make a new room
     */
    private void sendMessage() {
        //mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userMessage =  textMessage.getText().toString().trim();
                Long time = System.currentTimeMillis();

                if (dataSnapshot.child(chatRoomName).exists()) {
                    sendMessageFirebase(chatRoomName, userMessage, time);
                } else if (dataSnapshot.child(chatRoomName2).exists()) {
                    sendMessageFirebase(chatRoomName2, userMessage, time);
                } else if (dataSnapshot.child(chatRoomNameNoPlus).exists()) {
                    sendMessageFirebase(chatRoomNameNoPlus, userMessage, time);
                } else if (dataSnapshot.child(chatRoomName2NoPlus).exists()) {
                    sendMessageFirebase(chatRoomName2NoPlus, userMessage, time);
                } else {

                    // New chat, so create new chat in FCM and also database
                    Call<JsonObject> jsonObjectCall = getRetrofitUserChat()
                            .insertNewChat()
                            .insertNewChat(userChat.getUserOne(), userChat.getUserTwo());

                    jsonObjectCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressBarSendLoad.setVisibility(View.GONE);
                        }
                    });

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("photoUrl",
                            userLocalStore.getLoggedInUser().getUserImageURL());
                    userData.put("text", userMessage);
                    userData.put("time", time);
                    userData.put("userID", userLocalStore.getLoggedInUser().getUserID());
                    userData.put("isOtherUserClicked", 0); // If the other user viewed your message // 0 = false

                    // store message in the fire-base database
                    mFirebaseDatabaseReference.child(chatRoomName).push().setValue(userData);
                    textMessage.setText("");
                    setRecyclerView(chatRoomName); // since this is first message, recycler view hasn't been set before

                    passIntentResult(chatPosition, userMessage, time);
                }
                progressBarSendLoad.setVisibility(View.GONE);

                // Notify the other user the message has been sent to them
                conversationPresentor.notifyOtherUser(userMessage, otherUserID, userLocalStore.getLoggedInUser().getUsername(),
                        userLocalStore.getLoggedInUser().getUserID());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void sendMessageFirebase(String chatName, String userMessage, Long time) {
        Message message = new
                Message(userLocalStore.getLoggedInUser().getUserID(), userMessage,
                userLocalStore.getLoggedInUser().getUserImageURL(),
                time, 0);
        mFirebaseDatabaseReference.child(chatName).push().setValue(message);
        progressBarSendLoad.setVisibility(View.GONE);

        textMessage.setText("");
        passIntentResult(chatPosition, userMessage, time);
    }

    /**
     * On back press, pass the new sent message to the MessagesFragment, so it can
     * update the latest message on its list
     * @param position          position of message
     * @param newMessage        the new message
     * @param time              the new time stamp
     */
    private void passIntentResult(int position, String newMessage, long time) {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("newMessage", newMessage);
        intent.putExtra("time", time);
        setResult(2, intent);
    }

    private void getChatRoomInformation() {
        Bundle bundle = getIntent().getExtras();

        otherUserID = "0";
        String myUserID;
        if (bundle != null) {
            otherUserID = bundle.getString("otherUserID"); // ID of the other user in chat

            if (bundle.containsKey("otherUserImage")) {
                otherUserImage = bundle.getString("otherUserImage");
            }
            if (bundle.containsKey("position")) {
                chatPosition = bundle.getInt("position");
            }
            if (bundle.containsKey("backpressExit")) {
                backPressExit = bundle.getInt("backpressExit");
            }
            if (bundle.containsKey("chatRoom")) {
                realChatName = bundle.getString("chatRoom");
            }
        }
        myUserID = Integer.toString(userLocalStore.getLoggedInUser().getUserID());
        // Create name combo. Only one of these two names exist for the convo between the two users.
        chatRoomName = myUserID + "+" + otherUserID; chatRoomNameNoPlus = myUserID + otherUserID;
        chatRoomName2 = otherUserID + "+" + myUserID; chatRoomName2NoPlus = otherUserID + myUserID;

        // Create userChat object
        userChat.setUserOne(Integer.parseInt(myUserID));
        userChat.setUserTwo(Integer.parseInt(otherUserID));
    }

    /**
     * Chat rooms information passed as bundle by item inbox
     */
    private void setChatRoom() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Find which room exists
                if (dataSnapshot.child(chatRoomName).exists()) {
                    setRecyclerView(chatRoomName);
                    realChatName = chatRoomName;

                } else if (dataSnapshot.child(chatRoomName2).exists()) {
                    setRecyclerView(chatRoomName2);
                    realChatName = chatRoomName2;

                } else if (dataSnapshot.child(chatRoomNameNoPlus).exists()) {
                    setRecyclerView(chatRoomNameNoPlus);
                    realChatName = chatRoomNameNoPlus;

                } else if (dataSnapshot.child(chatRoomName2NoPlus).exists()) {
                    setRecyclerView(chatRoomName2NoPlus);
                    realChatName = chatRoomName2NoPlus;

                } else {
                    realChatName = "";
                    progressBar.setVisibility(View.INVISIBLE);
                }
                setListeners();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                realChatName = "";
            }
        });
    }

    private void setListeners() {
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Prevents double clicking
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (textMessage.getText().toString().trim().equals("")) {
                    Helpers.displayToast(ConversationActivity.this, "Empty message");
                } else {

                    progressBarSendLoad.setVisibility(View.VISIBLE);
                    if (!realChatName.isEmpty()) {
                        sendMessageThoughInbox(); // quick messaging
                    } else {
                        sendMessage(); // accessed through visiting profile. need to do more checking to see if room exists or
                    }
                }
            }
        });
    }

    /**
     * UserChat class holding the users in the conversation
     */
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
        // backPressExit 1 if user navigate naturally
        // backPressExit 0 if user opens app through push notification
        if (backPressExit == 0) {
            Intent intent = new Intent(this, UserMainPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();

        }
    }
}