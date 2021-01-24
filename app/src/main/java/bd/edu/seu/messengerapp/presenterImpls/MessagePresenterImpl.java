package bd.edu.seu.messengerapp.presenterImpls;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bd.edu.seu.messengerapp.Adapter.MessageAdapter;
import bd.edu.seu.messengerapp.Firebase.Entity.Message;
import bd.edu.seu.messengerapp.presenters.BasePresenter;
import bd.edu.seu.messengerapp.presenters.MessagePresenter;

public class MessagePresenterImpl implements MessagePresenter.Model {

    BasePresenter.MessageInterface messageInterface;
    FirebaseAuth auth;
    FirebaseDatabase database;

    Context context;

    public MessagePresenterImpl(BasePresenter.MessageInterface messageInterface, Context context) {
        this.messageInterface = messageInterface;
        this.context = context;

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void sendMessage(Message message,String senderChatId, String receiverChatId) {

        //sender message save on firebase database with senderChatId for sender
        database.getReference().child("messenger_app").child("Chats").child(senderChatId).push().setValue(message)
                .addOnSuccessListener(aVoid -> {
                    //sender message save on firebase database with receiverChatId for receiver
                    database.getReference().child("messenger_app").child("Chats").child(receiverChatId).push().setValue(message)
                            .addOnSuccessListener(aVoid1 -> {
                                messageInterface.onSuccess();
                            });
                });

    }

    @Override
    public void setChatList(RecyclerView recyclerView, String senderChatId, String receiverId) {

        List<Message> messages = new ArrayList<>();
        MessageAdapter messageAdapter = new MessageAdapter(messages, context,receiverId);

        recyclerView.setAdapter(messageAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        database.getReference().child("messenger_app").child("Chats").child(senderChatId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Message message1 = dataSnapshot.getValue(Message.class);
                            message1.setMessageId(dataSnapshot.getKey());
                            messages.add(message1);
                        }
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        messageInterface.onError(error.getMessage());
                    }
                });
    }
}
