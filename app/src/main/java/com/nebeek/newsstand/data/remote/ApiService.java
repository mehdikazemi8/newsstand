package com.nebeek.newsstand.data.remote;

import com.nebeek.newsstand.data.models.Keyword;
import com.nebeek.newsstand.data.models.Snippet;
import com.nebeek.newsstand.data.remote.request.FCMRequest;
import com.nebeek.newsstand.data.remote.response.KeywordsResponse;
import com.nebeek.newsstand.data.remote.response.SearchResponse;
import com.nebeek.newsstand.data.remote.response.TokenResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {
    //    String BASE_URL = "http://www.mocky.io/v2/";
    String BASE_URL = "http://130.185.74.170:8090/";
//    String BASE_URL = "http://136.243.149.242:8090/";

    @DELETE("keywords/{id}/")
    Call<ResponseBody> removeKeyword(@Path("id") Integer id);

    @GET("keywords/")
    Call<KeywordsResponse> getKeywords();

    @FormUrlEncoded
    @POST("keywords/")
    Call<Keyword> addKeyword(@Field("keyword") String keyword);

    @PUT("api-register/")
    Call<ResponseBody> sendFcmIDToServer(@Body FCMRequest fcmRequest);

    @GET
    Call<ResponseBody> downloadPhoto(@Url String photoURL);

    @POST("59bd2fcb3c00007501529f89")
    Call<TokenResponse> fakeRegister();


    @GET("url/{snippetID}")
    Call<Snippet> getSingleSnippet(@Path("snippetID") String snippetID);

    @FormUrlEncoded
    @POST("59a07c3d110000810464427a")
    Call<SearchResponse> searchKeyword(@Field("keyword") String keyword);

}
