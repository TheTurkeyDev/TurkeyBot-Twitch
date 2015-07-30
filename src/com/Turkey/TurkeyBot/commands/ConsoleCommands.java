package com.Turkey.TurkeyBot.commands;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class ConsoleCommands
{
	/**
	 * Called when someone enters a line in the console.
	 * 
	 * @param args
	 *            The arguments of the message
	 */
	public static void onCommand(String[] args)
	{
		if(args[0].substring(0, 1).equalsIgnoreCase("/"))
		{
			String command = args[0].substring(1);

			if(command.equalsIgnoreCase("test"))
			{
				ConsoleTab.output(Level.Alert, "Test");
			}
			else if(command.equalsIgnoreCase("Join"))
			{
				if(args.length == 2)
				{
					TurkeyBot.bot.connectToChannel(args[1].toLowerCase());
				}
				else
				{
					ConsoleTab.output(Level.Alert, "Your number of arguments is incorrect! try /Join (Channel)");
				}
			}
			else if(command.equalsIgnoreCase("leave"))
			{
				TurkeyBot.bot.disconnectFromChannel();
			}
			else if(command.equalsIgnoreCase("say"))
			{
				String msg = "";
				for(int i = 1; i < args.length; i++)
				{
					String s = args[i];
					msg += s + " ";
				}
				TurkeyBot.bot.sendMessage(msg.trim());
			}
			else
			{
				ConsoleTab.output(Level.Alert, "That is not a valid Command");
			}
		}
	}
}
