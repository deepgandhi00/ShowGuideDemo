package com.example.deepgandhi.movieguidedemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class main_frame extends Fragment {
    RecyclerView recyclerView;
    List<moviePojo> lst;
    MoviesAdapter adapter;
    boolean isScrolling=false;
    GridLayoutManager manager;
    int current_items,total_items,scrolled_out,counter=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tv_show_list,container,false);
        recyclerView=v.findViewById(R.id.list_movies);
        lst=new ArrayList<>();
        adapter=new MoviesAdapter(lst,getActivity());
        manager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        MoviesList moviesList=new MoviesList();
        moviesList.execute();

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                   isScrolling=true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                current_items=manager.getChildCount();
                scrolled_out=manager.findFirstVisibleItemPosition();
                total_items=manager.getItemCount();
                if(isScrolling && current_items+scrolled_out==total_items){
                    isScrolling=false;
                    fetchData();
                }
            }
        });
        return v;
    }

    private void fetchData() {
        Snackbar.make(getView(),"Fetching more data...",Snackbar.LENGTH_SHORT).show();
        MoviesList moviesList=new MoviesList();
        moviesList.execute();
    }

    class MoviesList extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder response=new StringBuilder();
            try{
                URL url=new URL(getResources().getString(R.string.popular_url)+getResources().getString(R.string.api_key)+"&language=en-US&page="+counter);
                Log.i("urlmovies",url.toString());
                URLConnection ucon=url.openConnection();
                InputStream ins = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(ins);
                int data=0;
                while((data=bis.read())!=-1){
                    response.append((char)data);
                }
                bis.close();
                ins.close();
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
                    lst.add(new moviePojo(object.getInt("id"),object.getString("original_name"),object.getString("poster_path")));
                }
                adapter.notifyDataSetChanged();
                counter++;
            }
            catch (Exception e){

            }
        }
    }
}
