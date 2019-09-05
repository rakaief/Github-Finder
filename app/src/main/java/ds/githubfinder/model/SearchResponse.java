package ds.githubfinder.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ds.githubfinder.model.User;

public class SearchResponse {
    @SerializedName("total_counts") Integer totalCounts;
    @SerializedName("incomplete_result") Boolean incompleteResults;
    @SerializedName("items") List<User> items;

    public List<User> getItems() {
        return items;
    }
}
