package com.nebeek.newsstand.data.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.MyAdapterFactory;
import com.nebeek.newsstand.data.local.ChannelsManager;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.data.models.LikeRequest;
import com.nebeek.newsstand.data.models.TelegramMessage;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.data.models.TopicData;
import com.nebeek.newsstand.data.models.User;
import com.nebeek.newsstand.data.remote.request.FCMRequest;
import com.nebeek.newsstand.data.remote.request.Subscription;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;
import com.nebeek.newsstand.data.remote.response.SubscribesResponse;
import com.nebeek.newsstand.data.remote.response.TokenResponse;
import com.nebeek.newsstand.data.remote.response.TopicsResponse;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
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

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder();
        okHttpClientBuilder.addInterceptor(
                chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder().header(
                            "Authorization",
                            preferenceManager.getAuthorization() == null ? "token_is_null" : preferenceManager.getAuthorization()
                    );

                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
        ).build();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(loggingInterceptor);

        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapterFactory(MyAdapterFactory.create())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    @Override
    public void searchKeyword(String keyword, SearchKeywordCallback callback) {
        Call<MessagesResponse> call = apiService.searchKeyword(keyword);
        call.enqueue(new Callback<MessagesResponse>() {
            @Override
            public void onResponse(Call<MessagesResponse> call, Response<MessagesResponse> response) {

                if (response.isSuccessful()) {
                    callback.onResponse(response.body().getResults());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MessagesResponse> call, Throwable t) {

                callback.onFailure();
            }
        });
    }

    @Override
    public void getSubscribes(GetSubscribesCallback callback) {
        Log.d("TAG", "remoteDataSource getSubscriptions ");
        Call<SubscribesResponse> call = apiService.getSubscriptions();
        call.enqueue(new Callback<SubscribesResponse>() {
            @Override
            public void onResponse(Call<SubscribesResponse> call, Response<SubscribesResponse> response) {

                if (response.isSuccessful()) {

                    for (Topic topic : response.body().getTopics()) {
                        for (TopicData topicData : response.body().getData()) {
                            if (topic.getId().equals(topicData.getArgument())) {
                                topic.setDeleteId(topicData.getId());
                                break;
                            }
                        }
                    }

                    callback.onResponse(response.body().getTopics());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<SubscribesResponse> call, Throwable t) {
                Log.d("TAG", "onFailure " + t.getCause());

                callback.onFailure();
            }
        });
    }

    @Override
    public void subscribeToTopic(String id, SubscribeCallback callback) {
        // todo what is this? :))
//        id = "5a0c411201019f20af053da4";

        Call<ResponseBody> call = apiService.addSubscription(new Subscription(id));
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

                Log.d("Aeoa", "onResponse " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                callback.onFailure();
                Log.d("Aeoa", "onFailure " + t.getCause());
                Log.d("Aeoa", "onFailure " + t.getMessage());
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
        Call<ResponseBody> call = apiService.sendFcmIDToServer(
                FCMRequest.builder().instanceId(fcmID).type("FirebaseInstance").build()
        );
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
    public void getAllTopics(String topicName, TopicsResponseCallback callback) {
        Call<TopicsResponse> call = apiService.getAllTopics(topicName);
        Log.d("TAG", "abcd " + call.request().url());
        call.enqueue(new Callback<TopicsResponse>() {
            @Override
            public void onResponse(Call<TopicsResponse> call, Response<TopicsResponse> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(response.body().getTopics());
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
        // user.getId(),
//        user.setType("authenticate_pass");
        Call<TokenResponse> call = apiService.authenticateUser(user);
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

    @Override
    public void getMessages(Integer currentPage, String topicID, GetMessagesCallback callback) {
        Call<MessagesResponse> call = apiService.getMessages(topicID,
                (currentPage == null ? null : currentPage * 10)
        );
        call.enqueue(new Callback<MessagesResponse>() {
            @Override
            public void onResponse(Call<MessagesResponse> call, Response<MessagesResponse> response) {
                if (response.isSuccessful()) {

                    ChannelsManager.getInstance().addAll(response.body().getChannels());

                    for (TelegramMessage telegramMessage : response.body().getResults()) {
                        telegramMessage.setSource(ChannelsManager.getInstance().getChannel(telegramMessage.getChannelId()));
                    }
                    callback.onResponse(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MessagesResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void likeMessage(LikeRequest request, LikeMessageCallback callback) {
        Call<ResponseBody> call = apiService.likeMessage(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.d("TAG", "onResponse " + response.isSuccessful());
                Log.d("TAG", "onResponse " + response.code());
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
}
