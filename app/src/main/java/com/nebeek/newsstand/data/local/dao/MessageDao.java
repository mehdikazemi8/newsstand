package com.nebeek.newsstand.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nebeek.newsstand.data.models.TelegramMessage;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface MessageDao {

    @Query("SELECT * from TelegramMessage")
    List<TelegramMessage> fetchAll();

    @Query("SELECT * from TelegramMessage WHERE archive=1")
    List<TelegramMessage> fetchAllArchive();

    @Query("UPDATE TelegramMessage SET liked=:liked WHERE id=:id")
    void likeMessage(String id, boolean liked);

    @Query("UPDATE TelegramMessage SET archive=:archive WHERE id=:id")
    void bookmarkMessage(String id, boolean archive);

    @Insert(onConflict = IGNORE)
    void insertAll(List<TelegramMessage> messages);
}
