package com.example.plantcare.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.plantcare.api.response.humidityResponse.HumidityResponse;
import com.example.plantcare.api.response.plantListResponse.PlantListResponse;
import com.example.plantcare.api.response.plantResponse.PlantResponse;
import com.example.plantcare.api.response.rainfallResponse.RainFallResponse;
import com.example.plantcare.api.response.sunlightResponse.SunlightResponse;
import com.example.plantcare.api.response.weather24response.Weather24Response;
import com.example.plantcare.api.response.weatherResponse.WeatherResponse;

import java.util.List;

/**
 * Created by Malcom Teh on 26/2/2019.
 * Any question on how to use, ask Malcom
 */

public interface ApiInterface {
    @GET("https://trefle.io/api/plants")
    Call<List<PlantListResponse>> getPlantList
            (@Query("token") String token,
             @Query("q") String searchTerm);

    @GET("https://trefle.io/api/plants/{id}/")
    Call<PlantResponse> getPlantById
            (@Path("id") String id,
             @Query("token") String token);

    @GET("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast/")
    Call<WeatherResponse> getWeatherData
            (@Query("date_time") String dateTime);

    @GET("https://api.data.gov.sg/v1/environment/24-hour-weather-forecast/")
    Call<Weather24Response> get24WeatherData
            (@Query("date") String date);

    @GET("https://api.data.gov.sg/v1/environment/rainfall/")
    Call<RainFallResponse> getRainFallData
            (@Query("date_time") String date);

    // e.g. https://api.data.gov.sg/v1/environment/relative-humidity?date_time=2020-03-19T11:00:00
    @GET("https://api.data.gov.sg/v1/environment/relative-humidity/")
    Call<HumidityResponse> getHumidityData
    (@Query("date_time") String dateTime);

    // https://stackoverflow.com/questions/36730086/retrofit-2-url-query-parameter
    @GET("json")
    Call<SunlightResponse> getSunRiseSet
            (@Query("lat") String latitude,
             @Query("lng") String longitude,
             @Query("date") String date,
             @Query("formatted") int formatted);
}


//    public static final String PLANT_TOKEN = "Nk12U0ttU1I0U2RxWW4rcDRyU2NJQT09";
//    public static final String PLANT_URL = "https://trefle.io/api/plants";
//
//    public static final String WEATHER_URL = "https://api.data.gov.sg/v1/environment/2-hour-weather-forecast";
//
//    public static final String RAINFAILL_URL = "https://api.data.gov.sg/v1/environment/rainfall";
//
//    public static final String HUMIDITY_URL = "https://api.data.gov.sg/v1/environment/relative-humidity";
//
//    public static final String SUNLIGHT_URL = "https://api.sunrise-sunset.org/json";