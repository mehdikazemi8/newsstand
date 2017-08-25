package com.nebeek.newsstand.data.remote;

import com.nebeek.newsstand.data.remote.response.SearchResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    String BASE_URL = "http://www.mocky.io/v2/";

    @FormUrlEncoded
    @POST("59a07c3d110000810464427a")
    Call<SearchResponse> searchKeyword(@Field("keyword") String keyword);
}
