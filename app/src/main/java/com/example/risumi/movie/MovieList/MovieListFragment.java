package com.example.risumi.movie.MovieList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.risumi.movie.Model.Movie;
import com.example.risumi.movie.R;
import com.example.risumi.movie.Activity.ProfileActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements View.OnClickListener, MovieListContract.View {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String sessionID;
    private Button button;
    private List<Movie> listMovie;
    private ProgressDialog dialog;
    private int page =1;
    private int selectedFilter =0 ;
    private int selectedSort  =1 ;
    private MovieListPresenter movieListPresenter;
    private String query = "vote_average.desc";
    private String[] sort = {".asc", ".desc"};
    private String[] filter = {"vote_average", "popularity", "favorite"};

    private OnFragmentInteractionListener mListener;

    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance(String param1, String param2) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_movie, container, false);
        initializeComponent(view);
        page =1;
        setButton();
        movieListPresenter = new MovieListPresenter(this);
        Log.d("Query",query);
        Log.d("Filter", ((Integer) selectedFilter).toString());
        movieListPresenter.requestDataFromServer(page,query,selectedFilter);

        showRecyclerGrid();

        return view;
    }

    public void initializeComponent(View view){
        listMovie = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.Recycler);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getAdapter() instanceof MovieListAdapter){
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
        button = view.findViewById(R.id.button);
        button.setOnClickListener(this);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

    }

    public void showRecyclerGrid(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mAdapter = new MovieListAdapter(listMovie);
        ((MovieListAdapter)mAdapter).setOnItemClickCallback(new MovieListAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(int position) {
                mListener.onItemClicked(listMovie.get(position),sessionID);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    public void showAlertFilter(){
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Filter Movie by")
                .setSingleChoiceItems(R.array.filter, selectedFilter, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        listMovie.clear();
                        page =1;
                        selectedFilter = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        setQuery();
                        setButton();
                        movieListPresenter.requestDataFromServer(page,query,selectedFilter);
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
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Sorting by")
                .setSingleChoiceItems(R.array.sort, selectedSort, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        listMovie.clear();
                        page = 1;
                        selectedSort = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        setQuery();
                        setButton();
                        movieListPresenter.requestDataFromServer(page,query,selectedFilter);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    void setButton(){
        if (selectedFilter==2){
            button.setVisibility(View.GONE);
        }else {
            button.setVisibility(View.VISIBLE);
        }
    }


    void setQuery(){
        query = filter[selectedFilter]+sort[selectedSort];
    }

    public void setmListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                Intent intent = new Intent(getContext(), ProfileActivity.class);
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
    public void onClick(View view) {
        movieListPresenter.requestDataFromServer(page,query,selectedFilter);
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
    public void setDataToRecyclerView(List<Movie> movieList, String sessionID) {
        listMovie.addAll(movieList);
        mAdapter.notifyDataSetChanged();
        page++;
        this.sessionID = sessionID;
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(getContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        void onItemClicked(Movie movie,String sessionID);
    }
}
