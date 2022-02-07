package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connection {

//Reference:  http://www.mkyong.com/java/java-httpurlconnection-follow-redirect-example/
	
	private static int count;
	
	public static String get(String url){
		URL urlConnection;
        StringBuffer html = null;
        String inputLine;
		try {
			
			urlConnection = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlConnection.openConnection();
			conn.setReadTimeout(5000);
			conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			conn.addRequestProperty("User-Agent", "Mozilla");
			conn.addRequestProperty("Referer", "google.com");
		 
			count++;
			if(count%100 == 0){
				System.err.println("Sleep 5 seconds...");
				Thread.sleep(5000);
			}
			
			System.out.println("Request URL ... " + url);
			
			html = new StringBuffer();
			
			boolean redirect = false;
		 
			// normally, 3xx is redirect
			int status = conn.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM
						|| status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
			}
		 
			System.out.println("Response Code ... " + status);
		 
			if (redirect) {
		 
				// get redirect url from "location" header field
				String newUrl = conn.getHeaderField("Location");
		 
				// get the cookie if need, for login
				String cookies = conn.getHeaderField("Set-Cookie");
		 
				// open the new connnection again
				conn = (HttpURLConnection) new URL(newUrl).openConnection();
				conn.setRequestProperty("Cookie", cookies);
				conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
				conn.addRequestProperty("User-Agent", "Mozilla");
				conn.addRequestProperty("Referer", "google.com");
		 
				System.out.println("Redirect to URL : " + newUrl);
		 
			}
		 
			BufferedReader in = new BufferedReader(
		                              new InputStreamReader(conn.getInputStream()));
			
		 
			while ((inputLine = in.readLine()) != null) {
				html.append(inputLine);
			}
			in.close();
		 
//			System.out.println("URL Content... \n" + html.toString());
//			System.out.println("Done");
		}catch (Exception e){
			System.err.println("Bad URL");
			html.append("  ");
		}
        
		return html.toString();
	}
}