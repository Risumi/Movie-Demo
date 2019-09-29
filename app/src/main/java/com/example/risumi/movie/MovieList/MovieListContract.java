package com.example.risumi.movie.MovieList;

import com.example.risumi.movie.Model.Movie;

import java.util.List;

public interface MovieListContract {
    interface Model{
        void getMovieList(int page, String query, int selectedFilter);

        void getFavoriteMovie();

        interface OnFinishedListener {
            void onFinished(List<Movie> movieArrayList,String sessionID);

            void onFailure(Throwable t);

        }
    }

    interface View{
        void showProgress();

        void dismissProgress();

        void setDataToRecyclerView(List<Movie> movieList, String sessionID);

        void onResponseFailure(Throwable throwable);

    }

    interface Presenter{

        void requestDataFromServer(int page, String query, int selectedFilter);

    }
}
