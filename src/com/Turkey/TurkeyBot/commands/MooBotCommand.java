package com.Turkey.TurkeyBot.commands;

import com.Turkey.TurkeyBot.TurkeyBot;

public class MooBotCommand extends Command
{
	public MooBotCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot, String channel, String sender, String login, String hostname, String message)
	{
		bot.sendMessage("MooBot? More like GobbleBot");
	}

	public boolean canEdit()
	{
		return false;
	}
}