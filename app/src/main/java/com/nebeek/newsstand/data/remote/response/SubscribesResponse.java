package com.nebeek.newsstand.data.remote.response;

import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.data.models.TopicData;

import java.util.List;

public class SubscribesResponse {
    List<Topic> included;
    List<TopicData> data;

    public List<Topic> getTopics() {
        return included;
    }

    public void setTopics(List<Topic> topics) {
        this.included = topics;
    }

    public List<TopicData> getData() {
        return data;
    }

    public void setData(List<TopicData> data) {
        this.data = data;
    }
}
