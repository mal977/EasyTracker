package com.ntu.staizen.EasyTracker.api;


import com.ntu.staizen.EasyTracker.api.response.BaseResponse;

import retrofit2.Call;

/**
 * Created by Malcom Teh on 18/9/2020.
 * Any question on how to use, ask Malcom
 */

public interface ApiResponseInterface<T> {

    void onResponse(Call<T> call, T response, boolean isLoading);
    void onFailure(Call<T> call, Throwable throwable);
    void onError(BaseResponse baseResponse);
}
