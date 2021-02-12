package bd.edu.seu.messengerapp.presenterImpls;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import bd.edu.seu.messengerapp.Firebase.Entity.User;
import bd.edu.seu.messengerapp.presenters.BasePresenter;
import bd.edu.seu.messengerapp.presenters.SignInPresenter;

public class SignInPresenterImpl implements SignInPresenter.Modal {

    BasePresenter.View view;
    //Declare the instance of FirebaseAuth
    FirebaseAuth auth;
    FirebaseDatabase database;

    GoogleSignInClient googleSignInClient;
    Activity activity;


    public SignInPresenterImpl(BasePresenter.View view,Activity activity) {
        this.view = view;
        this.activity = activity;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //Check user already logged in or not
        if (auth.getCurrentUser() !=null){
            view.onSuccess();
        }
    }

    @Override
    public void singInWithEmailAndPassword(String username, String password) {
        if (TextUtils.isEmpty(username)|| TextUtils.isEmpty(password)){
            view.onError("Username or password can't be Empty.");
        }else {
            view.showProgressBar("Please Wait...");
            auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                view.hideProgressBar();
                if (task.isSuccessful()) {
                    view.onSuccess();
                } else {
                    view.onError(task.getException().getMessage());
                }
            });
        }

    }

    @Override
    public void singInWithEmailGoogle(int RC_SIGN_IN, int requestCode, int resultCode, Intent data) {


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("MyTag", "Google sign in failed", e);
                Toast.makeText(activity, "Google sign in failed",Toast.LENGTH_LONG).show();
                // ...
            }
        }

    }

    @Override
    public void singInWithEmailFacebook() {

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            User localUser = new User();
                            localUser.setUserId(user.getUid());
                            localUser.setUserName(user.getDisplayName());
                            localUser.setProfilePic(user.getPhotoUrl().toString());


                            // Set user in firebase database

                            database.getReference().child("messenger_app").child("Users").child(user.getUid()).setValue(localUser);


                            //After authentication successful open MainActivity
                            view.onSuccess();

                        } else {
                            // If sign in fails, display a message to the user.
//                            Toast.makeText(, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            view.onError(task.getException().getMessage());
                        }

                    }
                });
    }
}
