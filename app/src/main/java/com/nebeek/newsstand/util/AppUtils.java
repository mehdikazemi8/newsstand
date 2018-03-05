package com.nebeek.newsstand.util;

import android.util.Log;

import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class AppUtils {
    public static void updateTopics(List<Topic> newList, List<Topic> cachedList) {
        for (Topic x : newList) {
            Log.d("TAG", "topicDao " + x.getReadCount().getSize());
        }
        for (Topic x : cachedList) {
            Log.d("TAG", "topicDao cached " + x.getReadCount().getSize());
        }

        for (Topic topic : newList) {
            for (Topic cached : cachedList) {
                if (topic.getId().equals(cached.getId())) {
                    topic.setReadCount(cached.getReadCount());
                }
            }
        }
    }
}
