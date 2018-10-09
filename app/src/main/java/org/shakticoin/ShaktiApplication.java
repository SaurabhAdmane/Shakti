package org.shakticoin;

import android.app.Application;

import org.shakticoin.room.AppDatabase;


public class ShaktiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

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
    }
}
