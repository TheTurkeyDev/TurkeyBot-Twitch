package com.Turkey.TurkeyBot.commands;

import com.Turkey.TurkeyBot.TurkeyBot;

public class TurkeyBotCommand extends Command
{
	public TurkeyBotCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot,String channel, String sender, String login, String hostname, String message)
	{
		bot.sendMessage("Yes, I am TurkeyBot.");
	}
	
	public boolean canEdit()
	{
		return false;
	}
}