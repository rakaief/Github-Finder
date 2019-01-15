package ds.githubfinder;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ds.githubfinder.adapter.UserAdapter;
import ds.githubfinder.data.Constants;
import ds.githubfinder.helper.JsonHelper;
import ds.githubfinder.helper.NetworkHelper;
import ds.githubfinder.model.User;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SearchView searchView;
    private List<User> userList = new ArrayList<>();
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private SearchTask searchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setSearchView();
        setRecyclerView();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setSearchView();
    }

    private void setRecyclerView() {
        userRecyclerView = findViewById(R.id.main_recycler_view);
        userAdapter = new UserAdapter(userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        userRecyclerView.setLayoutManager(layoutManager);
        userRecyclerView.setAdapter(userAdapter);
    }

    private void setSearchView() {
        searchView = findViewById(R.id.main_search);

        ImageView closeButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchTask != null) {
                    searchTask.cancel(true);
                }
                searchTask = new SearchTask();
                searchTask.execute(newText);
                return true;
            }
        });
    }

    class SearchTask extends AsyncTask<String, Void, List<User>> {

        @Override
        protected List<User> doInBackground(String... strings) {
            try {
                String response = NetworkHelper.getResponse(Constants.NETWORK_BASE_URL + strings[0], "GET");
                List<User> userList = JsonHelper.getUsersFromResponse(response);
                return userList;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            userList = users;
            userAdapter.setUserList(userList);
        }
    }
}
