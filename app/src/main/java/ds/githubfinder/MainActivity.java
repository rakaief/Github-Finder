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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ds.githubfinder.adapter.UserAdapter;
import ds.githubfinder.data.Constants;
import ds.githubfinder.helper.JsonHelper;
import ds.githubfinder.helper.NetworkHelper;
import ds.githubfinder.model.User;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private SearchView mainSearch;
    private RecyclerView mainRecyclerView;
    private ProgressBar mainProgress;

    private SearchTask searchTask;

    private String searchQuery = "";
    private List<User> userList = new ArrayList<>();
    private UserAdapter userAdapter = new UserAdapter(userList);
    private static boolean isLoading = false;
    private static boolean isLastPage =false;
    private static int currentPage = 1;
    private static int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        setSupportActionBar(mainToolbar);
        setSearchView();
        setRecyclerView();
    }

    /**
     * Bind xml component through it's id;
     */
    private void bindView() {
        mainToolbar = findViewById(R.id.main_toolbar);
        mainSearch = findViewById(R.id.main_search);
        mainRecyclerView = findViewById(R.id.main_recycler_view);
        mainProgress = findViewById(R.id.main_progress);
    }

    /**
     * Setup mainRecyclerView layout manager, adapter, and scroll listener.
     */
    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener(layoutManager);
        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.addOnScrollListener(endlessScrollListener);
        mainRecyclerView.setAdapter(userAdapter);
    }

    /**
     * Setup mainSearch and it's query text listener.
     */
    private void setSearchView() {
        ImageView closeButton = (ImageView) mainSearch.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setVisibility(View.GONE);

        mainSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                    currentPage = 1;
                    userAdapter.setUserList(userList);
                }
                return true;
            }
        });
    }

    /**
     * Asynctask to retrieve Github Search API.
     */
    class SearchTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
            mainProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                String stringUrl = Constants.API_SEARCH_BASE_URL + strings[0] + "&page=" + String.valueOf(currentPage);
                String response = NetworkHelper.getResponse(stringUrl, Constants.API_SEARCH_HTTP_METHOD);

                if (response.equals(Constants.API_SEARCH_FORBIDDEN)) {
                    return Constants.API_SEARCH_RATE_LIMIT;
                } else {
                    List<User> newUserList = JsonHelper.getUsersFromResponse(response);
                    if (strings[1].equals(Constants.SEARCH_NEW_USERS)) {
                        userList = newUserList;

                        int maxTotalPage = (int) Math.ceil(1.0 * JsonHelper.getUsersCountFromResponse(response) / Constants.API_PAGINATION_SIZE);
                        int maxPage = (int) Math.ceil(1.0 * Constants.API_MAX_SEARCH / Constants.API_PAGINATION_SIZE);

                        totalPage = Math.min(maxTotalPage, maxPage);
                    } else {
                        userList.addAll(newUserList);
                    }
                    return Constants.API_SEARCH_SUCCESS;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Constants.API_SEARCH_ERROR;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer.equals(Constants.API_SEARCH_RATE_LIMIT)) {
                Toast.makeText(MainActivity.this, getString(R.string.rate_limit_message), Toast.LENGTH_SHORT).show();

            } else if (integer.equals(Constants.API_SEARCH_ERROR)) {
                Toast.makeText(MainActivity.this, getString(R.string.unknown_error_message), Toast.LENGTH_SHORT).show();
            } else {
                userAdapter.setUserList(userList);
            }
            isLoading = false;
            mainProgress.setVisibility(View.GONE);
        }
    }

    /**
     * Scroll listener on mainRecyclerView to handle Github Search API pagination
     */
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
                }
            }
        }

        /**
         * Load next pagination of users.
         */
        private void loadMore() {
            currentPage++;

            if (currentPage >= totalPage) {
                isLastPage = true;
            }

            if (searchQuery != null) {
                if (searchTask != null) {
                    searchTask.cancel(true);
                }
                searchTask = new SearchTask();
                searchTask.execute(searchQuery, Constants.SEARCH_ADD_USERS);
            }
        }

        /**
         * Check is currentPage has achieved the last page.
         * @return
         */
        private boolean isLastPage() {
            return MainActivity.isLastPage;
        }

        /**
         * Check if current searchTask is on working.
         * @return
         */
        private boolean isLoading() {
            return MainActivity.isLoading;
        }
    }
}
