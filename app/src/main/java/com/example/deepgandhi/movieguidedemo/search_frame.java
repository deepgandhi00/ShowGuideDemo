package com.example.deepgandhi.movieguidedemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class search_frame extends Fragment {
    RecyclerView recyclerView;
    List<moviePojo> lst;
    MoviesAdapter adapter;
    GridLayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tv_show_list,container,false);
        String query=getArguments().getString("searched");
        recyclerView=v.findViewById(R.id.list_movies);
        lst=new ArrayList<>();
        adapter=new MoviesAdapter(lst,getActivity());
        manager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        MoviesList moviesList=new MoviesList();
        moviesList.execute(query);
        return v;
    }

    class MoviesList extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder response=new StringBuilder();
            try{
                URL url=new URL("https://api.themoviedb.org/3/search/tv?api_key=725d9fcd70a52e52d951ffe672bd7f2d&language=en-US&query="+strings[0]+"&page=1");
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
            }
            catch (Exception e){

            }
        }
    }
}
