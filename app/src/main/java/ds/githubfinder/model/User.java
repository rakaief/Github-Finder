package ds.githubfinder.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("login") private String username;
    @SerializedName("avatar_url") private String imageUrl;

    public User(String username, String imageUrl) {
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
