package com.example.simpleinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AddPost extends AppCompatActivity {

    public final String TAG = "AddPost";

    private EditText etDescription;
    private ImageButton btnTakePicture;
    private Button btnShare;
    private ImageView ivPhotoToPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        etDescription = findViewById(R.id.etDescription);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnShare = findViewById(R.id.btnShare);
        ivPhotoToPost = findViewById(R.id.ivPhotoToPost);
    }
}