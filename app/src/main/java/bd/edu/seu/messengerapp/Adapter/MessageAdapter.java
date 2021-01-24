package bd.edu.seu.messengerapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

import bd.edu.seu.messengerapp.Firebase.Entity.Message;
import bd.edu.seu.messengerapp.R;
import bd.edu.seu.messengerapp.util.TimeStampConverter;

//1
public class MessageAdapter extends RecyclerView.Adapter{


    List<Message> messages;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;


    String receiverId; //for delete message, get from ChatDetailActivity through Constructor
    public MessageAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    public MessageAdapter(List<Message> messages, Context context, String receiverId) {
        this.messages = messages;
        this.context = context;
        this.receiverId = receiverId;
    }

    //4
    @Override
    public int getItemViewType(int position) {

        // set view type according to current user as sender and receiver
        if (messages.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else {
            return RECEIVER_VIEW_TYPE;
        }

    }


    //5
    //display view according to View type
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }else {
            return new SenderViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false));
        }
    }



    //6
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder) holder).tv_sender_message.setText(message.getMessage());
            ((SenderViewHolder) holder).tv_sender_time.setText(TimeStampConverter.TimeStampToDateOrTime(message.getTimestamp()));
        }
        else {
            ((ReceiverViewHolder) holder).tv_receiver_message.setText(message.getMessage());
            ((ReceiverViewHolder) holder).tv_receiver_time.setText(TimeStampConverter.TimeStampToDateOrTime(message.getTimestamp()));
        }


        //delete message
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete this message")
                    .setMessage("Are you sure?")
                    .setPositiveButton("yes", (dialog, which) -> {
                        // senderChatId = senderId+receiverId
                        String  senderChatId = FirebaseAuth.getInstance().getUid()+receiverId;
                        FirebaseDatabase.getInstance().getReference().child("messenger_app").child("Chats").child(senderChatId)
                                .child(message.getMessageId()).setValue(null);

                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

            return false;
        });

    }



    //7
    @Override
    public int getItemCount() {
        return messages.size();
    }



    //2
    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView tv_sender_message, tv_sender_time;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sender_message = itemView.findViewById(R.id.tv_receiver_message);
            tv_sender_time = itemView.findViewById(R.id.tv_receiver_time);
        }
    }

    //3
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView tv_receiver_message, tv_receiver_time;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_receiver_message = itemView.findViewById(R.id.tv_receiver_message);
            tv_receiver_time = itemView.findViewById(R.id.tv_receiver_time);
        }
    }
}
