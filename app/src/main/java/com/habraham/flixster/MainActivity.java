package com.habraham.flixster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.habraham.flixster.adapters.MovieAdapter;
import com.habraham.flixster.databinding.ActivityMainBinding;
import com.habraham.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public static final String TAG = "MainActivity";
    public RecyclerView rvMovies;
    Parcelable recyclerViewState;
    LinearLayoutManager llm;
    List<Movie> movies;
    MovieAdapter movieAdapter;
    int current;

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        rvMovies.setAdapter(movieAdapter);
        llm.scrollToPosition(current);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        final String NOW_PLAYING_URL = String.format("https://api.themoviedb.org/3/movie/now_playing?api_key=%s", getString(R.string.tmdb_api_key));

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        rvMovies = binding.rvMovies;
        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                current = llm.findFirstCompletelyVisibleItemPosition();
            }
        });

        movies = new ArrayList<>();

        movieAdapter = new MovieAdapter(this, movies);
        rvMovies.setAdapter(movieAdapter);
        llm = new LinearLayoutManager(this);
        rvMovies.setLayoutManager(llm);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

    }
}
