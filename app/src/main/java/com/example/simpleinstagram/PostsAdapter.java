package com.example.simpleinstagram;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.w3c.dom.Text;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsernameTop = itemView.findViewById(R.id.tvUsernameTop);
            ivPostPicture = itemView.findViewById(R.id.ivPostPicture);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvUsernameBottom = itemView.findViewById(R.id.tvUsernameBotton);
        }

        public void bind(Post post) {
            tvUsernameTop.setText(post.getUser().getUsername());
            tvUsernameBottom.setText(post.getUser().getUsername());
            tvCaption.setText(post.getDescription());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivPostPicture);
            }

        }
    }
}
