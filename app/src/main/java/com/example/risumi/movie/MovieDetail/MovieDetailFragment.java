package com.example.risumi.movie.MovieDetail;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import info.movito.themoviedbapi.TmdbApi;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.risumi.movie.Model.Movie;
import com.example.risumi.movie.MovieList.MovieListPresenter;
import com.example.risumi.movie.R;

public class MovieDetailFragment extends Fragment implements View.OnClickListener, MovieDetailContract.View{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Movie movie;
    private String sessionID;

    private ImageView imgFavorite;
    private ProgressDialog dialog;
    private MovieDetailPresenter movieDetailPresenter;
    private boolean fav = false;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(Movie param1, String param2) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(ARG_PARAM1);
            sessionID = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_movie_detail, container, false);
        initializeComponent(view);
        movieDetailPresenter = new MovieDetailPresenter(this);
        movieDetailPresenter.requestDataFromServer(sessionID,movie.getId());
        return view;
    }

    private void initializeComponent(View view){
        TextView txtJudul = view.findViewById(R.id.txtJudul);
        TextView txtRating = view.findViewById(R.id.txtRating);
        TextView txtRilis = view.findViewById(R.id.txtRilis);
        TextView txtKeterangan = view.findViewById(R.id.txtKeterangan);

        ImageView imgCover = view.findViewById(R.id.imgCover);
        ImageView imgPoster = view.findViewById(R.id.imgPoster);
        imgFavorite = view.findViewById(R.id.imgFavorite);
        imgFavorite.setOnClickListener(this);

        txtJudul.setText(movie.getTitle());
        txtRating.setText(movie.getVoteAverage().toString());
        txtRilis.setText(movie.getReleaseDate());
        txtKeterangan.setText(movie.getOverview());

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+movie.getBackdropPath())
                .apply(new RequestOptions().override(getResources().getDisplayMetrics().widthPixels, (int) (getResources().getDisplayMetrics().heightPixels* 0.4)))
                .into(imgCover);

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+movie.getPosterPath())
                .apply(new RequestOptions().override((int) (getResources().getDisplayMetrics().widthPixels* 0.35), (int) (getResources().getDisplayMetrics().heightPixels* 0.25)))
                .into(imgPoster);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
    }


    @Override
    public void onClick(View view) {
        movieDetailPresenter.requestFavorite(!fav);
    }

    @Override
    public void showProgress() {
        dialog.show();
    }

    @Override
    public void dismissProgress() {
        dialog.dismiss();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFavorite(boolean favorite,boolean click) {
        if (favorite) {
            imgFavorite.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorFavorite), android.graphics.PorterDuff.Mode.SRC_IN);
            fav=true;
        } else {
            imgFavorite.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_background), android.graphics.PorterDuff.Mode.SRC_IN);
            fav=false;
        }
        if (click ) {
            if (fav){
                Toast.makeText(getContext(), "Marked as Favorite", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "Removed from Favorite", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
