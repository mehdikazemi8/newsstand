package com.nebeek.newsstand.data.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Keyword;
import com.nebeek.newsstand.data.remote.response.KeywordsResponse;
import com.nebeek.newsstand.data.remote.response.SearchResponse;

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

    public static RemoteDataSource getInstance() {
        if (remoteDataSource == null) {
            remoteDataSource = new RemoteDataSource();
        }
        return remoteDataSource;
    }

    private RemoteDataSource() {
        prepare();
    }

    private void prepare() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(
                chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder().header(
                            "Authorization",
                            Credentials.basic("aUsername", "aPassword")
                    );

                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
        ).build();

        Gson gson = new GsonBuilder()
                .setLenient()
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
        Log.d("TAG", "remoteDataSource getKeywords ");
        Call<KeywordsResponse> call = apiService.getKeywords();
        call.enqueue(new Callback<KeywordsResponse>() {
            @Override
            public void onResponse(Call<KeywordsResponse> call, Response<KeywordsResponse> response) {
                Log.d("TAG", "onResponse " + response.isSuccessful());
                Log.d("TAG", "onResponse " + response.code());

                if (response.isSuccessful()) {
                    callback.onResponse(response.body().getKeywords());
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
    public void addKeyword(String keyword, AddKeywordCallback callback) {
        Call<Keyword> call = apiService.addKeyword(keyword);
        call.enqueue(new Callback<Keyword>() {
            @Override
            public void onResponse(Call<Keyword> call, Response<Keyword> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Keyword> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void removeKeyword(Integer id, RemoveKeywordCallback callback) {
        Call<ResponseBody> call = apiService.removeKeyword(id);
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
}
