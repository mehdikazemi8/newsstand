package com.nebeek.newsstand.controller.base;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.LikeRequest;
import com.nebeek.newsstand.data.models.TelegramMessage;
import com.nebeek.newsstand.data.remote.ApiService;
import com.nebeek.newsstand.event.NewBookmarkAdded;
import com.nebeek.newsstand.event.RxBus;
import com.nebeek.newsstand.ui.topic.MessageRowView;
import com.nebeek.newsstand.util.DateManager;

import java.util.ArrayList;
import java.util.List;

public class MessageListPresenter implements BaseMessageListPresenter {

    protected DataRepository dataRepository;
    protected final List<TelegramMessage> messageList = new ArrayList<>();

    public MessageListPresenter(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public int getMessagesCount() {
        return messageList.size();
    }

    @Override
    public void onBindRowViewAtPosition(int position, MessageRowView view) {
        if (messageList.get(position).getSource() != null) {
            view.setSourcePhoto(ApiService.BASE_URL + messageList.get(position).getSource().getImages().get(0).getImages().get(0).getData());
            view.setSource(messageList.get(position).getSource().getNames().get(0).getFa());
        }

        if (messageList.get(position).getImages() != null && messageList.get(position).getImages().size() > 0) {
            try {
                view.setPhoto(
                        messageList.get(position)
                                .getImages().get(0)
                                .getImages().get(0)
                                .getWidth(),
                        messageList.get(position).getImages().get(0).getImages().get(0).getHeight(),
                        ApiService.BASE_URL + messageList.get(position).getImages().get(0).getImages().get(0).getData()
                );
                view.makePhotoVisible();
            } catch (Exception e) {
                view.makePhotoInvisible();
            }
        } else {
            view.makePhotoInvisible();
        }

        if (messageList.get(position).getLiked() != null && messageList.get(position).getLiked()) {
            view.showLikeIcon();
        } else {
            view.hideLikeIcon();
        }

        if (messageList.get(position).getBookmarked() != null && messageList.get(position).getBookmarked()) {
            view.showBookmark();
        } else {
            view.hideBookmark();
        }

        view.setDate(DateManager.convertStringToDate(messageList.get(position).getDateCreated()));

        view.setDescription(messageList.get(position).getFullText().getFa());
    }

    @Override
    public void likeMessage(int position) {
        messageList.get(position).setLiked(true);
        dataRepository.likeMessage(new LikeRequest(messageList.get(position).getId()),
                new DataSource.LikeMessageCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onNetworkFailure() {

                    }
                });
    }

    @Override
    public void addBookmark(int position) {
        messageList.get(position).setBookmarked(true);
        long bookmarkTime = System.currentTimeMillis();
        messageList.get(position).setBookmarkTime(bookmarkTime);
        dataRepository.addBookmark(messageList.get(position).getId(), bookmarkTime);

        RxBus.getInstance().send(new NewBookmarkAdded());
    }

    @Override
    public void removeBookmark(int position) {
        messageList.get(position).setBookmarked(false);
        dataRepository.removeBookmark(messageList.get(position).getId());
    }
}
