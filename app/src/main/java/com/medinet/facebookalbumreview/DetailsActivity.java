package com.medinet.facebookalbumreview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.medinet.facebookalbumreview.adapters.PhotosAdapter;
import com.medinet.facebookalbumreview.models.ImageAlbumModel;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mehdi on 12/09/2017.
 */

public class DetailsActivity extends AppCompatActivity implements View.OnLongClickListener{

    public boolean is_action_mode=false;
    private TextView counterText;
    private Toolbar toolbar;
    private RecyclerView rv_Photo;
    private List<ImageAlbumModel> imageAlbumModels;
    private PhotosAdapter photosAdapter;
    private int counter=0;
    private String albumID,albumName;
    private List<ImageAlbumModel> selectedImage=new ArrayList<>();

    //Firebase Storage
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Album Details");
        rv_Photo= (RecyclerView) findViewById(R.id.rv_Photo);
        rv_Photo.setLayoutManager(new GridLayoutManager(this,2));
        rv_Photo.setHasFixedSize(true);
        mStorageRef= FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        albumID = getIntent().getStringExtra("albumID").toString();
        albumName = getIntent().getStringExtra("albumName").toString();

        loadImageUrl();
    }

    /**
     * Load Image Url Function
     */
    private void loadImageUrl(){
        Bundle bundle=new Bundle();
        bundle.putString("fields","images");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/"+albumID+"/photos",
                bundle,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            if (response.getError() == null) {
                                JSONObject data = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                if (data.has("data")) {
                                    JSONArray jaData = data.optJSONArray("data"); //find JSONArray from JSONObject
                                    imageAlbumModels=new ArrayList<>();
                                    for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                        JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                        JSONArray jaImages=joAlbum.getJSONArray("images");
                                        ImageAlbumModel albumModel=new ImageAlbumModel(jaImages.getJSONObject(0).getString("source"));
                                        imageAlbumModels.add(albumModel);
                                    }
                                    photosAdapter=new PhotosAdapter(DetailsActivity.this,imageAlbumModels);
                                    rv_Photo.setAdapter(photosAdapter);
                                }
                            } else {
                                Log.d("Erreur", response.getError().toString());
                            }
                        }catch (Exception ex){

                        }
                    }
                }).executeAsync();
    }



    @Override
    public boolean onLongClick(View v) {
        toolbar.inflateMenu(R.menu.menu_details);
        getSupportActionBar().setTitle("0 Selected Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        photosAdapter.notifyDataSetChanged();
        is_action_mode=true;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_save:
                for(ImageAlbumModel model:selectedImage){
                    new DownloadUrl(this,albumName).execute(model.getUrlImage());
                }
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        photosAdapter.notifyDataSetChanged();
        if(is_action_mode){
            toolbar.getMenu().clear();
            getSupportActionBar().setTitle("Album Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            is_action_mode=false;
        }else{
            super.onBackPressed();
        }
    }

    public void prepaseSelection(View v,int position){
        if(((CheckBox)v).isChecked()){
            selectedImage.add(imageAlbumModels.get(position));
            counter=counter+1;
            updateCounter(counter);
        }else{
            selectedImage.remove(imageAlbumModels.get(position));
            counter=counter-1;
            updateCounter(counter);
        }
    }
    public void updateCounter(int counter){
        getSupportActionBar().setTitle(counter+" Selected Items");
    }

}
