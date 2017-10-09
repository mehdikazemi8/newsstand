package com.nebeek.newsstand.data;

import com.nebeek.newsstand.data.models.Keyword;
import com.nebeek.newsstand.data.models.Snippet;
import com.nebeek.newsstand.data.remote.response.TokenResponse;

import java.util.List;

import okhttp3.ResponseBody;

public abstract class DataSource {

    public interface SearchKeywordCallback {

        void onResponse(List<Snippet> snippetList);

        void onFailure();

        void onNetworkFailure();
    }

    public interface GetKeywordsCallback {

        void onResponse(List<Keyword> keywordList);

        void onFailure();

        void onNetworkFailure();
    }

    public interface AddKeywordCallback {

        void onResponse(Keyword keyword);

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

    public interface FakeRegisterCallback {

        void onSuccess(TokenResponse tokenResponse);

        void onFailure();

        void onNetworkFailure();
    }

    public abstract void prepareDataSource();

    public abstract void searchKeyword(String keyword, SearchKeywordCallback callback);

    public abstract void getKeywords(GetKeywordsCallback callback);

    public abstract void addKeyword(String keyword, AddKeywordCallback callback);

    public abstract void removeKeyword(Integer id, RemoveKeywordCallback callback);

    public abstract void downloadPhoto(String photoURL, DownloadPhotoCallback callback);

    public abstract void sendFcmIDToServer(String fcmID, SendFcmIDCallback callback);

    public abstract void fakeRegister(FakeRegisterCallback callback);
}
