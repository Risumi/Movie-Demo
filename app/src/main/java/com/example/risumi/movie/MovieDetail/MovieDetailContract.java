package com.example.risumi.movie.MovieDetail;

import com.example.risumi.movie.Model.Movie;

import java.util.List;

public interface MovieDetailContract {
    interface Model{
        void getFavoriteMovie(String sessionID);

        void setFavoriteMovie(int movieId, boolean favorite);
        interface OnFinishedListener {
            void onFinished(List<Movie> movieArrayList);

            void setFavorite(boolean favorite);

            void onFailure(Throwable t);

        }
    }

    interface View{
        void showProgress();

        void dismissProgress();

        void onResponseFailure(Throwable throwable);

        void setFavorite(boolean favorite, boolean click);

    }

    interface Presenter{

        void requestDataFromServer(String sessionID,int movieID);

        void requestFavorite(boolean favorite);

    }
}
