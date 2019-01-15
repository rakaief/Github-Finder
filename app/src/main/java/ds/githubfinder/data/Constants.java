package ds.githubfinder.data;

public class Constants {

    public static final String NETWORK_BASE_URL = "https://api.github.com/search/users?q=";
    public static final int NETWORK_READ_TIMEOUT = 10000;
    public static final int NETWORK_CONNECT_TIMEOUT = 10000;

    public static final String JSON_USERNAME_FIELD = "login";
    public static final String JSON_AVATAR_URL_FIELD = "avatar_url";
    public static final String JSON_USER_LIST = "items";
}
