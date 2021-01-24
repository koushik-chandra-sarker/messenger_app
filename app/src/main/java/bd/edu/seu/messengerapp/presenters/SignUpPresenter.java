package bd.edu.seu.messengerapp.presenters;

import android.content.Context;

public interface SignUpPresenter extends BasePresenter {
    interface Modal{
        void signUpWithEmail(String email, String password,String confirmPassword);
        void signUpWithGoogle(Context context);
        void signUpWithFacebook();
    }
}
