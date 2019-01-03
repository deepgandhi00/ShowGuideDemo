package com.example.deepgandhi.movieguidedemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class MoreDetailsActivity extends AppCompatActivity {
    TextView tv_created_by,tv_episode_runtime,tv_network,tv_total_episode,tv_total_seasons,tv_genre;
    ExpandableLayout exlayout;
    Toolbar more_toolbar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        Intent intent=getIntent();
        final int id=intent.getIntExtra("showId",0);
        final String name=intent.getStringExtra("showName");

        LoadSeasonsDeatils deatils=new LoadSeasonsDeatils();
        deatils.execute(id+"");
        exlayout=findViewById(R.id.more_expandable);
        tv_created_by=findViewById(R.id.more_creator);
        tv_episode_runtime=findViewById(R.id.more_runtime);
        tv_network=findViewById(R.id.more_networks);
        tv_total_episode=findViewById(R.id.more_episode);
        tv_total_seasons=findViewById(R.id.more_seasons);
        tv_genre=findViewById(R.id.more_genre);
        more_toolbar=findViewById(R.id.moredetails_toolbar);


        setSupportActionBar(more_toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.getSupportActionBar().setTitle(name);

        exlayout.setRenderer(new ExpandableLayout.Renderer<parentPojo,childPojo>(){

            @Override
            public void renderParent(View view, parentPojo parentPojo, boolean isExpanded, int parentPosition) {
                //TextView tv_seasons=view.findViewById(R.id.parent_tv);
                ((ImageView)view.findViewById(R.id.parent_imgv)).setBackgroundResource(isExpanded?R.drawable.ic_keyboard_arrow_up_black_24dp:R.drawable.ic_keyboard_arrow_down_black_24dp);
            }

            @Override
            public void renderChild(View view, childPojo childPojo, int i, int i1) {
                TextView tv_no=view.findViewById(R.id.child_no);
                TextView tv_name=view.findViewById(R.id.child_name);
                TextView tv_no_episodes=view.findViewById(R.id.child_episode);
                TextView tv_air=view.findViewById(R.id.child_air);
                tv_no.setText("Season NO : "+childPojo.getSeason_no());
                tv_name.setText("Seasson Name : "+childPojo.getSeason_name());
                tv_no_episodes.setText("No of Episode In Season : "+childPojo.getNo_of_episode());
                tv_air.setText("First Episode Of season air date : "+childPojo.getAir_date());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    class LoadSeasonsDeatils extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder response=new StringBuilder();
            try {
                URL url = new URL(getResources().getString(R.string.more_details_url)+strings[0]+"?api_key="+getResources().getString(R.string.api_key));
                URLConnection ucon=url.openConnection();
                BufferedInputStream bis=new BufferedInputStream(ucon.getInputStream());
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
            super.onPostExecute(s);
            try {
                String creators="",runtime="",networks="",genre="";
                int no_of_episodes=0,no_of_seasons=0;
                JSONObject jsonObject=new JSONObject(s);
                Section<parentPojo, childPojo> section = new Section<>();
                parentPojo parent = new parentPojo("Seasons Details");
                List<childPojo> lst = new ArrayList<>();
                JSONArray creators_array=jsonObject.getJSONArray("created_by");
                JSONArray runtime_array=jsonObject.getJSONArray("episode_run_time");
                JSONArray networks_array=jsonObject.getJSONArray("networks");
                JSONArray genre_array=jsonObject.getJSONArray("genres");
                no_of_episodes=jsonObject.getInt("number_of_episodes");
                no_of_seasons=jsonObject.getInt("number_of_seasons");

                for(int i=0;i<runtime_array.length();i++){
                    int runTimeSingle=runtime_array.getInt(i);
                    runtime=runtime+(runTimeSingle+"")+",";
                }

                for(int i=0;i<networks_array.length();i++){
                    JSONObject singleNetwork=networks_array.getJSONObject(i);
                    networks=networks+singleNetwork.getString("name")+",";
                }

                for(int i=0;i<genre_array.length();i++){
                    JSONObject singleGenre=genre_array.getJSONObject(i);
                    genre=genre+singleGenre.getString("name")+",";
                }

                for(int i=0;i<creators_array.length();i++){
                    JSONObject singleCreator=creators_array.getJSONObject(i);
                    creators=creators+singleCreator.getString("name")+",";
                }
                JSONArray array=jsonObject.optJSONArray("seasons");
                for (int i=0;i<array.length();i++){
                    JSONObject object=array.getJSONObject(i);
                    lst.add(new childPojo(object.getString("season_number"),object.getString("name"),object.getString("episode_count"),object.getString("air_date")));
                }
                section.parent=parent;
                section.children.addAll(lst);
                exlayout.addSection(section);

                tv_created_by.setText("Created by : "+creators);
                tv_episode_runtime.setText("Episode Runtime : "+runtime);
                tv_genre.setText("Genre : "+genre);
                tv_network.setText("Networks : "+networks);
                tv_total_episode.setText("Total number of Episodes : "+no_of_episodes+"");
                tv_total_seasons.setText("Total number of seasons : "+no_of_seasons);
            }
            catch (Exception e){}
        }
    }
}
