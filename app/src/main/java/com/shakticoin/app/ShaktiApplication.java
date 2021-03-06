package com.shakticoin.app;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.shakticoin.app.api.Session;
import com.shakticoin.app.room.AppDatabase;
import com.shakticoin.app.util.NetworkStateReceiver;
import com.stripe.android.PaymentConfiguration;


public class ShaktiApplication extends MultiDexApplication {
    private static Context context;

    // The BroadcastReceiver that tracks network connectivity changes.
    private NetworkStateReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        /*
         * Check the current network connectivity.
         */
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            Session.setNetworkConnected(networkInfo != null && networkInfo.isConnected());
            if (!Session.isNetworkConnected()) {
                Toast.makeText(this, R.string.err_no_internet, Toast.LENGTH_LONG).show();
            }

            // registers BroadcastReceiver to track network connection changes
            receiver = new NetworkStateReceiver();
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            receiver = new NetworkStateReceiver();
            registerReceiver(receiver, filter);
        }

        /*
         * Create a new database if necessary.
         * It seems Room.databaseBuilder(...).build() does not create the database immediately.
         * At least, callback onCreate (where we load initial data) is called when we request database
         * for the first time.
        * */
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getDatabase(getApplicationContext()).geoDao().getEmpty();
            }
        }).start();

        /**
         * Initialize Stripe
         */
        PaymentConfiguration.init(getApplicationContext(), getString(R.string.stripe_pub_key));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // unregisters BroadcastReceiver when app is destroyed.
        if (receiver != null) unregisterReceiver(receiver);
    }

    public static Context getContext() {
        return context;
    }
}
