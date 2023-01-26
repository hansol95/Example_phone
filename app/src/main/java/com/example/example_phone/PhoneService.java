package com.example.example_phone;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PhoneService {
    @GET("list")
    Call<List<Phone>> findAll();
    //list는 스프링이랑 url이 동일해야 이동가능

    @POST("insert")
    Call<Phone> save(@Body Phone phoneDto);

    @PUT("update/{id}")
    Call<Phone> update(@Path("id") Long id, @Body Phone phoneDto);

    @DELETE("delete/{id}")
    Call<Void> deleteById(@Path("id")Long id);

}
