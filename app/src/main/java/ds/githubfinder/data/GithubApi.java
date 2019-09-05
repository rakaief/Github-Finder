package ds.githubfinder.data;

import java.util.List;

import ds.githubfinder.model.SearchResponse;
import ds.githubfinder.model.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GithubApi {

    private Retrofit retrofit;

    public interface GithubEndpoint {

        @GET("/users")
        public Call<List<User>> getAllUsers();

        @GET("/search/users")
        public Call<SearchResponse> searchUserByName(
                @Query("q") String query,
                @Query("client_id") String clientId,
                @Query("client_secret") String clientSecret
        );


    }


    public GithubApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public GithubEndpoint getEndpoint() {
        return retrofit.create(GithubEndpoint.class);
    }
}