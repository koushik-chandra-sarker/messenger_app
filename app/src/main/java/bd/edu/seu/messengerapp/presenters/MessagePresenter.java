package bd.edu.seu.messengerapp.presenters;

import androidx.recyclerview.widget.RecyclerView;

import bd.edu.seu.messengerapp.Firebase.Entity.Message;

public interface MessagePresenter extends BasePresenter.MessageInterface {
    interface Model{
        void sendMessage(Message message,String senderChatId, String receiverChatId);
        void setChatList(RecyclerView recyclerView,String senderChatId, String receiverId);
    }
}
