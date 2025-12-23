package com.example.albert01;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;

public class Activity3 extends MasterActivity {

    private ImageView ivSelectedImage;
    private Uri imageUri;
    private StorageReference imageRef = FBRef.refStorageRoot.child("images/my_uploaded_image.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        ivSelectedImage = findViewById(R.id.ivSelectedImage);
    }

    private final ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageUri = uri;
                    ivSelectedImage.setImageURI(imageUri); // הצגת התמונה הנבחרת ב-ImageView
                    uploadImageToStorage();
                }
            });

    public void selectImage(View view) {
        galleryLauncher.launch("image/*");
    }

    private void uploadImageToStorage() {
        if (imageUri != null) {
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(Activity3.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Activity3.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        } else {
            Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadAndDisplayImage(View view) {
        imageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(this)
                            .load(uri)
                            .into(ivSelectedImage);
                    Toast.makeText(Activity3.this, "Image downloaded and displayed!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Activity3.this, "Download failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}