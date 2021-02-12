package bd.edu.seu.messengerapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bd.edu.seu.messengerapp.Adapter.UserChatAdapter;
import bd.edu.seu.messengerapp.Firebase.Entity.User;
import bd.edu.seu.messengerapp.R;
import bd.edu.seu.messengerapp.databinding.FragmentChatBinding;


public class ChatFragment extends Fragment {

    FragmentChatBinding binding;
    FirebaseDatabase database;
    List<User> users = new ArrayList<>();
    public ChatFragment() {
        database = FirebaseDatabase.getInstance();

        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);

        UserChatAdapter adapter = new UserChatAdapter(users, getContext());
        binding.userChatRecycleView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.userChatRecycleView.setLayoutManager(layoutManager);

        //get all users from database and set in chatFragment
        database.getReference().child("messenger_app").child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    user.setUserId(dataSnapshot.getKey());

                    //add all users except logged in user
                    if (!user.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                        users.add(user);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return binding.getRoot();
    }
}