package com.Turkey.TurkeyBot.commands;


import com.Turkey.TurkeyBot.TurkeyBot;

public class CurrencyCommand extends Command
{
	public CurrencyCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot,String channel, String sender, String login, String hostname, String message)
	{
		int currency = bot.getProfile().currency.getCurrencyFor(sender);
		bot.sendMessage("" + bot.capitalizeName(sender) + " You have " + currency + " " + bot.getProfile().getCurrencyName());
	}
	
	public boolean canEdit()
	{
		return false;
	}
}
