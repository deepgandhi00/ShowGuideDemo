package com.example.deepgandhi.movieguidedemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.SimilarViewHolder>{
    List<moviePojo> similarShows;
    Context context;

    public SimilarAdapter(List<moviePojo> similarShows, Context context) {
        this.similarShows = similarShows;
        this.context = context;
    }

    @NonNull
    @Override
    public SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.similar_show_item,viewGroup,false);
        return new SimilarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarViewHolder similarViewHolder, int i) {
        moviePojo sm=similarShows.get(i);
        similarViewHolder.id=sm.getId();
        similarViewHolder.similar_name.setText(sm.getMovieName());
        Picasso.with(context).load(context.getResources().getString(R.string.image_url)+sm.getImageUrl()).into(similarViewHolder.similar_poster);
    }

    @Override
    public int getItemCount() {
        return similarShows.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    public class SimilarViewHolder extends RecyclerView.ViewHolder{
        int id;
        ImageView similar_poster;
        TextView similar_name;

        public SimilarViewHolder(@NonNull View itemView) {
            super(itemView);
            similar_poster=itemView.findViewById(R.id.similar_image);
            similar_name=itemView.findViewById(R.id.similar_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,ShowDetailActivity.class);
                    intent.putExtra("showId",id);
                    intent.putExtra("showName",similar_name.getText().toString());
                    context.startActivity(intent);
                }
            });
        }
    }
}
