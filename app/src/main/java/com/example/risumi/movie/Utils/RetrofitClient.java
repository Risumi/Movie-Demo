package com.example.risumi.movie.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit ourInstance ;
    public static final String BASE_URL = "http://api.themoviedb.org/3/";


//    private static Converter.Factory createGsonConverter(Type type, Object typeAdapter) {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(type, typeAdapter);
//        Gson gson = gsonBuilder.create();
//        return GsonConverterFactory.create(gson);
//    }

    public static synchronized  Retrofit getInstance()  {
        if (ourInstance==null){
            ourInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return ourInstance;
    }

    private RetrofitClient() {
    }
}
