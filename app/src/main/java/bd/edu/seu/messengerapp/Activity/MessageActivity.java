package bd.edu.seu.messengerapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bd.edu.seu.messengerapp.Adapter.MessageAdapter;
import bd.edu.seu.messengerapp.Firebase.Entity.Message;
import bd.edu.seu.messengerapp.R;
import bd.edu.seu.messengerapp.databinding.ActivityMessageBinding;
import bd.edu.seu.messengerapp.presenterImpls.MessagePresenterImpl;
import bd.edu.seu.messengerapp.presenters.BasePresenter;
import bd.edu.seu.messengerapp.presenters.MessagePresenter;

public class MessageActivity extends AppCompatActivity implements BasePresenter.MessageInterface {


    ActivityMessageBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    MessagePresenter.Model presenterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //1 Firebase auth and database instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        presenterModel  = new MessagePresenterImpl(this,this);

        final String senderId = auth.getUid();
        final String receiverId = getIntent().getStringExtra("userId");
        final String username = getIntent().getStringExtra("username");
        String profilePic = getIntent().getStringExtra("userProfilePic");
        // declare ChatId
        final String senderChatId = senderId + receiverId;
        final String receiverChatId = receiverId + senderId;

        //  set receiver username in chatDetailActivity
        binding.tvUsernameChatDetail.setText(username);
        // set receiver user profile pic in chatDetailActivity
        Picasso.get().load(profilePic).placeholder(R.drawable.user_icon).into(binding.profileImageChatDetail);


        //backArrow Button Action
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });



        RecyclerView recyclerView = binding.messageRecycleView;

        // get all message and add on messageAdapter through List of message
        presenterModel.setChatList(recyclerView,senderChatId,receiverId);


        // send message
        binding.btnSend.setOnClickListener(v -> {
            String messageFromUi = binding.etMessage.getText().toString();
            final Message message = new Message(senderId, messageFromUi, new Date().getTime());
            binding.etMessage.setText("");
            presenterModel.sendMessage(message,senderChatId,receiverChatId);
        });

    }

    @Override
    public void onSuccess() {
      /*  TODO: Message delivered-> do something... */
    }

    @Override
    public void onError(String... message) {
        Toast.makeText(this,message[0],Toast.LENGTH_LONG).show();
    }
}