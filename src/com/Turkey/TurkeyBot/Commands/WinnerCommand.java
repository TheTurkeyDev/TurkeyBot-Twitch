package com.Turkey.TurkeyBot.Commands;

import java.util.Random;

import org.jibble.pircbot.User;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.Gui;

public class WinnerCommand extends Command
{

	public WinnerCommand(String n)
	{
		super(n, "");
	}

	public void oncommand(TurkeyBot bot,String channel, String sender, String login, String hostname, String message)
	{
		User[] viewers = Gui.getBot().getViewers();
		Random r = new Random();
		User winner = viewers[r.nextInt(viewers.length-1)];
		bot.sendMessage("And the winner is...... " + winner.getNick() +"!");
	}

	public String getPermissionLevel()
	{
		return "Mod";
	}

	public boolean canEdit()
	{
		return false;
	}
}
