package com.Turkey.TurkeyBot.files;

import java.io.File;
import java.io.IOException;

import com.Turkey.TurkeyBot.TurkeyBot;

public class CurrencyFile extends BotFile
{
	private static String propName = "currency.properties";

	public CurrencyFile(TurkeyBot bot) throws IOException
	{
		super(bot,"C:" + File.separator + "TurkeyBot" + File.separator + "properties" + File.separator + propName);
	}

	/**
	 * Gets the currency for the given username.
	 * @param name to get the currency of.
	 * @return The amount of curreny the user has
	 */
	public int getCurrencyFor(String name)
	{
		if(!properties.containsKey(name))
		{
			properties.put(name, "1000");
			save();
		}
		return Integer.parseInt(properties.getProperty(name));
	}

	/**
	 * Adds a given amount of currency to the specified username.
	 * @param name The name to add the given currency to.
	 * @param ammount The amount of currency to be given the the user.
	 */
	public void addCurrencyFor(String name, int ammount)
	{
		if(!properties.containsKey(name))
		{
			if(bot.followersFile.isFollower(name))
			properties.put(name, "" + (1000 + ammount));
		}
		else
		{
			int current = Integer.parseInt(properties.getProperty(name));
			properties.setProperty(name, "" + (current+ammount));
		}
		save();
	}
}
