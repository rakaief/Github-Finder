package ds.githubfinder;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ds.githubfinder.data.Constants;
import ds.githubfinder.data.GithubApi;
import ds.githubfinder.model.SearchResponse;
import ds.githubfinder.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MainPresenter {

    private MainView mainView;

    public void attachView(MainView mainView) {
        this.mainView = mainView;
    }

    public void search(String keywords){
        List<User> users = new ArrayList<User>();
        // Searching

        GithubApi githubApi = new GithubApi();
        githubApi.getEndpoint().searchUserByName(keywords, Constants.CLIENT_ID, Constants.CLIENT_SECRET).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()){
                    mainView.showSearchResult(response.body().getItems());
                } else {
                    Log.e("ERROR", response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });

    }
}
