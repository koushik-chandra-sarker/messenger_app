package bd.edu.seu.messengerapp.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class ConnectivityService extends Service {
    Handler networkHandler = new Handler(Looper.getMainLooper());

    public ConnectivityService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        networkHandler.post(runnable);

//        checkConnectivity();
        return START_STICKY;
    }

    private boolean isConnected(Context context) {
        boolean f = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isMetered = connectivityManager.isActiveNetworkMetered();
        Log.i("myTag", "isConnected:" + isMetered);

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            /*if (isMetered){
                f = true;
            }*/
            f = true;

        } else {
            f = false;
        }
                   
        return f;
    }

    private void sendMessage(Boolean data) {
        Intent intent1 = new Intent("Network-stage-check");
        intent1.putExtra("connected", isConnected(ConnectivityService.this));
        sendBroadcast(intent1);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            networkHandler.postDelayed(runnable, 2000);
            Intent intent1 = new Intent("Network-stage-check");
            intent1.putExtra("connected", isConnected(ConnectivityService.this));
            sendBroadcast(intent1);
        }
    };


}