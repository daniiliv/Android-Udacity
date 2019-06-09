package com.example.voiceassistant;

import android.support.v4.util.Consumer;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Artificial Intelligence that answers some questions.
 *
 * It can: <b>answer any question from database</b>, <b>tell the current time</b>,
 * <b>give information about weather in city chosen</b>, <b>tell a random aphorism</b>,
 * <b>translate words</b>.
 *
 *
 * @author Daniil Ivanov
 * @version 1.0
 */
public class AI {

    /**
     * Answers user's question.
     *
     * Returns list of possible answers separated with comma via callback.
     *
     * @param userQuestion question that user asked.
     */
    public static void getAnswer(String userQuestion, final Consumer<String> callback) {

        // Time and date formatting.
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault());

        // Date object initialized with the current date / time.
        Date date = new Date();

        // Current time and date as String.
        final String currentTime = timeFormat.format(date);
        final String currentDate = dateFormat.format(date);

        // This map contains key: question, value: answer.
        Map<String, String> dataBase = new HashMap<String, String>() {{
            put("привет", "Приветствую");
            put("как дела", "Как земля");
            put("чем занимаешься", "Отвечаю кожаному существу");
            put("есть ли жизнь на марсе", "Наука не знает точного ответа на этот вопрос");
            put("кто президент россии", "Порядочный человек");
            put("какого цвета небо", "Я думаю, небо имеет фирменный цвет компании Skillbox");
            put("какой сегодня день", "Сегодня " + currentDate);
            put("сколько сейчас времени", "Время - деньги. Сейчас " + currentTime);
        }};

        // String, converted to lowercase. It helps to search in our database.
        userQuestion = userQuestion.toLowerCase();

        // List of answers found.
        final List<String> answers = new ArrayList<>();

        int maxScore = 0;
        String maxScoreAnswer = "OK";
        String[] split_user = userQuestion.split("\\s+");


        // Smart searching in database.
        for (String databaseQuestion : dataBase.keySet()) {
            databaseQuestion = databaseQuestion.toLowerCase();
            String[] splitDb = databaseQuestion.split("\\s+");
            int score = 0;
            for (String wordUser : split_user) {
                for (String wordDb : splitDb) {
                    int min_len = Math.min(wordDb.length(), wordUser.length());
                    int cutLen = (int) (min_len * 0.7);
                    String wordUserCut = wordUser.substring(0, cutLen);
                    String wordDbCut = wordDb.substring(0, cutLen);
                    if (wordUserCut.equals(wordDbCut)) {
                        score++;
                    }
                }
            }
            if (score > maxScore) {
                maxScore = score;
                maxScoreAnswer = dataBase.get(databaseQuestion);
            }
        }

        if (maxScore > 0) {
            answers.add(maxScoreAnswer);
        }

        // Extracts city from user's input for weather query.
        Pattern cityPattern = Pattern.compile("какая погода в городе (\\p{L}+)", Pattern.CASE_INSENSITIVE);
        Matcher cityMatcher = cityPattern.matcher(userQuestion);

        // If city is found, then perform a weather query.
        if (cityMatcher.find()) {
            answers.add("Сейчас узнаю...");

            String cityName = cityMatcher.group(1);

            // Consumer receives our answer.
            Weather.getWeather(cityName, new Consumer<String>() {
                @Override
                public void accept(String weatherForecast) {
                    answers.add(weatherForecast);
                    callback.accept(weatherForecast);
                }
            });
        }

        // If user wants a random aphorism.
        if (userQuestion.contains("расскажи афоризм")) {

            // Set language preference.
            String lang;
            if (userQuestion.contains("на английском")) {
                lang = "en";
            } else {
                lang = "ru";
            }

            answers.add("Дайте-ка подумать...");
            AphorismGenerator.getAphorism(lang, new Consumer<String>() {
                @Override
                public void accept(String randomAphorism) {
                    answers.add(randomAphorism);
                    callback.accept(randomAphorism);
                }
            });
        }

        // Extracts word from user's input for translation query.
        Pattern wordPattern = Pattern.compile("переведи (\\p{L}+)", Pattern.CASE_INSENSITIVE);
        Matcher wordMatcher = wordPattern.matcher(userQuestion);

        // If word is found, then perform a translation query.
        if (wordMatcher.find()) {
            answers.add("Перевожу с английского...");

            String wordToTranslate = wordMatcher.group(1);

            // Consumer receives our answer.
            Translator.getTranslation(wordToTranslate, new Consumer<String>() {
                @Override
                public void accept(String translatedWord) {
                    answers.add(translatedWord);
                    callback.accept(translatedWord);
                }
            });
        }

        // If there are no answers found, add answer OK.
        if (answers.isEmpty()) {
            answers.add("OK");
        }

        // Asynchronously return our answer.
        callback.accept(TextUtils.join(", ", answers));
    }
}
