package bd.edu.seu.messengerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import bd.edu.seu.messengerapp.databinding.ActivityChatRoomBinding;

public class ChatRoomActivity extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}