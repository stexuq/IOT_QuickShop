package com.zing.basket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    // constructor
    public JSONParser() {
 
    }
    
    public JSONObject getJSONFromUrl(String url) throws Exception {
    	 
  	  // Making HTTP request
      try {
          
         HttpParams httpParameters = new BasicHttpParams();
         int timeoutConnection = 20000;
         HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
         // Set the default socket timeout (SO_TIMEOUT) 
         // in milliseconds which is the timeout for waiting for data.
         int timeoutSocket = 25000;
      // defaultHttpClient
         DefaultHttpClient httpClient = new DefaultHttpClient();
         httpClient.setParams(httpParameters);
         //HttpPost httpPost = new HttpPost(url);
        HttpGet httpPost = new HttpGet(url);
         HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

          HttpResponse httpResponse = httpClient.execute(httpPost);
          
          HttpEntity httpEntity = httpResponse.getEntity();
          is = httpEntity.getContent();          
      	
          
          /*HttpClient httpclient = new DefaultHttpClient();
          HttpPost httppost = new HttpPost(url);
          HttpResponse response = httpclient.execute(httppost);
          HttpEntity entity = response.getEntity();
          is = entity.getContent();*/

      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      } catch (ClientProtocolException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
       
      try {
          BufferedReader reader = new BufferedReader(new InputStreamReader(
                  is, "iso-8859-1"), 8);
          StringBuilder sb = new StringBuilder();
          String line = null;
          while ((line = reader.readLine()) != null) {
              sb.append(line + "\n");
          }
          is.close();
          json = sb.toString();
          //Log.d("string","string is "+json);
      } catch (Exception e) {
          //Log.e("Buffer Error", "Error converting result " + e.toString());
      }

      // try parse the string to a JSON object
      try {
          jObj = new JSONObject(json);
      } catch (JSONException e) {
          //Log.e("JSON Parser", "Error parsing data " + e.toString());
      }

      // return JSON String
      return jObj;
  }
	
}
