package com.nebeek.newsstand.data.remote.response;

import com.nebeek.newsstand.data.models.BaseModel;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class TopicsResponse extends BaseModel {
    private int count;
    private String previous;
    private String next;
    private List<Topic> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<Topic> getResults() {
        return results;
    }

    public void setResults(List<Topic> results) {
        this.results = results;
    }
}
