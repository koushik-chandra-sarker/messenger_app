package bd.edu.seu.messengerapp.presenters;

import androidx.recyclerview.widget.RecyclerView;

import bd.edu.seu.messengerapp.Firebase.Entity.Message;
import bd.edu.seu.messengerapp.Firebase.Entity.User;

public interface MessagePresenter extends BasePresenter.CommonView {
    interface Model{
        void sendMessage(Message message,String senderChatId, String receiverChatId);
        void setChatList(RecyclerView recyclerView,String senderChatId, String receiverId);

        void sendNotification(User currentUser, String messageFromUi, String receiverToken);
    }
}
