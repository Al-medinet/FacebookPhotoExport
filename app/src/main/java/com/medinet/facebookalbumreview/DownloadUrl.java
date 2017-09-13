package com.medinet.facebookalbumreview;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Mehdi on 13/09/2017.
 */

public class DownloadUrl extends AsyncTask<String,Void,byte[]> {

    private Context mContext;
    private ProgressDialog progressDialog;
    private String albumName;
    //Firebase Storage
    private StorageReference mStorageRef= FirebaseStorage.getInstance().getReference();

    public DownloadUrl(Context mContext,String albumName){
        this.mContext=mContext;
        this.albumName=albumName;
    }
    @Override
    protected void onPreExecute() {
        progressDialog=new ProgressDialog(this.mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Waiting until saving ...");
        progressDialog.show();
    }

    @Override
    protected byte[] doInBackground(String... params) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            URL toDownload=new URL(params[0]);
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        Log.i("NameAlbum", "onPostExecute: "+albumName);
        StorageReference riversRef = mStorageRef.child(albumName.concat(bytes.toString()).concat(".jpg"));
        UploadTask uploadTask = riversRef.putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Image Has Been Added", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
