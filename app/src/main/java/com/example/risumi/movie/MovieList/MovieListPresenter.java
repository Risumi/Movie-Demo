package com.example.risumi.movie.MovieList;

import com.example.risumi.movie.Model.Movie;

import java.util.List;

public class MovieListPresenter implements MovieListContract.Presenter,MovieListContract.Model.OnFinishedListener {

    private MovieListContract.View movieListView;

    private MovieListContract.Model movieModel;

    public MovieListPresenter(MovieListContract.View movieListView) {
        this.movieListView = movieListView;
        movieModel = new MovieListModel(this);
    }
//
//    @Override
//    public void onDestroy() {
//        this.movieListView = null;
//    }

    @Override
    public void requestDataFromServer(int page, String query, int selectedFilter) {
        if (movieListView != null) {
            movieListView.showProgress();
        }
        if (selectedFilter==2){
            movieModel.getFavoriteMovie();
        }else {
            movieModel.getMovieList( page,query,selectedFilter);
        }
    }


    @Override
    public void onFinished(List<Movie> movieArrayList,String sessionID) {
        movieListView.setDataToRecyclerView(movieArrayList,sessionID);
        if (movieListView != null) {
            movieListView.dismissProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {

        movieListView.onResponseFailure(t);
        if (movieListView != null) {
            movieListView.dismissProgress();
        }
    }
}
