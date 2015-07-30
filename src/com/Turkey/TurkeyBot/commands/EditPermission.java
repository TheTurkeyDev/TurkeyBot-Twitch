package com.Turkey.TurkeyBot.commands;

import com.Turkey.TurkeyBot.TurkeyBot;

public class EditPermission extends Command
{

	public EditPermission(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot,String channel, String sender, String login, String hostname, String message)
	{
		String[] contents = message.split(" ");
		if(contents.length != 3)
			bot.sendMessage(bot.capitalizeName(sender) + ": That is not valid! Try !editCommand <command> <Perm>");
		String commandName  = contents[1];
		if(!commandName.substring(0,1).equalsIgnoreCase("!"))
			commandName = "!"+commandName;
		Command c = CommandManager.getCommandFromName(commandName);
		String perm = contents[2];
		if(c!=null)
		{
			if(c.isValidPerm(perm))
				c.setPermissionLevel(perm);
			else
			{
				bot.sendMessage(bot.capitalizeName(sender) + ": Invalid permission level!");
				return;
			}
			c.getFile().updateCommand();
			bot.sendMessage(bot.capitalizeName(sender) + ": The command !" + c.getName() + " has been changed!");
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
