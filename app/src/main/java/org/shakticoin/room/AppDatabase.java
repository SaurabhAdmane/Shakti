package org.shakticoin.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import org.shakticoin.room.entity.Geo;

import java.util.concurrent.Executors;


@Database(entities = {Geo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database;

    public abstract GeoDao geoDao();

    public synchronized  static AppDatabase getDatabase(Context context) {
        if (database == null) {
            database = buildDatabase(context);
        }
        return database;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "shakticoin")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getDatabase(context).geoDao().insertAll(Geo.populateSeedData(context));
                            }
                        });
                    }
                })
                .build();
    }
}
