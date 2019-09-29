package com.example.risumi.movie.MovieDetail;

import android.util.Log;

import com.example.risumi.movie.Model.FavoriteRequest;
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

public class MovieDetailModel implements MovieDetailContract.Model {

    private OnFinishedListener onFinishedListener;
    private final static String  APIKey = "d3170d4e0bd99960e0773fdd77d2479a";
    private final static String  Username = "Klisel";
    private final static String  Password = "3LTyB35dMkhudM9";

    private String requestToken;
    private LoginRequest loginRequest;
    private String sessionID;

    public MovieDetailModel(OnFinishedListener onFinishedListener) {
        this.onFinishedListener = onFinishedListener;
    }
    @Override
    public void getFavoriteMovie(String sessionID) {
        if (this.sessionID==null){
            getRequestToken();
        }else {
            List<Movie> movies = new ArrayList<>();
            MovieAPI movieAPI = RetrofitClient.getInstance().create(MovieAPI.class);
            movieAPI.getFavoriteMovie(APIKey,sessionID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MovieResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(MovieResponse movieResponse) {
                            movies.addAll(movieResponse.getResults());
                        }

                        @Override
                        public void onError(Throwable e) {
                            onFinishedListener.onFailure(e);
                        }

                        @Override
                        public void onComplete() {
                            onFinishedListener.onFinished(movies);
                        }
                    });
        }
    }

    @Override
    public void setFavoriteMovie(int movieId, boolean favorite) {
        MovieAPI movieAPI = RetrofitClient.getInstance().create(MovieAPI.class);
        FavoriteRequest favoriteRequest = new FavoriteRequest("movie",movieId,favorite);
        movieAPI.setFavoriteMovie(APIKey,sessionID,favoriteRequest)
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        onFinishedListener.setFavorite(favorite);
                    }
                });
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
                        getFavoriteMovie(sessionID);
                    }
                });
    }
}
