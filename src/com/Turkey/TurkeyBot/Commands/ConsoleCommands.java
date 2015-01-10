package com.Turkey.TurkeyBot.Commands;

import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.Gui;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class ConsoleCommands
{
	public static void onCommand(String[] args)
	{
		if(args[0].substring(0,1).equalsIgnoreCase("/"))
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
					Gui.getBot().connectToChannel(args[1].toLowerCase());
				}
				else 
				{
					ConsoleTab.output(Level.Alert, "Your number of arguments is incorrect! try /Join (Channel)");
				}
			}
			else if(command.equalsIgnoreCase("leave"))
			{
				Gui.getBot().disconnectFromChannel();
			}
			else if(command.equalsIgnoreCase("say"))
			{
				String msg = "";
				for(int i = 1; i < args.length; i++)
				{
					String s = args[i];
					msg+=s + " ";
				}
				Gui.getBot().sendMessage(msg);
			}
			else if(command.equalsIgnoreCase("connect"))
			{
				Gui.getBot().connectToTwitch();
			}
			else if(command.equalsIgnoreCase("disconnect"))
			{
				Gui.getBot().disconnectFromTwitch();
			}
			else
			{
				ConsoleTab.output(Level.Alert, "That is not a valid Command");
			}
		}
	}
}
