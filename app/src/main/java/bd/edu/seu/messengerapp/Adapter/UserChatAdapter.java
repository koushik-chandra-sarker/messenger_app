package bd.edu.seu.messengerapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import bd.edu.seu.messengerapp.Activity.MessageActivity;
import bd.edu.seu.messengerapp.Firebase.Entity.User;
import bd.edu.seu.messengerapp.R;
import bd.edu.seu.messengerapp.util.TimeStampConverter;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.ViewHolder> {

    List<User> users;
    Context context;

    public UserChatAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_chat_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.user_icon).into(holder.profilePic);
        holder.username.setText(user.getUserName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("userId", user.getUserId());
            intent.putExtra("userProfilePic", user.getProfilePic());
            intent.putExtra("username", user.getUserName());
            context.startActivity(intent);
        });
        FirebaseDatabase.getInstance().getReference("messenger_app").child("Chats")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid() + user.getUserId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                holder.lastMessage.setText(dataSnapshot.child("message").getValue(String.class));
                                holder.lastMessageTime.setText(TimeStampConverter.TimeStampToDateOrTime((Long) dataSnapshot.child("timestamp").getValue()));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView username, lastMessage, lastMessageTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_image_chat);
            username = itemView.findViewById(R.id.tv_username_chat);
            lastMessage = itemView.findViewById(R.id.tv_last_message);
            lastMessageTime = itemView.findViewById(R.id.tv_last_message_time);
        }
    }
}
