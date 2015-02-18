package com.Turkey.TurkeyBot.Commands;

import java.util.ArrayList;
import java.util.Random;

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
		ArrayList<String> viewers = Gui.getBot().getViewers();
		Random r = new Random();
		String winner = viewers.get(r.nextInt(viewers.size()-1));
		bot.sendMessage("And the winner is...... " + winner +"!");
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
