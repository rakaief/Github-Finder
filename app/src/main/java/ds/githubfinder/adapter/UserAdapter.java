package ds.githubfinder.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import ds.githubfinder.R;
import ds.githubfinder.model.User;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList = new ArrayList<>();

    public UserAdapter() {

    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user, parent, false));
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        if (userList == null) {
            return 0;
        } else {
            return userList.size();
        }
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private ImageView profileImage;
        private ShimmerFrameLayout userShimmer;
        private LinearLayout userContainer;

        UserViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_username);
            profileImage = itemView.findViewById(R.id.user_profile_image);
            userContainer = itemView.findViewById(R.id.user_container);
            userShimmer = itemView.findViewById(R.id.user_shimmer);
        }

        private void bind(User user) {
            username.setText(user.getUsername());

            Glide.with(itemView.getContext())
                    .load(user.getImageUrl())
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            System.out.println("onload failed");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            userShimmer.setVisibility(View.GONE);
                            userContainer.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(profileImage);
        }
    }
}