package com.nebeek.newsstand.ui.topic;

public interface SnippetRowView {

    void setTitle(String title);

    void setSourcePhoto(String sourcePhotoUrl);

    void setPhoto(String photoUrl);

    void setSource(String source);

    void setDate(String date);

    void setDescription(String description);

    void makePhotoVisible();

    void makePhotoInvisible();
}
