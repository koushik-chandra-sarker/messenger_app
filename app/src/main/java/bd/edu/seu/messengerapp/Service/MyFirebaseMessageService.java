package bd.edu.seu.messengerapp.Service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import bd.edu.seu.messengerapp.util.NotificationHelper;

public class MyFirebaseMessageService extends FirebaseMessagingService {

    private static final String TAG = "MyTag";
    FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification()!=null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.i("MyTag", "onMessageReceived: "+title+" "+ body);
            NotificationHelper.showNotification(this,title,body);

        }
        if (remoteMessage.getData() != null){
            Log.i("MyTag", "onMessageReceived: "+remoteMessage.getData().toString());
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        super.onNewToken(s);
        //upload this token on the app server
        if (auth.getCurrentUser()!=null){
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    String token = task.getResult();
                    Log.e(TAG, "onNewToken: "+token );
                     database.getReference().child("messenger_app").child("Users").child(auth.getCurrentUser().getUid())
                            .child("token").setValue(token)
                            .addOnSuccessListener(aVoid -> {

                            });
                }else {
                    Log.e(TAG, "onCreate: "+task.getException().getMessage() );
                }
            });
        }

    }
}
