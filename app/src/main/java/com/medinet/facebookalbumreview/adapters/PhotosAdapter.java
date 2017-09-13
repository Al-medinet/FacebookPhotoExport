package com.medinet.facebookalbumreview.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medinet.facebookalbumreview.DetailsActivity;
import com.medinet.facebookalbumreview.R;
import com.medinet.facebookalbumreview.models.ImageAlbumModel;

import java.util.List;

/**
 * Created by Mehdi on 12/09/2017.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder> {


    private DetailsActivity detailsActivity;
    private Context mContext;
    private List<ImageAlbumModel> imageAlbumModels;
    public PhotosAdapter(Context mContext, List<ImageAlbumModel> imageAlbumModels){
        this.mContext=mContext;
        this.imageAlbumModels=imageAlbumModels;
        this.detailsActivity= (DetailsActivity) mContext;
    }
    @Override
    public PhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =layoutInflater.inflate(R.layout.row_photo,parent,false);
        return new PhotosViewHolder(v,this.detailsActivity);
    }

    @Override
    public void onBindViewHolder(final PhotosViewHolder holder, int position) {
        ImageAlbumModel albumModel=imageAlbumModels.get(position);
        Glide.with(mContext).load(albumModel.getUrlImage()).placeholder(R.drawable.albumplaceholder)
                .into(holder.iv_Photo);
        if(!detailsActivity.is_action_mode){
            holder.checkBox.setVisibility(View.GONE);
            holder.iv_Photo.setAlpha(1f);
        }else{
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
            holder.iv_Photo.setAlpha(.5f);
        }
    }


    @Override
    public int getItemCount() {
        return imageAlbumModels.size();
    }

    class PhotosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView iv_Photo;
        CardView cvRow;
        CheckBox checkBox;
        private DetailsActivity detailsActivity;
        public PhotosViewHolder(View itemView,DetailsActivity detailsActivity) {
            super(itemView);
            iv_Photo= (ImageView) itemView.findViewById(R.id.iv_Photo);
            cvRow= (CardView) itemView.findViewById(R.id.cvRow);
            checkBox= (CheckBox) itemView.findViewById(R.id.itemChecked);
            this.detailsActivity=detailsActivity;
            cvRow.setOnLongClickListener(this.detailsActivity);
            cvRow.setOnClickListener(this);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.itemChecked:
                    this.detailsActivity.prepaseSelection(v,getAdapterPosition());
                    break;
            }
        }
    }
}
