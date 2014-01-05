package com.pressx.facebook;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class FacebookActions {
	URL url;
	HttpURLConnection con = null;
	String gameID = "228239587356038";
	String gameSecret = "bedba18380408106d95fc73fcbe8ec14";
	
	String key = null;
	public String getKey() {
		try {
			url = new URL("https://graph.facebook.com/oauth/access_token?");
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("client_id", gameID);
			con.setRequestProperty("client_secret", gameSecret);
			con.setRequestProperty("grant_type", "client_credentials");
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes("client_id=228239587356038&client_secret=bedba18380408106d95fc73fcbe8ec14&grant_type=client_credentials");
			wr.flush();
			wr.close();
			
			InputStream is = con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null)
				response.append(line);
			rd.close();
			key = response.substring(13);
			
		} catch (MalformedURLException e) {
			System.out.println("URL: BAD URL");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("CONNETION: BAD CONNETION");
			e.printStackTrace();
		} finally {
			if (con != null)
				con.disconnect();
		}
		return key;
	}
	
	public boolean post(String post) {
		try {
			url = new URL("http://graph.facebook.com/[USER FB ID]/feed?");
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("message", "{"+post+"}");
			con.setRequestProperty("access_token", "{"+key+"}");
			con.setDoOutput(true);
			
			StringBuffer param = new StringBuffer();
			param.append(URLEncoder.encode("message", "UTF-8"));
			param.append("=");
			param.append(URLEncoder.encode("ScoreData", "UTF-8"));
			param.append(URLEncoder.encode("access_token", "UTF-8"));
			param.append("=");
			param.append(URLEncoder.encode(key, "UTF-8"));
			
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(param.toString());
			wr.flush();
			wr.close();
			
		} catch (MalformedURLException e) {
			System.out.println("URL: BAD URL");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("CONNETION: BAD CONNETION");
			e.printStackTrace();
		}
		return false;
	}
}