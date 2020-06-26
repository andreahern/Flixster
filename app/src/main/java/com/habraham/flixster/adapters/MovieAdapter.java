package com.habraham.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.habraham.flixster.MovieDetailsActivity;
import com.habraham.flixster.R;
import com.habraham.flixster.databinding.ActivityMainBinding;
import com.habraham.flixster.databinding.ItemMovieBinding;
import com.habraham.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ItemMovieBinding binding;

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = ItemMovieBinding.inflate(inflater, parent, false);
        View movieView = binding.getRoot();
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder: " + position);
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvOverView;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = binding.tvTitle;
            tvOverView = binding.tvOverview;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                image = binding.ivBackdrop;
            else image = binding.ivPoster;

            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverView.setText(movie.getOverview());
            String imgUrl;
            int placeholder;

            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imgUrl = movie.getBackdropPath();
                placeholder = R.drawable.flicks_backdrop_placeholder;
            } else {
                imgUrl = movie.getPosterPath();
                placeholder = R.drawable.flicks_movie_placeholder;
            }

            Glide.with(context).load(imgUrl).placeholder(placeholder).transform(new RoundedCornersTransformation(30, 10)).into(image);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);
                Intent i = new Intent(context, MovieDetailsActivity.class);
                i.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                context.startActivity(i);
            }
        }
    }

}
