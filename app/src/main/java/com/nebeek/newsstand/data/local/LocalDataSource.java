package com.nebeek.newsstand.data.local;

import com.nebeek.newsstand.data.DataSource;

public class LocalDataSource extends DataSource {
    @Override
    public void searchKeyword(String keyword, SearchKeywordCallback callback) {

    }

    @Override
    public void getKeywords(GetKeywordsCallback callback) {

    }

    @Override
    public void addKeyword(String keyword, AddKeywordCallback callback) {

    }

    @Override
    public void removeKeyword(Integer id, RemoveKeywordCallback callback) {

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
    public void fakeRegister(FakeRegisterCallback callback) {

    }
}
