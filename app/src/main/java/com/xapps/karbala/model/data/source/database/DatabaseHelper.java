package com.xapps.karbala.model.data.source.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {TemplateDBDTO.class}, version = 1, exportSchema = false)
public abstract class DatabaseHelper extends RoomDatabase {

    public abstract TemplateDao metaDataDao();

    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getDatabaseHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = Room.databaseBuilder(context.getApplicationContext(),
                    DatabaseHelper.class, "Template-Database")
                    .allowMainThreadQueries()
                    .build();
        }

        return databaseHelper;
    }

}
