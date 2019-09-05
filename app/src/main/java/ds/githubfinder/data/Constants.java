package ds.githubfinder.data;

public class Constants {

    public static final String API_SEARCH_BASE_URL = "https://api.github.com/search/users?q=";
    public static final String API_SEARCH_FORBIDDEN = "Forbidden";
    public static final String API_SEARCH_HTTP_METHOD = "GET";
    public static final Integer API_SEARCH_SUCCESS = 0;
    public static final Integer API_SEARCH_RATE_LIMIT = 1;
    public static final Integer API_SEARCH_ERROR = 2;
    public static final int API_PAGINATION_SIZE = 30;
    public static final int API_MAX_SEARCH = 1000;

    public static final int NETWORK_READ_TIMEOUT = 10000;
    public static final int NETWORK_CONNECT_TIMEOUT = 10000;

    public static final String JSON_USERNAME_FIELD = "login";
    public static final String JSON_AVATAR_URL_FIELD = "avatar_url";
    public static final String JSON_USER_LIST = "items";
    public static final String JSON_TOTAL_COUNT = "total_count";

    public static final String SEARCH_NEW_USERS = "new";
    public static final String SEARCH_ADD_USERS = "add";

    public static final String CLIENT_ID = "245bcd04581513f36a64" ;
    public static final String CLIENT_SECRET = "8471c1b4bad816c5806c2412f9b4b8323b51193e";
}

