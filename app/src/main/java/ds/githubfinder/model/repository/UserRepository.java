package ds.githubfinder.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ds.githubfinder.model.entity.User;
import ds.githubfinder.model.webservice.GithubApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private GithubApi githubApi;
    private MutableLiveData<List<User>> selectedUsers;

    public UserRepository() {
        githubApi = new GithubApi();
        selectedUsers = new MutableLiveData<>();
    }

    public MutableLiveData<List<User>> getSelectedUsers() {
        return selectedUsers;
    }

    public void getAllUsers() {
        githubApi.getEndpoint().getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    selectedUsers.setValue(response.body());
                } else {
                    Log.e("ERROR", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getSelectedUser(final String searchQuery) {
        githubApi.getEndpoint().getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    ArrayList<User> users = new ArrayList<>();
                    if (!response.body().isEmpty()) {
                        for (User user : response.body()) {
                            if (user.getUsername().contains(searchQuery)) {
                                users.add(user);
                            }
                        }
                    }
                    selectedUsers.setValue(users);
                } else {
                    Log.e("ERROR", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }
}
