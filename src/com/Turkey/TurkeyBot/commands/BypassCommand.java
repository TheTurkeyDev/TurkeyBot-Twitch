package com.Turkey.TurkeyBot.commands;

import com.Turkey.TurkeyBot.TurkeyBot;

public class BypassCommand extends Command
{

	public BypassCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot, String channel, String sender, String login, String hostname, String message)
	{
		String[] args = message.split(" ");
		if(args.length == 2)
		{
			if(bot.isUser(args[1].toLowerCase()))
			{
				bot.getProfile().chatmoderation.giveImmunityTo(args[1].toLowerCase());
				bot.sendMessage("The user " + args[1] + " now bypasses the filter next message.");
			}
			else bot.sendMessage(args[1] + " is not in this chat!");
		}
		else
		{
			bot.sendMessage("Invalid Arguments! Try !Bypass <UserName>");
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
