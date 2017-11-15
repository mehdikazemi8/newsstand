package com.nebeek.newsstand.data.remote.response;

import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class SubscribesResponse {
    List<Topic> included;

    public List<Topic> getTopics() {
        return included;
    }

    public void setTopics(List<Topic> topics) {
        this.included = topics;
    }
}
