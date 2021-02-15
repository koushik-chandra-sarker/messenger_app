package bd.edu.seu.messengerapp.presenterImpls;

import android.content.Context;
import android.widget.Toast;

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
import bd.edu.seu.messengerapp.Firebase.Entity.User;
import bd.edu.seu.messengerapp.Notifications.Client;
import bd.edu.seu.messengerapp.Notifications.Data;
import bd.edu.seu.messengerapp.Notifications.FCMApiService;
import bd.edu.seu.messengerapp.Notifications.Notification;
import bd.edu.seu.messengerapp.Notifications.Response;
import bd.edu.seu.messengerapp.Notifications.Sender;
import bd.edu.seu.messengerapp.presenters.BasePresenter;
import bd.edu.seu.messengerapp.presenters.MessagePresenter;
import retrofit2.Call;
import retrofit2.Callback;

public class MessagePresenterImpl implements MessagePresenter.Model {

    BasePresenter.CommonView commonView;
    FirebaseAuth auth;
    FirebaseDatabase database;

    Context context;

    public MessagePresenterImpl(BasePresenter.CommonView commonView, Context context) {
        this.commonView = commonView;
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


                                commonView.onSuccess();
                            });
                });

    }


    @Override
    public void sendNotification(User currentUser, String messageFromUi, String receiverToken) {

        Data data = Data.getInstance();
        Notification notification = Notification.getInstance();
        Sender sender = Sender.getInstance();

        data.setUserId(currentUser.getUserId());
        data.setUsername(currentUser.getUserName());
        data.setUserProfilePic(currentUser.getProfilePic());

        notification.setTitle(currentUser.getUserName());
        notification.setBody(messageFromUi);
        notification.setImage(currentUser.getProfilePic());

        sender.setTo(receiverToken);
        sender.setData(data);
        sender.setNotification(notification);
        FCMApiService fcmApiService = Client.getClient("https://fcm.googleapis.com/").create(FCMApiService.class);
        fcmApiService.sendNotification(sender).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response){
                if (response.code() == 200){
                    if (response.body().success != 1){
                        Toast.makeText(context,"Notification sent Faield", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
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
                        commonView.onError(error.getMessage());
                    }
                });
    }


}
