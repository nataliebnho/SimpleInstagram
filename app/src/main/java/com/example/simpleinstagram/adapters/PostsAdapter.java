package com.example.simpleinstagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.simpleinstagram.R;
import com.example.simpleinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private static final String TAG = "POSTSADAPTER";
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfilePic;
        private TextView tvUsernameTop;
        private ImageView ivPostPicture;
        private TextView tvCaption;
        private TextView tvUsernameBottom;
        private TextView tvDate;
        private ImageView ivLikes;
        private TextView tvNumLikes;
        private TextView tvNumLikesConstant;
        private int currentSize;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsernameTop = itemView.findViewById(R.id.tvUsernameTop);
            ivPostPicture = itemView.findViewById(R.id.ivPostPicture);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvUsernameBottom = itemView.findViewById(R.id.tvUsernameBotton);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivLikes = itemView.findViewById(R.id.ivLikes);
            tvNumLikes = itemView.findViewById(R.id.tvNumLikes);
            tvNumLikesConstant = itemView.findViewById(R.id.tvLikesConstant);
        }

        public void bind(Post post) {
            tvUsernameTop.setText(post.getUser().getUsername());
            tvUsernameBottom.setText(post.getUser().getUsername());
            tvCaption.setText(post.getDescription());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivPostPicture);
            }
            tvDate.setText(calculateTimeAgo(post.getCreatedAt()));

            queryLikesForNumLikes(post);
            queryLikesForHeartImage(post, ivLikes);
            changeLikeButtons(post);

        }

        private void changeLikeButtons(Post post) {
            ivLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (ivLikes.isSelected()) {
                            deleteLikes(post);
                            ivLikes.setImageResource(R.drawable.ufi_heart);
                            ivLikes.setSelected(false);
                            tvNumLikes.setText("" + (currentSize - 1));
                            currentSize -= 1;

                            if (currentSize == 1) {
                                tvNumLikesConstant.setText("Like");
                            } else {
                                tvNumLikesConstant.setText("Likes");
                            }
                        } else {
                            postLikes(post);
                            ivLikes.setImageResource(R.drawable.ufi_heart_active);
                            ivLikes.setSelected(true);
                            tvNumLikes.setText("" + (currentSize + 1));
                            currentSize += 1;

                            if (currentSize == 1) {
                                tvNumLikesConstant.setText("Like");
                            } else {
                                tvNumLikesConstant.setText("Likes");
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e(TAG, "post was not liked");
                    }
                }
            });
        }

        private void queryLikesForNumLikes(Post post) {
            ParseQuery simpleQuery = post.getRelation("like").getQuery();
                simpleQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            tvNumLikes.setText("" + objects.size());
                            currentSize = objects.size();

                            if (currentSize == 1) {
                                tvNumLikesConstant.setText("Like");
                            } else {
                                tvNumLikesConstant.setText("Likes");
                            }
                        }
                    }
                });
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    private void queryLikesForHeartImage(Post post, ImageView ivLikes) {
        ParseQuery query = post.getRelation("like").getQuery().whereContains("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        //do not like
                        ivLikes.setImageResource(R.drawable.ufi_heart);
                        ivLikes.setSelected(false);
                        Log.d(TAG, "current user not liked it");
                    } else {
                        // display that it is liked
                        ivLikes.setImageResource(R.drawable.ufi_heart_active);
                        ivLikes.setSelected(true);
                        Log.d(TAG, "current user did already liked it");
                    }
                }
            }
        });
    }

    private void postLikes(Post post) throws ParseException {
        ParseRelation<ParseObject> relation = post.getRelation("like");
        relation.add(ParseUser.getCurrentUser());
        post.save();
    }

    private void deleteLikes(Post post) throws ParseException {
        ParseRelation<ParseObject> relation = post.getRelation("like");
        relation.remove(ParseUser.getCurrentUser());
        post.save();
    }

    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }

}
