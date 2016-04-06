package com.rana.androidchallenge;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by sandeeprana on 01/09/15.
 *
 * @author Sandeep Rana
 */
public class Downloader {
   /**
	* Returns the content of page as a string. This method must be run apart from the main UI Thread
	*
	* @param url of page u wants to download
	* @return data as a string that can be further utilized for any purpose
	*/
   public String downloadContent(String url) throws IOException {
	  URL website = new URL(url);
	  URLConnection connection = website.openConnection();
	  BufferedReader in = new BufferedReader(
			  new InputStreamReader(
					  connection.getInputStream()));

	  StringBuilder response = new StringBuilder();
	  String inputLine;

	  while ((inputLine = in.readLine()) != null)
		 response.append(inputLine);

	  in.close();
	  return response.toString();
   }

   /**
	* This method can be used to download data using post parameters
	*
	* @param urlString  url to be used without any parameter
	* @param parameters parameters in plain text like this e.g  param1=value1&param2=value2&param3=value3
	* @return returns the data downloaded
	* @throws IOException
	*/
   public String downloadContentUsingPostMethod(String urlString, String parameters) throws IOException {
	  URL url = new URL(urlString);
	  URLConnection conn = url.openConnection();
	  conn.setDoOutput(true);
	  OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	  writer.write(parameters);
	  writer.flush();
	  String line;
	  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	  StringBuilder response = new StringBuilder();
	  while ((line = reader.readLine()) != null) {
		 response.append(line);
	  }
	  writer.close();
	  reader.close();

	  return response.toString();
   }


   /**
	* Check whether Internet is connected or not.
	* @param context Working context
	* @return true if network is available false if not available.
	*/
   public static boolean isNetwork(Context context){
	  ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context
			  .CONNECTIVITY_SERVICE);
	  NetworkInfo info=cm.getActiveNetworkInfo();
	  if (info==null){
		 return false;
	  }
	  NetworkInfo.State network = info.getState();
	  return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
   }

}
