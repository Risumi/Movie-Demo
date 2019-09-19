package com.example.risumi.movie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.card.MaterialCardView;

import androidx.recyclerview.widget.RecyclerView;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    List<MovieDb> movieArrayList;
    MovieDb current;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }


    public MovieAdapter(List<MovieDb> movieArrayList) {
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
                .apply(new RequestOptions().override(400, 500))
                .placeholder(R.drawable.noimg)
                .dontAnimate()
                .into(holder.movieImage);
//        holder.PokemonIndex.setText(current.getIndexPokedex());
//        holder.PokemonName.setText(current.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(movieArrayList.get(position));
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
        void onItemClicked(MovieDb movie);
    }
}
