package com.Turkey.TurkeyBot.Commands;

import java.io.IOException;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.files.CommandFile;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class Command
{
	public String name;
	private String response;
	private int args;
	protected boolean enabled = true;
	protected String permLevel = "User";

	private static String[] permList = {"User", "Mod", "Streamer"};

	private CommandFile file;

	public Command(String n, String r)
	{
		name = n;
		response = r;
		try
		{
			file = new CommandFile(this);
		} catch (IOException e)
		{
			ConsoleTab.output(Level.Error, "Could not load " + name + "'s command file!");
		}
		int index = response.indexOf("%arg", 0);
		int higharg = 0;
		while(index != -1)
		{
			try{
				int argnum = Integer.parseInt(response.substring(index + 5, response.indexOf(" ", index)));
				if(argnum > higharg)
					higharg = argnum;
				index+=5;
			}catch(NumberFormatException ex){ConsoleTab.output(Level.Error, response.substring(index + 4, index + 5) + " is not a valid argument integer in command " + name);}
			catch(StringIndexOutOfBoundsException ex)
			{
				int argnum = Integer.parseInt(response.substring(index + 5));
				if(argnum > higharg)
					higharg = argnum;
				index+=5;
			}
			index = response.indexOf("%arg", index);
		}
		args = higharg;
	}

	public void oncommand(TurkeyBot bot,String channel, String sender, String login, String hostname, String message)
	{
		if(response == "")
			return;
		String[] arguments = message.split(" ");
		String resposeEdited = response;
		resposeEdited = resposeEdited.replaceAll("%Sender", bot.capitalizeName(sender));
		for(int i = 1; i <= args; i++)
		{
			try{
				resposeEdited = resposeEdited.replaceAll("%args"+i, arguments[i]);
			}catch(ArrayIndexOutOfBoundsException ex){bot.sendMessage(bot.capitalizeName(sender) + " you have not entered all of the required arguments"); return;}
		}

		bot.sendMessage(resposeEdited);
	}

	public String getPermissionLevel()
	{
		return permLevel;
	}

	public void setPermissionLevel(String lev)
	{
		if(isValidPerm(lev))
		{
			permLevel = lev;
		}

	}

	public boolean isValidPerm(String p)
	{
		String perm = p.substring(0,1).toUpperCase() + p.substring(1).toLowerCase();
		for(String realPerm: permList)
			if(perm.equals(realPerm))
				return true;
		return false;
	}

	public boolean canEdit()
	{
		return true;
	}

	public String getName()
	{
		return name;
	}
	public String getReponse()
	{
		return response;
	}
	public void setResponse(String res)
	{
		response = res;
	}

	public void enable()
	{
		enabled = true;
	}
	public void disable()
	{
		enabled = false;
	}
	public boolean isEnabled()
	{
		return enabled;
	}

	public CommandFile getFile()
	{
		return file;
	}
}
