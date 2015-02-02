package com.Turkey.TurkeyBot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.Gui;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class Start
{
	public static void main(String[] args)
	{
		try
		{
			new Gui(new TurkeyBot());
			checkVersion();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Checks for an update to the bot.
	 */
	private static void checkVersion()
	{
		String result = "";
		try
		{
			URL url = new URL("http://theprogrammingturkey.uphero.com/API/versionCheck.php?application=TurkeyBot");
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
			ConsoleTab.output(Level.Error, "Could not connect to the server to check for the current version of TurkeyBot");
			return;
		}

		if(!result.equalsIgnoreCase(TurkeyBot.VERSION))
		{
			ConsoleTab.output(Level.Important, "Your version of TurkeyBot is currently out of date!");
		}
	}
}