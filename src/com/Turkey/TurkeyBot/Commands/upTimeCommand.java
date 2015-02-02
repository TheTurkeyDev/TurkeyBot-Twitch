package com.Turkey.TurkeyBot.Commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.Turkey.TurkeyBot.TurkeyBot;

public class upTimeCommand extends Command
{
	public upTimeCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot,String channel, String sender, String login, String hostname, String message)
	{
		String result = "";
		try
		{
			URL url = new URL("https://api.twitch.tv/kraken/streams/" + bot.getChannel(false));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				result += line;
			}
			reader.close();

			int create_index = result.indexOf("\"created_at\":") + 14;
			String uptime = result.substring(create_index);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date parse = format.parse(uptime);

			long diff = System.currentTimeMillis() - parse.getTime();

			int hours = (int) (diff / 3600000);
			int remainder = (int) (diff - hours * 3600000);
			int mins = (remainder / 60000);
			remainder = (remainder - mins * 60000);
			int secs = remainder / 1000;

			bot.sendMessage(bot.capitalizeName(bot.getChannel(false)) + " has been streaming for " + hours + " hours " + mins + " minutes and " + secs + " seconds!");

		} catch (Exception e){e.printStackTrace();}
	}
	
	public boolean canEdit()
	{
		return false;
	}
	
	public String getPermissionLevel()
	{
		return "Mod";
	}
}
