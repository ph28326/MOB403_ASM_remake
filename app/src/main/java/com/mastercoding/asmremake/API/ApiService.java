package com.mastercoding.asmremake.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mastercoding.asmremake.Model.Phone;
import com.mastercoding.asmremake.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    // ip may ao connect localhost: 10.0.2.2
    // genymontion: 10.0.3.2
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("listPhone")
    Call<List<Phone>> getPhone();

    @POST("addPhones")
    Call<List<Phone>> addPhone(@Body Phone phone);

    @PUT("phones/{id}")
    Call<Phone> updatePhone(@Path("id") String id, @Body Phone phone);

    @DELETE("/phones/{id}")
    Call<Void> deletePhones(@Path("id") String id);

    @POST("registerNewUser")
    Call<List<User>> registerUser(@Body User user);

    @POST("login")
    Call<List<User>> loginUser(@Body User user);

}
