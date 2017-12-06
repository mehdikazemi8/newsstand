package com.nebeek.newsstand.data.remote;

import com.nebeek.newsstand.data.models.Snippet;
import com.nebeek.newsstand.data.models.User;
import com.nebeek.newsstand.data.remote.request.FCMRequest;
import com.nebeek.newsstand.data.remote.request.Subscription;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;
import com.nebeek.newsstand.data.remote.response.SubscribesResponse;
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
import retrofit2.http.Path;
import retrofit2.http.Query;
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
    @POST("authenticate/")
    Call<TokenResponse> authenticateUser(@Body User user);
    // todo @Path("id") String id, put it in User (in body)

    @GET("topics/")
    Call<TopicsResponse> getAllTopics(@Query("filter[names]") String topicName);

    @POST("subscribes/")
    Call<ResponseBody> addSubscription(@Body Subscription subscription);

    @GET("messages/?include=channel")
    Call<MessagesResponse> getMessages(@Query("filter[topics.ref]") String topicId);


    @DELETE("subscribes/{id}/")
    Call<ResponseBody> removeSubscription(@Path("id") String id);


    @GET("subscribes/?me&include=argument&fields=argument")
    Call<SubscribesResponse> getSubscriptions();


    @POST("firebase-instances/")
    Call<ResponseBody> sendFcmIDToServer(@Body FCMRequest fcmRequest);


    @GET("url/{snippetID}")
    Call<Snippet> getSingleSnippet(@Path("snippetID") String snippetID);

    @FormUrlEncoded
    @POST("59a07c3d110000810464427a")
    Call<MessagesResponse> searchKeyword(@Field("keyword") String keyword);


    @GET
    Call<ResponseBody> downloadPhoto(@Url String photoURL);

}
