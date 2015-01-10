package com.Turkey.TurkeyBot.Commands;

import com.Turkey.TurkeyBot.TurkeyBot;

public class NightBotCommand extends Command
{
	public NightBotCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot,String channel, String sender, String login, String hostname, String message)
	{
		bot.sendMessage("Nightbot? pfft wannabe");
	}
	
	public boolean canEdit()
	{
		return false;
	}
}