import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

/**
 * Created by Qian on 4/29/16.
 */
public class Post {

public static void main(String[] args) {
    try {
        String url = "http://ec2-54-208-116-244.compute-1.amazonaws.com/post";
        URL object = new URL(url);

        HttpURLConnection con = (HttpURLConnection) object.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestMethod("POST");

        JSONObject auth = new JSONObject();

        auth.put("name", "test");
        auth.put("username", "test");
        auth.put("age", 22);
        auth.put("password", "test");

        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(auth.toString());
        wr.flush();

        //display what returns the POST request

        StringBuilder sb = new StringBuilder();
        int HttpResult = con.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            System.out.println("" + sb.toString());
        } else {
            System.out.println(con.getResponseMessage());
        }

    } catch (Exception e) {
        System.out.println("InputStream", e.getLocalizedMessage());
    }
}
