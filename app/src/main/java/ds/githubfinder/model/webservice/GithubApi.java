package ds.githubfinder.model.webservice;

import java.util.List;

import ds.githubfinder.model.entity.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class GithubApi {

    private Retrofit retrofit;

    public interface GithubEndpoint {

        @GET("/users")
        public Call<List<User>> getAllUsers();
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
