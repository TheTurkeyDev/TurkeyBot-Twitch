package com.Turkey.TurkeyBot.files;

import java.io.File;
import java.io.IOException;

import com.Turkey.TurkeyBot.Commands.Command;

public class CommandFile
{
	private String commandName;
	private String propName = "commands.properties";
	private BotFile botfile;
	private Command command;

	public CommandFile(Command c) throws IOException
	{
		command = c;
		commandName = command.getName();
		propName = commandName + ".properties";
		botfile = new BotFile(null, "C:" + File.separator + "TurkeyBot" + File.separator + "commands" + File.separator + propName);
		loadCommand();
	}

	/**
	 * Loads a command from its property file.
	 * @throws IOException
	 */
	public void loadCommand()
	{
		String response = command.getReponse();
		botfile.properties.put(commandName , response);
		botfile.properties.put(commandName + "_State", "Enabled");
		botfile.properties.put(commandName + "_PermLevel", command.getPermissionLevel());
		botfile.properties.put(commandName + "_LoadFile", command.canEdit());

		botfile.save();
	}

	/**
	 * Updates a command's property file.
	 */
	public void updateCommand()
	{
		String response = command.getReponse();
		if(!botfile.properties.containsKey(commandName))
		{
			botfile.properties.put(commandName , response);
			botfile.properties.put(commandName + "_State", "Enabled");
			botfile.properties.put(commandName + "_PermLevel", command.getPermissionLevel());
			botfile.properties.put(commandName + "_LoadFile", command.canEdit());
		}
		else
		{
			botfile.properties.setProperty(commandName, response);
			botfile.properties.setProperty(commandName + "_State", "Enabled");
			botfile.properties.setProperty(commandName + "_PermLevel", command.getPermissionLevel());
			botfile.properties.setProperty(commandName + "_LoadFile", "" + command.canEdit());
		}
		botfile.save();
	}

	/**
	 * Removes the commands property file.
	 */
	public void removeCommand()
	{
		botfile.file.delete();
	}

	/**
	 * Disables the command in the file.
	 */
	public void disableCommand()
	{
		botfile.properties.setProperty(commandName + "_State", "Disabled");
		botfile.save();
	}
	
	/**
	 * Enables the command in the file.
	 */
	public void enableCommand()
	{
		botfile.properties.setProperty(commandName + "_State", "Enabled");
		botfile.save();
	}

	/**
	 * Sets the permission level for the command.
	 * @param level to set the command to.
	 */
	public void setPermLevel(String level)
	{
		botfile.properties.setProperty(commandName + "_PermLevel", level);
		botfile.save();
	}

	/**
	 * Gets the permission of the command.
	 * @return The permission level of this command.
	 */
	public String getPermLevel()
	{
		return botfile.properties.getProperty(commandName + "_PermLevel");
	}
}
