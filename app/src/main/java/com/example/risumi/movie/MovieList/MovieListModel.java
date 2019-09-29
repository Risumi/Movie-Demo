package com.example.risumi.movie.MovieList;

import android.util.Log;

import com.example.risumi.movie.Model.LoginRequest;
import com.example.risumi.movie.Model.Movie;
import com.example.risumi.movie.Model.MovieResponse;
import com.example.risumi.movie.Utils.MovieAPI;
import com.example.risumi.movie.Utils.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MovieListModel implements MovieListContract.Model {
    private final static String  APIKey = "d3170d4e0bd99960e0773fdd77d2479a";
    private final static String  Username = "Klisel";
    private final static String  Password = "3LTyB35dMkhudM9";

    private String requestToken;
    private LoginRequest loginRequest;
    private String sessionID;
    private OnFinishedListener onFinishedListener;

    public MovieListModel(OnFinishedListener onFinishedListener) {
        this.onFinishedListener = onFinishedListener;
    }

    @Override
    public void getMovieList(int page,String query,int selectedFilter) {

        MovieAPI movieAPI = RetrofitClient.getInstance().create(MovieAPI.class);
        List<Movie> movies = new ArrayList<>();
        movieAPI.getMovie(APIKey, page, query, selectedFilter == 0 ? 5000 : 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MovieResponse response) {
                        movies.addAll(response.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {
                        onFinishedListener.onFailure(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        onFinishedListener.onFinished(movies, sessionID);
                    }
                });

    }

    @Override
    public void getFavoriteMovie() {
        if (sessionID==null){
            getRequestToken();
        }else {
            MovieAPI movieAPI = RetrofitClient.getInstance().create(MovieAPI.class);
            List<Movie> movies = new ArrayList<>();
            movieAPI.getFavoriteMovie(APIKey, sessionID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MovieResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                        @Override
                        public void onNext(MovieResponse response) {
                            movies.addAll(response.getResults());
                        }
                        @Override
                        public void onError(Throwable e) {
                            onFinishedListener.onFailure(e);
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            onFinishedListener.onFinished(movies,sessionID);
                        }
                    });
        }
    }


    private void getRequestToken(){
        MovieAPI movieAPI = RetrofitClient.getInstance().create(MovieAPI.class);

        movieAPI.getRequestToken(APIKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody.string());
                            requestToken = jsonObject.getString("request_token");
                            Log.d("request_token",requestToken);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            onFinishedListener.onFailure(e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onFinishedListener.onFailure(e);
                    }

                    @Override
                    public void onComplete() {
                        validate();
                    }
                });
    }

    private void validate(){
        MovieAPI movieAPI = RetrofitClient.getInstance().create(MovieAPI.class);
        loginRequest = new LoginRequest(Username,Password,requestToken);
        movieAPI.validate(APIKey,loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(ResponseBody responseBody) {

                }

                @Override
                public void onError(Throwable e) {
                    onFinishedListener.onFailure(e);
                }

                @Override
                public void onComplete() {
                    getSessionID();
                }
            });
    }

    private void getSessionID(){
        MovieAPI movieAPI = RetrofitClient.getInstance().create(MovieAPI.class);
        movieAPI.createSession(APIKey,loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        boolean success = jsonObject.getBoolean("success");
                        if (success){
                            sessionID = jsonObject.getString("session_id");
                            Log.d("Session",sessionID);
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        onFinishedListener.onFailure(e);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    onFinishedListener.onFailure(e);
                }

                @Override
                public void onComplete() {
                    getFavoriteMovie();
                }
            });
    }
}