package bd.edu.seu.messengerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import bd.edu.seu.messengerapp.Firebase.Entity.User;
import bd.edu.seu.messengerapp.databinding.ActivitySignUpBinding;
import bd.edu.seu.messengerapp.presenterImpls.SignUpPresenterImpl;
import bd.edu.seu.messengerapp.presenters.BasePresenter;
import bd.edu.seu.messengerapp.presenters.SignUpPresenter;

public class SignUpActivity extends AppCompatActivity implements BasePresenter.View {

    //Declare the instance of FirebaseAuth
    private FirebaseAuth auth;

    //Declare the instance of FirebaseDatabase
    FirebaseDatabase database;
    SignUpPresenter.Modal signUpPresenterModel;

    ActivitySignUpBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize the FirebaseAuth instance.
        auth = FirebaseAuth.getInstance();
        // initialize the FirebaseDatabase instance.
        database = FirebaseDatabase.getInstance();

        //create object of SignUpPresenterImpl
        signUpPresenterModel = new SignUpPresenterImpl(this);

        binding.btnSignUp.setOnClickListener(v -> {
            //Show progressDialog when Button Clicked
            signUpPresenterModel.signUpWithEmail(
                    binding.etEmailPhone.getText().toString(),
                    binding.etPassword.getText().toString(),
                    binding.etConfirmPassword.getText().toString()
            );
        });


    }

    @Override
    public void onSuccess() {
        Toast.makeText(this,"SignUp Successful.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(String... message) {
        Toast.makeText(this,message[0], Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar(String title) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(title);
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }
}