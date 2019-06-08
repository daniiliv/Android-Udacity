package com.example.voiceassistant;

import android.support.v4.util.Consumer;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Translates one word inputted by user from English to Russian and returns answer to user.
 *
 * @author Daniil Ivanov
 * @version 1.0
 */
public class Translator {

    public static class ApiResult {

        // Translated text.
        @SerializedName("text")
        public List<String> text;
    }

    // Helps to work with Retrofit to let it know which queries we can perform.
    public interface TranslatorService {
        @GET("/api/v1.5/tr.json/translate?key=trnsl.1.1.20190607T203353Z.702ef50f34468eb6.667ae74ce7426e5b5a4e780c8734a5f8f48b01eb")
        Call<ApiResult> getTextTranslation(@Query("text") String textToTranslate, @Query("lang") String lang);
    }

    // Performs a translation query depending on inputted word.
    public static void getTranslation(String wordToTranslate, final Consumer<String> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        // Does a query in background.
        Call<ApiResult> call = retrofit
                .create(TranslatorService.class)
                .getTextTranslation(wordToTranslate, "en-ru");

        call.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {

                // Gets a body without technical information.
                ApiResult result = response.body();

                String answer;
                if (result != null) {
                    answer = "Перевод на русский: " + result.text.get(0);
                } else {
                    answer = "Ошибка перевода :(";
                }

                callback.accept(answer);
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.w("TRANSLATOR", t.getMessage());
            }
        });

    }
}
