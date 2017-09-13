package com.medinet.facebookalbumreview.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medinet.facebookalbumreview.DetailsActivity;
import com.medinet.facebookalbumreview.models.AlbumModel;
import com.medinet.facebookalbumreview.R;
import java.util.List;

/**
 * Created by Mehdi on 12/09/2017.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {


    private Context mContext;
    private List<AlbumModel> albumModels;
    public AlbumsAdapter(Context mContext, List<AlbumModel> albumModels){
        this.mContext=mContext;
        this.albumModels=albumModels;
    }
    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =layoutInflater.inflate(R.layout.row_album,parent,false);
        return new AlbumViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        final AlbumModel albumModel=albumModels.get(position);
        holder.tv_NameAlbum.setText(albumModel.getNameAlbum());
        Glide.with(mContext).load(R.drawable.albumplaceholder).into(holder.iv_Album);
        holder.cvRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, DetailsActivity.class);
                i.putExtra("albumID",albumModel.getIdAlbum());
                i.putExtra("albumName",albumModel.getNameAlbum());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumModels.size();
    }


    class AlbumViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_NameAlbum;
        private ImageView iv_Album;
        private CardView cvRow;
        public AlbumViewHolder(View itemView) {
            super(itemView);
            tv_NameAlbum= (TextView) itemView.findViewById(R.id.tv_NameAlbum);
            iv_Album= (ImageView) itemView.findViewById(R.id.iv_Album);
            cvRow= (CardView) itemView.findViewById(R.id.cvRow);
        }
    }
}
