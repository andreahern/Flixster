package com.habraham.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.habraham.flixster.databinding.ActivityMovieDetailsBinding;
import com.habraham.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding binding;
    Movie movie;

    TextView tvTitle;
    TextView tvOverview;
    ImageView ivBackdrop;
    ImageView ivPlay;
    RatingBar rbVoteAverage;
    TextView tvReleased;

    @Override
    protected void onCreate(    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        rbVoteAverage = binding.rbVoteAverage;
        tvReleased = binding.tvReleased;
        ivBackdrop = binding.ivBackdrop;
        ivPlay = binding.ivPlay;

        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvReleased.setText(movie.getReleased());

        int width, height;
        Glide.with(this).load(movie.getBackdropPath()).placeholder(R.drawable.flicks_backdrop_placeholder).into(ivBackdrop);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) width = height = 175;
        else width = height = 100;
        Glide.with(this).load(R.drawable.play).override(width, height).into(ivPlay);

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        ivBackdrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                i.putExtra("id", movie.getId());
                MovieDetailsActivity.this.startActivity(i);
            }
        });
    }
}
