package bd.edu.seu.messengerapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import bd.edu.seu.messengerapp.R;
import bd.edu.seu.messengerapp.databinding.ActivitySingInBinding;
import bd.edu.seu.messengerapp.presenterImpls.SignInPresenterImpl;
import bd.edu.seu.messengerapp.presenters.BasePresenter;
import bd.edu.seu.messengerapp.presenters.SignInPresenter;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, BasePresenter.View {

    ActivitySingInBinding binding;

    ProgressDialog progressDialog;
    SignInPresenter.Modal signInPresenterModal;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingInBinding.inflate(getLayoutInflater());
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide the app title
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Configure Google Sign In
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this,gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        signInPresenterModal = new SignInPresenterImpl(this,this);
        binding.signUpCard.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        binding.signInBtn.setOnClickListener(this);
        binding.btnSignInWithGoogle.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInBtn:
                signInPresenterModal.singInWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
                break;
            case R.id.btn_sign_in_with_google:
                GoogleSignIn();
                break;
            default:
                break;

        }
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onError(String... message) {
        Toast.makeText(SignInActivity.this, message[0], Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }



    int RC_SIGN_IN = 99;
    public void GoogleSignIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this,gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); // see onActivityResult-> singInWithEmailGoogle
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        signInPresenterModal.singInWithEmailGoogle(RC_SIGN_IN,requestCode, resultCode, data);

    }

}