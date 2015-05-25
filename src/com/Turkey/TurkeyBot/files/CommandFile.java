package com.Turkey.TurkeyBot.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.commands.Command;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CommandFile
{
	private Command command;
	private File file;
	private Gson gson;
	private JsonObject mainFile;

	public CommandFile(Command c, TurkeyBot b) throws IOException
	{
		command = c;
		gson = new Gson();
		file = new File("C:" + File.separator + "TurkeyBot" + File.separator + b.getChannel(false) + File.separator + "commands" + File.separator + command.getName() +".json");
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
			updateCommand();
		}
	}

	/**
	 * Updates a command's property file.
	 */
	public void updateCommand()
	{
		mainFile = new JsonObject();
		mainFile.add("State", gson.toJsonTree(command.isEnabled()));
		mainFile.add("PermLevel", gson.toJsonTree(command.getPermissionLevel()));
		mainFile.add("LoadFile", gson.toJsonTree(command.canEdit()));
		mainFile.add("Number_Of_Responses", gson.toJsonTree(command.getNumberOfResponses()));

		JsonObject responses = new JsonObject();
		ArrayList<String> list = command.getResponses();
		for(int i = 0; i < list.size(); i++)
		{
			responses.addProperty("" + i, list.get(i));
		}

		mainFile.add("Responses", responses);
		try{
			FileOutputStream outputStream = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			writer.append(mainFile.toString());
			writer.close();
			outputStream.close();
		}catch(IOException ex){ConsoleTab.output(Level.Error, "Could not write to json file for the command "  + command.getName());}
	
	}

	/**
	 * Removes the commands property file.
	 */
	public void removeCommand()
	{
		file.delete();
	}

	/**
	 * Disables the command in the file.
	 */
	public void disableCommand()
	{
		mainFile.addProperty("State", "Disabled");
	}

	/**
	 * Enables the command in the file.
	 */
	public void enableCommand()
	{
		mainFile.addProperty("State", "Enabled");
	}

	/**
	 * Sets the permission level for the command.
	 * @param level to set the command to.
	 */
	public void setPermLevel(String level)
	{
		mainFile.addProperty("PermLevel", level);
	}

	/**
	 * Gets the permission of the command.
	 * @return The permission level of this command.
	 */
	public String getPermLevel()
	{
		return mainFile.get("PermLevel").getAsString();
	}
}
