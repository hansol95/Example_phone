package com.example.example_phone;

import android.icu.text.Collator;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit2Client {
    private static Retrofit2Client instance;
    private PhoneService phoneService;

    public Retrofit2Client(){
        Retrofit retrofit = new Retrofit.Builder()
                //내가 만든 spring의 서버
                .baseUrl("http://10.100.102.39:8899/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        phoneService = retrofit.create(PhoneService.class);
    }

    public static Retrofit2Client getInstance(){
        if(instance == null){
            instance = new Retrofit2Client();
        }
        return instance;
    }

    public PhoneService getPhoneService(){
        return phoneService;
    }
}
