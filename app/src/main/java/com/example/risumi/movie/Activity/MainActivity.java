package com.example.risumi.movie.Activity;

import android.os.Bundle;

import com.example.risumi.movie.Model.Movie;
import com.example.risumi.movie.MovieDetail.MovieDetailFragment;
import com.example.risumi.movie.MovieList.MovieListFragment;
import com.example.risumi.movie.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements MovieListFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_container) != null) {
            MovieListFragment firstFragment = MovieListFragment.newInstance("","");
            firstFragment.setmListener(this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onItemClicked(Movie movie,String sessionID) {
        MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(movie,sessionID);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, movieDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}