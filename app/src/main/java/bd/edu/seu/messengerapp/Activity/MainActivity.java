package bd.edu.seu.messengerapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import bd.edu.seu.messengerapp.Adapter.FragmentAdapter;
import bd.edu.seu.messengerapp.Firebase.Entity.User;
import bd.edu.seu.messengerapp.R;
import bd.edu.seu.messengerapp.Service.ConnectivityService;
import bd.edu.seu.messengerapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    public static User currentUser;
    ActivityMainBinding binding;
    FirebaseAuth auth;
    private FirebaseDatabase database;
    private boolean isConnected;

    private IntentFilter intentFilter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        intentFilter = new IntentFilter("Network-stage-check");
        //Start Connectivity Service
        Intent connectivityServiceIntent = new Intent(this, ConnectivityService.class);
        startService(connectivityServiceIntent);


        //Connectivity warning text invisible
        binding.tvConnectivityWarning.setVisibility(View.GONE);
        /*
        * Get Current Logged In User Info
        * */
        database.getReference().child("messenger_app").child("Users").child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        currentUser = snapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //get firebase cloud message Token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String token = task.getResult();
                Log.e(TAG, "onCreate: "+token );
                database.getReference().child("messenger_app").child("Users").child(auth.getCurrentUser().getUid())
                        .child("token").setValue(token)
                        .addOnSuccessListener(aVoid -> {
                           
                        });
            }else {
                Log.e(TAG, "onCreate: "+task.getException().getMessage() );
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,intentFilter);
//        if (!isConnected){
//            binding.tvConnectivityWarning.setVisibility(View.VISIBLE);
//        }else  binding.tvConnectivityWarning.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(receiver,intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_manu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                    auth.signOut();
                Intent intent1 = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.chat_room:
                Intent intent2 = new Intent(MainActivity.this, ChatRoomActivity.class);
                startActivity(intent2);
                break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isConnected = intent.getBooleanExtra("connected",false);
            if (intent.getAction().equals("Network-stage-check")){
                if (!isConnected){
                    binding.tvConnectivityWarning.setVisibility(View.VISIBLE);
                }else  binding.tvConnectivityWarning.setVisibility(View.GONE);
            }

        }
    };
}