package com.example.risumi.movie.MovieDetail;

import com.example.risumi.movie.Model.Movie;

import java.util.List;

public class MovieDetailPresenter implements MovieDetailContract.Presenter,MovieDetailContract.Model.OnFinishedListener {
    private MovieDetailContract.View movieDetailView;

    private MovieDetailContract.Model movieDetailModel;

    private int movieID;

    public MovieDetailPresenter(MovieDetailContract.View movieDetailView) {
        this.movieDetailView = movieDetailView;
        movieDetailModel = new MovieDetailModel(this);
    }

    @Override
    public void requestDataFromServer(String sessionID,int movieID) {
        this.movieID = movieID;
        if (movieDetailView != null) {
            movieDetailView.showProgress();
        }
        movieDetailModel.getFavoriteMovie(sessionID);
    }

    @Override
    public void onFinished(List<Movie> movieArrayList) {
        if (movieDetailView != null) {
            movieDetailView.dismissProgress();
        }
        for (int i=0;i<movieArrayList.size();i++){
            if (movieArrayList.get(i).getId()==movieID){
                movieDetailView.setFavorite(true,false);
                break;
            }else if (i==movieArrayList.size()-1){
                movieDetailView.setFavorite(false,false);
            }
        }
    }

    @Override
    public void setFavorite(boolean favorite) {
        if (movieDetailView != null) {
            movieDetailView.dismissProgress();
        }
        movieDetailView.setFavorite(favorite,true);
    }

    @Override
    public void requestFavorite(boolean favorite) {
        if (movieDetailView != null) {
            movieDetailView.showProgress();
        }
        movieDetailModel.setFavoriteMovie(movieID,favorite);
    }

    @Override
    public void onFailure(Throwable t) {
        movieDetailView.onResponseFailure(t);
        if (movieDetailView != null) {
            movieDetailView.dismissProgress();
        }
    }
}