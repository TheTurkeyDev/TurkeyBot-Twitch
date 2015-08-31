package com.Turkey.TurkeyBot.commands;

import com.Turkey.TurkeyBot.TurkeyBot;

public class AddResponse extends Command
{

	public AddResponse(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot, String channel, String sender, String login, String hostname, String message)
	{
		String[] contents = message.split(" ");
		if(contents.length < 3) bot.sendMessage(bot.capitalizeName(sender) + ": That is not valid! Try !addResponse <command> <response>");
		String commandName = contents[1];
		if(!commandName.substring(0, 1).equalsIgnoreCase("!")) commandName = "!" + commandName;
		Command c = CommandManager.getCommandFromName(commandName);
		String response = message.substring(message.toLowerCase().indexOf(commandName.substring(1).toLowerCase()) + commandName.length());
		if(c != null)
		{
			c.addResponse(response);
			c.getFile().updateCommand();
			bot.sendMessage(bot.capitalizeName(sender) + ": The response for the command !" + c.getName() + " has been added!");
		}
		else
		{
			bot.sendMessage(bot.capitalizeName(sender) + ": That is not a valid command!");
		}
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
