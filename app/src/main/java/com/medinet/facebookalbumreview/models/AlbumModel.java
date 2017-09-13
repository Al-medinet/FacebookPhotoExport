package com.medinet.facebookalbumreview.models;

/**
 * Created by Mehdi on 12/09/2017.
 */

public class AlbumModel {


    private String idAlbum;
    private String nameAlbum;

    public AlbumModel(String idAlbum,String nameAlbum){
        this.idAlbum=idAlbum;
        this.nameAlbum=nameAlbum;
    }
    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }
}
