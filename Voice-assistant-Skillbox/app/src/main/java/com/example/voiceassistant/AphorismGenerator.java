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
 * Returns a random aphorism.
 *
 * @author Daniil Ivanov
 * @version 1.0
 */
public class AphorismGenerator {


    public static class ApiResult {

        // Random aphorism.
        @SerializedName("quoteText")
        public String quoteText;
    }

    // Helps to work with Retrofit to let it know which queries we can perform.
    public interface AphorismService {
        @GET("/api/1.0/?method=getQuote&format=json")
        Call<ApiResult> getRandomAphorism(@Query("lang") String lang);
    }

    // Performs an aphorism query.
    public static void getAphorism(String lang, final Consumer<String> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.forismatic.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        // Does a query in background.
        Call<ApiResult> call = retrofit
                .create(AphorismService.class)
                .getRandomAphorism(lang);

        call.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {

                // Gets a body without technical information.
                ApiResult result = response.body();

                String answer;
                if (result != null) {
                    answer = "Как вам такая мысль: \"" + result.quoteText + "\"";
                } else {
                    answer = "Я слишком глуп для таких вещей";
                }

                callback.accept(answer);
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.w("AphorismGenerator", t.getMessage());
            }
        });

    }


}
