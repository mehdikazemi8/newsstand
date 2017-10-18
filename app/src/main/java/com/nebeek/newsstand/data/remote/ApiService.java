package com.nebeek.newsstand.data.remote;

import com.nebeek.newsstand.data.models.Snippet;
import com.nebeek.newsstand.data.models.User;
import com.nebeek.newsstand.data.remote.request.FCMRequest;
import com.nebeek.newsstand.data.remote.response.KeywordsResponse;
import com.nebeek.newsstand.data.remote.response.SearchResponse;
import com.nebeek.newsstand.data.remote.response.TokenResponse;
import com.nebeek.newsstand.data.remote.response.TopicsResponse;

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
    String BASE_URL = "http://nebeek.com:8090/";
//    String BASE_URL = "http://136.243.149.242:8090/";

    /**
     * for anonymous login create a password and create a user with that
     *
     * @param user
     * @return
     */
    @POST("users/")
    Call<User> registerNewUser(@Body User user);

    /**
     * for anonymous login authenticate the fake user you created with registerNewUser api
     *
     * @param user
     * @return
     */
    @POST("users/{id}/authenticate_pass/")
    Call<TokenResponse> authenticateUser(@Path("id") String id, @Body User user);


    @GET("topics/")
    Call<TopicsResponse> getAllTopics();

    @FormUrlEncoded
    @POST("subscribes/")
    Call<ResponseBody> addSubscription(@Field("argument") String id);


    @DELETE("subscribes/{id}/")
    Call<ResponseBody> removeSubscription(@Path("id") String id);


    @GET("subscribes/")
    Call<KeywordsResponse> getSubscriptions();


    @PUT("api-register/")
    Call<ResponseBody> sendFcmIDToServer(@Body FCMRequest fcmRequest);


    @GET("url/{snippetID}")
    Call<Snippet> getSingleSnippet(@Path("snippetID") String snippetID);

    @FormUrlEncoded
    @POST("59a07c3d110000810464427a")
    Call<SearchResponse> searchKeyword(@Field("keyword") String keyword);


    @GET
    Call<ResponseBody> downloadPhoto(@Url String photoURL);

}
