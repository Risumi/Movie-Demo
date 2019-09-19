package com.example.risumi.movie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.TokenAuthorisation;
import info.movito.themoviedbapi.model.core.AccountID;
import info.movito.themoviedbapi.model.core.SessionToken;

import android.os.Bundle;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    Button button;
    List<MovieDb> listMovie;
    SessionToken sessionToken;
    AccountID accountID;
    int page =0;
    TmdbApi tmdbApi;
    int selectedFilter =0 ;
    int selectedSort  =1 ;
    final static String  APIKey = "d3170d4e0bd99960e0773fdd77d2479a";
    final static String  Username = "Klisel";
    final static String  Password = "3LTyB35dMkhudM9";
    String filter [] = {".asc",".desc"};
    String sort [] = {"popularity","vote_count","favorite"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponent();
        showRecyclerGrid();
        checkInternetConnection();
    }

    public void initializeComponent(){
        listMovie = new ArrayList<>();
        mRecyclerView = findViewById(R.id.Recycler);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getAdapter() instanceof MovieAdapter){
                    int position = parent.getChildAdapterPosition(view);
                    int spanCount = 2;
                    int spacing = 10;
                    if (position >= 0) {
                        int column = position % spanCount;
                        outRect.left = spacing - column * spacing / spanCount;
                        outRect.right = (column + 1) * spacing / spanCount;
                        if (position < spanCount) {
                            outRect.top = spacing;
                        }
                        outRect.bottom = spacing;
                    }
                    else {
                        outRect.left = 0;
                        outRect.right = 0;
                        outRect.top = 0;
                        outRect.bottom = 0;
                    }
                }
            }
        });
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        checkInternetConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                Intent intent = new Intent(this,ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.filter:
                showAlertFilter();
                break;
            case R.id.sort:
                showAlertSort();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

//    public void showAlertError(final int lastVisibleItem){
//        new MaterialAlertDialogBuilder(this)
//                .setTitle("Connection Failed")
//                .setMessage("Please check your internet connection")
//                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        checkInternetConnection(lastVisibleItem,page);
//                    }
//                })
//                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                    }
//                })
//                .show();
//    }
//
//    public void showAlertPokedex(){
//        new MaterialAlertDialogBuilder(this)
//                .setSingleChoiceItems(pokedexes, selectedPokedex, null)
//                .setTitle("Set Pokédex")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                        listMovie.clear();
//                        selectedPokedex= ((AlertDialog)dialog).getListView().getCheckedItemPosition();
//                        checkInternetConnection(0,listPokedex.get(selectedPokedex).getUrl(),listLimit[selectedLimit]);
//                    }
//                })
//                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int i) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
//    }

    public void showAlertFilter(){
        new MaterialAlertDialogBuilder(this)
                .setTitle("Filter Movie by")
                .setSingleChoiceItems(R.array.filter, selectedFilter, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        listMovie.clear();
                        page = 0;
                        selectedFilter = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        checkInternetConnection();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void showAlertSort(){
        new MaterialAlertDialogBuilder(this)
                .setTitle("Sorting by")
                .setSingleChoiceItems(R.array.sort, selectedSort, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        listMovie.clear();
                        page = 0;
                        selectedSort = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        checkInternetConnection();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    public void showRecyclerGrid(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mAdapter = new MovieAdapter(listMovie);
        ((MovieAdapter)mAdapter).setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(MovieDb movie) {
                Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie",movie);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
//                Log.d("Object", ((Float) movie.getVoteAverage()).toString());
//                Toast.makeText(MainActivity.this, ((Float) movie.getUserRating()).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    public class StartAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        TokenAuthorisation token = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tmdbApi = new TmdbApi(APIKey);
            switch (selectedFilter){
                case 0:
                    if (selectedSort==0){
                        Discover discover = new Discover();
                        discover.sortBy("vote_average.asc");
                        discover.page(++page);
                        listMovie.addAll(tmdbApi.getDiscover().getDiscover(discover).getResults());
                    }else {
                        listMovie.addAll(tmdbApi.getMovies().getTopRatedMovies("en-US",++page).getResults());
                    }
                    break;
                case 1:
                    if (selectedSort==0){
                        Discover discover = new Discover();
                        discover.sortBy("popular.desc");
                        discover.page(++page);
                        listMovie.addAll(tmdbApi.getDiscover().getDiscover(discover).getResults());
                    }else {
                        listMovie.addAll(tmdbApi.getMovies().getPopularMovies("en-US",++page).getResults());
                    }
                    break;
                case 2:
                    token = tmdbApi.getAuthentication().getAuthorisationToken();
                    if (sessionToken==null || accountID ==null){
                        sessionToken = new SessionToken(tmdbApi.getAuthentication().getSessionLogin(Username,Password).getSessionId());
                        accountID = new AccountID(tmdbApi.getAccount().getAccount(sessionToken).getId());
                    }
                    listMovie.addAll(tmdbApi.getAccount().getFavoriteMovies(sessionToken,accountID).getResults());
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.cancel();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void checkInternetConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean connected = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if (connected){
            new StartAsyncTask().execute();
        }else {
//            showAlertError(lastVisibleItem);
        }
    }
}