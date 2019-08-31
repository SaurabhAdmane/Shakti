package com.shakticoin.app.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.shakticoin.app.room.entity.Geo;
import com.shakticoin.app.room.entity.PhoneCountryCode;

import java.util.concurrent.Executors;


@Database(version = 2, entities = {Geo.class, PhoneCountryCode.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database;

    abstract public PhoneCountryCodeDao phoneCountryCodeDao();
    abstract public GeoDao geoDao();

    public synchronized  static AppDatabase getDatabase(Context context) {
        if (database == null) {
            synchronized (AppDatabase.class) {
                if (database == null) {
                    database = buildDatabase(context);
                }
            }
        }
        return database;
    }

    /**
     * One new table PhoneCountryCode and we need to populate it with seed data.
     */
    static final Migration migration_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE PhoneCountryCode ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "geo_code TEXT,"
                    + "country_code TEXT);");
            PhoneCountryCode[] countryCodes = PhoneCountryCode.populateSeedData();
            for (PhoneCountryCode item : countryCodes) {
                database.execSQL(String.format("INSERT INTO PhoneCountryCode (geo_code, country_code) VALUES ('%1$s', '%2$s');",
                        item.getGeoCode(), item.getCountryCode()));
            }
        }
    };

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "shakticoin")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase db = getDatabase(context);
                                db.geoDao().insertAll(Geo.populateSeedData(context));
                                db.phoneCountryCodeDao().insertAll(PhoneCountryCode.populateSeedData());
                            }
                        });
                    }
                })
                .addMigrations(migration_1_2)
                .build();
    }
}
