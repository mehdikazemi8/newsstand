package com.nebeek.newsstand.data.local;

import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.LikeRequest;
import com.nebeek.newsstand.data.models.TelegramMessage;
import com.nebeek.newsstand.data.models.User;

import java.util.List;

public class LocalDataSource extends DataSource {
    @Override
    public void searchKeyword(String keyword, SearchKeywordCallback callback) {

    }

    @Override
    public void getSubscribes(GetSubscribesCallback callback) {

    }

    @Override
    public void subscribeToTopic(String id, SubscribeCallback callback) {

    }

    @Override
    public void removeKeyword(String id, RemoveKeywordCallback callback) {

    }

    @Override
    public void downloadPhoto(String photoURL, DownloadPhotoCallback callback) {

    }

    @Override
    public void sendFcmIDToServer(String fcmID, SendFcmIDCallback callback) {

    }

    @Override
    public void prepareDataSource() {

    }

    @Override
    public void registerNewUser(User user, RegisterCallback callback) {

    }

    @Override
    public void getAllTopics(String topicName, TopicsResponseCallback callback) {

    }

    @Override
    public void authenticateUser(User user, AuthenticateCallback callback) {

    }

    @Override
    public void getMessages(Integer currentPage, String topicID, GetMessagesCallback callback) {

    }

    @Override
    public void likeMessage(LikeRequest request, LikeMessageCallback callback) {

    }

    @Override
    public void bookmarkMessage(String id) {

    }

    @Override
    public List<TelegramMessage> getAllBookmarked() {
        return null;
    }
}
