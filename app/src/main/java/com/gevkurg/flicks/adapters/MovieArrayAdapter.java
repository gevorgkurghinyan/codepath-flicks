package com.gevkurg.flicks.adapters;


import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gevkurg.flicks.R;
import com.gevkurg.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static final int ROUNDED_CORNER_RADIUS = 20;
    private static final int ROUNDED_CORNER_MARGIN = 20;

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        ViewModel viewModel;
        if (convertView == null) {
            viewModel = new ViewModel();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);

            viewModel.movieImageView = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewModel.movieTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewModel.movieOverview = (TextView) convertView.findViewById(R.id.tvOverview);

            convertView.setTag(viewModel);
        } else {
            viewModel = (ViewModel) convertView.getTag();
        }

        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(getContext()).load(movie.getPosterPath())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.image_placeholder)
                    .transform(new RoundedCornersTransformation(ROUNDED_CORNER_RADIUS, ROUNDED_CORNER_MARGIN))
                    .into(viewModel.movieImageView);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.with(getContext()).load(movie.getBackdropPath())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.image_placeholder)
                    .transform(new RoundedCornersTransformation(ROUNDED_CORNER_RADIUS, ROUNDED_CORNER_MARGIN))
                    .into(viewModel.movieImageView);
        }

        viewModel.movieTitle.setText(movie.getOriginalTitle());
        viewModel.movieOverview.setText(movie.getOverview());

        return convertView;
    }

    // View Lookup cache
    private static class ViewModel {
        ImageView movieImageView;
        TextView movieTitle;
        TextView movieOverview;
    }
}
