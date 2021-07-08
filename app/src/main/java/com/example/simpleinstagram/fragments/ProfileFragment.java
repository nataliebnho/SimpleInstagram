package com.example.simpleinstagram.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.simpleinstagram.Post;
import com.example.simpleinstagram.PostsAdapter;
import com.example.simpleinstagram.ProfileAdapter;
import com.example.simpleinstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView tvProfileUsername;
    private ImageView ivProfilePicture;
    private TextView tvNumPosts;

    protected ProfileAdapter adapter;
    protected List<Post> userPosts;
    RecyclerView rvUserPosts;

    public static final String TAG = "ProfileFragment";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        ivProfilePicture = view.findViewById(R.id.ivProfilePicOnPage);
        tvProfileUsername.setText(ParseUser.getCurrentUser().getUsername());
        tvNumPosts = view.findViewById(R.id.tvNumPosts);

        rvUserPosts = view.findViewById(R.id.rvUserPosts);
        userPosts = new ArrayList<>();
        adapter = new ProfileAdapter(getContext(), userPosts);

        rvUserPosts.setAdapter(adapter);
        rvUserPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));

        queryUserPosts();

    }

    private void queryUserPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue w getting posts", e);
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                userPosts.clear(); //TODO add why not a bug
                userPosts.addAll(posts);
                tvNumPosts.setText("" + posts.size());
                adapter.notifyDataSetChanged();
            }
        });
    }
}