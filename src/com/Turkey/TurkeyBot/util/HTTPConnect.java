package com.Turkey.TurkeyBot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HTTPConnect
{

	/**
	 * This method is used to connect to a website and return its response.
	 * Mainly used for connecting to API's
	 * 
	 * @param link
	 *            The URL of the website
	 * @return The response from the website
	 */
	public static String GetResponsefrom(String link)
	{
		BufferedReader reader = null;
		StringBuilder buffer = new StringBuilder();

		try
		{
			reader = new BufferedReader(new InputStreamReader(new URL(link).openStream()));

			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);
		} catch (Exception e){} 
		finally
		{
			try
			{
				if (reader != null) reader.close();
			} catch (IOException e){}
		}
		return buffer.toString();
	}
}
