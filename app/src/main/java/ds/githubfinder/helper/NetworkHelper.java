package ds.githubfinder.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class NetworkHelper {

    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static HttpURLConnection createUrlConnection(URL url, String requestMethod) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(Constants.NETWORK_READ_TIMEOUT);
            urlConnection.setConnectTimeout(Constants.NETWORK_CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlConnection;
    }

    public static String getResponse(URL url) {
        String response = "";
        if (url == null) {
            return response;
        }

        HttpURLConnection urlConnection = createUrlConnection(url, "GET");
        InputStream inputStream = null;
        try {
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                response = streamToString(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (urlConnection != null) {
            urlConnection.disconnect();
        }

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }

    private static String streamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            try {
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

}
