package com.Turkey.TurkeyBot.files;

import java.io.File;
import java.io.IOException;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;
import com.Turkey.TurkeyBot.util.HTTPConnect;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Followers extends BotFile implements Runnable
{
	private JsonParser json = new JsonParser();
	public static boolean run = true;

	public Followers(TurkeyBot b) throws IOException
	{
		super(b,  "C:" + File.separator + "TurkeyBot" + File.separator + "follower tracking" + File.separator + "Followers_"+b.getChannel(false)+".properties");
	}

	/**
	 * Starts the follower tracker for the bot in the channel
	 */
	public void initFollowerTracker()
	{
		if(super.properties.isEmpty())
			loadFollowers();
		checkFollowers(false);
		run = true;
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * Stops the follower tracker for the bot in the channel
	 */
	public void stopFollowerTracker()
	{
		run = false;
	}

	/**
	 * Starts the thread the controls the follower check
	 */
	@Override
	public void run()
	{
		while(run)
		{
			checkFollowers(true);
			//ConsoleTab.output(Level.DeBug, "Checked for new followers");
			try
			{
				synchronized(this)
				{
					this.wait(10000);
				}
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
		JsonObject obj = json.parse(HTTPConnect.GetResponsefrom("https://api.twitch.tv/kraken/channels/"+this.bot.getChannel(false)+"/follows?direction=DESC&limit=100")).getAsJsonObject();

		int total = obj.get("_total").getAsInt();
		int current = 0;
		String nexturl = "https://api.twitch.tv/kraken/channels/"+this.bot.getChannel(false)+"/follows?direction=DESC&limit=100";
		while(current < total)
		{
			boolean success = false;
			int loops = 0;
			while(!success)
			{
				try{
					obj  = json.parse(HTTPConnect.GetResponsefrom(nexturl)).getAsJsonObject();
					success = true;
				}catch(IllegalStateException e){loops++; if(loops > 10)return;}
			}
			try{
				nexturl = obj.get("_links").getAsJsonObject().get("next").getAsString();
			}catch(IndexOutOfBoundsException e){nexturl = obj.get("_links").getAsJsonObject().get("next").getAsString();}
			JsonArray list = obj.get("follows").getAsJsonArray();
			for(int i = 0; i < list.size(); i++)
			{
				String temp = list.get(i).getAsJsonObject().get("user").getAsJsonObject().get("display_name").getAsString();
				super.setSetting(temp, System.currentTimeMillis());
			}
			current+=100;
		}
		ConsoleTab.output(Level.Info, "Updated follower list for " + this.bot.getChannel(false));
	}

	/**
	 * Checks for new followers.
	 */
	public void checkFollowers(boolean output)
	{
		JsonObject obj;
		try{
			obj = json.parse(HTTPConnect.GetResponsefrom("https://api.twitch.tv/kraken/channels/"+this.bot.getChannel(false)+"/follows?direction=DESC&limit=100")).getAsJsonObject();
		}catch(IllegalStateException ex){return;}
		JsonArray list = obj.get("follows").getAsJsonArray();
		for(int i = 0; i < list.size(); i++)
		{

			String temp = list.get(i).getAsJsonObject().get("user").getAsJsonObject().get("display_name").getAsString();
			if(!super.properties.containsKey(temp))
			{
				if(output)
				{
					this.bot.sendMessage(temp + "Has just followed!!!");
				}
				super.setSetting(temp, System.currentTimeMillis());
			}
		}
	}

}