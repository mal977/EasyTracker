//package com.ntu.staizen.myapplication.api.request;
//
//import com.example.plantcare.api.ApiInterface;
//import com.example.plantcare.api.ApiResponseInterface;
//import com.example.plantcare.api.param.WeatherParam;
//import com.example.plantcare.api.response.weatherResponse.WeatherResponse;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class RequestExample extends Thread implements Callback<WeatherResponse> {
//    static final String TAG = RequestWeather.class.getSimpleName();
//
//    ApiResponseInterface<WeatherResponse> listener;
//    ApiInterface api;
//    WeatherParam param;
//
//    public RequestExample(ApiResponseInterface<WeatherResponse> listener, ApiInterface api, WeatherParam param) {
//        this.listener = listener;
//        this.api = api;
//        this.param = param;
//    }
//
//    @Override
//    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
//        WeatherResponse basicResponse = ((WeatherResponse) response.body());
//        listener.onResponse(call, basicResponse, false);
//    }
//
//    @Override
//    public void onFailure(Call<WeatherResponse> call, Throwable t) {
//        listener.onFailure(call, t);
//    }
//
//    public void run() {
//        Call<WeatherResponse> call;
//        call = api.getWeatherData(param.getDateTime());
//        call.enqueue(this);
//    }
//
//}