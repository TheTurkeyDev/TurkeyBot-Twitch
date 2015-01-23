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

	/**
	 * Called when a command is entered in the chat.
	 * @param bot The TurkryBot instance.
	 * @param channel The current channel the bot is in.
	 * @param sender The user that sent the command.
	 * @param login Not sure what this is.
	 * @param hostname Twitch by default as we are on twitch servers if moderating a stream.
	 * @param message The full message entered.
	 */
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

	/**
	 * Gets the permission level of the command.
	 * User by default.
	 * @return The permission level of the command.
	 */
	public String getPermissionLevel()
	{
		return permLevel;
	}

	/**
	 * Sets the permission level of the command.
	 * @param level The level to change the permission to.
	 */
	public void setPermissionLevel(String lev)
	{
		if(isValidPerm(lev))
		{
			permLevel = lev;
		}

	}

	/**
	 * Returns if the permission level is valid.
	 * @param perm The permission level to test validity of.
	 * @return If the permission level is valid.
	 */
	public boolean isValidPerm(String p)
	{
		String perm = p.substring(0,1).toUpperCase() + p.substring(1).toLowerCase();
		for(String realPerm: permList)
			if(perm.equals(realPerm))
				return true;
		return false;
	}

	/**
	 * If the command can be edited.
	 * @return If the command is editable.
	 */
	public boolean canEdit()
	{
		return true;
	}

	/**
	 * Returns the name of the command.
	 * @return The command name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the text response of the command if the command is entered.
	 * @return The commands response.
	 */
	public String getReponse()
	{
		return response;
	}
	
	/**
	 * Sets the text response of the command.
	 * @param res The new response for the command.
	 */
	public void setResponse(String res)
	{
		response = res;
	}

	/**
	 * Enables the command to be used.
	 */
	public void enable()
	{
		enabled = true;
	}
	
	/**
	 * Disables the command from being used.
	 */
	public void disable()
	{
		enabled = false;
	}
	
	/**
	 * Returns if the command is enabled or not. 
	 * @return If the command is enabled.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Gets the command file of the command containing all of the command data that is stored on a file.
	 * @return The commands file.
	 */
	public CommandFile getFile()
	{
		return file;
	}
}
