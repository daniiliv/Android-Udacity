package com.example.voiceassistant;

import android.support.v4.util.Consumer;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Performs a weather forecast query and returns answer to user.
 *
 * @author Daniil Ivanov
 * @version 1.0
 */
public class Weather {

    public static class Condition {

        // Weather condition text description.
        @SerializedName("text")
        public String text;
    }

    public static class Forecast {

        // Temperature in celsius degrees.
        @SerializedName("temp_c")
        public Float temperature;

        // Weather condition.
        @SerializedName("condition")
        public Condition condition;
    }

    public static class ApiResult {

        // Forecast result.
        @SerializedName("current")
        public Forecast current;
    }

    // Helps to work with Retrofit to let it know which queries we can perform.
    public interface WeatherService {
        @GET("/v1/current.json?key=ead51d93931240d58a0155627190706")
        Call<ApiResult> getCurrentWeather(@Query("q") String city, @Query("lang") String lang);
    }

    // Performs a weather query depending on city.
    public static void getWeather(String city, final Consumer<String> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.apixu.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        // Does a query in background.
        Call<ApiResult> call = retrofit
                .create(WeatherService.class)
                .getCurrentWeather(city, "ru");

        call.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {

                // Gets a body without technical information.
                ApiResult result = response.body();

                String answer;
                if (result != null) {
                    answer = "Там сейчас " + result.current.condition.text +
                            ", где-то " + result.current.temperature.intValue() +
                            " градусов";
                } else {
                    answer = "Такого города нет";
                }

                callback.accept(answer);
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.w("WEATHER", t.getMessage());
            }
        });

    }
}
