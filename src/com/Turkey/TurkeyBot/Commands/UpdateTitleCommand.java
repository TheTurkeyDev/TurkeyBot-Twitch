package com.Turkey.TurkeyBot.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class UpdateTitleCommand extends Command
{

	public UpdateTitleCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot,String channel, String sender, String login, String hostname, String message)
	{
		try{
			String url = "https://api.twitch.tv/kraken/channels/"+bot.getChannel().toLowerCase().substring(1)+"?oauth_token=" + SecretStuff.oAuth;
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestProperty("Accept", "application/vnd.twitchtv.v2+json");
			conn.setRequestMethod("PUT");
			conn.setDoOutput(true);

			String data = "channel[status]=testing!";
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(data);
			out.flush();
			
			String result = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				result += line;
			}
			ConsoleTab.output(Level.Info, result);
			reader.close();
		}catch(IOException e){System.out.println(e.getMessage());   bot.sendMessage("An error has occured changing the title!");};
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
