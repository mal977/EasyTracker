package com.example.plantcare.api;

import com.example.plantcare.api.response.BaseResponse;

import retrofit2.Call;

/**
 * Created by Malcom Teh on 26/2/2019.
 * Any question on how to use, ask Malcom
 */

public interface ApiResponseInterface<T> {

    void onResponse(Call<T> call, T response, boolean isLoading);
    void onFailure(Call<T> call, Throwable throwable);
    void onError(BaseResponse baseResponse);
}
