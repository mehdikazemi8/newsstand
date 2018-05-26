package com.nebeek.newsstand.data.models;

import java.util.HashSet;
import java.util.Set;

public class TrackedMessages extends BaseModel {
    Set<String> ids = new HashSet<>();

    public TrackedMessages() {
    }

    public void addId(String id) {
        ids.add(id);
    }

    public Set<String> getIds() {
        return ids;
    }

    public void setIds(Set<String> ids) {
        this.ids = ids;
    }
}
