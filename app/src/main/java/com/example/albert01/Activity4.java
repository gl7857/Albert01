package com.example.albert01;

import com.google.firebase.storage.StorageReference;

import static com.example.albert01.FBRef.refFull;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Activity4 extends MasterActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 100;

    private ImageView iV;
    private String lastImageName;
    private StorageReference refImg;
    private File localFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        iV = findViewById(R.id.iV);
    }

    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null) {

            ProgressDialog pd = ProgressDialog.show(this,
                    "Upload image", "Uploading...", true);

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            lastImageName = "img_" + System.currentTimeMillis() + ".png";
            refImg = refFull.child(lastImageName);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();

            refImg.putBytes(bytes)
                    .addOnSuccessListener(taskSnapshot -> {
                        pd.dismiss();
                        Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        pd.dismiss();
                        Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void readImage(View view) {
        if (lastImageName == null) {
            Toast.makeText(this, "No image to download", Toast.LENGTH_SHORT).show();
            return;
        }

        refImg = refFull.child(lastImageName);

        try {
            localFile = File.createTempFile("temp", "png");
        } catch (IOException e) {
            return;
        }

        ProgressDialog pd = ProgressDialog.show(this,
                "Image download", "Downloading...", true);

        refImg.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    pd.dismiss();
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getPath());
                    iV.setImageBitmap(bitmap);
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(this, "Download failed", Toast.LENGTH_SHORT).show();
                });
    }
}
