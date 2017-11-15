package com.nebeek.newsstand.data.remote.response;

import com.nebeek.newsstand.data.models.BaseModel;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class TopicsResponse extends BaseModel {
    private List<Topic> data;

    public List<Topic> getTopics() {
        return data;
    }

    public void setTopics(List<Topic> data) {
        this.data = data;
    }
}
