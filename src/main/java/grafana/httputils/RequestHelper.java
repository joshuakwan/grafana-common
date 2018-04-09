package grafana.httputils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class RequestHelper {
    private final static String AUTH = System.getenv("AUTH");

    public static String GetHTTPResponse(String urlString, String method, String payload) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(method);
            conn.setRequestProperty("Authorization", AUTH);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");

            if (payload !=null) {
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(payload.getBytes());
		        os.flush();
            }
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode() + " " + conn.getResponseMessage());
            }
    
            String output = IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8); 
    
            conn.disconnect();

            return output;

		} catch (MalformedURLException e) {
		    e.printStackTrace();
            return null;
		} catch (IOException e) {
		    e.printStackTrace();
            return null;
		}
        
    }
}