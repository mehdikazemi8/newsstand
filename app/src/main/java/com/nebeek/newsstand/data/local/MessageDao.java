package com.nebeek.newsstand.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nebeek.newsstand.data.models.TelegramMessage;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface MessageDao {

    @Query("SELECT * from TelegramMessage")
    List<TelegramMessage> findAllMessages();

    @Query("UPDATE TelegramMessage SET liked=:liked WHERE id=:id")
    void likeMessage(String id, boolean liked);

    @Insert(onConflict = IGNORE)
    void insertMessages(List<TelegramMessage> messages);

}
