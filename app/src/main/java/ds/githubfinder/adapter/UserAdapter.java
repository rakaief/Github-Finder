package ds.githubfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ds.githubfinder.models.User;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> dataList = new ArrayList<>();

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user, parent, false));
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

    }

    public void addItems(List<User> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        UserViewHolder(View itemView) {
            super(itemView);
        }
    }
}