package ds.githubfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ds.githubfinder.R;
import ds.githubfinder.model.User;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList = new ArrayList<>();

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
        return userList.size();
    }

    public void addItems(List<User> dataList) {
        this.userList.addAll(dataList);
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private ImageView profileImage;

        UserViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_username);
            profileImage = itemView.findViewById(R.id.user_profile_image);
        }

        public void bind(User user) {
            username.setText(user.getUsername());

            // TODO: set profile image;
        }
    }
}