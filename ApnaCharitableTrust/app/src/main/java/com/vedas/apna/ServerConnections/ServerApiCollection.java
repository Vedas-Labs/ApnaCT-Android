package com.vedas.apna.ServerConnections;

import com.google.gson.JsonObject;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ServerApiCollection {
    //String BASE_URL = "http://54.167.167.144:3000/api/";
    String BASE_URL = "https://api.apnacharitabletrust.org/api/";
    //String BASE_URL1 = "http://54.167.167.144:3000/";
    String BASE_URL1 = "https://api.apnacharitabletrust.org/";
    // --Commented out by Inspection (2/26/2021 10:10 AM):String CHAT_SERVER_URL = "http://54.167.167.144:3000"; //https://socket-io-chat.now.sh/

    @POST
    Call<ResponseBody> PostDataFromServer(@Url String url, @Body JsonObject registerObj);

    @Multipart
    @POST
    Call<ResponseBody> PostDataFromServerProfile(@Url String url,
                                                 @Part("registerData") RequestBody registerData,
                                                 @Part MultipartBody.Part profilePic);

    //Logout API
    @POST
    Call<ResponseBody> PostDataFromLogout(@Header("x-access-token") String accessToken, @Url String url, @Body JsonObject logoutObj);

    //Gallery API
    @GET
    Call<ResponseBody> fetchGalleryAPI( @Url String url);

    //Donate Offline API
    @Multipart
    @POST
    Call<ResponseBody> PostDataFromOffline(@Url String url, @Part("offlineData") RequestBody offlineData,
                                         @Part MultipartBody.Part image1,
                                         @Part MultipartBody.Part image2,
                                         @Part MultipartBody.Part image3);
    //Delete Notifications API
    @HTTP(method = "DELETE", path = "notification/deleteNotification", hasBody = true)
    Call<ResponseBody> deleteNotification(@Body JsonObject deletenotify);
}