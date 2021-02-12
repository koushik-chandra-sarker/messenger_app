package bd.edu.seu.messengerapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import bd.edu.seu.messengerapp.Firebase.Entity.User;
import bd.edu.seu.messengerapp.R;
import bd.edu.seu.messengerapp.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    //firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//
//        //StatusBar color change
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg_setting_1, this.getTheme()));


        //firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        binding.btnBackArrow.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


        // get Current user info and set on this activity
        database.getReference().child("messenger_app").child("Users").child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.user_icon).into(binding.ivProPic);
                        binding.tvUsername.setText(user.getUserName());
                        if (!user.getAbout().isEmpty()) {
                            binding.tvAbout.setText(user.getAbout());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        // profile pic upload button action
        binding.btnAddProPic.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*"); // */* for get all type like video audio etc.
            startActivityForResult(intent, 50); // see onActivityResult method

        });


        EditText editText = new EditText(this);

        //Update username
        binding.btnEditUsername.setOnClickListener(v -> {
            editText.setText(binding.tvUsername.getText().toString());
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("Edit Username")
                    .setView(editText)
                    .setPositiveButton("Save", (dialog, which) -> {
                        database.getReference().child("messenger_app").child("Users").child(auth.getCurrentUser().getUid())
                                .child("userName").setValue(editText.getText().toString())
                                .addOnSuccessListener(aVoid -> {
                                    binding.tvUsername.setText(editText.getText().toString());
                                });


                        //---or--> multiple children update using hashMap
                        /*Map<String, Object> object = new HashMap<>();
                        object.put("userName",editText.getText().toString());
                        database.getReference().child("messenger_app").child("Users").child(auth.getCurrentUser().getUid())
                                .updateChildren(object)
                                .addOnSuccessListener(aVoid -> {
                                    binding.tvUsername.setText(editText.getText().toString());
                                });*/

                        //remove for using editText object in another place
                       ((ViewGroup)editText.getParent()).removeView(editText);

                        dialog.dismiss();
                    }).show();

        });

        //Update About
        binding.btnEditAbout.setOnClickListener(v -> {
            editText.setText(binding.tvAbout.getText().toString());
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("Edit About")
                    .setView(editText)
                    .setPositiveButton("Save", (dialog, which) -> {
                        database.getReference().child("messenger_app").child("Users").child(auth.getCurrentUser().getUid())
                                .child("about").setValue(editText.getText().toString())
                                .addOnSuccessListener(aVoid -> {
                                    binding.tvAbout.setText(editText.getText().toString());
                                });
                        //remove for using editText object in another place
                        ((ViewGroup)editText.getParent()).removeView(editText);
                        dialog.dismiss();
                    }).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //get image from gallery
        if (data.getData() != null && requestCode == 50) {
            //get image
            Uri uri = data.getData();
            binding.ivProPic.setImageURI(uri);

            //get firebase storage reference where image will save
            StorageReference storageReference = storage.getReference().child("messenger_app").child("Profile_Pictures")
                    .child(auth.getCurrentUser().getUid());
            //save image on firebase storage
            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                //download saved image's url and save is on firebase database
                storageReference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                    database.getReference().child("messenger_app").child("Users").child(auth.getCurrentUser().getUid()).child("profilePic")
                            .setValue(uri1.toString());
                });
            });

        }
    }
}