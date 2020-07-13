package com.shakticoin.app.room;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.shakticoin.app.room.entity.Enumeration;
import com.shakticoin.app.room.entity.EnumerationType;
import com.shakticoin.app.room.entity.Geo;
import com.shakticoin.app.room.entity.LockStatus;
import com.shakticoin.app.room.entity.PhoneCountryCode;

import java.util.concurrent.Executors;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;


@Database(version = 3, entities = {Geo.class, PhoneCountryCode.class, EnumerationType.class,
        Enumeration.class, LockStatus.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database;

    abstract public PhoneCountryCodeDao phoneCountryCodeDao();
    abstract public GeoDao geoDao();
    abstract public EnumerationTypeDao enumerationTypeGeo();
    abstract public EnumerationDao enumerationDao();
    abstract public LockStatusDao lockStatusDao();

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

    /**
     * Adds new entities for lists of common values: EnumerationType and Enumeration.
     * Adds new entity for lock statues and populates corresponding seed data.
     */
    static final Migration migration_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE EnumerationType ("
                    + "enum_type_id INTEGER PRIMARY KEY NOT NULL,"
                    + "description TEXT);");
            database.execSQL("CREATE TABLE Enumeration ("
                    + "enum_id INTEGER PRIMARY KEY NOT NULL,"
                    + "enum_type_id INTEGER NOT NULL,"
                    + "description TEXT,"
                    + "FOREIGN KEY (enum_type_id) REFERENCES EnumerationType (enum_type_id) ON DELETE SET NULL);");
            database.execSQL("CREATE TABLE LockStatus ("
                    + "type_enum_id INTEGER NOT NULL,"
                    + "category_enum_id INTEGER NOT NULL,"
                    + "status TEXT,"
                    + "PRIMARY KEY (type_enum_id, category_enum_id),"
                    + "FOREIGN KEY (type_enum_id) REFERENCES Enumeration (enum_id),"
                    + "FOREIGN KEY (category_enum_id) REFERENCES Enumeration (enum_id));");
            EnumerationType[] enumerationTypes = EnumerationType.populateSeedData();
            for (EnumerationType enumerationType : enumerationTypes) {
                ContentValues values = new ContentValues();
                values.put("enum_type_id", enumerationType.getEnumTypeId());
                values.put("description", enumerationType.getDescription());
                database.insert("EnumerationType", CONFLICT_REPLACE, values);
            }
            Enumeration[] enumerations = Enumeration.populateSeedData();
            for (Enumeration enumeration : enumerations) {
                ContentValues values = new ContentValues();
                values.put("enum_id", enumeration.getEnumId());
                values.put("enum_type_id", enumeration.getEnumTypeId());
                values.put("description", enumeration.getDescription());
                database.insert("Enumeration", CONFLICT_REPLACE, values);
            }
            LockStatus[] lockStatuses = LockStatus.populateSeedData();
            for (LockStatus lockStatus : lockStatuses) {
                ContentValues values = new ContentValues();
                values.put("type_enum_id", lockStatus.getTypeEnumId());
                values.put("category_enum_id", lockStatus.getCategoryEnumId());
                values.put("status", lockStatus.getStatus());
                database.insert("LockStatus", CONFLICT_REPLACE, values);
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
                                db.enumerationTypeGeo().insertAll(EnumerationType.populateSeedData());
                                db.enumerationDao().insertAll(Enumeration.populateSeedData());
                                db.lockStatusDao().insertAll(LockStatus.populateSeedData());
                            }
                        });
                    }
                })
                .addMigrations(migration_1_2)
                .addMigrations(migration_2_3)
                .build();
    }
}
