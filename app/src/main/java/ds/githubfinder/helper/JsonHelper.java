package ds.githubfinder.helper;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ds.githubfinder.data.Constants;
import ds.githubfinder.model.User;

public class JsonHelper {

    /**
     * Return the number of user in response.
     * @param response
     * @return
     */
    public static int getUsersCountFromResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return 0;
        } else {
            try {
                JSONObject userJsonObject = new JSONObject(response);
                return userJsonObject.getInt(Constants.JSON_TOTAL_COUNT);
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    /**
     * Parse response and get list of all users on it.
     * @param response
     * @return
     */
    public static List<User> getUsersFromResponse(String response) {
        List<User> userList = new ArrayList<>();

        if (TextUtils.isEmpty(response) || response.equals(Constants.API_SEARCH_FORBIDDEN)) {
            return userList;
        }

        try {
            JSONObject userJsonObject = new JSONObject(response);

            JSONArray userJsonArray = userJsonObject.getJSONArray(Constants.JSON_USER_LIST);
            for (int i=0; i<userJsonArray.length(); i++) {
                JSONObject jsonObject = userJsonArray.getJSONObject(i);
                String username = jsonObject.getString(Constants.JSON_USERNAME_FIELD);
                String imageUrl = jsonObject.getString(Constants.JSON_AVATAR_URL_FIELD);

                userList.add(new User(username, imageUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return userList;
    }
}
