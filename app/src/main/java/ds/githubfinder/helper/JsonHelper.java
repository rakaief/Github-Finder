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

    public static JSONArray stringToJsonArray(String string) throws JSONException {
        if (TextUtils.isEmpty(string)) {
            return null;
        } else {
            return new JSONArray(string);
        }
    }

    public static List<User> getUsersFromResponse(String response) {
        try {
            List<User> users= new ArrayList<>();

            JSONArray userJsonArray = stringToJsonArray(response);
            for (int i=0; i<userJsonArray.length(); i++) {
                JSONObject userJsonObject = userJsonArray.getJSONObject(i);
                String username = userJsonObject.getString(Constants.JSON_USERNAME_FIELD);
                String imageUrl = userJsonObject.getString(Constants.JSON_AVATAR_URL_FIELD);
                users.add(new User(username, imageUrl));
            }
            return users;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
