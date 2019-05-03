package ds.githubfinder.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ds.githubfinder.R;
import ds.githubfinder.model.entity.User;
import ds.githubfinder.view.adapter.UserAdapter;
import ds.githubfinder.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    private Toolbar mainToolbar;
    private SearchView mainSearch;
    private RecyclerView mainRecyclerView;
    private ProgressBar mainProgress;

    private List<User> userList = new ArrayList<>();
    private UserAdapter userAdapter = new UserAdapter(userList);
    private String searcQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        setSupportActionBar(mainToolbar);
        setSearchView();
        setRecyclerView();

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.init();

        userViewModel.getSelectedUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                Log.d(MainActivity.class.getSimpleName(), String.valueOf(counter++));
                userAdapter.setUserList(users);
            }
        });
    }

    static int counter = 0;

    private void bindView() {
        mainToolbar = findViewById(R.id.main_toolbar);
        mainSearch = findViewById(R.id.main_search);
        mainRecyclerView = findViewById(R.id.main_recycler_view);
        mainProgress = findViewById(R.id.main_progress);
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.setAdapter(userAdapter);
    }

    private void setSearchView() {
        ImageView closeButton = (ImageView) mainSearch.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setVisibility(View.GONE);

        mainSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userViewModel.searchUser(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userViewModel.searchUser(newText);
                return true;
            }
        });
    }
}