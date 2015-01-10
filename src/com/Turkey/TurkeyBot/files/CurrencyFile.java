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

	public int getCurrencyFor(String name)
	{
		if(!properties.containsKey(name))
		{
			properties.put(name, "1000");
			save();
		}
		return Integer.parseInt(properties.getProperty(name));
	}

	public void addCurrencyFor(String name, int ammount)
	{
		if(!properties.containsKey(name))
		{
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
