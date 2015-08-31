package com.Turkey.TurkeyBot.commands;

import com.Turkey.TurkeyBot.TurkeyBot;

public class AddCommand extends Command
{

	public AddCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot, String channel, String sender, String login, String hostname, String message)
	{
		String[] contents = message.split(" ");
		if(contents.length < 3) bot.sendMessage(bot.capitalizeName(sender) + ": That is not valid! Try !addCommand <command> <response>");
		String commandName = contents[1];
		if(commandName.substring(0, 1).equalsIgnoreCase("!")) commandName = commandName.substring(1);
		String response = message.substring(message.toLowerCase().indexOf(commandName.toLowerCase()) + commandName.length());
		Command c = new Command(commandName, response);
		CommandManager.addCommand(c);
		bot.sendMessage(bot.capitalizeName(sender) + ": The command !" + c.getName() + " has been created!");
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
