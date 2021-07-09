package com.example.simpleinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.simpleinstagram.adapters.PostsAdapter;
import com.example.simpleinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "POSTDETAILACTIVITY";
    Post post;
    int position;

    private ImageView ivProfilePic;
    private TextView tvUsernameTop;
    private ImageView ivPostPicture;
    private TextView tvCaption;
    private TextView tvUsernameBottom;
    private TextView tvDate;
    private ImageView ivLikes;

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
        ivLikes = findViewById(R.id.ivLikes);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        tvUsernameTop.setText(post.getUser().getUsername());
        tvUsernameBottom.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPostPicture);
        }
        tvDate.setText(PostsAdapter.calculateTimeAgo(post.getCreatedAt()));
        queryForLikes();
    }

    protected void queryForLikes() {
        ParseQuery query = post.getRelation("like").getQuery().whereContains("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null){
                    if(objects.size() == 0){
                        //do not like
                        ivLikes.setImageResource(R.drawable.ufi_heart);
                        ivLikes.setSelected(false);

                        Log.d(TAG, "current user not liked it");
                    }
                    else{
                        // display that it is liked
                        ivLikes.setImageResource(R.drawable.ufi_heart_active);
                        ivLikes.setSelected(true);
                        Log.d(TAG, "current user did already liked it");
                    }
                }
            }
        });
    }
}