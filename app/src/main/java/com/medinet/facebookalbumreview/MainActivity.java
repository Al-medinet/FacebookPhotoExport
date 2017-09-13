package com.medinet.facebookalbumreview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.medinet.facebookalbumreview.adapters.AlbumsAdapter;
import com.medinet.facebookalbumreview.models.AlbumModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mehdi on 12/09/2017.
 */

public class MainActivity extends AppCompatActivity {
    RecyclerView rv_Album;
    List<AlbumModel> albumModels;
    Toolbar toolbar;
    TextView counterText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        rv_Album= (RecyclerView) findViewById(R.id.rv_Gallery);
        rv_Album.setLayoutManager(new GridLayoutManager(this,2));
    }

    @Override
    protected void onStart() {
        super.onStart();
        String UserId= AccessToken.getCurrentAccessToken().getUserId();
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/"+UserId+"/albums",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            if (response.getError() == null) {
                                JSONObject data = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                if (data.has("data")) {
                                    JSONArray jaData = data.optJSONArray("data"); //find JSONArray from JSONObject
                                    albumModels=new ArrayList<>();
                                    for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                        JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                        albumModels.add(new AlbumModel(joAlbum.getString("id"),joAlbum.getString("name")));
                                    }
                                    AlbumsAdapter albumsAdapter=new AlbumsAdapter(MainActivity.this,albumModels);
                                    rv_Album.setAdapter(albumsAdapter);
                                }
                            } else {
                                Log.e("Erreur", response.getError().toString());
                            }
                        }catch (Exception ex){

                        }
                    }
                }).executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout){
            LoginManager.getInstance().logOut();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        return true;
    }

}
