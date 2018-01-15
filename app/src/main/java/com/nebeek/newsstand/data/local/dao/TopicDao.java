package com.nebeek.newsstand.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface TopicDao {

    @Insert(onConflict = IGNORE)
    void insertAll(List<Topic> topics);

    @Query("SELECT COUNT(*) from TOPIC")
    int count();

    @Query("SELECT * from TOPIC")
    List<Topic> fetchAll();

}