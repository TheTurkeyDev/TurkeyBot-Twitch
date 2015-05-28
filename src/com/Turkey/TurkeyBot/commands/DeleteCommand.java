package com.Turkey.TurkeyBot.commands;

import com.Turkey.TurkeyBot.TurkeyBot;

public class DeleteCommand extends Command
{

	public DeleteCommand(String n)
	{
		super(n, "");
	}
	
	public void oncommand(TurkeyBot bot,String channel, String sender, String login, String hostname, String message)
	{
		String[] contents = message.split(" ");
		if(contents.length != 2)
			bot.sendMessage(bot.capitalizeName(sender) + ": That is not valid! Try !deleteCommand <command>");
		String commandName  = contents[1];
		if(!commandName.substring(0,1).equalsIgnoreCase("!"))
			commandName = "!"+commandName;
		Command c = TurkeyBot.getCommandFromName(commandName);
		if(c!=null)
		{
			TurkeyBot.bot.removeCommand(c);
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
