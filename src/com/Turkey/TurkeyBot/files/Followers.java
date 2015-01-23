package com.Turkey.TurkeyBot.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.Turkey.TurkeyBot.TurkeyBot;

public class Followers extends BotFile implements Runnable
{
	private static String propName = "Followers.properties";
	
	public static boolean run = true;

	public Followers(TurkeyBot b) throws IOException
	{
		super(b,  "C:" + File.separator + "TurkeyBot" + File.separator + "properties" + File.separator + propName);

		if(super.properties.isEmpty())
			loadFollowers();
		checkFollowers();
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * Starts the thread the controls the follower check
	 */
	@Override
	public void run()
	{
		while(run)
		{
			String result = "";
			try
			{
				URL url = new URL("https://api.twitch.tv/kraken/channels/turkey2349/follows?direction=DESC&limit=100&offset=0");
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

			int index = 0;
			while(index > -1)
			{
				String temp = result.substring(result.indexOf("display_name") + 15, result.indexOf(",", result.indexOf("display_name")) -1);
				if(!super.properties.containsKey(temp))
				{
					System.out.println("New Follower");
					super.setSetting(temp, System.currentTimeMillis());
				}
				index = result.indexOf(",", result.indexOf("display_name"));
				result = result.substring(index);
				index = result.indexOf("display_name");
			}

			System.out.println("checking");

			try
			{
				Thread.sleep(60000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks for any new followers since last time the bot ran.
	 */
	public void loadFollowers()
	{
		String result = "";
		try
		{
			URL url = new URL("https://api.twitch.tv/kraken/channels/turkey2349/follows?direction=DESC&limit=1&offset=0");
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
		int total = Integer.parseInt(result.substring(result.indexOf("_total") + 8, result.indexOf(",", result.indexOf("_total"))));
		int current = 0;
		result= "";
		String nexturl = "https://api.twitch.tv/kraken/channels/turkey2349/follows?direction=DESC&limit=100&offset=0";
		while(current < total)
		{
			result = "";
			try
			{
				URL url = new URL(nexturl);
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
			try{
				nexturl = result.substring(result.indexOf("\"next\":") + 8, result.indexOf(",", result.indexOf("\"next\":")));
			}catch(IndexOutOfBoundsException e){nexturl = result.substring(result.indexOf("\"next\":") + 8, result.indexOf("}", result.indexOf("\"next\":")));}
			//System.out.println(nexturl);
			int index = 0;
			while(index > -1)
			{
				String temp = result.substring(result.indexOf("display_name") + 15, result.indexOf(",", result.indexOf("display_name")) -1);
				super.setSetting(temp, System.currentTimeMillis());
				index = result.indexOf(",", result.indexOf("display_name"));
				result = result.substring(index);
				index = result.indexOf("display_name");
			}
			current+=100;
		}
		System.out.println("Added Followers");
	}

	/**
	 * Checks for new followers.
	 */
	public void checkFollowers()
	{
		String result = "";
		try
		{
			URL url = new URL("https://api.twitch.tv/kraken/channels/turkey2349/follows?direction=DESC&limit=100&offset=0");
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

		int index = 0;
		while(index > -1)
		{
			String temp = result.substring(result.indexOf("display_name") + 15, result.indexOf(",", result.indexOf("display_name")) -1);
			if(!super.properties.containsKey(temp))
			{
				System.out.println("New Follower");
				super.setSetting(temp, System.currentTimeMillis());
			}
			index = result.indexOf(",", result.indexOf("display_name"));
			result = result.substring(index);
			index = result.indexOf("display_name");
		}
	}

}
