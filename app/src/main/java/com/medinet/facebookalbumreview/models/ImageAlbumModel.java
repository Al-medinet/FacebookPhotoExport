package com.medinet.facebookalbumreview.models;

/**
 * Created by Mehdi on 12/09/2017.
 */

public class ImageAlbumModel {
    private String urlImage;

    public ImageAlbumModel(String urlImage){
        this.urlImage=urlImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
