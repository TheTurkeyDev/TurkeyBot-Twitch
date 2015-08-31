package com.Turkey.TurkeyBot.commands;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class StatusCommand extends Command
{

	public StatusCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot, String channel, String sender, String login, String hostname, String message)
	{
		ConsoleTab.output(Level.DeBug, "Status");
		String[] contents = message.split(" ");
		Command command;
		if(contents.length == 2)
		{
			ConsoleTab.output(Level.DeBug, "here");
			if((command = CommandManager.getCommandFromName(contents[1])) != null)
			{
				ConsoleTab.output(Level.DeBug, "2");
				String name = command.getName();
				String perm = command.getPermissionLevel();
				boolean enabled = command.isEnabled();
				boolean editable = command.canEdit();
				int numOfResponses = command.getNumberOfResponses();
				bot.sendMessage(bot.capitalizeName(sender) + ", The command's status is: Name: " + name + ", Permission Level: " + perm + ", Enabled: " + enabled + ", Editable: " + editable + ", Number of Responses: " + numOfResponses);
			}
			else
			{
				bot.sendMessage(bot.capitalizeName(sender) + ": That is not a valid command");
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