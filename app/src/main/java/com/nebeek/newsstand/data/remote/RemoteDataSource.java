package com.nebeek.newsstand.data.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.MyAdapterFactory;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.data.models.User;
import com.nebeek.newsstand.data.remote.request.FCMRequest;
import com.nebeek.newsstand.data.remote.response.KeywordsResponse;
import com.nebeek.newsstand.data.remote.response.SearchResponse;
import com.nebeek.newsstand.data.remote.response.TokenResponse;
import com.nebeek.newsstand.data.remote.response.TopicsResponse;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource extends DataSource {
    private ApiService apiService = null;
    private static RemoteDataSource remoteDataSource = null;
    private PreferenceManager preferenceManager;

    public static RemoteDataSource getInstance(PreferenceManager preferenceManager) {
        if (remoteDataSource == null) {
            remoteDataSource = new RemoteDataSource(preferenceManager);
        }
        return remoteDataSource;
    }

    private RemoteDataSource(PreferenceManager preferenceManager) {
        prepare(preferenceManager);
    }

    private void prepare(PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(
                chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder().header(
                            "Authorization",
                            preferenceManager.getAuthorization()
                    );

                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
        ).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapterFactory(MyAdapterFactory.create())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    @Override
    public void searchKeyword(String keyword, SearchKeywordCallback callback) {
        Call<SearchResponse> call = apiService.searchKeyword(keyword);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                if (response.isSuccessful()) {
                    callback.onResponse(response.body().getResults());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {

                callback.onFailure();
            }
        });
    }

    @Override
    public void getKeywords(GetKeywordsCallback callback) {
        Log.d("TAG", "remoteDataSource getSubscriptions ");
        Call<KeywordsResponse> call = apiService.getSubscriptions();
        call.enqueue(new Callback<KeywordsResponse>() {
            @Override
            public void onResponse(Call<KeywordsResponse> call, Response<KeywordsResponse> response) {
                Log.d("TAG", "onResponse " + response.isSuccessful());
                Log.d("TAG", "onResponse " + response.code());

                if (response.isSuccessful()) {
                    callback.onResponse(response.body().getTopics());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<KeywordsResponse> call, Throwable t) {
                Log.d("TAG", "onFailure " + t.getCause());

                callback.onFailure();
            }
        });
    }

    @Override
    public void subscribeToTopic(String id, SubscribeCallback callback) {
        Call<ResponseBody> call = apiService.addSubscription(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void removeKeyword(String id, RemoveKeywordCallback callback) {
        Call<ResponseBody> call = apiService.removeSubscription(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void downloadPhoto(String photoURL, DownloadPhotoCallback callback) {
        Call<ResponseBody> call = apiService.downloadPhoto(photoURL);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void sendFcmIDToServer(String fcmID, SendFcmIDCallback callback) {
        Call<ResponseBody> call = apiService.sendFcmIDToServer(FCMRequest.builder().fcm(fcmID).build());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void registerNewUser(User user, RegisterCallback callback) {
        Call<User> call = apiService.registerNewUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void prepareDataSource() {
        prepare(this.preferenceManager);
    }

    @Override
    public void getAllTopics(TopicsResponseCallback callback) {
        Call<TopicsResponse> call = apiService.getAllTopics();
        call.enqueue(new Callback<TopicsResponse>() {
            @Override
            public void onResponse(Call<TopicsResponse> call, Response<TopicsResponse> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<TopicsResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void authenticateUser(User user, AuthenticateCallback callback) {
        Call<TokenResponse> call = apiService.authenticateUser(user.getId(), user);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
    }
}
