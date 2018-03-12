package com.nebeek.newsstand.ui.topic;

public interface MessageRowView {

    void setTitle(String title);

    void setSourcePhoto(String sourcePhotoUrl);

    void setPhoto(int width, int height, String photoUrl);

    void setSource(String source);

    void setDate(String date);

    void setDescription(String description);

    void makePhotoVisible();

    void makePhotoInvisible();

    void showLikeIcon();

    void hideLikeIcon();

    void showBookmark();

    void hideBookmark();

    void showPlayButton();

    void hidePlayButton();

    void showLikeCount();

    void hideLikeCount();

    void setLikeCount(Integer likeCount);
}
