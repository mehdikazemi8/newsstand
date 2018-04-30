package com.nebeek.newsstand.data;

import android.util.Log;

import com.nebeek.newsstand.data.local.AppDatabase;
import com.nebeek.newsstand.data.models.AppSize;
import com.nebeek.newsstand.data.models.LikeRequest;
import com.nebeek.newsstand.data.models.TelegramMessage;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.data.models.User;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;
import com.nebeek.newsstand.util.NetworkHelper;

import java.util.List;

import static com.nebeek.newsstand.util.AppUtils.updateTopics;

public class DataRepository extends DataSource {
    private DataSource remoteDataSource;
    private DataSource localDataSource;
    private NetworkHelper networkHelper;
    private AppDatabase appDatabase;

    private static DataRepository dataRepository;

    private DataRepository(AppDatabase appDatabase, DataSource remoteDataSource, DataSource localDataSource, NetworkHelper networkHelper) {
        this.appDatabase = appDatabase;
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.networkHelper = networkHelper;
    }

    public static synchronized void init(AppDatabase appDatabase, DataSource remoteDataSource, DataSource localDataSource, NetworkHelper networkHelper) {
        dataRepository = new DataRepository(appDatabase, remoteDataSource, localDataSource, networkHelper);
    }

    public static synchronized DataRepository getInstance() {
        return dataRepository;
    }

    @Override
    public void prepareDataSource() {
        remoteDataSource.prepareDataSource();
    }

    @Override
    public void searchKeyword(String keyword, SearchKeywordCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.searchKeyword(keyword, callback);
        }
    }

    @Override
    public void getSubscribes(GetSubscribesCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.getSubscribes(new GetSubscribesCallback() {
                @Override
                public void onResponse(List<Topic> topicList) {

                    List<Topic> cachedTopics = appDatabase.topicModel().fetchAll();

                    updateTopics(topicList, cachedTopics);

                    appDatabase.topicModel().insertAll(topicList);

                    callback.onResponse(topicList);
                }

                @Override
                public void onFailure() {
                    callback.onFailure();
                }

                @Override
                public void onNetworkFailure() {
                    callback.onNetworkFailure();
                }
            });
        }
    }

    @Override
    public void subscribeToTopic(String id, SubscribeCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.subscribeToTopic(id, callback);
        }
    }

    @Override
    public void removeKeyword(String id, RemoveKeywordCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.removeKeyword(id, callback);
        }
    }

    @Override
    public void downloadPhoto(String photoURL, DataSource.DownloadPhotoCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.downloadPhoto(photoURL, callback);
        }
    }

    @Override
    public void sendFcmIDToServer(String fcmID, SendFcmIDCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.sendFcmIDToServer(fcmID, callback);
        }
    }

    @Override
    public void registerNewUser(User user, RegisterCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.registerNewUser(user, callback);
        }
    }

    @Override
    public void getAllTopics(String topicName, TopicsResponseCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.getAllTopics(topicName, callback);
        }
    }

    @Override
    public void authenticateUser(User user, AuthenticateCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.authenticateUser(user, callback);
        }
    }

    @Override
    public void getMessages(Integer offset, String topicID, GetMessagesCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.getMessages(offset, topicID, new GetMessagesCallback() {
                @Override
                public void onResponse(MessagesResponse response) {
                    appDatabase.messageModel().insertAll(response.getResults());


                    // todo
                    List<TelegramMessage> dbMessages = appDatabase.messageModel().fetchAll();
                    for (TelegramMessage currentMessage : response.getResults()) {
                        for (TelegramMessage cache : dbMessages) {
                            if (currentMessage.getId().equals(cache.getId())) {
                                currentMessage.setLiked(cache.getLiked());
                                currentMessage.setBookmarked(cache.getBookmarked());
                            }
                        }
                    }

                    callback.onResponse(response);
                }

                @Override
                public void onFailure() {
                    callback.onFailure();
                }

                @Override
                public void onNetworkFailure() {
                    callback.onNetworkFailure();
                }
            });
        }
    }

    @Override
    public void likeMessage(LikeRequest request, LikeMessageCallback callback) {
        Log.d("tag", "like id " + request.getArgument());
        appDatabase.messageModel().likeMessage(request.getArgument(), true);

        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.likeMessage(request, new LikeMessageCallback() {
                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onFailure() {
                    callback.onFailure();
                }

                @Override
                public void onNetworkFailure() {
                    callback.onNetworkFailure();
                }
            });
        }
    }

    @Override
    public void addBookmark(String id, Long bookmarkTime) {
        appDatabase.messageModel().bookmarkMessage(id, true, bookmarkTime);
    }

    @Override
    public void removeBookmark(String id) {
        appDatabase.messageModel().bookmarkMessage(id, false, 0);
    }

    @Override
    public List<TelegramMessage> getAllBookmarked() {
        return appDatabase.messageModel().fetchAllBookmarked();
    }

    @Override
    public void fetchRelatedTopics(List<List<Object>> request, TopicsResponseCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.fetchRelatedTopics(request, callback);
        }
    }

    @Override
    public void updateTopicReadCount(AppSize readCount, String topicId) {
        appDatabase.topicModel().updateReadCount(readCount.getSize(), topicId);
    }

    @Override
    public boolean isMessageLiked(String id) {
//        Log.d("tag", "is message like id " + id + " " + appDatabase.messageModel().isMessageLiked(id));

        return appDatabase.messageModel().isMessageLiked(id);
    }

    @Override
    public void getOnboardingTopics(TopicsResponseCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.getOnboardingTopics(callback);
        }
    }
}
