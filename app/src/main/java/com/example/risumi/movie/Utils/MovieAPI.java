package com.example.risumi.movie.Utils;


import com.example.risumi.movie.Model.FavoriteRequest;
import com.example.risumi.movie.Model.LoginRequest;
import com.example.risumi.movie.Model.MovieResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MovieAPI {

    @GET("discover/movie")
    Observable<MovieResponse> getMovie(@Query("api_key")String apiKey,
                                       @Query("page")int page,
                                       @Query("sort_by")String sort,
                                       @Query("vote_count.gte")int vote_count);

    @GET("authentication/token/new")
    Observable<ResponseBody> getRequestToken(@Query("api_key")String apiKey);

    @POST("authentication/token/validate_with_login")
    Observable<ResponseBody> validate(@Query("api_key")String apiKey , @Body LoginRequest loginRequest);

    @POST("authentication/session/new")
    Observable<ResponseBody> createSession(@Query("api_key")String apiKey,@Body LoginRequest loginRequest);

    @GET("account/{account_id}/favorite/movies")
    Observable<MovieResponse> getFavoriteMovie(@Query("api_key")String apiKey,@Query("session_id")String sessionID);

    @POST("account/{account_id}/favorite")
    Observable<ResponseBody> setFavoriteMovie(@Query("api_key")String apiKey,@Query("session_id")String sessionID,@Body FavoriteRequest favoriteRequest);
}

