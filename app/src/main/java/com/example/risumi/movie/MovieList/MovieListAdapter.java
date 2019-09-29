package com.example.risumi.movie.MovieList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.risumi.movie.Model.Movie;
import com.example.risumi.movie.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    List<Movie> movieArrayList;
    Movie current;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }


    public MovieListAdapter(List<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movies,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        current = movieArrayList.get(position);
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w500"+current.getPosterPath())
                .apply(new RequestOptions().override(600, 800))
                .placeholder(R.drawable.noimg)
                .dontAnimate()
                .into(holder.movieImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;
//        MaterialCardView cardView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.imgMovies);
//            cardView = itemView.findViewById(R.id.cardMovies);
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(int position);
    }
}
