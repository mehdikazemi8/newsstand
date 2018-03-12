package com.nebeek.newsstand.controller.base;

import com.nebeek.newsstand.ui.topic.MessageRowView;

public interface BaseMessageListPresenter {

    int getMessagesCount();

    void onBindRowViewAtPosition(int position, MessageRowView view);

    void likeMessage(int position);

    void addBookmark(int position);

    void removeBookmark(int position);

    void playButtonOnClick(int position);
}
