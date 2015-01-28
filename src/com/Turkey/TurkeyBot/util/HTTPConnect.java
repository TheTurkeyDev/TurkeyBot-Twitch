package com.Turkey.TurkeyBot.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPConnect
{

	/**
	 * This method is used to connect to a website and return its response.
	 * Mainly used for connecting to API's
	 * @param link The URL of the website
	 * @return The response from the website
	 */
	public static String GetResponsefrom(String link)
	{
		String result = "";
		try
		{
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				result += line;
			}
			reader.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
