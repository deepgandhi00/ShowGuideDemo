package com.example.deepgandhi.movieguidedemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ShowDetailActivity extends AppCompatActivity {
    TextView detail_name,summary,release_date,rating;
    ImageView detail_poster;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    boolean isLiked=false;
    Activity activity;
    boolean expandedActionBar=true;
    LinearLayout more_details;
    List<moviePojo> similars;
    SimilarAdapter adapter;
    LinearLayoutManager manager;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        appBarLayout=findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow=true;
            int scrollrange=-1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollrange == -1){
                    scrollrange=appBarLayout.getTotalScrollRange();
                }
                if(scrollrange + verticalOffset==0){
                    collapsingToolbarLayout.setTitle("Show Details");
                    isShow=true;
                }
                else if(isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow=false;
                }
            }
        });
        collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent i=getIntent();
        final int id=i.getIntExtra("showId",0);
        final String name=i.getStringExtra("showName");
        activity=getParent();
        detail_name=findViewById(R.id.detail_name);
        summary=findViewById(R.id.detail_summary);
        release_date=findViewById(R.id.detail_release_date);
        rating=findViewById(R.id.detail_rating);
        detail_poster=findViewById(R.id.detail_imageView);


        //recycler view setup
        recyclerView=findViewById(R.id.detail_similar_recycler_view);
        similars=new ArrayList<>();
        adapter=new SimilarAdapter(similars,this);
        manager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        LoadSimilar similar=new LoadSimilar();
        similar.execute(id+"");


        fab=findViewById(R.id.detial_like);
        more_details=findViewById(R.id.linear_moredetails);
        more_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShowDetailActivity.this, "Details Clicked", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(ShowDetailActivity.this,MoreDetailsActivity.class);
                i.putExtra("showId",id);
                i.putExtra("showName",name);
                startActivity(i);
            }
        });
        detail_name.setText(name);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(Math.abs(i)>200){
                    expandedActionBar=false;
                    //collapsingToolbarLayout.setTitle("Show Details");
                    invalidateOptionsMenu();
                }
                else{
                    expandedActionBar=true;
                    collapsingToolbarLayout.setTitle("");
                    invalidateOptionsMenu();
                }
            }
        });

        SharedPreferences preferences=getSharedPreferences("data",MODE_PRIVATE);
        final String userid=preferences.getString("id","");

        checkLike cl=new checkLike(fab);
        cl.execute(id+"",userid);

        getDetails gd=new getDetails();
        gd.execute(id+"");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLiked){
                    deleteLike dl=new deleteLike(fab);
                    dl.execute(id+"",userid);
                }
                else{
                    insertLike in=new insertLike(fab);
                    in.execute(id+"",userid);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    class getDetails extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder response=new StringBuilder();
            try{
                URL url=new URL(getResources().getString(R.string.details_url)+strings[0]+"?api_key="+getResources().getString(R.string.api_key));
                Log.i("urldetails",url.toString());
                URLConnection ucon=url.openConnection();
                InputStream inst=ucon.getInputStream();
                BufferedInputStream bis=new BufferedInputStream(inst);
                int data=0;
                while((data=bis.read())!=-1){
                    response.append((char)data);
                }
                bis.close();
                inst.close();
            }
            catch (Exception e){

            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String first_air_date=jsonObject.getString("first_air_date");
                release_date.setText("First Air : "+first_air_date);
                String Ssummary=jsonObject.getString("overview");
                summary.setText(Ssummary);
                String votes=jsonObject.getString("vote_average");
                rating.setText("Ratings : "+votes +"/10");
                String poster_path=jsonObject.getString("poster_path");
                Picasso.with(ShowDetailActivity.this).load(getResources().getString(R.string.image_url)+poster_path).into(detail_poster);
            }
            catch (Exception e){

            }
        }
    }
    class LoadImage extends AsyncTask<String,String,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp=null;
            try{
                URL url=new URL(getResources().getString(R.string.image_url)+strings[0]);
                InputStream in=url.openStream();
                bmp= BitmapFactory.decodeStream(in);
                in.close();
            }
            catch (Exception e){

            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            detail_poster.setImageBitmap(bitmap);
        }
    }
    class checkLike extends AsyncTask<String,String,String>{
        FloatingActionButton ft;

        public checkLike(FloatingActionButton ft) {
            this.ft = ft;
        }

        @Override
        protected String doInBackground(String... strings) {
            String response="";
            try {
                URL url = new URL(getResources().getString(R.string.check_like_url)+"show_id="+strings[0]+"&user_id="+strings[1]);
                URLConnection ucon = url.openConnection();
                InputStream ins = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(ins);
                int data = 0;
                while ((data = bis.read()) != -1) {
                    response=response+(char)data;
                }
                bis.close();
                ins.close();
            }
            catch (Exception e){}
            response=response.trim();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.charAt(0)=='1'){
                isLiked=true;
                ft.setImageDrawable(ContextCompat.getDrawable(ShowDetailActivity.this, R.drawable.ic_favorite_black_24dp));
            }
        }
    }
    class insertLike extends AsyncTask<String,String,String>{
        FloatingActionButton ft;

        public insertLike(FloatingActionButton ft) {
            this.ft = ft;
        }

        @Override
        protected String doInBackground(String... strings) {
            String response="";
            try {
                URL url = new URL(getResources().getString(R.string.insert_like_url)+"show_id="+strings[0]+"&user_id="+strings[1]);
                Log.i("insertlike",url.toString());
                URLConnection ucon = url.openConnection();
                InputStream ins = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(ins);
                int data = 0;
                while ((data = bis.read()) != -1) {
                    response=response+(char)data;
                }
                bis.close();
                ins.close();
            }
            catch (Exception e){}
            response=response.trim();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.charAt(0)=='1'){
                isLiked=true;
                ft.setImageDrawable(ContextCompat.getDrawable(ShowDetailActivity.this, R.drawable.ic_favorite_black_24dp));
            }
        }
    }
    class deleteLike extends AsyncTask<String,String,String>{
        FloatingActionButton ft;

        public deleteLike(FloatingActionButton ft) {
            this.ft = ft;
        }

        @Override
        protected String doInBackground(String... strings) {
            String response="";
            try {
                URL url = new URL(getResources().getString(R.string.delete_like_url)+"show_id="+strings[0]+"&user_id="+strings[1]);
                Log.i("deleteurl",url.toString());
                URLConnection ucon = url.openConnection();
                InputStream ins = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(ins);
                int data = 0;
                while ((data = bis.read()) != -1) {
                    response=response+(char)data;
                }
                bis.close();
                ins.close();
            }
            catch (Exception e){}
            response=response.trim();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.charAt(0)=='1'){
                ft.setImageDrawable(ContextCompat.getDrawable(ShowDetailActivity.this, R.drawable.ic_favorite_border_black_24dp));
                isLiked=false;
            }
        }
    }
    class LoadSimilar extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder response=new StringBuilder();
            try{
                URL u=new URL(getResources().getString(R.string.similar_url)+strings[0]+"/similar?api_key="+getResources().getString(R.string.api_key)+"&language=en-US&page=1");
                URLConnection ucon=u.openConnection();
                InputStream ins=ucon.getInputStream();
                BufferedInputStream bis=new BufferedInputStream(ins);
                int data=0;
                while((data=bis.read())!=-1){
                    response.append((char)data);
                }
            }
            catch (Exception e){

            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.i("responsejson",s);
                JSONObject jsonObject = new JSONObject(s);
                JSONArray array=jsonObject.optJSONArray("results");
                Log.i("totalitems",array.length()+"");
                for(int i=0;i<array.length();i++){
                    JSONObject object=array.getJSONObject(i);
                    similars.add(new moviePojo(object.getInt("id"),object.getString("original_name"),object.getString("poster_path")));
                }
                adapter.notifyDataSetChanged();
            }
            catch (Exception e){

            }
        }
    }
}
