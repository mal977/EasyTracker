//package com.ntu.staizen.myapplication.api.response;
//
//import android.util.Log;
//
//import com.google.gson.annotations.SerializedName;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExampleResponse extends BaseResponse {
//
//    private final String TAG = "WEATHER_RESPONSE_OBJ";
//
//    @SerializedName("area_metadata")
//    private List<WeatherAreaMetadata> areaMetaData;
//
//    @SerializedName("items")
//    private List<ResponseItem> items;
//
//    public List<WeatherAreaMetadata> getAreaMetaData() {
//        return areaMetaData;
//    }
//
//    public void setAreaMetaData(List<WeatherAreaMetadata> areaMetaData) {
//        this.areaMetaData = areaMetaData;
//    }
//
//    public ResponseItem getItem() {
//        return items.get(0);
//    }
//
//    public void setItem(ResponseItem item) {
//        List<ResponseItem> inputItemList = new ArrayList<ResponseItem>();
//        inputItemList.add(item);
//        this.items = inputItemList;
//    }
//
//    public ExampleResponse(List<WeatherAreaMetadata> areaMetaData, List<ResponseItem> items) {
//        this.areaMetaData = areaMetaData;
//        this.items = items;
//    }
//
//    public double[] getLatLong (String areaName) {
//        for (WeatherAreaMetadata metadata: this.areaMetaData) {
//            if (metadata.getStationName().equals(areaName)) {
//                double[] latLong = new double[2];
//                latLong[0] = metadata.getLabelLocation().getLatitude();
//                latLong[1] = metadata.getLabelLocation().getLongitude();
//                return latLong;
//            }
//        }
//
//        Log.e(TAG, "STATION ID DOES NOT EXIST");
//        return null;
//    }
//
//    public String getForecastByStationName(String areaName) {
//        for (WeatherForecast weatherForecast: this.items.get(0).getForecasts()) {
//            if (weatherForecast.getArea().equals(areaName)) {
//                return weatherForecast.getForecast();
//            }
//        }
//
//        Log.e(TAG, "STATION ID DOES NOT EXIST");
//        return null;
//    }
//
//    public boolean getRainPredictionByStationId(String areaName) {
//        for (WeatherForecast weatherForecast: this.items.get(0).getForecasts()) {
//            if (weatherForecast.getArea().equals(areaName)) {
//                if (weatherForecast.getForecast().toLowerCase().contains("rain")) {
//                    return true;
//                };
//            }
//        }
//        return false;
//    }
//}
