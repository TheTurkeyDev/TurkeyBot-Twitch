package com.Turkey.TurkeyBot.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.files.CommandFile;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class Command
{
	public String name;
	private ArrayList<String> responses;
	private int args;
	protected boolean enabled = true;
	protected String permLevel = "User";

	private static String[] permList = { "User", "Mod", "Streamer" };

	private CommandFile file;

	public Command(String n, String r)
	{
		name = n;
		responses = new ArrayList<String>();
		responses.add(r);
		try
		{
			file = new CommandFile(this, TurkeyBot.bot);
		} catch(IOException e)
		{
			ConsoleTab.output(Level.Error, "Could not load " + name + "'s command file!");
		}
		int index = r.indexOf("%arg", 0);
		int higharg = 0;
		while(index != -1)
		{
			try
			{
				int argnum = Integer.parseInt(r.substring(index + 5, r.indexOf(" ", index)));
				if(argnum > higharg) higharg = argnum;
				index += 5;
			} catch(NumberFormatException ex)
			{
				ConsoleTab.output(Level.Error, r.substring(index + 4, index + 5) + " is not a valid argument integer in command " + name);
			} catch(StringIndexOutOfBoundsException ex)
			{
				int argnum = Integer.parseInt(r.substring(index + 5));
				if(argnum > higharg) higharg = argnum;
				index += 5;
			}
			index = r.indexOf("%arg", index);
		}
		args = higharg;
	}

	/**
	 * Called when a command is entered in the chat.
	 * 
	 * @param bot
	 *            The TurkryBot instance.
	 * @param channel
	 *            The current channel the bot is in.
	 * @param sender
	 *            The user that sent the command.
	 * @param login
	 *            Not sure what this is.
	 * @param hostname
	 *            Twitch by default as we are on twitch servers if moderating a stream.
	 * @param message
	 *            The full message entered.
	 */
	public void oncommand(TurkeyBot bot, String channel, String sender, String login, String hostname, String message)
	{
		if(responses.size() == 0) return;
		String[] arguments = message.split(" ");
		String resposeEdited = this.getRandomResponse();
		resposeEdited = resposeEdited.replaceAll("%Sender", bot.capitalizeName(sender));
		for(int i = 1; i <= args; i++)
		{
			try
			{
				resposeEdited = resposeEdited.replaceAll("%args" + i, arguments[i]);
			} catch(ArrayIndexOutOfBoundsException ex)
			{
				bot.sendMessage(bot.capitalizeName(sender) + " you have not entered all of the required arguments");
				return;
			}
		}

		bot.sendMessage(resposeEdited);
	}

	/**
	 * Gets the permission level of the command. User by default.
	 * 
	 * @return The permission level of the command.
	 */
	public String getPermissionLevel()
	{
		return permLevel;
	}

	/**
	 * Sets the permission level of the command.
	 * 
	 * @param level
	 *            The level to change the permission to.
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
	 * 
	 * @param perm
	 *            The permission level to test validity of.
	 * @return If the permission level is valid.
	 */
	public boolean isValidPerm(String p)
	{
		String perm = p.substring(0, 1).toUpperCase() + p.substring(1).toLowerCase();
		for(String realPerm : permList)
			if(perm.equals(realPerm)) return true;
		return false;
	}

	/**
	 * If the command can be edited.
	 * 
	 * @return If the command is editable.
	 */
	public boolean canEdit()
	{
		return true;
	}

	/**
	 * Returns the name of the command.
	 * 
	 * @return The command name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets a random response of the command if the command is entered.
	 * 
	 * @return The commands response.
	 */
	public String getRandomResponse()
	{
		return responses.get((new Random()).nextInt(responses.size()));
	}

	/**
	 * Gets the first text response of the command.
	 * 
	 * @return The commands response.
	 */
	public String getFirstResponse()
	{
		return responses.get(0);
	}

	/**
	 * Gets all of the responses for this command.
	 * 
	 * @return The responses of the command.
	 */
	public ArrayList<String> getResponses()
	{
		return responses;
	}

	/**
	 * Gets the number of responses for this command.
	 * 
	 * @return The number of responses.
	 */
	public int getNumberOfResponses()
	{
		return responses.size();
	}

	/**
	 * Sets the text response of the command.
	 * 
	 * @param res
	 *            The new response for the command.
	 */
	public void addResponse(String res)
	{
		int index = res.indexOf("%arg", 0);
		int higharg = 0;
		while(index != -1)
		{
			try
			{
				int argnum = Integer.parseInt(res.substring(index + 5, res.indexOf(" ", index)));
				if(argnum > higharg) higharg = argnum;
				index += 5;
			} catch(NumberFormatException ex)
			{
				ConsoleTab.output(Level.Error, res.substring(index + 4, index + 5) + " is not a valid argument integer in command " + name);
			} catch(StringIndexOutOfBoundsException ex)
			{
				int argnum = Integer.parseInt(res.substring(index + 5));
				if(argnum > higharg) higharg = argnum;
				index += 5;
			}
			index = res.indexOf("%arg", index);
		}

		if(higharg == args) responses.add(res);
		else ConsoleTab.output(Level.Error, "The response of: \"" + res + "\", for the command " + this.getName() + " does not have the correct amount of arguments as the original response");
	}

	/**
	 * Sets the text of the first response of the command.
	 * 
	 * @param res
	 *            The new response for the command.
	 */
	public void setFirstResponse(String res)
	{
		int index = res.indexOf("%arg", 0);
		int higharg = 0;
		while(index != -1)
		{
			try
			{
				int argnum = Integer.parseInt(res.substring(index + 5, res.indexOf(" ", index)));
				if(argnum > higharg) higharg = argnum;
				index += 5;
			} catch(NumberFormatException ex)
			{
				ConsoleTab.output(Level.Error, res.substring(index + 4, index + 5) + " is not a valid argument integer in command " + name);
			} catch(StringIndexOutOfBoundsException ex)
			{
				int argnum = Integer.parseInt(res.substring(index + 5));
				if(argnum > higharg) higharg = argnum;
				index += 5;
			}
			index = res.indexOf("%arg", index);
		}

		args = higharg;
		responses.set(0, res);
	}

	/**
	 * Sets the given response number's text response of the command.
	 * 
	 * @param res
	 *            The new response to replace the old one.
	 */
	public void editResponse(int pos, String res)
	{
		int index = res.indexOf("%arg", 0);
		int higharg = 0;
		while(index != -1)
		{
			try
			{
				int argnum = Integer.parseInt(res.substring(index + 5, res.indexOf(" ", index)));
				if(argnum > higharg) higharg = argnum;
				index += 5;
			} catch(NumberFormatException ex)
			{
				ConsoleTab.output(Level.Error, res.substring(index + 4, index + 5) + " is not a valid argument integer in command " + name);
			} catch(StringIndexOutOfBoundsException ex)
			{
				int argnum = Integer.parseInt(res.substring(index + 5));
				if(argnum > higharg) higharg = argnum;
				index += 5;
			}
			index = res.indexOf("%arg", index);
		}

		if(higharg == args) responses.set(pos, res);
		else ConsoleTab.output(Level.Error, "The response of: \"" + res + "\", for the command " + this.getName() + " does not have the correct amount of arguments as the original response");
	}

	/**
	 * Removes the given response number for the command
	 * 
	 * @param number
	 *            of the response to remove
	 */
	public void removeResponse(int num)
	{
		responses.remove(num);
		file.updateCommand();
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
	 * 
	 * @return If the command is enabled.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Gets the command file of the command containing all of the command data that is stored on a file.
	 * 
	 * @return The commands file.
	 */
	public CommandFile getFile()
	{
		return file;
	}
}
