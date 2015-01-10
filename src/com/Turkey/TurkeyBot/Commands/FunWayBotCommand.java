package com.Turkey.TurkeyBot.Commands;

import com.Turkey.TurkeyBot.TurkeyBot;

public class FunWayBotCommand extends Command
{
	public FunWayBotCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot,String channel, String sender, String login, String hostname, String message)
	{
		bot.sendMessage("FunwayBot? Aren't your supposed to be fun and stuff?");
	}
	
	public boolean canEdit()
	{
		return false;
	}
}
