package ds.githubfinder.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ds.githubfinder.model.entity.User;
import ds.githubfinder.model.repository.UserRepository;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;

    public void init() {
        userRepository = new UserRepository();
    }

    public MutableLiveData<List<User>> getSelectedUsers() {
        return userRepository.getSelectedUsers();
    }

    public void searchUser(String searchQuery) {
        userRepository.getSelectedUser(searchQuery);
    }
}
