package ds.githubfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import ds.githubfinder.adapter.UserAdapter;
import ds.githubfinder.model.User;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private List<User> userList = new ArrayList<>();
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSearchView();
        setRecyclerView();
    }

    private void setSearchView() {
        searchView = findViewById(R.id.main_search);
        searchView.setIconified(false);
//        searchView.onActionViewExpanded();
//        searchView.setIconified(true);
    }

    private void setRecyclerView() {
        userRecyclerView = findViewById(R.id.main_recycler_view);
        userAdapter = new UserAdapter(userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        userRecyclerView.setLayoutManager(layoutManager);
        userRecyclerView.setAdapter(userAdapter);
    }


}
