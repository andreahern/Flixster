package com.habraham.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.habraham.flixster.databinding.ActivityMovieTrailerBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {
    private ActivityMovieTrailerBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        binding = ActivityMovieTrailerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        final int movieId = getIntent().getIntExtra("id", 0);
        String movieLookupUrl = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s&language=en-US", movieId, getString(R.string.tmdb_api_key));
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(movieLookupUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                Log.d("MovieTrailerActivity", jsonObject.toString());
                try {
                    JSONArray result = jsonObject.getJSONArray("results");
                    Log.d("MovieTrailerActivity", result.toString());
                    final String videoId = result.getJSONObject(0).getString("key");
                    Log.d("MovieTrailerActivity", videoId);

                    YouTubePlayerView playerView = (YouTubePlayerView) binding.player;

                    // initialize with API key stored in secrets.xml
                    playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            youTubePlayer.cueVideo(videoId);
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {
                            Log.e("MovieTrailerActivity", "Error initializing YouTube player");
                        }
                    });

                } catch (JSONException e) {
                    Log.e("MovieTrailerActivity", "Error: " + e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("MovieTrailerActivity", "Failed");
            }
        });
    }
}
