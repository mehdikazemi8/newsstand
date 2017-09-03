package com.nebeek.newsstand.data.remote;

import com.nebeek.newsstand.data.models.Keyword;
import com.nebeek.newsstand.data.remote.response.KeywordsResponse;
import com.nebeek.newsstand.data.remote.response.SearchResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    String BASE_URL = "http://www.mocky.io/v2/";

    @FormUrlEncoded
    @POST("59a07c3d110000810464427a")
    Call<SearchResponse> searchKeyword(@Field("keyword") String keyword);

    @GET("59aad27c270000ea0cef71fb")
    Call<KeywordsResponse> getKeywords();

    @FormUrlEncoded
    @POST("59ac2169100000570bf9c232")
    Call<Keyword> addKeyword(@Field("keyword") String keyword);

    @DELETE("59ac2169100000570bf9c232/{id}")
    Call<ResponseBody> removeKeyword(@Path("id") Integer id);
}
