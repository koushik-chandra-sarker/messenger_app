package bd.edu.seu.messengerapp.presenters;

import android.content.Intent;

public interface SignInPresenter extends BasePresenter {

    interface Modal{
        void singInWithEmailAndPassword(String username, String password);
        void singInWithEmailGoogle(int RC_SIGN_IN, int requestCode, int resultCode, Intent data);
        void singInWithEmailFacebook();
    }
}
