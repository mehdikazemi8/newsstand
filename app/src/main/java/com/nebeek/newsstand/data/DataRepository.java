package com.nebeek.newsstand.data;

import android.util.Log;

import com.nebeek.newsstand.data.local.AppDatabase;
import com.nebeek.newsstand.data.models.LikeRequest;
import com.nebeek.newsstand.data.models.TelegramMessage;
import com.nebeek.newsstand.data.models.User;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;
import com.nebeek.newsstand.util.NetworkHelper;

import java.util.List;

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
            Log.d("TAG", "want to get keywords");
            remoteDataSource.getSubscribes(callback);
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
    public void getMessages(Integer currentPage, String topicID, GetMessagesCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.getMessages(currentPage, topicID, new GetMessagesCallback() {
                @Override
                public void onResponse(MessagesResponse response) {
                    appDatabase.messageModel().insertMessages(response.getResults());


                    // todo
                    List<TelegramMessage> dbMessages = appDatabase.messageModel().findAllMessages();
                    for (TelegramMessage currentMessage : response.getResults()) {
                        for (TelegramMessage cache : dbMessages) {
                            if (currentMessage.getId().equals(cache.getId())) {
                                currentMessage.setLiked(cache.getLiked());
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
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.likeMessage(request, new LikeMessageCallback() {
                @Override
                public void onSuccess() {
                    appDatabase.messageModel().likeMessage(request.getArgument(), true);
                    callback.onSuccess();
                }

                @Override
                public void onFailure() {
                    callback.onFailure();
                    ;
                }

                @Override
                public void onNetworkFailure() {
                    callback.onNetworkFailure();
                }
            });
        }
    }
}
