package ds.githubfinder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ds.githubfinder.adapter.UserAdapter;
import ds.githubfinder.model.User;

public class MainActivity extends AppCompatActivity implements MainView {

    private Toolbar mainToolbar;
    private SearchView mainSearch;
    private RecyclerView mainRecyclerView;
    private ProgressBar mainProgress;

    private String searchQuery = "";
    private List<User> userList = new ArrayList<>();
    private UserAdapter userAdapter = new UserAdapter(userList);
    private static boolean isLoading = false;
    private static boolean isLastPage =false;
    private static int currentPage = 1;
    private static int totalPage = 0;

    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPresenter();
        bindView();
        setSupportActionBar(mainToolbar);
        setSearchView();
        setRecyclerView();
    }

    private void setPresenter(){
        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
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
//        mainRecyclerView.addOnScrollListener(endlessScrollListener);
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
                mainPresenter.search(query);
                return true;

            }
            @Override
            public boolean onQueryTextChange(String newText) {
                mainPresenter.search(newText);
                return true;
            }
        });
    }

    @Override
    public void showSearchResult(List<User> users) {
        userAdapter.setUserList(users);
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