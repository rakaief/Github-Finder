package ds.githubfinder;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
    private String searchQuery;

    private static boolean isLoading = false;
    private static boolean isLastPage =false;
    private static int currentPage = 1;

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener(layoutManager);
        userRecyclerView.setLayoutManager(layoutManager);
        userRecyclerView.addOnScrollListener(endlessScrollListener);
        userRecyclerView.setAdapter(userAdapter);
    }

    private void setSearchView() {
        searchView = findViewById(R.id.main_search);

        ImageView closeButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchTask != null) {
                    searchTask.cancel(true);
                }
                searchTask = new SearchTask();
                searchQuery = query;
                searchTask.execute(query, Constants.SEARCH_NEW_USERS);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    userList = new ArrayList<>();
                    userAdapter.setUserList(userList);
                }
                return true;
            }
        });
    }

    class SearchTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String response = NetworkHelper.getResponse(Constants.NETWORK_BASE_URL + strings[0], "GET");
                List<User> newUserList = JsonHelper.getUsersFromResponse(response);

                if (strings[1].equals(Constants.SEARCH_NEW_USERS)) {
                    userList = newUserList;
                } else {
                    userList.addAll(newUserList);
                }
                isLoading = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            userAdapter.setUserList(userList);
        }
    }

    class EndlessScrollListener extends RecyclerView.OnScrollListener {

        LinearLayoutManager layoutManager;

        public EndlessScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleCount = layoutManager.getChildCount();
            int totalCount = layoutManager.getItemCount();
            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading() && !isLastPage()) {
                if ((visibleCount + firstVisiblePosition) >= totalCount && firstVisiblePosition >= 0) {
                    loadMore();
                    System.out.println("load more");
                }
            }
        }

        private void loadMore() {
            MainActivity.isLoading = true;
            currentPage++;
            if (searchQuery != null) {
                if (searchTask != null) {
                    searchTask.cancel(true);
                }
                searchTask = new SearchTask();
                searchTask.execute(searchQuery, Constants.SEARCH_ADD_USERS);
            }
        }

        private boolean isLastPage() {
            return MainActivity.isLastPage;
        }

        private boolean isLoading() {
            return MainActivity.isLoading;
        }
    }
}
