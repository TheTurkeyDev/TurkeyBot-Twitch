package com.Turkey.TurkeyBot.commands;

import com.Turkey.TurkeyBot.TurkeyBot;

public class StatusCommand extends Command
{

	public StatusCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot, String channel, String sender, String login, String hostname, String message)
	{
		String[] contents = message.split(" ");
		Command command;
		if(contents.length == 2)
		{
			if((command = CommandManager.getCommandFromName(contents[1])) != null)
			{
				String name = command.getName();
				String perm = command.getPermissionLevel();
				boolean enabled = command.isEnabled();
				boolean editable = command.canEdit();
				int numOfResponses = command.getNumberOfResponses();
				bot.sendWhisper("The command's status is: Name: " + name + ", Permission Level: " + perm + ", Enabled: " + enabled + ", Editable: " + editable + ", Number of Responses: " + numOfResponses, sender);
			}
			else
			{
				bot.sendWhisper("That is not a valid command", sender);
			}
		}
		else
		{
			bot.sendMessage(bot.capitalizeName(sender) + ": That is not valid! Try !commandStatus <command>");
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