package com.nebeek.newsstand.data;

import com.nebeek.newsstand.data.models.Snippet;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.data.models.User;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;
import com.nebeek.newsstand.data.remote.response.TokenResponse;

import java.util.List;

import okhttp3.ResponseBody;

public abstract class DataSource {

    public interface SearchKeywordCallback {

        void onResponse(List<Snippet> snippetList);

        void onFailure();

        void onNetworkFailure();
    }

    public interface GetSubscribesCallback {

        void onResponse(List<Topic> topicList);

        void onFailure();

        void onNetworkFailure();
    }

    public interface SubscribeCallback {

        void onSuccess();

        void onFailure();

        void onNetworkFailure();
    }

    public interface RemoveKeywordCallback {

        void onSuccess();

        void onFailure();

        void onNetworkFailure();
    }

    public interface DownloadPhotoCallback {

        void onResponse(ResponseBody response);

        void onFailure();

        void onNetworkFailure();
    }

    public interface SendFcmIDCallback {

        void onSuccess();

        void onFailure();

        void onNetworkFailure();
    }

    public interface RegisterCallback {

        void onSuccess(User user);

        void onFailure();

        void onNetworkFailure();
    }

    public interface TopicsResponseCallback {

        void onResponse(List<Topic> topicList);

        void onFailure();

        void onNetworkFailure();
    }

    public interface AuthenticateCallback {

        void onResponse(TokenResponse tokenResponse);

        void onFailure();

        void onNetworkFailure();
    }

    public interface GetMessagesCallback {

        void onResponse(MessagesResponse response);

        void onFailure();

        void onNetworkFailure();
    }

    public abstract void prepareDataSource();

    public abstract void searchKeyword(String keyword, SearchKeywordCallback callback);

    public abstract void getSubscribes(GetSubscribesCallback callback);

    public abstract void subscribeToTopic(String id, SubscribeCallback callback);

    public abstract void removeKeyword(String id, RemoveKeywordCallback callback);

    public abstract void downloadPhoto(String photoURL, DownloadPhotoCallback callback);

    public abstract void sendFcmIDToServer(String fcmID, SendFcmIDCallback callback);

    public abstract void registerNewUser(User user, RegisterCallback callback);

    public abstract void authenticateUser(User user, AuthenticateCallback callback);

    public abstract void getAllTopics(String topicName, TopicsResponseCallback callback);

    public abstract void getMessages(String topicID, GetMessagesCallback callback);
}
