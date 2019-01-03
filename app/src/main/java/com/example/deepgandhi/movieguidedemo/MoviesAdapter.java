package com.example.deepgandhi.movieguidedemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Random;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{
    List<moviePojo> movieList;
    Context context;
    int[] rainbow;

    public MoviesAdapter(List<moviePojo> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
        rainbow = context.getResources().getIntArray(R.array.rainbow);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_card,viewGroup,false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        moviePojo movie=movieList.get(i);
        //movieViewHolder.progressBar.setVisibility(View.VISIBLE);
        int rd=new Random().nextInt(5);
       // movieViewHolder.movie_view.setBackgroundColor(rainbow[rd]);
        movieViewHolder.movie_name.setText(movie.getMovieName());
        movieViewHolder.id=movie.getId();
        Picasso.with(context).load(context.getResources().getString(R.string.image_url)+movie.getImageUrl()).into(movieViewHolder.movie_image);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        ImageView movie_image;
        //View movie_view;
        TextView movie_name;
        //ProgressBar progressBar;
        int id;

        public MovieViewHolder(@NonNull final View itemView) {
            super(itemView);
            movie_image=itemView.findViewById(R.id.card_imageView);
           // movie_view=itemView.findViewById(R.id.card_view);
            movie_name=itemView.findViewById(R.id.card_movieName);
            //progressBar=itemView.findViewById(R.id.card_progressBar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "id : "+id+"Name : "+movie_name.getText(), Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(context,ShowDetailActivity.class);
                    i.putExtra("showId",id);
                    i.putExtra("showName",movie_name.getText().toString());
                    context.startActivity(i);
                }
            });
        }
    }
}
