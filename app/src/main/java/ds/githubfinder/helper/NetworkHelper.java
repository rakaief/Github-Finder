package ds.githubfinder.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import ds.githubfinder.data.Constants;

public class NetworkHelper {

    /**
     * Setup new HTTP Connection from stringUrl with requestMethod
     * @param stringUrl
     * @param requestMethod
     * @return
     * @throws IOException
     */
    private static HttpURLConnection createUrlConnection(String stringUrl, String requestMethod)
            throws IOException{

        URL url = new URL(stringUrl);

        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(Constants.NETWORK_READ_TIMEOUT);
        urlConnection.setConnectTimeout(Constants.NETWORK_CONNECT_TIMEOUT);
        urlConnection.setRequestMethod(requestMethod);
        urlConnection.connect();

        return urlConnection;
    }

    /**
     * Convert inputStream to string
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String streamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Get response from http connection.
     * @param stringUrl
     * @param requestMethod
     * @return
     * @throws IOException
     */
    public static String getResponse(String stringUrl, String requestMethod) throws IOException {
        String response = "";
        HttpURLConnection urlConnection = createUrlConnection(stringUrl, requestMethod);
        InputStream inputStream = null;

        if (urlConnection.getResponseCode() == 200) {
            inputStream = urlConnection.getInputStream();
            response = streamToString(inputStream);
        } else if (urlConnection.getResponseCode() == 403) {
            return urlConnection.getResponseMessage();

        }

        urlConnection.disconnect();

        if (inputStream != null) {
            inputStream.close();
        }

        return response;
    }

}
