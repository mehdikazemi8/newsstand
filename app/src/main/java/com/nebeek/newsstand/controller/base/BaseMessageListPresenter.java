package com.nebeek.newsstand.controller.base;

import com.nebeek.newsstand.ui.topic.SnippetRowView;

public interface BaseMessageListPresenter {

    int getMessagesCount();

    void onBindRowViewAtPosition(int position, SnippetRowView view);
}
