package com.example.simpleinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetailActivity extends AppCompatActivity {

    Post post;
    int position;

    private ImageView ivProfilePic;
    private TextView tvUsernameTop;
    private ImageView ivPostPicture;
    private TextView tvCaption;
    private TextView tvUsernameBottom;
    private TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvUsernameTop = findViewById(R.id.tvUsernameTop);
        ivPostPicture = findViewById(R.id.ivPostPicture);
        tvCaption = findViewById(R.id.tvCaption);
        tvUsernameBottom = findViewById(R.id.tvUsernameBotton);
        tvDate = findViewById(R.id.tvDate);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        tvUsernameTop.setText(post.getUser().getUsername());
        tvUsernameBottom.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPostPicture);
        }
        tvDate.setText(PostsAdapter.calculateTimeAgo(post.getCreatedAt()));

    }
}