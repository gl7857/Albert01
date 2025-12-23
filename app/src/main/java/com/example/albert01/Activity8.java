package com.example.albert01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;



import java.io.File;
import java.io.IOException;

public class Activity8 extends com.example.albert01.MasterActivity {
    private TextView tVMsg;
    private ImageView iV;
    private GeminiManager  geminiManager;
    private Bitmap imageBitmap;
    private String currentPath;

    private final String TAG = "GeminiPhotoActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 6709;
    private static final int REQUEST_FULL_IMAGE_CAPTURE = 9051;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_8);

        tVMsg = findViewById(R.id.tVMsg);
        iV = findViewById(R.id.iV);

        tVMsg.setMovementMethod(new ScrollingMovementMethod());

        geminiManager = GeminiManager.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getPhoto(View view) {
        String filename = "tempfile";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Uri imageUri;
        Intent takePhotoIntent = null;

        try {
            File imgFile = File.createTempFile(filename, ".jpg", storageDir);
            currentPath = imgFile.getAbsolutePath();

            imageUri = FileProvider.getUriForFile(
                    Activity8.this,
                    "com.example.albert01.fileprovider",
                    imgFile
            );
            takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } catch (IOException e) {
            Toast.makeText(this, "Failed to create temporary file", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error creating temp file", e);
            return;
        }

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(
                galleryIntent,
                "Select Source"
        );

        if (takePhotoIntent != null) {
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
        }

        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooserIntent, REQUEST_FULL_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No compatible application found.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data_back) {
        super.onActivityResult(requestCode, resultCode, data_back);

        if ((requestCode == REQUEST_FULL_IMAGE_CAPTURE) && (resultCode == Activity8.RESULT_OK)) {
            Bitmap finalBitmap = null;

            if (data_back != null && data_back.getData() != null) {
                try {
                    finalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data_back.getData());
                } catch (IOException e) {
                    Log.e(TAG, "Error loading gallery image", e);
                    Toast.makeText(this, "Failed to load image from gallery.", Toast.LENGTH_LONG).show();
                    return;
                }
            } else if (currentPath != null) {
                finalBitmap = BitmapFactory.decodeFile(currentPath);
            }

            if (finalBitmap != null) {
                imageBitmap = finalBitmap;
                iV.setImageBitmap(imageBitmap);

                final ProgressDialog pD = new ProgressDialog(this);
                pD.setTitle("Sent Prompt");
                pD.setMessage("Waiting for description....");
                pD.setCancelable(false);
                pD.show();

                String prompt = "תאר את התמונה הזו בצורה מפורטת, קצרה ומדויקת. זהה את האובייקטים המרכזיים, את הרקע ואת הפעולה שמתרחשת, אם יש כזו, אם אין אל תחזיר לי פעולה.";
                geminiManager.sendTextWithPhotoPrompt(prompt, imageBitmap, new GeminiCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        if (pD.isShowing()) {
                            pD.dismiss();
                        }
                        tVMsg.setText(result);
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        if (pD.isShowing()) {
                            pD.dismiss();
                        }
                        tVMsg.setText("Error: " + error.getMessage());
                        Log.e(TAG, "onActivityResult/ Error: " + error.getMessage());
                    }
                });
            }
        }
    }
}