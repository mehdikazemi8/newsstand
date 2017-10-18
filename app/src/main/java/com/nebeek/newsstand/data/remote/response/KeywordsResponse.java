package com.nebeek.newsstand.data.remote.response;

import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class KeywordsResponse {
    List<Topic> topics;

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
}
