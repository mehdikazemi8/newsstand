package com.nebeek.newsstand.controller.base;

import com.nebeek.newsstand.data.models.TelegramMessage;
import com.nebeek.newsstand.ui.topic.MessageRowView;

public interface BaseMessageListPresenter {

    int getMessagesCount();

    void onBindRowViewAtPosition(int position, MessageRowView view);

    void likeMessage(int position);
}
