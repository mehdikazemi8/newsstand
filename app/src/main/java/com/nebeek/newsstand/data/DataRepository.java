package com.nebeek.newsstand.data;

import android.util.Log;

import com.nebeek.newsstand.data.models.User;
import com.nebeek.newsstand.util.NetworkHelper;

public class DataRepository extends DataSource {
    private DataSource remoteDataSource;
    private DataSource localDataSource;
    private NetworkHelper networkHelper;

    private static DataRepository dataRepository;

    private DataRepository(DataSource remoteDataSource, DataSource localDataSource, NetworkHelper networkHelper) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.networkHelper = networkHelper;
    }

    public static synchronized void init(DataSource remoteDataSource, DataSource localDataSource, NetworkHelper networkHelper) {
        dataRepository = new DataRepository(remoteDataSource, localDataSource, networkHelper);
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
    public void getKeywords(GetKeywordsCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            Log.d("TAG", "want to get keywords");
            remoteDataSource.getKeywords(callback);
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
    public void getAllTopics(TopicsResponseCallback callback) {
        if (!networkHelper.isNetworkAvailable()) {
            callback.onNetworkFailure();
        } else {
            remoteDataSource.getAllTopics(callback);
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
}
