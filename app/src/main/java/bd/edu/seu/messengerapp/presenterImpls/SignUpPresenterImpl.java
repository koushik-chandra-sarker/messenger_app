package bd.edu.seu.messengerapp.presenterImpls;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import bd.edu.seu.messengerapp.Firebase.Entity.User;
import bd.edu.seu.messengerapp.R;
import bd.edu.seu.messengerapp.presenters.BasePresenter;
import bd.edu.seu.messengerapp.presenters.SignUpPresenter;

public class SignUpPresenterImpl implements SignUpPresenter.Modal {
    BasePresenter.View view;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    GoogleSignInClient googleSignInClient;

    public SignUpPresenterImpl(BasePresenter.View view) {
        this.view = view;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void signUpWithEmail(String email, String password, String confirmPassword) {
        view.showProgressBar("We're creating your account...");
        //Checking password and confirm password equal or not
        if (password.equals(confirmPassword)) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        //Stop progressDialog when Authentication done.
                        view.hideProgressBar();

                        // if authentication is successful "userInfo" will save on the database
                        if (task.isSuccessful()) {

                            User user = new User(email, email, password);
                            // get user id that created by FirebaseAuth (above^)
                            String id = task.getResult().getUser().getUid();
                            // Set user in firebase database
                            database.getReference().child("messenger_app").child("Users").child(id).setValue(user);

                            view.onSuccess();

                        } else {
                            view.onError(task.getException().getMessage());

                        }
                    });
        } else {
            view.onError("Confirm password is not matching.");
        }
    }

    @Override
    public void signUpWithGoogle(Context context) {
        // Configure Google Sign In
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Resources.getSystem().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(context,gso);



    }

    @Override
    public void signUpWithFacebook() {

    }



    /*private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }*/

}
