package com.nebeek.newsstand.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.nebeek.newsstand.data.models.TelegramMessage;

@Database(entities = {TelegramMessage.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract MessageDao messageModel();

    public static AppDatabase getInMemoryDatabase(Context context) {
        if (instance == null) {
            instance = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                    // todo remove this
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }


}
