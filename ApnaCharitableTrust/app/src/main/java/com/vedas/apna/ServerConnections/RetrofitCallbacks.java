package com.vedas.apna.ServerConnections;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vedas.apna.ServerConnections.ServerApiCollection.BASE_URL;
import static okhttp3.RequestBody.create;

@SuppressWarnings("ALL")
public class RetrofitCallbacks {
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static RetrofitCallbacks retrofitCallbacks;
    private static Retrofit retrofit = null;
    ServerResponseInterface serverResponseInterface;

    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static RetrofitCallbacks getInstace() {
        if (retrofitCallbacks == null) {
            retrofitCallbacks = new RetrofitCallbacks();
        }
        return retrofitCallbacks;
    }

    public void fillcontext(Context context) {
        this.context = context;
    }

    public void ApiCallbacks(final Context context, String EndUrl, JsonObject jsonobj, final String from) {
        this.context = context;
        ServerApiCollection apiCollection = getClient().create(ServerApiCollection.class);
        Call<ResponseBody> call = apiCollection.PostDataFromServer(EndUrl, jsonobj);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    String bodyString = null;
                    JSONObject jsonObject = null;
                    try {
                        bodyString = new String(response.body().bytes());
                        jsonObject = new JSONObject(bodyString);
                        Log.e("response ", "messsagesssssssssss"+bodyString);
                        if (jsonObject.has("Tokenval")){
                            Log.e("response ", "messsage"+bodyString);
                            serverResponseInterface.successCallBack(bodyString,from);
                        }else {
                            if (jsonObject.getString("response").equals("3")) {
                                serverResponseInterface.successCallBack(bodyString,from);
                            } else {
                                Log.e("response ", "messsage");
                                serverResponseInterface.failureCallBack(jsonObject.getString("message"));
                            }
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.e("nullll","jjddjnj"+response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                serverResponseInterface.failureCallBack(t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    // "Connection Timeout";
                    System.out.println("Connection Timeout");
                } else if (t instanceof IOException) {
                    // "Timeout";
                    System.out.println("Timeout");
                } else {
                    //Call was cancelled by user
                    if(call.isCanceled()) {
                        System.out.println("Call was cancelled forcefully");
                    } else {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    public void ApiCallbacksProfile(final Context context, String EndUrl, String profileObj, File chatuser, String from) {
        this.context = context;
        RequestBody profilepic = create(MediaType.parse("image/*"), chatuser);
        MultipartBody.Part fileImage = MultipartBody.Part.createFormData("profilePic", chatuser.getName(), profilepic);
        RequestBody registerData = create(MediaType.parse("text/plain"), profileObj);
        Log.e("fileeee", "" + fileImage + " , " + registerData);
        ServerApiCollection apiCollection = getClient().create(ServerApiCollection.class);
        Log.e("fileretro", "");
        Call<ResponseBody> call = apiCollection.PostDataFromServerProfile(EndUrl,registerData, fileImage);
        Log.e("fileretrofi", "");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.e("fileretrofit", "");
                if (response.body() != null) {
                    Log.e("fileretrofits", "");
                    String bodyString = null;
                    JSONObject jsonObject = null;
                    try {
                        Log.e("fileretrofitbo", "");
                        bodyString = new String(response.body().bytes());
                        jsonObject = new JSONObject(bodyString);
                        if (jsonObject.getString("response").equals("3")) {
                            serverResponseInterface.successCallBack(bodyString,from);
                        } else {
                            Log.e("response ", "messsage");
                            serverResponseInterface.failureCallBack(jsonObject.getString("message"));
                        }
                        Log.e("fileretrofitbody", "");
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.e("null","jjddjnjgggg"+response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.e("fileretrofitfail", "" + t.getMessage());
                serverResponseInterface.failureCallBack(t.getMessage());
                //Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                if (t instanceof SocketTimeoutException) {
                    // "Connection Timeout";
                    System.out.println("Connection Timeout");
                } else if (t instanceof IOException) {
                    // "Timeout";
                    System.out.println("Timeout");
                } else {
                    //Call was cancelled by user
                    if(call.isCanceled()) {
                        System.out.println("Call was cancelled forcefully");
                    } else {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    public void ApiCallbacksGallery(final Context context,String url,String from) {
        this.context = context;
        ServerApiCollection apiCollection = getClient().create(ServerApiCollection.class);
        Call<ResponseBody> call = apiCollection.fetchGalleryAPI(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    String bodyString = null;
                    JSONObject jsonObject = null;
                    try {
                        bodyString = new String(response.body().bytes());
                        jsonObject = new JSONObject(bodyString);
                        if (jsonObject.getString("response").equals("3")) {
                            serverResponseInterface.successCallBack(bodyString,from);
                        } else {
                            Log.e("response ", "messsage");
                            serverResponseInterface.failureCallBack(jsonObject.getString("message"));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.e("null","jjddjnjhhhh"+response.code());
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                serverResponseInterface.failureCallBack(t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    // "Connection Timeout";
                    System.out.println("Connection Timeout");
                } else if (t instanceof IOException) {
                    // "Timeout";
                    System.out.println("Timeout");
                } else {
                    //Call was cancelled by user
                    if(call.isCanceled()) {
                        System.out.println("Call was cancelled forcefully");
                    } else {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    public void ApiCallbacksLogout(final Context context, String accesstoken, String url, JsonObject jsonobj,String from) {
        this.context = context;
        ServerApiCollection apiCollection = getClient().create(ServerApiCollection.class);
        Call<ResponseBody> call = apiCollection.PostDataFromLogout(accesstoken,url,jsonobj);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    String bodyString = null;
                    JSONObject jsonObject = null;
                    try {
                        bodyString = new String(response.body().bytes());
                        jsonObject = new JSONObject(bodyString);
                        if (jsonObject.getString("response").equals("3")) {
                            serverResponseInterface.successCallBack(bodyString,from);
                        } else {
                            Log.e("response ", "messsage");
                            serverResponseInterface.failureCallBack(jsonObject.getString("message"));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.e("null","logout"+response.code());
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                serverResponseInterface.failureCallBack(t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    // "Connection Timeout";
                    System.out.println("Connection Timeout");
                } else if (t instanceof IOException) {
                    // "Timeout";
                    System.out.println("Timeout");
                } else {
                    //Call was cancelled by user
                    if(call.isCanceled()) {
                        System.out.println("Call was cancelled forcefully");
                    } else {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void ApiCallbacksOffline(final Context context, String url, JsonObject profileObj, ArrayList<File> imageuris, String type) {
        this.context = context;
        RequestBody image1 = null, image2 = null, image3 = null;
        MultipartBody.Part fileImage1 = null, fileImage2 = null, fileImage3 = null;

        if (imageuris == null) {

        } else {
            if (imageuris.size() == 1){
                    image1 = create(MediaType.parse("image/*"),imageuris.get(0));
                    fileImage1 = MultipartBody.Part.createFormData("image1", imageuris.get(0).getName(), image1);
            }
            if (imageuris.size() == 2) {
                    image1 = create(MediaType.parse("image/*"),imageuris.get(0));
                    fileImage1 = MultipartBody.Part.createFormData("image1", imageuris.get(0).getName(), image1);

                    image2 = create(MediaType.parse("image/*"), imageuris.get(1));
                    fileImage2 = MultipartBody.Part.createFormData("image2", imageuris.get(1).getName(), image2);
            }
            if (imageuris.size() == 3) {
                    image1 = create(MediaType.parse("image/*"),imageuris.get(0));
                    fileImage1 = MultipartBody.Part.createFormData("image1", imageuris.get(0).getName(), image1);

                    image2 = create(MediaType.parse("image/*"), imageuris.get(1));
                    fileImage2 = MultipartBody.Part.createFormData("image2", imageuris.get(1).getName(), image2);

                    image3 = create(MediaType.parse("image/*"), imageuris.get(2));
                    fileImage3 = MultipartBody.Part.createFormData("image3", imageuris.get(2).getName(), image3);
            }
        }
        Log.e("fileeee",""+imageuris);
        RequestBody offlineData = create(MediaType.parse("text/plain"), profileObj.toString());
        Log.e("fileeee",""+imageuris+" , "+offlineData);
        ServerApiCollection apiCollection = getClient().create(ServerApiCollection.class);
        Log.e("fileretro","1111");
        Call<ResponseBody> call = null;
        Log.e("fileretro","");
        if (imageuris == null) {
            call = apiCollection.PostDataFromOffline(url, offlineData, null, null, null);
        } else {
            Log.e("fileretro","22");
                if (imageuris.size() == 1) {
                    call = apiCollection.PostDataFromOffline(url, offlineData, fileImage1, null, null);
                } else  if (imageuris.size() == 2) {
                    call = apiCollection.PostDataFromOffline(url, offlineData, fileImage1, fileImage2, null);
                } else if (imageuris.size() == 3) {
                    call = apiCollection.PostDataFromOffline(url, offlineData, fileImage1, fileImage2, fileImage3);
                }
        }
        Log.e("fileretrofi","333"+call);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.e("fileretrofit","");
                if (response.body() != null) {
                    Log.e("fileretrofits","");
                    String bodyString = null;
                    JSONObject jsonObject = null;
                    try {
                        Log.e("fileretrofitbo","");
                        bodyString = new String(response.body().bytes());
                        jsonObject = new JSONObject(bodyString);
                        if (jsonObject.getString("response").equals("3")) {
                            serverResponseInterface.successCallBack(bodyString,type);
                        } else {
                            Log.e("response ", "messsage");
                            serverResponseInterface.failureCallBack(jsonObject.getString("message"));
                        }
                        Log.e("fileretrofitbody","");
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.e("fileretrofitfail",""+t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                serverResponseInterface.failureCallBack(t.getMessage());
                /*if (t instanceof SocketTimeoutException) {
                    // "Connection Timeout";
                    System.out.println("Connection Timeout");
                } else if (t instanceof IOException) {
                    // "Timeout";
                    System.out.println("Timeout");
                } else {
                    //Call was cancelled by user
                    if(call.isCanceled()) {
                        System.out.println("Call was cancelled forcefully");
                    } else {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }*/
            }
        });
    }

    public void ApiCallbacksDelete(final Context context,JsonObject jsonobj,String from,String notificationID) {
        this.context = context;
        ServerApiCollection apiCollection = getClient().create(ServerApiCollection.class);
        Call<ResponseBody> call = apiCollection.deleteNotification(jsonobj);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    String bodyString = null;
                    JSONObject jsonObject = null;
                    try {
                        bodyString = new String(response.body().bytes());
                        jsonObject = new JSONObject(bodyString);
                        Log.e("response ", "messsagesssssssssss"+bodyString);
                        if (jsonObject.getString("response").equals("3")) {
                            serverResponseInterface.successdelete(bodyString,from,notificationID);
                        } else {
                            Log.e("response ", "messsage");
                            serverResponseInterface.failureCallBack(jsonObject.getString("message"));
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.e("nullll","jjddjnj"+response.code());
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                serverResponseInterface.failureCallBack(t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    // "Connection Timeout";
                    System.out.println("Connection Timeout");
                } else if (t instanceof IOException) {
                    // "Timeout";
                    System.out.println("Timeout");
                } else {
                    //Call was cancelled by user
                    if(call.isCanceled()) {
                        System.out.println("Call was cancelled forcefully");
                    } else {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    public void initializeServerInterface(ServerResponseInterface responseInterface) {
        serverResponseInterface = responseInterface;
    }

    public interface ServerResponseInterface {
        void successCallBack(String body,String from);
        void successdelete(String body,String from,String notificationID);
        //void successCallBack(JSONObject jsonObject, String opetation);
        void failureCallBack(String failureMsg);
    }

    public static class MessageEvent {
        public final String message;
        public final String body;
        public String from;

        MessageEvent(String message, String body, String from) {
            this.message = message;
            this.body = body;
            this.from = from;
        }

        public MessageEvent(String message, String bodyString) {
            this.message = message;
            this.body = bodyString;
        }

        public String getMessage() {
            return message;
        }

        public String getBody() {
            return body;
        }

        public String getFrom() {
            return from;
        }
    }
}
