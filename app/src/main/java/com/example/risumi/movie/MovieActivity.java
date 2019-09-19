package com.example.risumi.movie;

import androidx.appcompat.app.AppCompatActivity;
import info.movito.themoviedbapi.model.MovieDb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class MovieActivity extends AppCompatActivity {
    TextView  txtJudul, txtRating, txtRilis, txtKeterangan;
    ImageView imgCover, imgPoster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        MovieDb movieDb = ((MovieDb) bundle.getSerializable("movie"));

        txtJudul = findViewById(R.id.txtJudul);
        txtRating = findViewById(R.id.txtRating);
        txtRilis= findViewById(R.id.txtRilis);
        txtKeterangan = findViewById(R.id.txtKeterangan);

        imgCover = findViewById(R.id.imgCover);
        imgPoster = findViewById(R.id.imgPoster);

        txtJudul.setText(movieDb.getTitle());
        txtRating.setText(((Float) movieDb.getVoteAverage()).toString());
        txtRilis.setText(movieDb.getReleaseDate());
        txtKeterangan.setText(movieDb.getOverview());

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+movieDb.getBackdropPath())
                .apply(new RequestOptions().override(getResources().getDisplayMetrics().widthPixels, 500))
                .into(imgCover);

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+movieDb.getPosterPath())
                .apply(new RequestOptions().override(200, 400))
                .into(imgPoster);
    }
}
