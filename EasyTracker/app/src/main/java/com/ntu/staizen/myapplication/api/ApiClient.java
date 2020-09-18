package com.ntu.staizen.myapplication.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Malcom Teh on 18/9/2020.
 * Any question on how to use, ask Malcom
 */

public class ApiClient implements Runnable{

    Retrofit retrofit;
    static ApiClient instance;
    static ApiInterface apiInterface;
    private static long delayTime;

    //Use this for app initiated api queries
    public static ApiInterface getApi() {
            if (instance == null) {
                instance = new ApiClient();
            }
            if (instance.retrofit == null) {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiConstants.baseURL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                apiInterface = retrofit.create(ApiInterface.class);


            return apiInterface;
        }else{
            return null;
        }
    }

    @Override
    public void run() {

    }
}
