package com.gevkurg.flicks;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gevkurg.flicks.models.Movie;
import com.gevkurg.flicks.models.YoutubeTrailer;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MovieDetailActivity extends YouTubeBaseActivity {

    public static final String MOVIE_KEY = "movieData";
    private static final String YOUTUBE_API_KEY = "AIzaSyBzxUV0FWVoVa-7Fq1wHuvX5CxyhsniCR8";
    private static final int MAX_RATING = 10;

    private List<YoutubeTrailer> youtubeTrailers;

    private static final String MOVIE_TRAILER_URL = "https://api.themoviedb.org/3/movie/%s/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private YouTubePlayerView youTubePlayerView;
    private TextView tvTitle;
    private TextView tvOverView;
    private RatingBar rbMovie;
    private TextView tvRating;
    private TextView tvReleaseDate;
    private TextView tvVotersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        youTubePlayerView = findViewById(R.id.playerDetailMovie);
        tvTitle = findViewById(R.id.tvTitle);
        tvOverView = findViewById(R.id.tvOverview);
        rbMovie = findViewById(R.id.rbDetailMovie);
        tvRating = findViewById(R.id.tvRating);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvVotersCount = findViewById(R.id.tvVotersCount);

        youtubeTrailers = new ArrayList<>();
        populateUI();
    }

    private void populateUI() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        final Movie movie = getIntent().getParcelableExtra(MOVIE_KEY);
        tvTitle.setText(movie.getOriginalTitle());
        tvOverView.setText(movie.getOverview());
        rbMovie.setRating((float) (movie.getVoteAverage() / 2));
        tvRating.setText(String.format("Average Rating: %s/%s", movie.getVoteAverage(), MAX_RATING));
        tvReleaseDate.setText(String.format("Release date: %s", movie.getReleaseDate()));
        tvVotersCount.setText(String.format("Reviews Counted: %s", movie.getVoteCount()));

        httpClient.get(String.format(MOVIE_TRAILER_URL, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray trailerJsonResult = null;
                try {
                    trailerJsonResult = response.getJSONArray("youtube");
                    youtubeTrailers.addAll(YoutubeTrailer.fromJSONArray(trailerJsonResult));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final ArrayList<String> youtubeMovieIds = new ArrayList<>();
                for(YoutubeTrailer youtube : youtubeTrailers) {
                    youtubeMovieIds.add(youtube.getSource());
                }

                youTubePlayerView.initialize(YOUTUBE_API_KEY,
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                YouTubePlayer youTubePlayer, boolean b) {

                                youTubePlayer.cueVideos(youtubeMovieIds);
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                YouTubeInitializationResult youTubeInitializationResult) {

                            }
                        });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}

